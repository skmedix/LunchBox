package org.bukkit.entity;

public interface Villager extends Ageable, NPC {

    Villager.Profession getProfession();

    void setProfession(Villager.Profession villager_profession);

    public static enum Profession {

        FARMER(0), LIBRARIAN(1), PRIEST(2), BLACKSMITH(3), BUTCHER(4);

        private static final Villager.Profession[] professions = new Villager.Profession[values().length];
        private final int id;

        static {
            Villager.Profession[] avillager_profession;
            int i = (avillager_profession = values()).length;

            for (int j = 0; j < i; ++j) {
                Villager.Profession type = avillager_profession[j];

                Villager.Profession.professions[type.getId()] = type;
            }

        }

        private Profession(int id) {
            this.id = id;
        }

        /** @deprecated */
        @Deprecated
        public int getId() {
            return this.id;
        }

        /** @deprecated */
        @Deprecated
        public static Villager.Profession getProfession(int id) {
            return id >= Villager.Profession.professions.length ? null : Villager.Profession.professions[id];
        }
    }
}
