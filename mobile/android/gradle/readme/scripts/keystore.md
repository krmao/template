### 生成
```
keytool -genkey -alias template -keyalg RSA -validity 20000 -keystore template.keystore
```
* -validity 20000 :有效天数:两万天

> JKS 密钥库使用专用格式。建议使用 "keytool -importkeystore -srckeystore debug.keystore -destkeystore debug.keystore -deststoretype pkcs12" 迁移到行业标准格式 PKCS12。




### 查看
```
keytool -list -v -keystore template.keystore -storepass template#767709667
```