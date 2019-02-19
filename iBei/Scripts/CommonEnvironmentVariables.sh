# -----------------------------------------------------------------------------
# It contains common environment variables to all scripts launching clients
# and servers. It must be inclued by all of them.
# -----------------------------------------------------------------------------

# ----------------------------- Run-time system -------------------------------

# The following environment variables must be adapted to your environment:
#
# - JDBC_DRIVER_CLASSPATH
# - JBOSS_HOME

# Java virtual machine.
JAVA_VIRTUAL_MACHINE=$JAVA_HOME/bin/java

# JDBC driver classpath.
# PostgreSQL.
#JDBC_DRIVER_CLASSPATH=/usr/local/postgresql-jdbc3/postgresql-8.0-312.jdbc3.jar
# MySQL.
JDBC_DRIVER_CLASSPATH=/usr/local/mysql/mysql-connector-java-3.1.10/mysql-connector-java-3.1.10-bin.jar

# JBoss
JBOSS_HOME=/usr/local/jboss-4.0.3RC2

# -------------------------------- J2EE-iBei ----------------------------------

# A variable with the same notation as CLASSPATH containing only the classpath
# for the infraestructure used and PropertiesConfiguration  directory.
J2EE_IBEI_REQUIRED_CLASSPATH=$JDBC_IBEI_CLASSPATH:\
$J2EE_IBEI_HOME/PropertiesConfiguration

for i in $JBOSS_HOME/client/*.jar
do
	J2EE_IBEI_REQUIRED_CLASSPATH=$J2EE_IBEI_REQUIRED_CLASSPATH:$i
done

# A variable defined by convenience to run classes with a main() method from
# the command line.
J2EE_IBEI_CLASSPATH=$J2EE_IBEI_REQUIRED_CLASSPATH:\
$J2EE_IBEI_HOME/Subsystems/iBei/Build/Classes

# Export J2EE_IBEI_CLASSPATH to execute unit tests from the command line.
export J2EE_IBEI_CLASSPATH
