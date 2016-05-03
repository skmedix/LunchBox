package org.bukkit.entity;

public interface Rabbit extends Animals {

    Rabbit.Type getRabbitType();

    void setRabbitType(Rabbit.Type rabbit_type);

    public static enum Type {

        BROWN, WHITE, BLACK, BLACK_AND_WHITE, GOLD, SALT_AND_PEPPER, THE_KILLER_BUNNY;
    }
}
