package org.apache.logging.log4j.core.helpers;

import org.apache.logging.log4j.status.StatusLogger;

public final class ClockFactory {

    public static final String PROPERTY_NAME = "log4j.Clock";
    private static final StatusLogger LOGGER = StatusLogger.getLogger();

    public static Clock getClock() {
        return createClock();
    }

    private static Clock createClock() {
        String s = System.getProperty("log4j.Clock");

        if (s != null && !"SystemClock".equals(s)) {
            if (!CachedClock.class.getName().equals(s) && !"CachedClock".equals(s)) {
                if (!CoarseCachedClock.class.getName().equals(s) && !"CoarseCachedClock".equals(s)) {
                    try {
                        Clock clock = (Clock) Class.forName(s).newInstance();

                        ClockFactory.LOGGER.debug("Using {} for timestamps", new Object[] { s});
                        return clock;
                    } catch (Exception exception) {
                        String s1 = "Could not create {}: {}, using default SystemClock for timestamps";

                        ClockFactory.LOGGER.error("Could not create {}: {}, using default SystemClock for timestamps", new Object[] { s, exception});
                        return new SystemClock();
                    }
                } else {
                    ClockFactory.LOGGER.debug("Using specified CoarseCachedClock for timestamps");
                    return CoarseCachedClock.instance();
                }
            } else {
                ClockFactory.LOGGER.debug("Using specified CachedClock for timestamps");
                return CachedClock.instance();
            }
        } else {
            ClockFactory.LOGGER.debug("Using default SystemClock for timestamps");
            return new SystemClock();
        }
    }
}
