package org.bukkit;

public enum Rotation {

    NONE, CLOCKWISE_45, CLOCKWISE, CLOCKWISE_135, FLIPPED, FLIPPED_45, COUNTER_CLOCKWISE, COUNTER_CLOCKWISE_45;

    private static final Rotation[] rotations = values();

    public Rotation rotateClockwise() {
        return Rotation.rotations[this.ordinal() + 1 & 7];
    }

    public Rotation rotateCounterClockwise() {
        return Rotation.rotations[this.ordinal() - 1 & 7];
    }
}
