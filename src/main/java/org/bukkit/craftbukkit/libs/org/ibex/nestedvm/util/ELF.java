package org.bukkit.craftbukkit.libs.org.ibex.nestedvm.util;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ELF {

    private static final int ELF_MAGIC = 2135247942;
    public static final int ELFCLASSNONE = 0;
    public static final int ELFCLASS32 = 1;
    public static final int ELFCLASS64 = 2;
    public static final int ELFDATANONE = 0;
    public static final int ELFDATA2LSB = 1;
    public static final int ELFDATA2MSB = 2;
    public static final int SHT_SYMTAB = 2;
    public static final int SHT_STRTAB = 3;
    public static final int SHT_NOBITS = 8;
    public static final int SHF_WRITE = 1;
    public static final int SHF_ALLOC = 2;
    public static final int SHF_EXECINSTR = 4;
    public static final int PF_X = 1;
    public static final int PF_W = 2;
    public static final int PF_R = 4;
    public static final int PT_LOAD = 1;
    public static final short ET_EXEC = 2;
    public static final short EM_MIPS = 8;
    private Seekable data;
    public ELF.ELFIdent ident;
    public ELF.ELFHeader header;
    public ELF.PHeader[] pheaders;
    public ELF.SHeader[] sheaders;
    private byte[] stringTable;
    private boolean sectionReaderActive;
    private ELF.Symtab _symtab;

    private void readFully(byte[] abyte) throws IOException {
        int i = abyte.length;

        int j;

        for (int k = 0; i > 0; i -= j) {
            j = this.data.read(abyte, k, i);
            if (j == -1) {
                throw new IOException("EOF");
            }

            k += j;
        }

    }

    private int readIntBE() throws IOException {
        byte[] abyte = new byte[4];

        this.readFully(abyte);
        return (abyte[0] & 255) << 24 | (abyte[1] & 255) << 16 | (abyte[2] & 255) << 8 | (abyte[3] & 255) << 0;
    }

    private int readInt() throws IOException {
        int i = this.readIntBE();

        if (this.ident != null && this.ident.data == 1) {
            i = i << 24 & -16777216 | i << 8 & 16711680 | i >>> 8 & '\uff00' | i >> 24 & 255;
        }

        return i;
    }

    private short readShortBE() throws IOException {
        byte[] abyte = new byte[2];

        this.readFully(abyte);
        return (short) ((abyte[0] & 255) << 8 | (abyte[1] & 255) << 0);
    }

    private short readShort() throws IOException {
        short short0 = this.readShortBE();

        if (this.ident != null && this.ident.data == 1) {
            short0 = (short) ((short0 << 8 & '\uff00' | short0 >> 8 & 255) & '\uffff');
        }

        return short0;
    }

    private byte readByte() throws IOException {
        byte[] abyte = new byte[1];

        this.readFully(abyte);
        return abyte[0];
    }

    public ELF(String s) throws IOException, ELF.ELFException {
        this((Seekable) (new Seekable.File(s, false)));
    }

    public ELF(Seekable seekable) throws IOException, ELF.ELFException {
        this.data = seekable;
        this.ident = new ELF.ELFIdent();
        this.header = new ELF.ELFHeader();
        this.pheaders = new ELF.PHeader[this.header.phnum];

        int i;

        for (i = 0; i < this.header.phnum; ++i) {
            seekable.seek(this.header.phoff + i * this.header.phentsize);
            this.pheaders[i] = new ELF.PHeader();
        }

        this.sheaders = new ELF.SHeader[this.header.shnum];

        for (i = 0; i < this.header.shnum; ++i) {
            seekable.seek(this.header.shoff + i * this.header.shentsize);
            this.sheaders[i] = new ELF.SHeader();
        }

        if (this.header.shstrndx >= 0 && this.header.shstrndx < this.header.shnum) {
            seekable.seek(this.sheaders[this.header.shstrndx].offset);
            this.stringTable = new byte[this.sheaders[this.header.shstrndx].size];
            this.readFully(this.stringTable);

            for (i = 0; i < this.header.shnum; ++i) {
                ELF.SHeader elf_sheader = this.sheaders[i];

                elf_sheader.name = this.getString(elf_sheader.nameidx);
            }

        } else {
            throw new ELF.ELFException("Bad shstrndx");
        }
    }

    private String getString(int i) {
        return this.getString(i, this.stringTable);
    }

    private String getString(int i, byte[] abyte) {
        StringBuffer stringbuffer = new StringBuffer();

        if (i >= 0 && i < abyte.length) {
            while (i >= 0 && i < abyte.length && abyte[i] != 0) {
                stringbuffer.append((char) abyte[i++]);
            }

            return stringbuffer.toString();
        } else {
            return "<invalid strtab entry>";
        }
    }

    public ELF.SHeader sectionWithName(String s) {
        for (int i = 0; i < this.sheaders.length; ++i) {
            if (this.sheaders[i].name.equals(s)) {
                return this.sheaders[i];
            }
        }

        return null;
    }

    public ELF.Symtab getSymtab() throws IOException {
        if (this._symtab != null) {
            return this._symtab;
        } else if (this.sectionReaderActive) {
            throw new ELF.ELFException("Can\'t read the symtab while a section reader is active");
        } else {
            ELF.SHeader elf_sheader = this.sectionWithName(".symtab");

            if (elf_sheader != null && elf_sheader.type == 2) {
                ELF.SHeader elf_sheader1 = this.sectionWithName(".strtab");

                if (elf_sheader1 != null && elf_sheader1.type == 3) {
                    byte[] abyte = new byte[elf_sheader1.size];
                    DataInputStream datainputstream = new DataInputStream(elf_sheader1.getInputStream());

                    datainputstream.readFully(abyte);
                    datainputstream.close();
                    return this._symtab = new ELF.Symtab(elf_sheader.offset, elf_sheader.size, abyte);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    private static String toHex(int i) {
        return "0x" + Long.toString((long) i & 4294967295L, 16);
    }

    public class Symbol {

        public String name;
        public int addr;
        public int size;
        public byte info;
        public byte type;
        public byte binding;
        public byte other;
        public short shndx;
        public ELF.SHeader sheader;
        public static final int STT_FUNC = 2;
        public static final int STB_GLOBAL = 1;

        Symbol(byte[] abyte) throws IOException {
            this.name = ELF.this.getString(ELF.this.readInt(), abyte);
            this.addr = ELF.this.readInt();
            this.size = ELF.this.readInt();
            this.info = ELF.this.readByte();
            this.type = (byte) (this.info & 15);
            this.binding = (byte) (this.info >> 4);
            this.other = ELF.this.readByte();
            this.shndx = ELF.this.readShort();
        }
    }

    public class Symtab {

        public ELF.Symbol[] symbols;

        Symtab(int i, int j, byte[] abyte) throws IOException {
            ELF.this.data.seek(i);
            int k = j / 16;

            this.symbols = new ELF.Symbol[k];

            for (int l = 0; l < k; ++l) {
                this.symbols[l] = ELF.this.new Symbol(abyte);
            }

        }

        public ELF.Symbol getSymbol(String s) {
            ELF.Symbol elf_symbol = null;

            for (int i = 0; i < this.symbols.length; ++i) {
                if (this.symbols[i].name.equals(s)) {
                    if (elf_symbol == null) {
                        elf_symbol = this.symbols[i];
                    } else {
                        System.err.println("WARNING: Multiple symbol matches for " + s);
                    }
                }
            }

            return elf_symbol;
        }

        public ELF.Symbol getGlobalSymbol(String s) {
            for (int i = 0; i < this.symbols.length; ++i) {
                if (this.symbols[i].binding == 1 && this.symbols[i].name.equals(s)) {
                    return this.symbols[i];
                }
            }

            return null;
        }
    }

    private class SectionInputStream extends InputStream {

        private int pos;
        private int maxpos;

        SectionInputStream(int i, int j) throws IOException {
            if (ELF.this.sectionReaderActive) {
                throw new IOException("Section reader already active");
            } else {
                ELF.this.sectionReaderActive = true;
                this.pos = i;
                ELF.this.data.seek(this.pos);
                this.maxpos = j;
            }
        }

        private int bytesLeft() {
            return this.maxpos - this.pos;
        }

        public int read() throws IOException {
            byte[] abyte = new byte[1];

            return this.read(abyte, 0, 1) == -1 ? -1 : abyte[0] & 255;
        }

        public int read(byte[] abyte, int i, int j) throws IOException {
            int k = ELF.this.data.read(abyte, i, Math.min(j, this.bytesLeft()));

            if (k > 0) {
                this.pos += k;
            }

            return k;
        }

        public void close() {
            ELF.this.sectionReaderActive = false;
        }
    }

    public class ELFException extends IOException {

        ELFException(String s) {
            super(s);
        }
    }

    public class SHeader {

        int nameidx = ELF.this.readInt();
        public String name;
        public int type = ELF.this.readInt();
        public int flags = ELF.this.readInt();
        public int addr = ELF.this.readInt();
        public int offset = ELF.this.readInt();
        public int size = ELF.this.readInt();
        public int link = ELF.this.readInt();
        public int info = ELF.this.readInt();
        public int addralign = ELF.this.readInt();
        public int entsize = ELF.this.readInt();

        SHeader() throws IOException {}

        public InputStream getInputStream() throws IOException {
            return new BufferedInputStream(ELF.this.new SectionInputStream(this.offset, this.type == 8 ? 0 : this.offset + this.size));
        }

        public boolean isText() {
            return this.name.equals(".text");
        }

        public boolean isData() {
            return this.name.equals(".data") || this.name.equals(".sdata") || this.name.equals(".rodata") || this.name.equals(".ctors") || this.name.equals(".dtors");
        }

        public boolean isBSS() {
            return this.name.equals(".bss") || this.name.equals(".sbss");
        }
    }

    public class PHeader {

        public int type = ELF.this.readInt();
        public int offset = ELF.this.readInt();
        public int vaddr = ELF.this.readInt();
        public int paddr = ELF.this.readInt();
        public int filesz = ELF.this.readInt();
        public int memsz = ELF.this.readInt();
        public int flags = ELF.this.readInt();
        public int align = ELF.this.readInt();

        PHeader() throws IOException {
            if (this.filesz > this.memsz) {
                throw ELF.this.new ELFException("ELF inconsistency: filesz > memsz (" + ELF.toHex(this.filesz) + " > " + ELF.toHex(this.memsz) + ")");
            }
        }

        public boolean writable() {
            return (this.flags & 2) != 0;
        }

        public InputStream getInputStream() throws IOException {
            return new BufferedInputStream(ELF.this.new SectionInputStream(this.offset, this.offset + this.filesz));
        }
    }

    public class ELFHeader {

        public short type = ELF.this.readShort();
        public short machine = ELF.this.readShort();
        public int version = ELF.this.readInt();
        public int entry;
        public int phoff;
        public int shoff;
        public int flags;
        public short ehsize;
        public short phentsize;
        public short phnum;
        public short shentsize;
        public short shnum;
        public short shstrndx;

        ELFHeader() throws IOException {
            if (this.version != 1) {
                throw ELF.this.new ELFException("version != 1");
            } else {
                this.entry = ELF.this.readInt();
                this.phoff = ELF.this.readInt();
                this.shoff = ELF.this.readInt();
                this.flags = ELF.this.readInt();
                this.ehsize = ELF.this.readShort();
                this.phentsize = ELF.this.readShort();
                this.phnum = ELF.this.readShort();
                this.shentsize = ELF.this.readShort();
                this.shnum = ELF.this.readShort();
                this.shstrndx = ELF.this.readShort();
            }
        }
    }

    public class ELFIdent {

        public byte klass;
        public byte data;
        public byte osabi;
        public byte abiversion;

        ELFIdent() throws IOException {
            if (ELF.this.readIntBE() != 2135247942) {
                throw ELF.this.new ELFException("Bad Magic");
            } else {
                this.klass = ELF.this.readByte();
                if (this.klass != 1) {
                    throw ELF.this.new ELFException("org.bukkit.craftbukkit.libs.org.ibex.nestedvm.util.ELF does not suport 64-bit binaries");
                } else {
                    this.data = ELF.this.readByte();
                    if (this.data != 1 && this.data != 2) {
                        throw ELF.this.new ELFException("Unknown byte order");
                    } else {
                        ELF.this.readByte();
                        this.osabi = ELF.this.readByte();
                        this.abiversion = ELF.this.readByte();

                        for (int i = 0; i < 7; ++i) {
                            ELF.this.readByte();
                        }

                    }
                }
            }
        }
    }
}
