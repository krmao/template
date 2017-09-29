### 生成
```
keytool -genkey -alias housekeeper -keyalg RSA -validity 20000 -keystore housekeeper.keystore
```
* -validity 20000 :有效天数:两万天




### 查看
```
keytool -list -v -keystore housekeeper.keystore -storepass housekeeper#767709667
```