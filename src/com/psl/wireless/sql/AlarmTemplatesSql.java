package com.psl.wireless.sql;

public class AlarmTemplatesSql {
  public static String generateUpdateRuleSetId(
      String templateName, String versionId, String ruleSetId) {
    String SQL = "";
    SQL += "UPDATE ALARM_TEMPLATES" + " ";
    SQL += "SET XML_DOCUMENT = TO_CLOB(" + " ";
    SQL += "    UPDATEXML(" + " ";
    SQL += "        XMLTYPE(XML_DOCUMENT), '/AlarmTemplate/LoaderParameters/Ruleset/text()', '##RULESET_ID##'" + " ";
    SQL += "    )" + " ";
    SQL += ")" + " ";
    SQL += "WHERE TEMPLATE_NAME = '##TEMPLATE_NAME##'" + " ";
    SQL += "AND VERSION_ID = '##VERSION_ID##'";
    
    SQL = SQL.replace("##RULESET_ID##", ruleSetId);
    SQL = SQL.replace("##TEMPLATE_NAME##", templateName);
    SQL = SQL.replace("##VERSION_ID##", versionId);
    
    return SQL;
  }
  
  public static String generateUpdateReportInfo(
      String templateName, String versionId, String documentId, String folderId) {
    String SQL = "";
    SQL += "UPDATE ALARM_TEMPLATES" + " ";
    SQL += "SET XML_DOCUMENT = TO_CLOB(" + " ";
    SQL += "    UPDATEXML(" + " ";
    SQL += "        XMLTYPE(XML_DOCUMENT)," + " ";
    SQL += "        '/AlarmTemplate/General/MappedReportID/text()', '##REPORT_ID##'" + "," + " ";
    SQL += "        '/AlarmTemplate/General/MappedReportFolderName/text()', '##FOLDER_ID##'";
    SQL += "    )" + " ";
    SQL += ")" + " ";
    SQL += "WHERE TEMPLATE_NAME = '##TEMPLATE_NAME##'" + " ";
    SQL += "AND VERSION_ID = '##VERSION_ID##'";
    
    SQL = SQL.replace("##REPORT_ID##", documentId);
    SQL = SQL.replace("##FOLDER_ID##", folderId);
    SQL = SQL.replace("##TEMPLATE_NAME##", templateName);
    SQL = SQL.replace("##VERSION_ID##", versionId);
    
    return SQL;
  }
}
