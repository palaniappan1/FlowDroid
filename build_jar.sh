cd soot-infoflow || exit
mvn clean install -DskipTests
cd ../

cd soot-infoflow-android || exit
mvn clean install -DskipTests
cd ../

cd soot-infoflow-cmd || exit
mvn clean install -DskipTests
cd ../

cd soot-infoflow-integration || exit
mvn clean install -DskipTests
cd ../

cd soot-infoflow-summaries || exit
mvn clean install -DskipTests
cd ../