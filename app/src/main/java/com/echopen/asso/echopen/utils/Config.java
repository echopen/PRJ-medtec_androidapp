package com.echopen.asso.echopen.utils;

/**
 * Singleton Method for Configuration
 */
public class Config {

    private static Config config = null;
    private String state;
    private Constants constants;

    private Config(){}

    public static Config getInstance() {
        if (config == null) {
            config = new Config();
        }
        return config;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Constants getConstants() {
        return constants;
    }

    public void setConstants(Constants constants) {
        this.constants = constants;
    }

    public void loadConstants(){
        constants = new Constants();
    }
}
