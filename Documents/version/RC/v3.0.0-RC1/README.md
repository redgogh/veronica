# 3.0.0-RC1

---------------------

## ⭐ 新增新能

- `UClass`新增`isPrimitiveCheck`检测基本数据类型函数。
- `BasicConverter`新增`typevalue`任意数据类型转换功能。
- `SHA256Codec`支持大文件编码。
- `Workbook`支持转Java对象。

## 👻 优化功能

- 优化`DateFormatter#format`功能参数顺序。
- 优化`Workbook`获取行数据时自动移除空行。
- 优化`strhas`命名，重命名为`strclude`以及`striclude`。
- `HttpClient`支持使用`setQueryValue`的方式设置查询参数。

## 🐞 BUG 修复

- 修复`Workbook`对象不支持多种数据类型解析问题。
