# TNPM Wireless Alarm Migration

## 

### Introduction

This repository consist Java program for the purpose of migrating TNPM Wireless Alarms.

### Environment

1. TNPM Wireless 1.4
2. Oracle Database

### Assumption

1. Migrate to a `Fresh/Empty` TNPM Wireless environment.
2. `ALARM_DOCUMENT_CONTEXT` is empty.
3. `ALARM_TEMPLATES` is empty.
4. `ALARM_DEFINITIONS` is empty.
5. `LC_ALARM_DEFINITIONS` is empty.

### Overview Procedure

1. Migrate ALARM_DOCUMENT_CONTEXT.
2. Migrate ALARM_TEMPLATES.
3. Migrate ALARM_DEFINITIONS.
4. #1 to #3 uses the Oracle `exp` utility.
5. Validate `Alarm Templates` and `Alarm Definitions`from PWEB Alarm Manager.
6. Run SQLs manually to validate data integrity and compatibility (e.g. `RULESET_ID`, `REPORT_ID`, etc).
7. Run Java program without JDBC Connection commit (To check if errors with SQLs).
8. Run Java program with JDBC Connection commit to finalize migration procedure. 
