package org.apache.logging.log4j.core.lookup;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(
    name = "jndi",
    category = "Lookup"
)
public class JndiLookup implements StrLookup {

    static final String CONTAINER_JNDI_RESOURCE_PATH_PREFIX = "java:comp/env/";

    public String lookup(String s) {
        return this.lookup((LogEvent) null, s);
    }

    public String lookup(LogEvent logevent, String s) {
        if (s == null) {
            return null;
        } else {
            try {
                InitialContext initialcontext = new InitialContext();

                return (String) initialcontext.lookup(this.convertJndiName(s));
            } catch (NamingException namingexception) {
                return null;
            }
        }
    }

    private String convertJndiName(String s) {
        if (!s.startsWith("java:comp/env/") && s.indexOf(58) == -1) {
            s = "java:comp/env/" + s;
        }

        return s;
    }
}
