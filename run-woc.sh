if [ "$1" != "CreateTableJob" ] && [ "$1" != "LibrariesIoImportJob" ] && [ "$1" != "LioJarParseJob" ] \
   && [ "$1" != "DataExportJob" ] && [ "$1" != "LibraryRecommendJob" ] && [ "$1" != "TestJob" ]; then
  echo "Usage: run-woc.sh <Job Name> <Arg1> <Arg2> <Arg3> <Arg4>"
  echo "Currently Supported Jobs: CreateTableJob, LibrariesIoImportJob, LioJarParseJob, "
  echo "                          DataExportJob, LibraryRecommendJob, TestJob"
  echo "See README.md for usage examples"
  exit
fi

echo "Pulling Latest Version..."
cd MigrationHelperJAR || exit
git pull origin master
cd ..

job=$1
shift 1
echo "MigrationHelper: Running Job $job..."

if [ "$1" == "LioJarParseJob" ]; then
  java -Djava.library.path=/home/heh/lib \
       -Dlog4j.configuration=mylog4j.properties \
       -Dspoon.log.path=./spoon.log \
       -Dspring.profiles.active=woc \
       -Dmigration-helper.job.enabled="$job" \
       -Xms160g -Xmx160g -XX:+UseG1GC -XX:ParallelGCThreads=8 \
       -XX:ConcGCThreads=4 -XX:MaxGCPauseMillis=600000 -XX:+UnlockExperimentalVMOptions \
       -XX:G1NewSizePercent=20 -XX:G1MaxNewSizePercent=20 \
       -jar MigrationHelperJAR/migration-helper-1.0-SNAPSHOT.jar "$@"
else
  java -Djava.library.path=/home/heh/lib \
       -Dlog4j.configuration=mylog4j.properties \
       -Dspoon.log.path=./spoon.log \
       -Dspring.profiles.active=woc \
       -Dmigration-helper.job.enabled="$job" \
       -jar MigrationHelperJAR/migration-helper-1.0-SNAPSHOT.jar "$@"
fi