set CORS_URLS_TO_ALLOW=https://localhost:4200,https://localhost:8081
set ENC_INPUT_DB_PASSWORD=ENC(LBHmmfdFreYW7P/RKY5PnAkq72ECZeRFzq6ZaAYjW7v3GgJmhUjHEY/BQ9liYckK)
set ENC_OUTPUT_DB_PASSWORD=ENC(Av8kemJmjXaR8iBuZxaJEwCkzkbxPmbXsGh4ZMjwOrc31P4ZYDw8w5/WkSyMhRma)
set ENC_SSL_KEY_STORE_PASSWORD=ENC(h5h7BNOXU1KJm2Bfd3kV+w3xBK/RAtvaw8Cjv5/+Vjj1OSXxoaYYtnaL9zVwB/8j)
set INPUT_DB_URL=jdbc:oracle:thin:@kerneldc.com:1522:sr22
set JASYPT_ENCRYPTOR_PASSWORD=FlightLogServer
set OAUTH2_ISSUER_URI=https://localhost:8083/realms/flight-log
set OUTPUT_DB_URL=jdbc:oracle:thin:@192.168.2.5:1521:sr22
set PORT=8441
set spring.profiles.active=dev

java -jar ..\..\..\target\FlightLogServer-2.1.1-SNAPSHOT.jar