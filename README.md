# jutil
Java 通用类库
> Java 版本: jdk11

## 模块
- jdk
- media
- tool
- validator
- web

## 构建
```
# 安装全部模块到本地仓库
mvn clean install

# 安装 jdk 模块到本地仓库
mvn clean install -am -Dmaven.test.skip -pl jdk
```

```
# 安装全部模块到 nexus 仓库
mvn clean deploy

# 安装 jdk 模块到 nexus 仓库
mvn clean deploy -am -Dmaven.test.skip -pl jdk
```
> nexus 仓库在 pom.xml 文件的 distributionManagement - repository 中指定
