package com.psl.wireless.technologypack;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.psl.wireless.alarm.Log;

public class TechnologyPackModel {

   public final static String TECH_PACK_DEFINITION_SQL = 
      "select" + " "
      + "tbl1.DATASOURCE_ID as \"SOURCE_DS_ID\", tbl2.RULESET_ID as \"SOURCE_RULESET_ID\"," + " "
      + "tbl1.TECHPACK_NAME || '|' || tbl1.TECHPACK_VERSION || '|' || tbl1.TECHNOLOGY || '|' || tbl1.VENDOR || '|' || tbl2.TYPE as \"TECHPACK_KEY\"" + " "
      + "from" + " "
      + "lc_datasource tbl1" + " "
      + "inner join" + " "
      + "lc_ruleset tbl2" + " "
      + "on" + " "
      + "tbl1.DATASOURCE_ID = tbl2.DATASOURCE_ID";
  
  public static ArrayList<TechPackDefinition> getTechPackDefinitions(Connection connection) {
    ArrayList<TechPackDefinition> x = new ArrayList<TechPackDefinition>();
    try {
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery(TECH_PACK_DEFINITION_SQL);
      while(resultSet.next()) {
        TechPackDefinition t = new TechPackDefinition(
            resultSet.getObject("SOURCE_DS_ID").toString(),
            resultSet.getObject("SOURCE_RULESET_ID").toString(),
            resultSet.getObject("TECHPACK_KEY").toString());
        x.add(t);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return x;
  }
}