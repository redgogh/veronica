# 3.0.2-RC2

---------------------

## ⭐ 新增新能

- 新增 `JWTGrantor` 创建授权 Token 对象。
- 新增 `UClass#getConstant` 获取常量属性方法。
- 支持 `Chrono` 直接作为 `Date` 使用。
- 新增 `UnauthorizationException` 未授权异常。

## 👻 优化功能

- 移除二级包名路径 `redgogh`。
- 优化 `Capturer` 命名更新为 `Captor`。
- 优化 `Captor` 所有捕获异常抛出 `SystemRuntimeException`。
- 优化 `BasicConverter` 命名更新为 `Transformer`。
- 优化源码注释，移除大多不必要的注解。

## 🐞 BUG 修复

- 修复 `Chrono#plusMillis` 添加时间错误问题。

## 🔨 构建工具

- 新增 `com.nimbusds:nimbus-jose-jwt:10.0.2` 依赖包。
- 移除 `joda-time:joda-time:2.12.2` 依赖包。
