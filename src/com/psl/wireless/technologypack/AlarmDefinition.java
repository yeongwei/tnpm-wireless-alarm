package com.psl.wireless.technologypack;

public class AlarmDefinition {
  public final String alarmId;
  public final String contextId;
  public final String definitionName;
  public final String isActive;
  public final String timeStamp;
  public final String versionId;
  public final String mappedReportName;
  public final String mappedReportId;
  public final  String mappedReportFolderName;
  public final String ruleSetId;
  
  public AlarmDefinition(
      String alarmId, String contextId, String definitionName,
      String isActive, String timeStamp, String versionId,
      String mappedReportName, String mappedReportId,
      String mappedReportFolderName, String ruleSetId) {
    this.alarmId = alarmId;
    this.contextId = contextId;
    this.definitionName = definitionName;
    this.isActive = isActive;
    this.timeStamp = timeStamp;
    this.versionId = versionId;
    this.mappedReportName = mappedReportName;
    this.mappedReportId = mappedReportId;
    this.mappedReportFolderName = mappedReportFolderName;
    this.ruleSetId = ruleSetId;
  }
}
