package com.psl.wireless.alarm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class BootStrap {
  public static String getOracleClassName() {
    return "oracle.jdbc.driver.OracleDriver";
  }
  
  public static void validateClass() {
    try {
      Class.forName(BootStrap.getOracleClassName()); 
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public static Connection getSourceConnection() {
    // SOURCE INFORMATION
    String sourceDbName = "10.51.238.20";
    String sourceDbPort = "1522";
    String sourceSid = "vtdb";
    String sourceDbUserName = "virtuo";
    String sourceDbPassword = "Virtuo01";

    String sourceJdbcUrl = "jdbc:oracle:thin:@";
    sourceJdbcUrl += sourceDbName + ":";
    sourceJdbcUrl += sourceDbPort + ":";
    sourceJdbcUrl += sourceSid;

    Properties sourceCredentials = new Properties();
    sourceCredentials.put("user", sourceDbUserName);
    sourceCredentials.put("password", sourceDbPassword);
    
    try {
      return DriverManager.getConnection(sourceJdbcUrl, sourceCredentials);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    
    return null;
  }

  public static Connection getSinkConnection() {
    // SINK INFORMATION
    String sinkDbName = "10.211.50.18";
    String sinkDbPort = "1521";
    String sinkSid = "vtdb";
    String sinkDbUserName = "virtuo";
    String sinkDbPassword = "Virtuo01";

    String sinkJdbcUrl = "jdbc:oracle:thin:@";
    sinkJdbcUrl += sinkDbName + ":";
    sinkJdbcUrl += sinkDbPort + ":";
    sinkJdbcUrl += sinkSid;

    Properties sinkCredentials = new Properties();
    sinkCredentials.put("user", sinkDbUserName);
    sinkCredentials.put("password", sinkDbPassword);

    try {
      return DriverManager.getConnection(sinkJdbcUrl, sinkCredentials);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    
    return null;
  }
}
