### 生成
```
keytool -genkey -alias template -keyalg RSA -validity 20000 -keystore template.keystore
```
* -validity 20000 :有效天数:两万天




### 查看
```
keytool -list -v -keystore template.keystore -storepass template#767709667
```