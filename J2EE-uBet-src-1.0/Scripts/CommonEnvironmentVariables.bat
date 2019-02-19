@echo off
rem ----------------------------------------------------------------------------
rem It contains common environment variables to all scripts launching clients
rem and servers. It must be inclued by all of them.
rem
rem NOTE: Due to lack of appropriate syntax in ".bat" scripting language, this
rem script does not initialize J2EE_EXAMPLES_CLASSPATH with JBoss' jar files.
rem In consequence, it can not be used to execute JBoss clients.
rem ----------------------------------------------------------------------------

rem ---------------------------- Run-time system -------------------------------

rem The following environment variables must be adapted to your environment:
rem
rem - JDBC_DRIVER_CLASSPATH

rem Java virtual machine.
set JAVA_VIRTUAL_MACHINE=%JAVA_HOME%\bin\java

rem JDBC driver classpath.
rem PostgreSQL.
rem set JDBC_DRIVER_CLASSPATH=c:\postgresql-jdbc3\postgresql-8.0-312.jdbc3.jar
rem MySQL.
set JDBC_DRIVER_CLASSPATH=c:\mysql-connector-java-3.1.10\mysql-connector-java-3.1.10-bin.jar

rem ------------------------------ J2EE-uBet -------------------------------

rem A variable with the same notation as CLASSPATH containing only the classpath
rem for the JDBC driver and PropertiesConfiguration directory.
set J2EE_UBET_REQUIRED_CLASSPATH=%JDBC_DRIVER_CLASSPATH%;%J2EE_UBET_HOME%\PropertiesConfiguration

rem A variable defined by convenience to run classes from the command line.
set J2EE_UBET_CLASSPATH=%J2EE_UBET_REQUIRED_CLASSPATH%;%J2EE_UBET_HOME%\Subsystems\uBet\Build\Classes;%J2EE_UBET_HOME%\Subsystems\WastingMoney\Build\Classes
