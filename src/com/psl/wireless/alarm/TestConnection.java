package com.psl.wireless.alarm;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestConnection {
  public static void main(String[] args) throws Exception {
    Log.show("About to test Source connection.");
    Connection sourceConnection = BootStrap.getSourceConnection();
    if (sourceConnection == null) {
      Log.show("Source connection is NULL.");
    } else {
      Statement statement = sourceConnection.createStatement();
      ResultSet resultSet = statement.executeQuery(
          "select 1 as \"val\" from dual");
      while (resultSet.next()) {
        Log.show("Found " + resultSet.getObject("val"));
      }
    }
    Log.show("Finished tesing Source connection.");
  }
}
