package org.bukkit.entity;

public interface Skeleton extends Monster {

    Skeleton.SkeletonType getSkeletonType();

    void setSkeletonType(Skeleton.SkeletonType skeleton_skeletontype);

    public static enum SkeletonType {

        NORMAL(0), WITHER(1);

        private static final Skeleton.SkeletonType[] types = new Skeleton.SkeletonType[values().length];
        private final int id;

        static {
            Skeleton.SkeletonType[] askeleton_skeletontype;
            int i = (askeleton_skeletontype = values()).length;

            for (int j = 0; j < i; ++j) {
                Skeleton.SkeletonType type = askeleton_skeletontype[j];

                Skeleton.SkeletonType.types[type.getId()] = type;
            }

        }

        private SkeletonType(int id) {
            this.id = id;
        }

        /** @deprecated */
        @Deprecated
        public int getId() {
            return this.id;
        }

        /** @deprecated */
        @Deprecated
        public static Skeleton.SkeletonType getType(int id) {
            return id >= Skeleton.SkeletonType.types.length ? null : Skeleton.SkeletonType.types[id];
        }
    }
}
