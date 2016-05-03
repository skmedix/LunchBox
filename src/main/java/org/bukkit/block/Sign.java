package org.bukkit.block;

public interface Sign extends BlockState {

    String[] getLines();

    String getLine(int i) throws IndexOutOfBoundsException;

    void setLine(int i, String s) throws IndexOutOfBoundsException;
}
