package com.psl.wireless.alarm;

public class TestConnection {
  public static void main(String[] args) {
    System.out.println(System.getProperty("java.security.egd"));
    BootStrap.getSourceConnection();
  }
}
