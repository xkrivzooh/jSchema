# SchemaType

jSchema内置支持类型，对应代码文件：`ren.wenchao.jschema.SchemaType`。

这些类型主要分为如下四大类：

- 基本类型
- 包装类型
- 集合类型
- 其他类型

如下是这些基本类型的相关列表，以及对应的简单描述。

## 基本类型

| Schema Type |      Java类型      |
|:------------|:----------------:|
| byte        |       byte       |
| short       |      short       |
| int         |       int        |
| long        |       long       |
| float       |      float       |
| double      |      double      |
| boolean     |     boolean      |
| char        |       char       |
| String      | java.lang.String |

## 包装类型

| Schema Type |      Java类型      |
|:-----------:|:----------------:|
|    BYTE_WRAPPER     |       java.lang.Byte       |
|    SHORT_WRAPPER    |      java.lang.Short       |
|     INT_WRAPPER     |       java.lang.Integer        |
|    LONG_WRAPPER     |       java.lang.Long       |
|    FLOAT_WRAPPER    |      java.lang.Float       |
|   DOUBLE_WRAPPER    |      java.lang.Double      |
|   BOOLEAN_WRAPPER   |     java.lang.Boolean      |
|    CHAR_WRAPPER     |       java.lang.Character       |

## 集合类型

| Schema Type |     Java类型     |
|:-----------:|:--------------:|
|    array     |       /        |
|    Map    | java.util.Map  |
|     List     | java.util.List |
|    Set     | java.util.Set  |

## 其他类型

- record
  - 表示这是一个class的结构描述
- bytes
  - 表示这是一个字节数据
- null
  - 表示null，对应java中的关键字`null`
- void
  - 表示无返回类型，对应java中的关键字`void`
- enum
  - 表示这是一个枚举类型
