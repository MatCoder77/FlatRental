# Flat-Rental-Service

Generate CERT

openssl genrsa -des3 -out privateA.pem 2048
openssl req -new -key privateA.pem -out certA.csr
openssl genrsa -des3 -out privateB.pem 2048
openssl req -new -x509 -key privateB.pem -out CAcert.crt -days 15
openssl x509 -req -days 45 -in certA.csr -CA CAcert.crt -CAkey privateB.pem -set_serial 01 -out certA.crt

openssl pkcs12 -export -in certA.crt -inkey privateA.pem -chain -CAfile CAcert.crt -name "flatrental" -out keystore.p12
