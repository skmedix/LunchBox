package org.apache.logging.log4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class MarkerManager {

    private static ConcurrentMap markerMap = new ConcurrentHashMap();

    public static Marker getMarker(String s) {
        MarkerManager.markerMap.putIfAbsent(s, new MarkerManager.Log4jMarker(s));
        return (Marker) MarkerManager.markerMap.get(s);
    }

    public static Marker getMarker(String s, String s1) {
        Marker marker = (Marker) MarkerManager.markerMap.get(s1);

        if (marker == null) {
            throw new IllegalArgumentException("Parent Marker " + s1 + " has not been defined");
        } else {
            return getMarker(s, marker);
        }
    }

    public static Marker getMarker(String s, Marker marker) {
        MarkerManager.markerMap.putIfAbsent(s, new MarkerManager.Log4jMarker(s, marker));
        return (Marker) MarkerManager.markerMap.get(s);
    }

    private static class Log4jMarker implements Marker {

        private static final long serialVersionUID = 100L;
        private final String name;
        private final Marker parent;

        public Log4jMarker(String s) {
            this.name = s;
            this.parent = null;
        }

        public Log4jMarker(String s, Marker marker) {
            this.name = s;
            this.parent = marker;
        }

        public String getName() {
            return this.name;
        }

        public Marker getParent() {
            return this.parent;
        }

        public boolean isInstanceOf(Marker marker) {
            if (marker == null) {
                throw new IllegalArgumentException("A marker parameter is required");
            } else {
                Object object = this;

                while (object != marker) {
                    object = ((Marker) object).getParent();
                    if (object == null) {
                        return false;
                    }
                }

                return true;
            }
        }

        public boolean isInstanceOf(String s) {
            if (s == null) {
                throw new IllegalArgumentException("A marker name is required");
            } else {
                Object object = this;

                while (!s.equals(((Marker) object).getName())) {
                    object = ((Marker) object).getParent();
                    if (object == null) {
                        return false;
                    }
                }

                return true;
            }
        }

        public boolean equals(Object object) {
            if (this == object) {
                return true;
            } else if (object != null && object instanceof Marker) {
                Marker marker = (Marker) object;

                if (this.name != null) {
                    if (!this.name.equals(marker.getName())) {
                        return false;
                    }
                } else if (marker.getName() != null) {
                    return false;
                }

                return true;
            } else {
                return false;
            }
        }

        public int hashCode() {
            return this.name != null ? this.name.hashCode() : 0;
        }

        public String toString() {
            StringBuilder stringbuilder = new StringBuilder(this.name);

            if (this.parent != null) {
                Marker marker = this.parent;

                stringbuilder.append("[ ");

                for (boolean flag = true; marker != null; marker = marker.getParent()) {
                    if (!flag) {
                        stringbuilder.append(", ");
                    }

                    stringbuilder.append(marker.getName());
                    flag = false;
                }

                stringbuilder.append(" ]");
            }

            return stringbuilder.toString();
        }
    }
}
