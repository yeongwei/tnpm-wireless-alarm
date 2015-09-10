package com.psl.wireless.alarm;

import java.io.File;
import java.util.logging.Logger;

public class LogFacilitator {
  protected static Logger LOGGER;
  
  public LogFacilitator () {
    LOGGER = Logger.getLogger("TnpmWirelessAlarmMigrator");
  }
  
  public LogFacilitator (String className) {
    LOGGER = Logger.getLogger(className);
  }
  
  public void servere(String msg) {
    LOGGER.severe(msg);
  }
  
  public void warning(String msg) {
    LOGGER.warning(msg);
  }
  
  public void info(String msg) {
    LOGGER.info(msg);
  }
  
  public void config(String msg) {
    LOGGER.config(msg);
  }
  
  public void fine(String msg) {
    LOGGER.fine(msg);
  }
  
  public void finer(String msg) {
    LOGGER.finer(msg);
  }
  
  public void finest(String msg) {
    LOGGER.finest(msg);
  }
  
  public static void main(String[] args) {
    System.out.println("java.util.logging.config.file - " 
        + System.getProperty("java.util.logging.config.file"));
    
    File file = new File(System.getProperty("java.util.logging.config.file"));
    System.out.println("logging.properties exist - " + file.exists());
    
    LogFacilitator logFacilitator = new LogFacilitator(UpdateAlarms.class.getName());
    
    System.out.println("LoggerName - " + logFacilitator.LOGGER.getName());
    
    logFacilitator.servere("This is servere.");
    logFacilitator.warning("This is warning.");
    logFacilitator.info("This is info.");
    logFacilitator.config("This is config.");
    logFacilitator.fine("This is fine.");
    logFacilitator.finer("This is finer.");
    logFacilitator.finest("This is finer.");
  }
}