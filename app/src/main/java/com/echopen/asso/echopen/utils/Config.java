package com.echopen.asso.echopen.utils;

/**
 * Singleton Method for Configuration
 */
public class Config {

    private Config config;
    private String state;
    private Constants constants;

    public Config Config(){
      if(config.getState() == null) {
          Config("on");
      }
      return config;
    }

    public Config Config(String string){
        config.setState("on");
        loadConstants();
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
