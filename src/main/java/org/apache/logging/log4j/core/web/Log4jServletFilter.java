package org.apache.logging.log4j.core.web;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class Log4jServletFilter implements Filter {

    static final String ALREADY_FILTERED_ATTRIBUTE = Log4jServletFilter.class.getName() + ".FILTERED";
    private ServletContext servletContext;
    private Log4jWebInitializer initializer;

    public void init(FilterConfig filterconfig) throws ServletException {
        this.servletContext = filterconfig.getServletContext();
        this.servletContext.log("Log4jServletFilter initialized.");
        this.initializer = Log4jWebInitializerImpl.getLog4jWebInitializer(this.servletContext);
        this.initializer.clearLoggerContext();
    }

    public void doFilter(ServletRequest servletrequest, ServletResponse servletresponse, FilterChain filterchain) throws IOException, ServletException {
        if (servletrequest.getAttribute(Log4jServletFilter.ALREADY_FILTERED_ATTRIBUTE) != null) {
            filterchain.doFilter(servletrequest, servletresponse);
        } else {
            servletrequest.setAttribute(Log4jServletFilter.ALREADY_FILTERED_ATTRIBUTE, Boolean.valueOf(true));

            try {
                this.initializer.setLoggerContext();
                filterchain.doFilter(servletrequest, servletresponse);
            } finally {
                this.initializer.clearLoggerContext();
            }
        }

    }

    public void destroy() {
        if (this.servletContext != null && this.initializer != null) {
            this.servletContext.log("Log4jServletFilter destroyed.");
            this.initializer.setLoggerContext();
        } else {
            throw new IllegalStateException("Filter destroyed before it was initialized.");
        }
    }
}
