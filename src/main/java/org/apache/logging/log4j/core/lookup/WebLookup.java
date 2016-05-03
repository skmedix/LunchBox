package org.apache.logging.log4j.core.lookup;

import javax.servlet.ServletContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.impl.ContextAnchor;

@Plugin(
    name = "web",
    category = "Lookup"
)
public class WebLookup implements StrLookup {

    private static final String ATTR_PREFIX = "attr.";
    private static final String INIT_PARAM_PREFIX = "initParam.";

    protected ServletContext getServletContext() {
        LoggerContext loggercontext = (LoggerContext) ContextAnchor.THREAD_CONTEXT.get();

        if (loggercontext == null) {
            loggercontext = (LoggerContext) LogManager.getContext(false);
        }

        if (loggercontext == null) {
            return null;
        } else {
            Object object = loggercontext.getExternalContext();

            return object != null && object instanceof ServletContext ? (ServletContext) object : null;
        }
    }

    public String lookup(String s) {
        ServletContext servletcontext = this.getServletContext();

        if (servletcontext == null) {
            return null;
        } else {
            String s1;

            if (s.startsWith("attr.")) {
                s1 = s.substring("attr.".length());
                Object object = servletcontext.getAttribute(s1);

                return object == null ? null : object.toString();
            } else if (s.startsWith("initParam.")) {
                s1 = s.substring("initParam.".length());
                return servletcontext.getInitParameter(s1);
            } else if ("rootDir".equals(s)) {
                s1 = servletcontext.getRealPath("/");
                if (s1 == null) {
                    String s2 = "failed to resolve web:rootDir -- servlet container unable to translate virtual path  to real path (probably not deployed as exploded";

                    throw new RuntimeException(s2);
                } else {
                    return s1;
                }
            } else if ("contextPath".equals(s)) {
                return servletcontext.getContextPath();
            } else if ("servletContextName".equals(s)) {
                return servletcontext.getServletContextName();
            } else if ("serverInfo".equals(s)) {
                return servletcontext.getServerInfo();
            } else if ("effectiveMajorVersion".equals(s)) {
                return String.valueOf(servletcontext.getEffectiveMajorVersion());
            } else if ("effectiveMinorVersion".equals(s)) {
                return String.valueOf(servletcontext.getEffectiveMinorVersion());
            } else if ("majorVersion".equals(s)) {
                return String.valueOf(servletcontext.getMajorVersion());
            } else if ("minorVersion".equals(s)) {
                return String.valueOf(servletcontext.getMinorVersion());
            } else if (servletcontext.getAttribute(s) != null) {
                return servletcontext.getAttribute(s).toString();
            } else if (servletcontext.getInitParameter(s) != null) {
                return servletcontext.getInitParameter(s);
            } else {
                servletcontext.log(this.getClass().getName() + " unable to resolve key \'" + s + "\'");
                return null;
            }
        }
    }

    public String lookup(LogEvent logevent, String s) {
        return this.lookup(s);
    }
}
