package com.psl.wireless.technologypack;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.psl.wireless.alarm.Log;

public class ReportModel {
  public final static String REPORT_INFO_SQL =
      "select" + " " 
      + "tbl1.DOCUMENT_ID as \"DOCUMENT_ID\", tbl1.NAME as \"DOCUMENT_NAME\"," + " "
      + "tbl1.PARENT_DOC_ID as \"FOLDER_ID\", tbl2.NAME as \"FOLDER_NAME\"" + " "
      + "from" + " " 
      + "pm_document tbl1" + " " // Report
      + "inner join" + " "
      + "pm_document tbl2" + " " // Report Folder
      + "on" + " " 
      + "tbl2.DOCUMENT_ID = tbl1.PARENT_DOC_ID" + " "
      + "where" + " "
      + "tbl1.DOCUMENT_TYPE_ID = 2" + " "
      + "and tbl2.NAME = 'sysadm'";
 
  public static ArrayList<ReportDefinition> getReportDefinitions(Connection connection) {
    Log.show("This program assumes that the Report Folder will always be 'sysadm', "
        + "else need to raise enhancement.");
    ArrayList<ReportDefinition> x = new ArrayList<ReportDefinition>();
    try {
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery(REPORT_INFO_SQL);
      while(resultSet.next()) {
        ReportDefinition r = new ReportDefinition(
            resultSet.getObject("DOCUMENT_ID").toString(),
            resultSet.getObject("DOCUMENT_NAME").toString(),
            resultSet.getObject("FOLDER_ID").toString());
        x.add(r);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return x;
  }
}
