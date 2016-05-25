package org.bukkit;

import com.google.common.collect.Maps;
import java.util.Map;
import org.apache.commons.lang3.Validate;

public class Note {

    private final byte note;

    public Note(int note) {
        Validate.isTrue(note >= 0 && note <= 24, "The note value has to be between 0 and 24.");
        this.note = (byte) note;
    }

    public Note(int octave, Note.Tone tone, boolean sharped) {
        if (sharped && !tone.isSharpable()) {
            tone = Note.Tone.values()[tone.ordinal() + 1];
            sharped = false;
        }

        if (octave >= 0 && octave <= 2 && (octave != 2 || tone == Note.Tone.F && sharped)) {
            this.note = (byte) (octave * 12 + tone.getId(sharped));
        } else {
            throw new IllegalArgumentException("Tone and octave have to be between F#0 and F#2");
        }
    }

    public static Note flat(int octave, Note.Tone tone) {
        Validate.isTrue(octave != 2, "Octave cannot be 2 for flats");
        tone = tone == Note.Tone.G ? Note.Tone.F : Note.Tone.values()[tone.ordinal() - 1];
        return new Note(octave, tone, tone.isSharpable());
    }

    public static Note sharp(int octave, Note.Tone tone) {
        return new Note(octave, tone, true);
    }

    public static Note natural(int octave, Note.Tone tone) {
        Validate.isTrue(octave != 2, "Octave cannot be 2 for naturals");
        return new Note(octave, tone, false);
    }

    public Note sharped() {
        Validate.isTrue(this.note < 24, "This note cannot be sharped because it is the highest known note!");
        return new Note(this.note + 1);
    }

    public Note flattened() {
        Validate.isTrue(this.note > 0, "This note cannot be flattened because it is the lowest known note!");
        return new Note(this.note - 1);
    }

    /** @deprecated */
    @Deprecated
    public byte getId() {
        return this.note;
    }

    public int getOctave() {
        return this.note / 12;
    }

    private byte getToneByte() {
        return (byte) (this.note % 12);
    }

    public Note.Tone getTone() {
        return Note.Tone.getById(this.getToneByte());
    }

    public boolean isSharped() {
        byte note = this.getToneByte();

        return Note.Tone.getById(note).isSharped(note);
    }

    public int hashCode() {
        byte result = 1;
        int result1 = 31 * result + this.note;

        return result1;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            Note other = (Note) obj;

            return this.note == other.note;
        }
    }

    public String toString() {
        return "Note{" + this.getTone().toString() + (this.isSharped() ? "#" : "") + "}";
    }

    public static enum Tone {

        G(1, true), A(3, true), B(5, false), C(6, true), D(8, true), E(10, false), F(11, true);

        private final boolean sharpable;
        private final byte id;
        private static final Map BY_DATA = Maps.newHashMap();
        public static final byte TONES_COUNT = 12;

        static {
            Note.Tone[] anote_tone;
            int i = (anote_tone = values()).length;

            for (int j = 0; j < i; ++j) {
                Note.Tone tone = anote_tone[j];
                int id = tone.id % 12;

                Note.Tone.BY_DATA.put(Byte.valueOf((byte) id), tone);
                if (tone.isSharpable()) {
                    id = (id + 1) % 12;
                    Note.Tone.BY_DATA.put(Byte.valueOf((byte) id), tone);
                }
            }

        }

        private Tone(int id, boolean sharpable) {
            this.id = (byte) (id % 12);
            this.sharpable = sharpable;
        }

        /** @deprecated */
        @Deprecated
        public byte getId() {
            return this.getId(false);
        }

        /** @deprecated */
        @Deprecated
        public byte getId(boolean sharped) {
            byte id = (byte) (sharped && this.sharpable ? this.id + 1 : this.id);

            return (byte) (id % 12);
        }

        public boolean isSharpable() {
            return this.sharpable;
        }

        /** @deprecated */
        @Deprecated
        public boolean isSharped(byte id) {
            if (id == this.getId(false)) {
                return false;
            } else if (id == this.getId(true)) {
                return true;
            } else {
                throw new IllegalArgumentException("The id isn\'t matching to the tone.");
            }
        }

        /** @deprecated */
        @Deprecated
        public static Note.Tone getById(byte id) {
            return (Note.Tone) Note.Tone.BY_DATA.get(Byte.valueOf(id));
        }
    }
}
