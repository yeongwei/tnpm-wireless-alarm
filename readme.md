# TNPM Wireless Alarm Migration

## 

### Introduction

This repository consist Java program for the purpose of migrating TNPM Wireless Alarms.

### Enviroment

1. TNPM Wireless 1.4
2. Oracle Database

### Assumption

1. Migrate to a `Fresh/Empty` TNPM Wireless environment.

### Overview Procedure

1. Migrate ALARM_DOCUMENT_CONTEXT.
2. Migrate ALARM_TEMPLATES.
3. Migrate ALARM_DEFINITIONS.
4. Validate from PWEB Alarm Manager.
5. Run SQL manually to validate date integrity and compatibility.
6. Run Java program without JDBC Connection commit.
7. Evaluate if error occurs.
8. Run Java program with JDBC Connection commit to perform actual migration. 
