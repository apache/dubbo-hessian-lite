## Hessian Lite(Apache Dubbo private version)

[![Build Status](https://travis-ci.org/apache/dubbo-hessian-lite.svg?branch=master)](https://travis-ci.org/apache/dubbo-hessian-lite)
[![codecov](https://codecov.io/gh/apache/dubbo-hessian-lite/branch/master/graph/badge.svg)](https://codecov.io/gh/apache/dubbo-hessian-lite)
[![Gitter](https://badges.gitter.im/apache/dubbo.svg)](https://gitter.im/apache/dubbo?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)
![license](https://img.shields.io/github/license/alibaba/dubbo.svg)
![maven](https://img.shields.io/maven-central/v/org.apache.dubbo/hessian-lite.svg)

Hessian-lite is an Apache dubbo embed version of [official hessian](https://github.com/ebourg/hessian) initially.  Then this module is separated from Dubbo. All branches of [Dubbo](https://github.com/apache/dubbo) : 2.5.x, 2.6.x(since 2.6.3) and 2.7.x  are dependent on it , please see the details：

- [2018-07-13 Delete the code of apache/dubbo-hessian-lite module of the 2.6.x and 2.5.x branches](https://lists.apache.org/thread.html/72f7bbca340e96fb7da6a7ada014312953cfccd19271fad8e60cbf39@%3Cdev.dubbo.apache.org%3E) 
- [2018-06-04 Moving hessian-lite from Dubbo codebase to eco-system](https://lists.apache.org/thread.html/872bbcada2db0f04145f853dd7c7f8abef589807b8089a5016478ec8@%3Cdev.dubbo.apache.org%3E) 

Now we are trying to build a hessian group https://github.com/hessian-group to make maintainers of Hessian community work more closely together .

## Maven dependency

```xml
<dependency>
    <groupId>org.apache.dubbo</groupId>
    <artifactId>hessian-lite</artifactId>
    <version>4.0.3</version>
</dependency>
```
## Release
Deploy to maven repo:
```shell script
mvn clean deploy -Prelease
```
Apache source package:
```shell script
mvn clean install -Papache-release
```

