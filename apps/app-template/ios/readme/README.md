| step  | kotlin         | swift                | 
| ----- | -----          | -----                | 
|0      |import/package  | import Foundation    |
|1      |val/ const val  | let                  |
|2      |?|              | ??                   |
|3      |\(}             | \()                  |
|4      |if else         | ? :                  |
|5      |fun             | func                 |
|6      |@Synchronized   | objc_sync_enter/exit |
|7      |@JvmStatic      | static               |




### 同步 '@Synchronized'
```
objc_sync_enter(self)

//something
    
objc_sync_exit(self)
```

### 别名 '_'
```
所有的参数名前面加一个下划线, 尤其是闭包
```

### 异常捕获 try catch
```

```

### 字符串表达式
```
\(success ? "成功" : "失败")
```
```
\(success ? "成功" : "失败")
```

### set to didSet
```

```

### null to nil
```

``` 

### 闭包 参数加 括号
```
configer?.invoke(moduleConfigUrl) {
                remoteConfig: CXHybirdModuleConfigModel? ->
```
```
configer?.invoke(moduleConfigUrl) {
                (remoteConfig: CXHybirdModuleConfigModel?) ->
```

### if 语句必须有括号

### rxbus 订阅机制

### x++