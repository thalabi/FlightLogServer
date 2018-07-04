set liquibase=java -jar C:\maven-repo\org\liquibase\liquibase-core\3.5.5\liquibase-core-3.5.5.jar
%liquibase% --driver=org.h2.Driver ^
      --classpath=C:\maven-repo\com\h2database\h2\1.4.197\h2-1.4.197.jar ^
      --changeLogFile=generated-changelog.xml ^
      --url="jdbc:h2:~/FlightLog/FlightLog;IFEXISTS=TRUE" ^
      --username=sa ^
      --password= ^
      generateChangeLog