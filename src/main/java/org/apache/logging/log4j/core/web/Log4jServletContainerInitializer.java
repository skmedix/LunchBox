package org.apache.logging.log4j.core.web;

import java.util.EnumSet;
import java.util.Set;
import javax.servlet.DispatcherType;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.FilterRegistration.Dynamic;

public class Log4jServletContainerInitializer implements ServletContainerInitializer {

    public void onStartup(Set set, ServletContext servletcontext) throws ServletException {
        if (servletcontext.getMajorVersion() > 2) {
            servletcontext.log("Log4jServletContainerInitializer starting up Log4j in Servlet 3.0+ environment.");
            Log4jWebInitializer log4jwebinitializer = Log4jWebInitializerImpl.getLog4jWebInitializer(servletcontext);

            log4jwebinitializer.initialize();
            log4jwebinitializer.setLoggerContext();
            servletcontext.addListener(new Log4jServletContextListener());
            Dynamic dynamic = servletcontext.addFilter("log4jServletFilter", new Log4jServletFilter());

            if (dynamic == null) {
                throw new UnavailableException("In a Servlet 3.0+ application, you must not define a log4jServletFilter in web.xml. Log4j 2 defines this for you automatically.");
            }

            dynamic.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, new String[] { "/*"});
        }

    }
}
