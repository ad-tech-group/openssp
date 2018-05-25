
while true; do
  java -jar target/open-ssp-dsp-sim-0.1.1-SNAPSHOT.jar

if [ $? -eq 5 ]; then
  echo " ............. Restarting ............. "
else
  exit 1;
fi

done

