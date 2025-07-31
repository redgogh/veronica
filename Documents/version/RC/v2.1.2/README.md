# 2.1.2

---------------------

## ⭐ 新增新能

- 重写`File#getParentFile`方法，将原生`File`转为增强文件对象类型。
- 重写`File#listFiles`方法，将原生`File`转为增强文件对象类型。
- 新增`UClass`对象提供获取类注解以及判断类注解是否存在方法。
- 新增`Capturer#icall`可忽略异常返回`null`。
- 新增`OSEnvironment`获取时间和日期方法。
- 新增`atob`方法，将任意类型转为字节数组。
- 新增`UClass#toString`方法，默认返回全类名。
- 新增`OSEnvironment#availableProcessors`方法获取可用处理器核心数。
- 新增`ThreadPool`类，提供线程池功能。
- 新增`AES`加密、解密功能。

## 👻 优化功能

- 优化`Crypto`算法实现，每个算法使用单独接口定义避免代码臃肿。
- 优化`TimeUnitOperator`转`TimeUnit`方法。
- 优化包名加密以及编码工具放入`crypto`包下。
- 优化`Base64`编码解码支持解码成字节数组。

## 🐞 BUG 修复

- 修复随机数生成器使用固定长度生成不生效问题。

## 🔨 依赖变更

- 从项目中移除`lombok`依赖包。