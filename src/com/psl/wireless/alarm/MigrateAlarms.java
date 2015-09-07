package com.psl.wireless.alarm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MigrateAlarms {
  public static void main(String[] args) throws Exception {
    BootStrap.validateClass();
    
    Connection sourceConnection = BootStrap.getSourceConnection();

    String SOURCE_LC_ALARM_DEFINITION_SQL = "";

    SOURCE_LC_ALARM_DEFINITION_SQL += "SELECT ";

    // LC_ALARM_DEFINITION COLUMNS
    SOURCE_LC_ALARM_DEFINITION_SQL += "tbl1.RULESET_ID,";
    SOURCE_LC_ALARM_DEFINITION_SQL += "tbl1.ALARM_ID,";
    SOURCE_LC_ALARM_DEFINITION_SQL += "tbl1.ENABLED,";
    SOURCE_LC_ALARM_DEFINITION_SQL += "tbl1.OBJECT_NAME,";
    SOURCE_LC_ALARM_DEFINITION_SQL += "tbl1.SCOPE,";
    SOURCE_LC_ALARM_DEFINITION_SQL += "tbl1.VALIDITY,";
    SOURCE_LC_ALARM_DEFINITION_SQL += "tbl1.STABILITY,";
    SOURCE_LC_ALARM_DEFINITION_SQL += "tbl1.REQ_STABILITY_DATA,";
    SOURCE_LC_ALARM_DEFINITION_SQL += "tbl1.AL_KEY,";
    SOURCE_LC_ALARM_DEFINITION_SQL += "tbl1.AL_KEY_CLASS,";
    SOURCE_LC_ALARM_DEFINITION_SQL += "tbl1.AL_NAME,";
    SOURCE_LC_ALARM_DEFINITION_SQL += "tbl1.AL_TYPE,";
    SOURCE_LC_ALARM_DEFINITION_SQL += "tbl1.CAUSE,";
    SOURCE_LC_ALARM_DEFINITION_SQL += "tbl1.USETREND,";
    SOURCE_LC_ALARM_DEFINITION_SQL += "tbl1.REPORTPRED,";
    SOURCE_LC_ALARM_DEFINITION_SQL += "tbl1.DOMAIN_NAME,";
    SOURCE_LC_ALARM_DEFINITION_SQL += "tbl1.REPORT_ID,";
    // TECHNOLOGY PACK INFORMATION
    SOURCE_LC_ALARM_DEFINITION_SQL += "tbl3.DATASOURCE_NAME,";
    SOURCE_LC_ALARM_DEFINITION_SQL += "tbl3.DATASOURCE_VERSION,";
    SOURCE_LC_ALARM_DEFINITION_SQL += "tbl3.DESCRIPTION,";
    SOURCE_LC_ALARM_DEFINITION_SQL += "tbl3.TECHPACK_NAME,";
    SOURCE_LC_ALARM_DEFINITION_SQL += "tbl3.TECHPACK_VERSION,";
    SOURCE_LC_ALARM_DEFINITION_SQL += "tbl3.TECHNOLOGY,";
    SOURCE_LC_ALARM_DEFINITION_SQL += "tbl3.VENDOR,";
    // REPORT INFORMATION
    SOURCE_LC_ALARM_DEFINITION_SQL += "tbl4.NAME,";
    SOURCE_LC_ALARM_DEFINITION_SQL += "tbl4.DOCUMENT_TYPE_ID,";
    // ALARM INFORMATION
    SOURCE_LC_ALARM_DEFINITION_SQL += "tbl5.DEFINITION_NAME,";
    SOURCE_LC_ALARM_DEFINITION_SQL += "tbl5.VERSION_ID,";
    // CONTEXT INFORMATION
    SOURCE_LC_ALARM_DEFINITION_SQL += "tbl6.CONTEXT_NAME";

    SOURCE_LC_ALARM_DEFINITION_SQL += " ";
    SOURCE_LC_ALARM_DEFINITION_SQL += "FROM LC_ALARM_DEFINITIONS tbl1";

    SOURCE_LC_ALARM_DEFINITION_SQL += " ";
    SOURCE_LC_ALARM_DEFINITION_SQL += "LEFT OUTER JOIN LC_RULESET tbl2";

    SOURCE_LC_ALARM_DEFINITION_SQL += " ";
    SOURCE_LC_ALARM_DEFINITION_SQL += "ON tbl1.RULESET_ID = tbl2.RULESET_ID";

    SOURCE_LC_ALARM_DEFINITION_SQL += " ";
    SOURCE_LC_ALARM_DEFINITION_SQL += "LEFT OUTER JOIN LC_DATASOURCE tbl3";

    SOURCE_LC_ALARM_DEFINITION_SQL += " ";
    SOURCE_LC_ALARM_DEFINITION_SQL += "ON tbl2.DATASOURCE_ID = tbl3.DATASOURCE_ID";

    SOURCE_LC_ALARM_DEFINITION_SQL += " ";
    SOURCE_LC_ALARM_DEFINITION_SQL += "LEFT OUTER JOIN PM_DOCUMENT tbl4";

    SOURCE_LC_ALARM_DEFINITION_SQL += " ";
    SOURCE_LC_ALARM_DEFINITION_SQL += "ON tbl4.DOCUMENT_ID = tbl1.REPORT_ID";
    
    SOURCE_LC_ALARM_DEFINITION_SQL += " ";
    SOURCE_LC_ALARM_DEFINITION_SQL += "LEFT OUTER JOIN ALARM_DEFINITIONS tbl5";
    
    SOURCE_LC_ALARM_DEFINITION_SQL += " ";
    SOURCE_LC_ALARM_DEFINITION_SQL += "ON tbl5.ALARM_ID = tbl1.ALARM_ID";
    
    SOURCE_LC_ALARM_DEFINITION_SQL += " ";
    SOURCE_LC_ALARM_DEFINITION_SQL += "LEFT OUTER JOIN ALARM_DOCUMENT_CONTEXT tbl6";
    
    SOURCE_LC_ALARM_DEFINITION_SQL += " ";
    SOURCE_LC_ALARM_DEFINITION_SQL += "ON tbl6.CONTEXT_ID = tbl5.CONTEXT_ID";

    consoleLog("Get information from Staging Server");
    consoleLog("SQL - " + SOURCE_LC_ALARM_DEFINITION_SQL + ";");

    Statement sourceStatement = sourceConnection.createStatement();
    ResultSet sourceResultSet = sourceStatement
        .executeQuery(SOURCE_LC_ALARM_DEFINITION_SQL);

    consoleLog("Parse ResultSet from Staging Server");

    Connection sinkConnection = BootStrap.getSinkConnection();

    // SINK RULESET INFORMATION
    String SINK_RULESET_SQL = "SELECT";
    SINK_RULESET_SQL += " ";
    SINK_RULESET_SQL += "tbl1.RULESET_ID, tbl1.DATASOURCE_ID, tbl1.TYPE,";
    SINK_RULESET_SQL += "tbl2.DATASOURCE_NAME, tbl2.DATASOURCE_VERSION,"
        + "tbl2.DESCRIPTION, tbl2.TECHPACK_NAME, "
        + "tbl2.TECHPACK_VERSION, tbl2.TECHNOLOGY, tbl2.VENDOR";
    SINK_RULESET_SQL += " ";
    SINK_RULESET_SQL += "FROM LC_RULESET tbl1";
    SINK_RULESET_SQL += " ";
    SINK_RULESET_SQL += "LEFT OUTER JOIN LC_DATASOURCE tbl2";
    SINK_RULESET_SQL += " ";
    SINK_RULESET_SQL += "ON tbl2.DATASOURCE_ID = tbl1.DATASOURCE_ID";

    consoleLog("Get RULESET information from Production Server");

    Statement sinkRuleSetStatement = sinkConnection.createStatement();
    ResultSet sinkRuleSetResultSet = sinkRuleSetStatement
        .executeQuery(SINK_RULESET_SQL);

    consoleLog("Parse RULESET ResultSet from Production Server");

    HashMap<String, EnrichedRuleSetDefinition> RULESET = new HashMap<String, EnrichedRuleSetDefinition>();
    while (sinkRuleSetResultSet.next()) {
      EnrichedRuleSetDefinition x = new EnrichedRuleSetDefinition(
          sinkRuleSetResultSet.getObject("RULESET_ID").toString(),
          sinkRuleSetResultSet.getObject("DATASOURCE_ID").toString(),
          sinkRuleSetResultSet.getObject("TYPE").toString(),
          sinkRuleSetResultSet.getObject("DATASOURCE_NAME").toString(),
          sinkRuleSetResultSet.getObject("DATASOURCE_VERSION").toString(),
          sinkRuleSetResultSet.getObject("DESCRIPTION").toString(),
          sinkRuleSetResultSet.getObject("TECHPACK_NAME").toString(),
          sinkRuleSetResultSet.getObject("TECHPACK_VERSION").toString(),
          sinkRuleSetResultSet.getObject("TECHNOLOGY").toString(),
          sinkRuleSetResultSet.getObject("VENDOR").toString());
      consoleLog("Found RULESET of '" + x.key + "'");
      RULESET.put(x.key, x);
    }

    // SINK REPORT INFORMATION
    String SINK_REPORT_SQL = "SELECT * FROM PM_DOCUMENT ";
    SINK_REPORT_SQL += "WHERE DOCUMENT_TYPE_ID = 2 AND NAME NOT LIKE '%sysadm%'";

    consoleLog("Get REPORT information from Production Server");

    Statement sinkReportStatement = sinkConnection.createStatement();
    ResultSet sinkReportResultSet = sinkReportStatement
        .executeQuery(SINK_REPORT_SQL);

    consoleLog("Parse REPORT ResultSet from Production Server");

    HashMap<String, EnrichedReportDefinitnion> REPORT = new HashMap<String, EnrichedReportDefinitnion>();
    while (sinkReportResultSet.next()) {
      EnrichedReportDefinitnion x = new EnrichedReportDefinitnion(
          sinkReportResultSet.getObject("DOCUMENT_ID").toString(),
          sinkReportResultSet.getObject("NAME").toString());

      consoleLog("Found REPORT of '" + x.key + "'");
      REPORT.put(x.key, x);
    }

    // ALARM INFORMATION
    String SINK_ALARM_SQL = "";
    SINK_ALARM_SQL += "SELECT";
    SINK_ALARM_SQL += " ";
    SINK_ALARM_SQL += "tbl1.ALARM_ID, tbl1.CONTEXT_ID, tbl1.DEFINITION_NAME, tbl1.VERSION_ID,";
    SINK_ALARM_SQL += "tbl2.CONTEXT_NAME";
    SINK_ALARM_SQL += " ";
    SINK_ALARM_SQL += "FROM ALARM_DEFINITIONS tbl1";
    SINK_ALARM_SQL += " ";
    SINK_ALARM_SQL += "LEFT OUTER JOIN ALARM_DOCUMENT_CONTEXT tbl2";
    SINK_ALARM_SQL += " ";
    SINK_ALARM_SQL += "ON tbl2.CONTEXT_ID = tbl1.CONTEXT_ID";
    
    consoleLog("Get ALARM information from Production Server");
    
    Statement sinkAlarmStatement = sinkConnection.createStatement();
    ResultSet sinkAlarResultSet = sinkAlarmStatement
        .executeQuery(SINK_ALARM_SQL);
    
    consoleLog("Parse ALARM ResultSet from Production Server");
    
    HashMap<String, EnrichedAlarmInformation> ALARM = new HashMap<String, EnrichedAlarmInformation>();
    while(sinkAlarResultSet.next()) {
      EnrichedAlarmInformation x = new EnrichedAlarmInformation(
          sinkAlarResultSet.getObject("ALARM_ID").toString(),
          sinkAlarResultSet.getObject("CONTEXT_ID").toString(),
          sinkAlarResultSet.getObject("DEFINITION_NAME").toString(),
          sinkAlarResultSet.getObject("VERSION_ID").toString(),
          sinkAlarResultSet.getObject("CONTEXT_NAME").toString());
      consoleLog("Found ALARM of '" + x.key + "'");
      ALARM.put(x.key, x);
    }
    
    consoleLog("Cosolidate information for LC_ALARM_DEFINITION");

    ArrayList<String> LC_ALARM_DEFINITIONS = new ArrayList<String>();
    while (sourceResultSet.next()) {
      String ruleSetId = "";
      ruleSetId += sourceResultSet.getObject("RULESET_ID");

      String ruleSetKey = "";
      ruleSetKey += sourceResultSet.getObject("DATASOURCE_NAME") + "|";
      ruleSetKey += sourceResultSet.getObject("DATASOURCE_VERSION") + "|";
      ruleSetKey += sourceResultSet.getObject("TECHPACK_NAME") + "|";
      ruleSetKey += sourceResultSet.getObject("TECHPACK_VERSION") + "|";
      ruleSetKey += sourceResultSet.getObject("TECHNOLOGY") + "|";
      ruleSetKey += sourceResultSet.getObject("VENDOR");

      String reportKey = "";
      reportKey += sourceResultSet.getObject("NAME");
      
      String alarmKey = "";
      alarmKey += sourceResultSet.getObject("DEFINITION_NAME") + "|";
      alarmKey += sourceResultSet.getObject("VERSION_ID") + "|";
      alarmKey += sourceResultSet.getObject("CONTEXT_NAME");

      consoleLog("Looking up with source RULESET key of " + ruleSetKey);

      boolean ruleSetFound = false;
      if (RULESET.containsKey(ruleSetKey)) {
        consoleLog(ruleSetId
            + " from source has VALID RULESET in Production Server");
        ruleSetFound = true;
      } else {
        consoleLog(ruleSetId
            + " from source has INVALID RULESET in Production Server");
      }

      consoleLog("Looking up with source ALARM key of " + alarmKey);
      
      boolean alarmFound = false;
      if (ALARM.containsKey(alarmKey)) {
        consoleLog(ruleSetId
            + " from source has VALID ALARM in Production Server");
        alarmFound = true;
      } else {
        consoleLog(ruleSetId
            + " from source has INVALID ALARM in Production Server");
      }
      
      consoleLog("Looking up with source REPORT key of " + reportKey);

      boolean reportFound = false;
      if (REPORT.containsKey(reportKey)) {
        consoleLog(ruleSetId
            + " from source has VALID REPORT in Production Server");
        reportFound = true;
      } else {
        consoleLog(ruleSetId
            + " from source has INVALID REPORT in Production Server");
      }

      if (ruleSetFound && reportFound) {
        consoleLog(ruleSetKey + " is fit for Production Server");
        
        String lcAlarmDefinition = "";
        lcAlarmDefinition += RULESET.get(ruleSetKey).ruleSetId + ",";
        lcAlarmDefinition += ALARM.get(alarmKey).alarmId + ",";
        lcAlarmDefinition += sourceResultSet.getObject("ENABLED") + ",";
        lcAlarmDefinition += sourceResultSet.getObject("OBJECT_NAME") + ",";
        lcAlarmDefinition += sourceResultSet.getObject("SCOPE") + ",";
        lcAlarmDefinition += sourceResultSet.getObject("VALIDITY") + ",";
        lcAlarmDefinition += sourceResultSet.getObject("STABILITY") + ",";
        lcAlarmDefinition += sourceResultSet.getObject("REQ_STABILITY_DATA") + ",";
        lcAlarmDefinition += sourceResultSet.getObject("AL_KEY") + ",";
        lcAlarmDefinition += sourceResultSet.getObject("AL_KEY_CLASS") + ",";
        lcAlarmDefinition += sourceResultSet.getObject("AL_NAME") + ",";
        lcAlarmDefinition += sourceResultSet.getObject("AL_TYPE") + ",";
        lcAlarmDefinition += sourceResultSet.getObject("CAUSE") + ",";
        lcAlarmDefinition += sourceResultSet.getObject("USETREND") + ",";
        lcAlarmDefinition += sourceResultSet.getObject("REPORTPRED") + ",";
        lcAlarmDefinition += sourceResultSet.getObject("DOMAIN_NAME") + ",";
        lcAlarmDefinition += REPORT.get(reportKey).documentID;
        
        LC_ALARM_DEFINITIONS.add(lcAlarmDefinition);
      }
    }
    
    consoleLog("Total " + LC_ALARM_DEFINITIONS.size() + " LC_ALARM_DEFINITIONS found");
    
    consoleLog("Dumping into CSV");
    String fileName = "";
    DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss");
    Date date = new Date();
    fileName = "lcAlarmDefinitions-" + dateFormat.format(date) + ".csv";
    
    consoleLog("CSV filename is " + fileName);
    
    File file = new File(fileName);
    file.createNewFile();
    FileWriter fw = new FileWriter(file.getAbsoluteFile());
    BufferedWriter bw = new BufferedWriter(fw);

    String header = "";
    header += "RULESET_ID,";
    header += "ALARM_ID,";
    header += "ENABLED,";
    header += "OBJECT_NAME,";
    header += "SCOPE,";
    header += "VALIDITY,";
    header += "STABILITY,";
    header += "REQ_STABILITY_DATA,";
    header += "AL_KEY,";
    header += "AL_KEY_CLASS,";
    header += "AL_NAME,";
    header += "AL_TYPE,";
    header += "CAUSE,";
    header += "USETREND,";
    header += "REPORTPRED,";
    header += "DOMAIN_NAME,";
    header += "REPORT_ID";
    bw.write(header + "\n");
    
    for(int i = 0; i < LC_ALARM_DEFINITIONS.size(); i ++) {
      bw.write(LC_ALARM_DEFINITIONS.get(i) + "\n");
    }
    bw.close();
    
    consoleLog("CSV " + fileName + " has been created");
  }

  public static void consoleLog(String msg) {
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    Date date = new Date();

    System.out.println("[" + dateFormat.format(date) + "] " + msg);
  }
}

