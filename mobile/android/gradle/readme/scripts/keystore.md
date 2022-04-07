### 生成签名文件
```
# -alias        别名
# -validity     有效天数
# -keystore     签名文件
# -destkeystore 签名文件(新标准)

keytool -genkey -alias template -keyalg RSA -validity 20000 -keystore template.keystore
keytool -importkeystore -srckeystore template.keystore -destkeystore template.keystore.pkcs12 -deststoretype pkcs12
```

### 签名 app
```
signingConfigs {

    debug {
        keyAlias 'template'
        keyPassword 'password'
        storeFile file('template.keystore.pkcs12')
        storePassword 'password'
        v2SigningEnabled true
    }

    release {
        keyAlias 'template'
        keyPassword 'password'
        storeFile file('template.keystore.pkcs12')
        storePassword 'password'
        v2SigningEnabled true
    }
}
```

### 查看签名信息
```
keytool -list -v -keystore template.keystore.pkcs12 -storepass password

密钥库类型: PKCS12
密钥库提供方: SUN

您的密钥库包含 1 个条目

别名: template
创建日期: 2020-12-28
条目类型: PrivateKeyEntry
证书链长度: 1
证书[1]:
所有者: CN=Unknown, OU=Unknown, O=Unknown, L=Unknown, ST=Unknown, C=Unknown
发布者: CN=Unknown, OU=Unknown, O=Unknown, L=Unknown, ST=Unknown, C=Unknown
序列号: 4ca0050c
有效期为 Mon Dec 28 15:05:25 CST 2020 至 Tue Oct 01 15:05:25 CST 2075
证书指纹:
         MD5:  00:14:24:83:38:CD:5D:56:04:06:FD:12:7D:D7:31:00
         SHA1: 00:74:9F:72:65:62:70:64:BF:94:5E:CF:89:60:0C:88:4F:F5:2F:00
         SHA256: 00:36:CF:EC:19:F3:98:83:01:E7:41:28:AE:D2:CF:4C:AD:69:00:D0:1F:8B:E9:77:2C:DB:77:46:2B:F2:36:00
签名算法名称: SHA256withRSA
主体公共密钥算法: 2048 位 RSA 密钥
版本: 3

扩展: 

#1: ObjectId: 2.5.29.10 Criticality=false
SubjectKeyIdentifier [
KeyIdentifier [
0000: 00 F7 7D 53 9F 62 85 E3   00 91 71 6F 27 38 00 F8  ...S.b....qo'8..
0010: 07 F3 9E 00                                        ...#
]
]



*******************************************
*******************************************



```