package com.psl.wireless.technologypack;

public class ReportDefinition {
  public final String documentId;
  public final String documentName;
  public final String folderId;
  public final String folderName;
  
  public final String reportKey;
  
  public ReportDefinition(String documentId, String documentName,
      String folderId) {
    this(documentId, documentName, folderId, "sysadm");
  }
  
  public ReportDefinition(String documentId, String documentName,
      String folderId, String folderName) {
    this.documentId = documentId;
    this.documentName = documentName;
    this.folderId = folderId;
    this.folderName = folderName;
    
    this.reportKey = folderName + "|" + documentName;
  }
  
  @Override
  public String toString() {
    return this.reportKey + "|" + this.folderId + "|" + this.documentId;
  }
}
