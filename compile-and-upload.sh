mvn clean package -DskipTests
cp ./target/migration-helper-1.0-SNAPSHOT.jar MigrationHelperJAR/
cd MigrationHelperJAR || exit
git add migration-helper-1.0-SNAPSHOT.jar
git commit -m "New version of MigrationHelper"
git push origin master
cd ..
