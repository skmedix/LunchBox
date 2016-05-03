package org.apache.logging.log4j.core.net.ssl;

import org.apache.logging.log4j.status.StatusLogger;

public class StoreConfiguration {

    protected static final StatusLogger LOGGER = StatusLogger.getLogger();
    private String location;
    private String password;

    public StoreConfiguration(String s, String s1) {
        this.location = s;
        this.password = s1;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String s) {
        this.location = s;
    }

    public String getPassword() {
        return this.password;
    }

    public char[] getPasswordAsCharArray() {
        return this.password == null ? null : this.password.toCharArray();
    }

    public void setPassword(String s) {
        this.password = s;
    }

    public boolean equals(StoreConfiguration storeconfiguration) {
        if (storeconfiguration == null) {
            return false;
        } else {
            boolean flag = false;
            boolean flag1 = false;

            if (this.location != null) {
                flag = this.location.equals(storeconfiguration.location);
            } else {
                flag = this.location == storeconfiguration.location;
            }

            if (this.password != null) {
                flag1 = this.password.equals(storeconfiguration.password);
            } else {
                flag1 = this.password == storeconfiguration.password;
            }

            return flag && flag1;
        }
    }

    protected void load() throws StoreConfigurationException {}
}
