package org.bukkit.block;

public interface CommandBlock extends BlockState {

    String getCommand();

    void setCommand(String s);

    String getName();

    void setName(String s);
}
