package com.echopen.asso.echopen.utils;

/**
 * Singleton Method for Configuration
 */
public class Config {

    private static Config config;
    private String state;
    private Constants constants;

    public static Config Config(){
      if(config.getState() == null) {
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
