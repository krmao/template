### 生成
```
keytool -genkey -alias fruitshop -keyalg RSA -validity 20000 -keystore fruitshop.keystore
```
* -validity 20000 :有效天数:两万天




### 查看
```
keytool -list -v -keystore fruitshop.keystore -storepass fruitshop#767709667
```