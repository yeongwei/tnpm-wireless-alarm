package com.psl.wireless.alarm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.psl.wireless.sql.AlarmDefinitionsSql;
import com.psl.wireless.sql.AlarmTemplatesSql;
import com.psl.wireless.technologypack.AlarmDefinition;
import com.psl.wireless.technologypack.AlarmModel;
import com.psl.wireless.technologypack.AlarmTemplateDefinition;
import com.psl.wireless.technologypack.ReportDefinition;
import com.psl.wireless.technologypack.ReportModel;
import com.psl.wireless.technologypack.TechPackDefinition;
import com.psl.wireless.technologypack.TechnologyPackModel;

public class UpdateAlarms {
  public static void main (String[] args) {
    
    Log.show("Make sure ALARM_DOCUMENT_CONTEXT is exported to sink server.");
    Log.show("Make sure ALARM_TEMPLATES is exported to sink server.");
    Log.show("Make sure ALARM_DEFINITIONS is exported to sink server.");
    
    Connection sourceConnection = BootStrap.getSourceConnection();
    Connection sinkConnection = BootStrap.getSinkConnection();
    
    //Consolidate Technology Pack information from both servers
    Log.show("Consolidate Technology Pack information from Source Server.");
    ArrayList<TechPackDefinition> sourceTechPacks = 
        TechnologyPackModel.getTechPackDefinitions(sourceConnection);
    Log.show("Consolidate Technology Pack information from Sink Server.");
    ArrayList<TechPackDefinition> sinkTechPacks = 
        TechnologyPackModel.getTechPackDefinitions(sinkConnection);
    
    Log.show("Correlate Technology Pack information between Source and Sink Server");
    HashMap<String, TechPackDefinition> enricedTechPackInfo;
    enricedTechPackInfo = new HashMap<String, TechPackDefinition>();
    for (int i = 0; i < sourceTechPacks.size(); i++) {
      for (int j = 0; j < sinkTechPacks.size(); j++) {

        if (sourceTechPacks.get(i).techpackKey
            .equals(sinkTechPacks.get(j).techpackKey)) {
          
          enricedTechPackInfo.put(
              sourceTechPacks.get(i).ruleSetId, sinkTechPacks.get(j));
          //Source RULESET_ID -> Sink OBJECT
          
          Log.show("[Technology Pack Definition] "
              + "(Source) "
              + sourceTechPacks.get(i) 
              + " maps to "
              + " (Sink) "
              + sinkTechPacks.get(j));
        }
      }
    }
    
    //Consolidate Report information from both servers
    Log.show("Consolidate PWEB Report information from Source Server.");
    ArrayList<ReportDefinition> sourceReports = 
        ReportModel.getReportDefinitions(sourceConnection);
    Log.show("Consolidate PWEB Report information from Sink Server.");
    ArrayList<ReportDefinition> sinkReports = 
        ReportModel.getReportDefinitions(sinkConnection);
    
    HashMap<String, ReportDefinition> enrichedReportInfo;
    enrichedReportInfo = new HashMap<String, ReportDefinition>();
    for (int i = 0; i < sourceReports.size(); i++) {
      for (int j = 0; j < sinkReports.size(); j++) {
        
        if (sourceReports.get(i).reportKey
            .equals(sinkReports.get(j).reportKey)) {
          
          enrichedReportInfo.put(
              sourceReports.get(i).reportKey, sinkReports.get(j));
          //Source REPORT_FOLDER|DOCUMENT_NAME -> Sink OBJECT
          
          Log.show("[Report Definition] " 
              + "(Source) "
              + sinkReports.get(i) 
              + " maps to "
              + " (Sink) "
              + sinkReports.get(j));
        }
      }
    }
    
    ArrayList<String> SQL_STORE = new ArrayList<String>();
    
    Log.show("Consolidate ALARM_TEMPLATES from Sink server");
    ArrayList<AlarmTemplateDefinition> sinkAlarmTemplates =
        AlarmModel.getAlarmTemplates(sinkConnection);
    
    Log.show("Consolidate ALARM_DEFINITIONS from Sink server");
    ArrayList<AlarmDefinition> sinkAlarmDefinitions = 
        AlarmModel.getAlarmDefinitions(sinkConnection);
    
    //Update ALARM_TEMPLATES in Sink server with RULESET_ID
    Log.show("About to update ALARM_TEMPLATES on sink server with RULESET_ID");
    
    for (int i = 0; i < sinkAlarmTemplates.size(); i++) {
      AlarmTemplateDefinition sinkAlarmTemplate = sinkAlarmTemplates.get(i);
      
      Log.show("Attempt to correlate RULESET_ID of " + sinkAlarmTemplate.ruleSetId);
      if (enricedTechPackInfo.containsKey(sinkAlarmTemplate.ruleSetId)) {
        TechPackDefinition tp = enricedTechPackInfo.get(sinkAlarmTemplate.ruleSetId);
        Log.show("RULESET_ID of " + sinkAlarmTemplate.ruleSetId + " maps to " + tp.ruleSetId);
        /*
        Log.show("[SQL] (ALARM_TEMPLATES) Update " 
            + sinkAlarmTemplate.templateName + " " 
            + sinkAlarmTemplate.versionId + " " 
            + "with RULESET_ID of "
            + tp.ruleSetId);
            */
        String SQL = AlarmTemplatesSql.generateUpdateRuleSetId(
            sinkAlarmTemplate.templateName, sinkAlarmTemplate.versionId, tp.ruleSetId);
        Log.show("[SQL] (ALARM_TEMPLATES) " + SQL);
        SQL_STORE.add(SQL);
      } else {
        Log.show("[WARN] (ALARM_TEMPLATES) RULESET_ID of " + sinkAlarmTemplate.ruleSetId + " does not have a correlation.");
      }
    }
    
    //Update ALARM_DEFINITIONS in Sink Server with RULESET_ID
    Log.show("About to update ALARM_DEFINITIONS on sink server with RULESET_ID");
    
    for (int i = 0; i < sinkAlarmDefinitions.size(); i++) {
      AlarmDefinition sinkAlarmDefinition = sinkAlarmDefinitions.get(i);
      
      Log.show("Attempt to correlate RULESET_ID of " + sinkAlarmDefinition.ruleSetId);
      if (enricedTechPackInfo.containsKey(sinkAlarmDefinition.ruleSetId)) {
        TechPackDefinition tp = enricedTechPackInfo.get(sinkAlarmDefinition.ruleSetId);
        Log.show("RULESET_ID of " + sinkAlarmDefinition.ruleSetId + " maps to " + tp.ruleSetId);
        /*
        Log.show("[SQL] (ALARM_DEFINITIONS) Update " 
            + sinkAlarmDefinition.definitionName + " " 
            + sinkAlarmDefinition.versionId + " " 
            + "with RULESET_ID of "
            + tp.ruleSetId);
            */
        String SQL = AlarmDefinitionsSql.generateUpdateRuleSetId(
            sinkAlarmDefinition.definitionName, sinkAlarmDefinition.versionId, 
            sinkAlarmDefinition.ruleSetId);
        Log.show("[SQL] (ALARM_DEFINITIONS) " + SQL);
        SQL_STORE.add(SQL);
      } else {
        Log.show("[WARN] (ALARM_DEFINITIONS) RULESET_ID of " + sinkAlarmDefinition.ruleSetId + " does not have a correlation.");
      }
    }
    
    //for (String key : enrichedReportInfo.keySet()) {
    //  Log.show("[DEBUG] " + key + "|_|" + enrichedReportInfo.get(key));
    //}
    
    //Update ALARM_TEMPLATES in Sink server with REPORT
    Log.show("About to update ALARM_TEMPLATES on sink server with REPORT (Assumed 'sysadm' folder)");
    for (int i = 0; i < sinkAlarmTemplates.size(); i++) {
      AlarmTemplateDefinition t = sinkAlarmTemplates.get(i);
      String reportKey = "sysadm" + "|" + t.mappedReportName;
      if (enrichedReportInfo.containsKey(reportKey)) {
        ReportDefinition r = enrichedReportInfo.get(reportKey);
        Log.show("(ALARM_TEMPLATES) REPORT KEY of " + reportKey + " maps to " + r);
        
        String SQL = AlarmTemplatesSql.generateUpdateReportInfo(
            t.templateName, t.versionId, r.documentId, r.folderId);
        Log.show("[SQL] (ALARM_TEMPLATES) " + SQL);
        SQL_STORE.add(SQL);
      } else {
        Log.show("[WARN] (ALARM_TEMPLATES) REPORT KEY of " + reportKey + " does not have a correlation.");
      }
    }
    
    //Update ALARM_DEFINITIONS in Sink Server with REPORT
    Log.show("About to update ALARM_DEFINITIONS on sink server with REPORT");
    for (int i = 0; i < sinkAlarmDefinitions.size(); i++) {
      AlarmDefinition d = sinkAlarmDefinitions.get(i);
      String reportKey = "sysadm" + "|" + d.mappedReportName;
      if (enrichedReportInfo.containsKey(reportKey)) {
        ReportDefinition r = enrichedReportInfo.get(reportKey);
        Log.show("(ALARM_DEFINITIONS) REPORT KEY of " + reportKey + " maps to " + r);
        
        String SQL = AlarmDefinitionsSql.generateUpdateReportInfo(
            d.definitionName, d.versionId, r.documentId, r.folderId);
        Log.show("[SQL] (ALARM_DEFINITIONS) " + SQL);
        SQL_STORE.add(SQL);
      } 
    }
    
    Log.show("About to execute " + SQL_STORE.size() + " SQLs.");
    Connection executeConnection = BootStrap.getSinkConnection();
    
    try {
      Log.show("Attempt to set Auto Commit to FALSE.");
      executeConnection.setAutoCommit(false);
    } catch(Exception ex) {
      ex.printStackTrace();
      Log.show("[ERRO] Program exited because Connection is only commit-capable.");
      System.exit(1);
    }
    
    String fileName = "";
    DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss");
    Date date = new Date();
    fileName = "AlarmsUpdate-" + dateFormat.format(date) + ".sql";
    
    File file = new File(fileName);
    FileWriter fw;
    BufferedWriter bw;
    try {
      file.createNewFile();
      fw = new FileWriter(file.getAbsoluteFile());
      bw = new BufferedWriter(fw);
      Log.show("Dumping " + SQL_STORE.size() + " SQLs into " + fileName + ".");
      for(int i = 0; i < SQL_STORE.size(); i++) {
        bw.write(SQL_STORE.get(i) + ";" + "\n");
      }
      bw.close();
      Log.show(fileName + " created.");
    } catch (Exception ex) {
      ex.printStackTrace();
      Log.show("[ERRO] Program exited because unable to dump SQLs into " + fileName + ".");
      System.exit(1);
    }

    for (int i = 0; i < SQL_STORE.size(); i++) {
      try {
        Statement executeStatement = executeConnection.createStatement();
        Log.show("About to execute " + SQL_STORE.get(i) + ".");
        int status = executeStatement.executeUpdate(SQL_STORE.get(i));
      } catch (Exception ex) {
        ex.printStackTrace();
        Log.show("Unable to execute [" + i + "]" + SQL_STORE.get(i) + ".");
        
        try {
          executeConnection.rollback();
        } catch (Exception ex2) {
          Log.show("[FATA] Unable to rollback error.");
        }
        
      }
    }
    
    Log.show("Launch Alarm Manager to activate the alarms.");
    Log.show("Review LC_ALARM_DEFINITIONS to ensure alarms are activated correctly.");
    
    try {
      Log.show("Safety rollback.");
      executeConnection.rollback();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
