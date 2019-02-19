@echo off
rem ----------------------------------------------------------------------------
rem It contains common environment variables to all scripts launching clients
rem and servers. It must be inclued by all of them.
rem ----------------------------------------------------------------------------

rem ---------------------------- Run-time system -------------------------------

rem The following environment variables must be adapted to your environment:
rem
rem - JDBC_DRIVER_CLASSPATH
rem - JBOSS_HOME

rem Java virtual machine.
set JAVA_VIRTUAL_MACHINE=%JAVA_HOME%\bin\java

rem JDBC driver classpath.
rem PostgreSQL.
rem set JDBC_DRIVER_CLASSPATH=C:\Archivos de programa\PostgreSQL\postgresql-8.0-312.jdbc3.jar
rem MySQL.
set JDBC_DRIVER_CLASSPATH=%MYSQL_HOME%\mysql-connector-java-3.1.10\mysql-connector-java-3.1.10-bin.jar

rem JBoss
set JBOSS_HOME=C:\Archivos de programa\jboss-4.0.3RC2

rem ------------------------------ J2EE-iBei -----------------------------------

rem A variable with the same notation as CLASSPATH containing only the classpath
rem for the JDBC driver and PropertiesConfiguration directory.
set J2EE_IBEI_REQUIRED_CLASSPATH=%JDBC_DRIVER_CLASSPATH%;%J2EE_IBEI_HOME%\PropertiesConfiguration

rem A variable defined by convenience to run classes with a main() method from
rem the command line.
set J2EE_IBEI_CLASSPATH=%J2EE_IBEI_REQUIRED_CLASSPATH%;%J2EE_IBEI_HOME%\Build