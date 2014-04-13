@set TIKA_APP=target/libs/tika-app-1.4.jar
@IF NOT EXIST %TIKA_APP% (
	echo =================================================================
 	echo Auto downloading %TIKA_APP% !
	echo =================================================================
	call mvn package
)

java -jar %TIKA_APP% -g