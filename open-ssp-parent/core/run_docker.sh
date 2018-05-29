
#docker run -p 8080:8080 ssp-server
docker run --net open-ssp-net -h ssp-server --ip 192.18.0.10 --add-host ssp-services:192.18.0.11 --add-host dsp-sim:192.18.0.12 -p 8080:8080 ssp-server

