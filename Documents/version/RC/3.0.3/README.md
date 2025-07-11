# 3.0.3

---------------------

## ⭐ 新增新能

- 新增 `Chrono#weekOfYear` 函数获取今天是今年内的第几周。
- 新增 `IOUtils#copy` 拷贝文件或文件夹函数。
- 新增 `IOUtils#move` 移动文件或文件夹函数。
- 新增 `IOUtils#serialize` 序列化对象函数。
- 新增 `IOUtils#deserialize` 反序列化对象函数。
- 新增 `Chrono#toZoned` 函数用于转换时区。
- 新增 `HttpClient#newCall` 函数 Lambda 回调方式。
- 新增 `Rethrow#swallow` 以及 `Rethrow#allow` 方法。

## 👻 优化功能

- 优化 `FileResource` 新增 `Home://` 目录。
- 优化 `Chrono#getToday` 函数命名更新 `Chrono#today`。
- 优化 `Chrono#getWeekOfMonth` 函数命名更新 `Chrono#weekOfMonth`。
- 移除 `Chrono#moments` 函数。
- 优化 `Transformer` 命名更新为 `TypeCvt`。
- 优化 `any*、checkin` 等函数移动到 `Comparators` 类中。
- 优化 `iface` 包下的函数式接口命名。
- 优化 `Pair` 内存布局。
- 移除 `SystemResource` 对象。
- 优化 `Lists` 取第一个和最后一个元素方法。
- 优化 `fromVarargs` 函数，改名为 `of`。
- 优化 `firstElement/lastElement` 方法命名，改为 `first/last`。
- 移除 `ByteBuffer` 类，使用原生 NIO `ByteBuffer` 代替
- 优化 `Captor` 改为 `ErrorCatcher` 更直观的命名

## 🐞 BUG 修复

- 修复 `HttpClient#post(String, RequestConfigure)` 死循环递归调用问题。

## 🔨 构建工具