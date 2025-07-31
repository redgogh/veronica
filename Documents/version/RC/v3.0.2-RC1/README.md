# 3.0.2-RC1

---------------------

## ⭐ 新增新能

- 新增 `Streams#anyMatch`匹配函数。
- 新增 `Streams#noneMatch`匹配函数。
- 新增 `StringUtils#strcap`首字母大写函数。
- 新增 `UClass#filterFields`方法用于过滤指定成员对象。
- 新增 `Optional#ifBlank`方法用于检查空字符串。
- 新增 `ByteBuffer#readByte` 方法用于读取单个字节。
- 新增 `ByteBuffer#readableBytes` 方法判断缓冲区剩余可读字节数。
- 新增 `ByteBuffer#readShort` 以及 `writeShort` 方法用于写入 short 类型数据。
- 新增 `MutableFile#loadProperties` 方法读取 `properties` 文件。
- 新增 `StreamReader` 用于统一操作输入流对象。
- 完善 `ByteBuffer` API。
- 新增 `BeanUtils#directCopy` 直接属性拷贝方法。
- 新增 `DateFormatter#autoParse` 支持自动根据常用格式解析。
- 新增 `IOUtils#strread` 支持根据文件路径读取数据。
- 新增 `Pair<A, B>` 支持两个数据的元组对象。
- 新增 `Tuple<A, B, C>` 支持三个数据的元组对象。
- 新增 `RSACipher` 加解密工具类。
- 新增 `Codec` 编码解码工具类。
- 新增 `Lists#splitIntoNChunk` 支持按块拆分集合。
- 新增 `Lists#splitByChunkSize` 支持按每块大小拆分集合。
- 新增 `BeanUtils#newInstance` 根据类创建 Bean 实例。
- 新增 `WorkBook#transferTo` 支持将工作簿转文件。
- 新增 `Chrono` 时间对象以及操作 API
- 新增 `Chrono#isThis*` 判断时间是否是本月、今年等。
- 新增 `OperatorSystem` 操作系统枚举类。

## 👻 优化功能

- 优化 `anyclude -> checkin`。
- 优化 `BeanUtils#copyProperties` 方法支持使用 `set/get` 拷贝。
- 优化 `Optional#ifNull -> Optional#ifNullable`。
- 明确 `ByteBuffer#write` 函数写入类型。
- 优化 `Crypto#uuid` 方法默认返回大写的 UUID。
- 增强 `HttpClient` API 提供通用配置项配置。
- 优化 `Lists` 工具类方法命名。
- 优化 `TimeUnits -> Chrono`。

## 🐞 BUG 修复

- 修复 `DateFormatter#parse` 方法参数传递不正确问题。
- 修复 `BeanUtils`拷贝空对象以及基本数据类型不能拷贝问题。
- 修复 `Workbook#checkout` 为空校验报错提示 Sheet 已存在问题。

## 🔨 构建工具

- 抑制 `Javadoc` 中文错误、乱码以及警告。

## ☠️已弃用的 API

- 弃用并移除 `TimeUnits` API。