#!/bin/bash

if [ "$#" -ne 3 ]; then
  echo "Usage: importcert.sh <CA cert PEM file> <bouncy castle jar> <keystore pass>"
  exit 1
fi

CACERT=$1
BCJAR=$2
SECRET=$3

TRUSTSTORE=mytruststore.bks
ALIAS=`openssl x509 -inform PEM -subject_hash -noout -in $CACERT`

if [ -f $TRUSTSTORE ]; then
    rm $TRUSTSTORE || exit 1
fi

echo "Adding certificate to $TRUSTSTORE..."
keytool -import -v -trustcacerts -alias $ALIAS \
      -file $CACERT \
      -keystore $TRUSTSTORE -storetype BKS \
      -providerclass org.bouncycastle.jce.provider.BouncyCastleProvider \
      -providerpath $BCJAR \
      -storepass $SECRET

echo "" 
echo "Added '$CACERT' with alias '$ALIAS' to $TRUSTSTORE..."
