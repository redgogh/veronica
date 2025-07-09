# Godev

![offical](Documents/svg/offical.svg)
![test](Documents/svg/test.svg)
![license](Documents/svg/license.svg)
![build](Documents/svg/build.svg)

基于核心项目基础设施抽象提炼的企业级Java工具包，提供生产级开发支持与标准化解决方案

## 开发规范

在提交代码前请严格阅读：

- [《代码规范》](Documents/codestyle.adoc) - 变量命名/类结构/注释要求
- [《提交规范》](Documents/commit-style.adoc) - Git 提交信息格式标准

## 环境要求

最低运行时要求：

- JDK1.9+（推荐 JDK21）
- Maven 3.9+
- Python 3.7+

## 构建指南

1. 设置环境变量：

```bash
export MAVEN_HOME=/path/to/maven
export PATH=$MAVEN_HOME/bin:$PATH
```

2. 执行构建

```bash
mvn clean package -DskipTests
```

## Maven 中央仓库

```xml
<dependency>
  <groupId>io.github.redgogh</groupId>
  <artifactId>godev</artifactId>
  <version>mark-3</version>
</dependency>
```

## 许可证

**Copyright © 2019 Godev Contributors.**
