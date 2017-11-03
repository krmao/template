# 解压
unzip -q myapp.jar
# 运行
java org.springframework.boot.loader.JarLauncher
# 生产模式用以下的nohup方式，以防程序随着shell一起关闭
nohup java org.springframework.boot.loader.JarLauncher &