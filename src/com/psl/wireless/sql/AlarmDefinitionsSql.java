package com.psl.wireless.sql;

public class AlarmDefinitionsSql {
  public static String generateUpdateRuleSetId(
      String definitionName, String versionId, String ruleSetId) {
    String SQL = "";
    SQL += "UPDATE ALARM_DEFINITIONS" + " ";
    SQL += "SET XML_DOCUMENT = TO_CLOB(" + " ";
    SQL += "    UPDATEXML(" + " ";
    SQL += "        XMLTYPE(XML_DOCUMENT), '/AlarmDefinition/LoaderParameters/Ruleset/text()', '##RULESET_ID##'" + " ";
    SQL += "    )" + " ";
    SQL += ")" + " ";
    SQL += "WHERE DEFINITION_NAME = '##DEFINITION_NAME##'" + " ";
    SQL += "AND VERSION_ID = '##VERSION_ID##'";
    
    SQL = SQL.replace("##RULESET_ID##", ruleSetId);
    SQL = SQL.replace("##DEFINITION_NAME##", definitionName);
    SQL = SQL.replace("##VERSION_ID##", versionId);
    
    return SQL;
  }
  
  public static String generateUpdateReportInfo(
      String definitionName, String versionId, String documentId, String folderId) {
    String SQL = "";
    SQL += "UPDATE ALARM_DEFINITIONS" + " ";
    SQL += "SET XML_DOCUMENT = TO_CLOB(" + " ";
    SQL += "    UPDATEXML(" + " ";
    SQL += "        XMLTYPE(XML_DOCUMENT)," + " ";
    SQL += "        '/AlarmDefinition/General/MappedReportID/text()', '##REPORT_ID##'" + "," + " ";
    SQL += "        '/AlarmDefinition/General/MappedReportFolderName/text()', '##FOLDER_ID##'" + " ";       
    SQL += "    )" + " ";
    SQL += ")";
    SQL += "WHERE DEFINITION_NAME = '##DEFINITION_NAME##'" + " ";
    SQL += "AND VERSION_ID = '##VERSION_ID##'";
    
    SQL = SQL.replace("##REPORT_ID##", documentId);
    SQL = SQL.replace("##FOLDER_ID##", folderId);
    SQL = SQL.replace("##DEFINITION_NAME##", definitionName);
    SQL = SQL.replace("##VERSION_ID##", versionId);
    
    return SQL;
  }
}
