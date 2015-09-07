package com.psl.wireless.technologypack;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.psl.wireless.alarm.Log;

public class AlarmModel {
  public static ArrayList<AlarmTemplateDefinition> getAlarmTemplates(Connection connection) {
    String SQL = 
        " select * from ("
        + "select CONTEXT_ID, TEMPLATE_NAME, TIME_STAMP, VERSION_ID," + " "
        + "extractValue(xmltype(XML_DOCUMENT), '/AlarmTemplate/General/MappedReportName') as \"MAPPED_REPORT_NAME\"," + " "
        + "extractValue(xmltype(XML_DOCUMENT), '/AlarmTemplate/General/MappedReportID') as \"MAPPED_REPORT_ID\"," + " "
        + "extractValue(xmltype(XML_DOCUMENT), '/AlarmTemplate/General/MappedReportFolderName') as \"MAPPED_REPORT_FOLDER_NAME\"," + " "
        + "extractValue(xmltype(XML_DOCUMENT), '/AlarmTemplate/LoaderParameters/Ruleset') as \"RULE_SET_ID\""
        + "from" + " "
        + "alarm_templates) tbl" + " "
        + "where MAPPED_REPORT_NAME != '(null)'" + " "
        + "and MAPPED_REPORT_ID != '(null)'" + " "
        + "and MAPPED_REPORT_FOLDER_NAME != '(null)'" + " "
        + "and RULE_SET_ID != '(null)'";
    ArrayList<AlarmTemplateDefinition> x = new ArrayList<AlarmTemplateDefinition>();
    try {
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery(SQL);
      while(resultSet.next()) {
        /*
        if(resultSet.getObject("MAPPED_REPORT_NAME") == null || resultSet.getObject("MAPPED_REPORT_ID") == null ||
            resultSet.getObject("MAPPED_REPORT_FOLDER_NAME") == null || resultSet.getObject("RULE_SET_ID") == null) {
          Log.show("Skipping records with NULLs for " 
            + resultSet.getObject("TEMPLATE_NAME") 
            + "|" 
            + resultSet.getObject("VERSION_ID"));
        } else {
        */
          AlarmTemplateDefinition t = new AlarmTemplateDefinition(
              resultSet.getObject("CONTEXT_ID").toString(),
              resultSet.getObject("TEMPLATE_NAME").toString(),
              resultSet.getObject("TIME_STAMP").toString(),
              resultSet.getObject("VERSION_ID").toString(),
              resultSet.getObject("MAPPED_REPORT_NAME").toString(),
              resultSet.getObject("MAPPED_REPORT_ID").toString(),
              resultSet.getObject("MAPPED_REPORT_FOLDER_NAME").toString(),
              resultSet.getObject("RULE_SET_ID").toString());
          x.add(t);
        /*}*/
      }
    } catch (Exception ex) {
     ex.printStackTrace(); 
    }
    return x;
  }
  
  public static ArrayList<AlarmDefinition> getAlarmDefinitions(Connection connection) {
    String SQL = "select * from (" + " "
        + "select" + " "
        + "ALARM_ID, CONTEXT_ID, DEFINITION_NAME, IS_ACTIVE, TIME_STAMP, VERSION_ID," + " "
        + "extractValue(xmltype(XML_DOCUMENT), '/AlarmDefinition/General/MappedReportName') as \"MAPPED_REPORT_NAME\"," + " "
        + "extractValue(xmltype(XML_DOCUMENT), '/AlarmDefinition/General/MappedReportID') as \"MAPPED_REPORT_ID\"," + " "
        + "extractValue(xmltype(XML_DOCUMENT), '/AlarmDefinition/General/MappedReportFolderName') as \"MAPPED_REPORT_FOLDER_NAME\"," + " "
        + "extractValue(xmltype(XML_DOCUMENT), '/AlarmDefinition/LoaderParameters/Ruleset') as \"RULE_SET_ID\"" + " "
        + "from" + " " 
        + "alarm_definitions) tbl" + " "
        + "where MAPPED_REPORT_NAME != '(null)'" + " "
        + "and MAPPED_REPORT_ID != '(null)'" + " "
        + "and MAPPED_REPORT_FOLDER_NAME != '(null)'" + " "
        + "and RULE_SET_ID != 'null'";
    //Log.show("[getAlarmDefinitions] SQL - " + SQL);
    ArrayList<AlarmDefinition> x = new ArrayList<AlarmDefinition>();
    try {
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery(SQL);
      while(resultSet.next()) {
        AlarmDefinition d = new AlarmDefinition(
            resultSet.getObject("ALARM_ID").toString(),
            resultSet.getObject("CONTEXT_ID").toString(),
            resultSet.getObject("DEFINITION_NAME").toString(),
            resultSet.getObject("IS_ACTIVE").toString(),
            resultSet.getObject("TIME_STAMP").toString(),
            resultSet.getObject("VERSION_ID").toString(),
            resultSet.getObject("MAPPED_REPORT_NAME").toString(),
            resultSet.getObject("MAPPED_REPORT_ID").toString(),
            resultSet.getObject("MAPPED_REPORT_FOLDER_NAME").toString(),
            resultSet.getObject("RULE_SET_ID").toString()
            );
        x.add(d);
      }
    } catch (Exception ex) {
      ex.printStackTrace(); 
    }
    return x;
  }
}
