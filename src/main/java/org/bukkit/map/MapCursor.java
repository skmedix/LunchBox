package org.bukkit.map;

public final class MapCursor {

    private byte x;
    private byte y;
    private byte direction;
    private byte type;
    private boolean visible;

    /** @deprecated */
    @Deprecated
    public MapCursor(byte x, byte y, byte direction, byte type, boolean visible) {
        this.x = x;
        this.y = y;
        this.setDirection(direction);
        this.setRawType(type);
        this.visible = visible;
    }

    public byte getX() {
        return this.x;
    }

    public byte getY() {
        return this.y;
    }

    public byte getDirection() {
        return this.direction;
    }

    public MapCursor.Type getType() {
        return MapCursor.Type.byValue(this.type);
    }

    /** @deprecated */
    @Deprecated
    public byte getRawType() {
        return this.type;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setX(byte x) {
        this.x = x;
    }

    public void setY(byte y) {
        this.y = y;
    }

    public void setDirection(byte direction) {
        if (direction >= 0 && direction <= 15) {
            this.direction = direction;
        } else {
            throw new IllegalArgumentException("Direction must be in the range 0-15");
        }
    }

    public void setType(MapCursor.Type type) {
        this.setRawType(type.value);
    }

    /** @deprecated */
    @Deprecated
    public void setRawType(byte type) {
        if (type >= 0 && type <= 15) {
            this.type = type;
        } else {
            throw new IllegalArgumentException("Type must be in the range 0-15");
        }
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public static enum Type {

        WHITE_POINTER(0), GREEN_POINTER(1), RED_POINTER(2), BLUE_POINTER(3), WHITE_CROSS(4);

        private byte value;

        private Type(int value) {
            this.value = (byte) value;
        }

        /** @deprecated */
        @Deprecated
        public byte getValue() {
            return this.value;
        }

        /** @deprecated */
        @Deprecated
        public static MapCursor.Type byValue(byte value) {
            MapCursor.Type[] amapcursor_type;
            int i = (amapcursor_type = values()).length;

            for (int j = 0; j < i; ++j) {
                MapCursor.Type t = amapcursor_type[j];

                if (t.value == value) {
                    return t;
                }
            }

            return null;
        }
    }
}
