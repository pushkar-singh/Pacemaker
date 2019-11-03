cd pacemaker-console/resources
mvn install:install-file -Dfile=java-ascii-table-1.0.jar -DgroupId=java-ascii-table -DartifactId=java-ascii-table -Dversion=1.0 -Dpackaging=jar
cd ../../
mvn clean install
cd pacemaker-console
mvn exec:java