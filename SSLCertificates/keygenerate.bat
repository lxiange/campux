keytool -genkey -keystore serverkey-campuxssl.jsk -keyalg rsa -alias campuxssl -validity 999

keytool -export -alias campuxssl -file campuxssl.cer -keystore serverkey-campuxssl.jsk

keytool -import -file campuxssl.cer -keystore clientkey-campuxssl.jks