package org.apache.logging.log4j.core.appender.db.jpa;

import java.lang.reflect.Constructor;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.db.AbstractDatabaseAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.helpers.Strings;

@Plugin(
    name = "JPA",
    category = "Core",
    elementType = "appender",
    printObject = true
)
public final class JPAAppender extends AbstractDatabaseAppender {

    private final String description = this.getName() + "{ manager=" + this.getManager() + " }";

    private JPAAppender(String s, Filter filter, boolean flag, JPADatabaseManager jpadatabasemanager) {
        super(s, filter, flag, jpadatabasemanager);
    }

    public String toString() {
        return this.description;
    }

    @PluginFactory
    public static JPAAppender createAppender(@PluginAttribute("name") String s, @PluginAttribute("ignoreExceptions") String s1, @PluginElement("Filter") Filter filter, @PluginAttribute("bufferSize") String s2, @PluginAttribute("entityClassName") String s3, @PluginAttribute("persistenceUnitName") String s4) {
        if (!Strings.isEmpty(s3) && !Strings.isEmpty(s4)) {
            int i = AbstractAppender.parseInt(s2, 0);
            boolean flag = Booleans.parseBoolean(s1, true);

            try {
                Class oclass = Class.forName(s3);

                if (!AbstractLogEventWrapperEntity.class.isAssignableFrom(oclass)) {
                    JPAAppender.LOGGER.error("Entity class [{}] does not extend AbstractLogEventWrapperEntity.", new Object[] { s3});
                    return null;
                } else {
                    try {
                        oclass.getConstructor(new Class[0]);
                    } catch (NoSuchMethodException nosuchmethodexception) {
                        JPAAppender.LOGGER.error("Entity class [{}] does not have a no-arg constructor. The JPA provider will reject it.", new Object[] { s3});
                        return null;
                    }

                    Constructor constructor = oclass.getConstructor(new Class[] { LogEvent.class});
                    String s5 = "jpaManager{ description=" + s + ", bufferSize=" + i + ", persistenceUnitName=" + s4 + ", entityClass=" + oclass.getName() + "}";
                    JPADatabaseManager jpadatabasemanager = JPADatabaseManager.getJPADatabaseManager(s5, i, oclass, constructor, s4);

                    return jpadatabasemanager == null ? null : new JPAAppender(s, filter, flag, jpadatabasemanager);
                }
            } catch (ClassNotFoundException classnotfoundexception) {
                JPAAppender.LOGGER.error("Could not load entity class [{}].", new Object[] { s3, classnotfoundexception});
                return null;
            } catch (NoSuchMethodException nosuchmethodexception1) {
                JPAAppender.LOGGER.error("Entity class [{}] does not have a constructor with a single argument of type LogEvent.", new Object[] { s3});
                return null;
            }
        } else {
            JPAAppender.LOGGER.error("Attributes entityClassName and persistenceUnitName are required for JPA Appender.");
            return null;
        }
    }
}