class EnrichedAlarmDefinition {
  public EnrichedAlarmDefinition() {
  }
}

class EnrichedRuleSetDefinition {
  // RULESET INFORMATION
  public final String ruleSetId;
  public final String dataSourceId;
  public final String type;
  // DATASOURCE INFOMATION
  public final String dataSourceName;
  public final String dataSourceVersion;
  public final String description;
  public final String techpackName;
  public final String techpackVersion;
  public final String technology;
  public final String vendor;

  public final String key;// Object special key

  public EnrichedRuleSetDefinition(String ruleSetId, String dataSourceId,
      String type, String dataSourceName, String dataSourceVersion,
      String description, String techpackName, String techpackVersion,
      String technology, String vendor) {

    this.key = dataSourceName + "|" + dataSourceVersion + "|" + techpackName
        + "|" + techpackVersion + "|" + technology + "|" + vendor;

    this.ruleSetId = ruleSetId;
    this.dataSourceId = dataSourceId;
    this.type = type;

    this.dataSourceName = dataSourceName;
    this.dataSourceVersion = dataSourceVersion;
    this.description = description;
    this.techpackName = techpackName;
    this.techpackVersion = techpackVersion;
    this.technology = technology;
    this.vendor = vendor;
  }
}

class EnrichedReportDefinitnion {
  public final String documentID;
  public final String name;

  public final String key;

  public EnrichedReportDefinitnion(String documentID, String name) {
    this.key = name;

    this.documentID = documentID;
    this.name = name;
  }
}

class EnrichedAlarmInformation {
  public final String alarmId; 
  public final String contextId;
  public final String definitionName;
  public final String version;
  
  public final String contextName;
  
  public final String key;
  
  public EnrichedAlarmInformation(
      String alarmId, String contextId, String definitionName, String version,
      String contextName) {
    this.key = definitionName + "|" + version + "|" + contextName;
    
    this.alarmId = alarmId;
    this.contextId = contextId;
    this.definitionName = definitionName;
    this.version = version;
    this.contextName = contextName;
  }
}
