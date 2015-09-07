package com.psl.wireless.technologypack;

public class TechPackDefinition {
  public final String dataSource;
  public final String dataSourceId;
  public final String ruleSetId;
  
  public final String techpackName;
  public final String techpackVersion;
  public final String technology;
  public final String vendor;
  
  public final String techpackKey;
  
  public TechPackDefinition(
      String dataSourceId, String ruleSetId, String techpackKey) {
    
    this.dataSource = null;
    this.dataSourceId = dataSourceId;
    this.ruleSetId = ruleSetId;
    
    this.techpackName = null;
    this.techpackVersion = null;
    this.technology = null;
    this.vendor = null;
    
    this.techpackKey = techpackKey;
  }
  
  public TechPackDefinition(
      String dataSource, String dataSourceId, String ruleSetId,
      String techpackName, String techpackVersion, String technology, String vendor) {
    
    this.dataSource = dataSource;
    this.dataSourceId = dataSourceId;
    this.ruleSetId = ruleSetId;
    
    this.techpackName = techpackName;
    this.techpackVersion = techpackVersion;
    this.technology = technology;
    this.vendor = vendor;
    
    this.techpackKey =
        techpackName + "|" + techpackVersion + "|" + technology + "|" + vendor;
  }
  
  @Override
  public String toString() {
    return this.ruleSetId + "|" + this.techpackKey;
  }
}
