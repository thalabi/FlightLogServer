set h2-script=java -cp C:\maven-repo\com\h2database\h2\1.4.197\h2-1.4.197.jar org.h2.tools.Script
%h2-script%	-url jdbc:h2:~/FlightLog/backup-2018-07-01/FlightLog;IFEXISTS=TRUE ^
	-user sa ^
    -password "" ^
    -script export-db.sql