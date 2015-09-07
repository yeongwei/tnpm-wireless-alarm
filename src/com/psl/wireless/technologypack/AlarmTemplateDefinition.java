package com.psl.wireless.technologypack;

public class AlarmTemplateDefinition {
  public final String contextId;
  public final String templateName;
  public final String timeStamp;
  public final String versionId;
  public final String mappedReportName;
  public final String mappedReportId;
  public final String mappedReportFolderName;
  public final String ruleSetId;
  
  public AlarmTemplateDefinition(
      String contextId, String templateName, String timeStamp, String versionId,
      String mappedReportName, String mappedReportId, String mappedReportFolderName,
      String ruleSetId) {
    this.contextId = contextId;
    this.templateName = templateName;
    this.timeStamp = timeStamp;
    this.versionId = versionId;
    this.mappedReportName = mappedReportName;
    this.mappedReportId = mappedReportId;
    this.mappedReportFolderName = mappedReportFolderName;
    this.ruleSetId = ruleSetId;    
  }
}
