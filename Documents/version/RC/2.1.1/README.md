# 2.1.1

---------------------

## ⭐ 新增新能

- 新增`Capturer#icall`函数用于忽略异常调用。
- 新增`Assert#ensure`函数用于校验为`true`的值。
- 提供`Assert#notEquals`检测两个对象是否不相等。
- 重写`Row#toString`用于快速测试时直接打印每行的数据内容。
- 重写`Workbook#toString`用于快速测试时直接打印工作簿数据内容。
- 新增`ArrayUtils#copyOf`多个基础类型数组复制函数。

## 👻 优化功能

- 移除断言`Assert#ignore`忽略异常函数。取而代之使用`Capaturer#icall`。
- 私有化`HttpClient#newCall0`方法，避免外部错误引用该方法从而产生异常。
- 更新整个项目包结构，将原来的`org.redgogh.tools`更新为`org.redgogh.karasuba.lang`。
- `Enumerates`命名更新为`Enumerate`。

## 🐞 BUG 修复

- 修复空`Workbook`创建时`Sheet`索引超出问题。