package org.bukkit.entity;

public interface Ocelot extends Animals, Tameable {

    Ocelot.Type getCatType();

    void setCatType(Ocelot.Type ocelot_type);

    boolean isSitting();

    void setSitting(boolean flag);

    public static enum Type {

        WILD_OCELOT(0), BLACK_CAT(1), RED_CAT(2), SIAMESE_CAT(3);

        private static final Ocelot.Type[] types = new Ocelot.Type[values().length];
        private final int id;

        static {
            Ocelot.Type[] aocelot_type;
            int i = (aocelot_type = values()).length;

            for (int j = 0; j < i; ++j) {
                Ocelot.Type type = aocelot_type[j];

                Ocelot.Type.types[type.getId()] = type;
            }

        }

        private Type(int id) {
            this.id = id;
        }

        /** @deprecated */
        @Deprecated
        public int getId() {
            return this.id;
        }

        /** @deprecated */
        @Deprecated
        public static Ocelot.Type getType(int id) {
            return id >= Ocelot.Type.types.length ? null : Ocelot.Type.types[id];
        }
    }
}
