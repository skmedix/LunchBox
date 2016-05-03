package org.bukkit.block;

import org.bukkit.Instrument;
import org.bukkit.Note;

public interface NoteBlock extends BlockState {

    Note getNote();

    /** @deprecated */
    @Deprecated
    byte getRawNote();

    void setNote(Note note);

    /** @deprecated */
    @Deprecated
    void setRawNote(byte b0);

    boolean play();

    /** @deprecated */
    @Deprecated
    boolean play(byte b0, byte b1);

    boolean play(Instrument instrument, Note note);
}
