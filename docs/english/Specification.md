# Specification

## Introduction

This document defines jSchema. It is intended to be the authoritative specification. Implementations of jSchema must
adhere to this document.

## Schema Declaration

A Schema is represented in JSON by one of:

- A JSON string, naming a defined type.
- A JSON object, of the form:

```json
{
  "type": "typeName"
  ...attributes...
}
```

where typeName is either a primitive or derived type name, as defined below. Attributes not defined in this document are
permitted as metadata, but must not affect the format of serialized data.

## Primitive Types

| jSchema type string representation |    Java Type     |
|:-----------------------------------|:----------------:|
| byte                               |       byte       |
| short                              |      short       |
| int                                |       int        |
| long                               |       long       |
| float                              |      float       |
| double                             |      double      |
| boolean                            |     boolean      |
| char                               |       char       |
| String                             | java.lang.String |

## Wrapper Types

| jSchema type string representation |   SchemaType    |     Java Types      |
|------------------------------------|:---------------:|:-------------------:|
| Byte                               |  BYTE_WRAPPER   |   java.lang.Byte    |
| Short                              |  SHORT_WRAPPER  |   java.lang.Short   |
| Integer                            |   INT_WRAPPER   |  java.lang.Integer  |
| Long                               |  LONG_WRAPPER   |   java.lang.Long    |
| Float                              |  FLOAT_WRAPPER  |   java.lang.Float   |
| Double                             | DOUBLE_WRAPPER  |  java.lang.Double   |
| Boolean                            | BOOLEAN_WRAPPER |  java.lang.Boolean  |
| Character                          |  CHAR_WRAPPER   | java.lang.Character |

## Collection Types


| jSchema type string representation |     Java类型     |
|:----------------------------------:|:--------------:|
|               array                |       /        |
|                Map                 | java.util.Map  |
|                List                | java.util.List |
|                Set                 | java.util.Set  |

## Other Types

- record
    - Records use the type name `record` and support the following attributes:
      - name: a JSON string providing the name of the record (required). 
      - namespace, a JSON string that qualifies the name (optional);
      - doc: a JSON string providing documentation to the user of this schema (optional).
      - aliases: a JSON array of strings, providing alternate names for this record (optional).
      - fields: a JSON array, listing fields (required). Each field is a JSON object with the following attributes:
      - name: a JSON string providing the name of the field (required), and
      - doc: a JSON string describing this field for users (optional).
      - type: a schema, as defined above
      - default: A default value for this field, only used when reading instances that lack the field for schema evolution purposes.
- bytes
    - 表示这是一个字节数据
- null
    - 表示null，对应java中的关键字`null`
- void
    - 表示无返回类型，对应java中的关键字`void`
- enum
    - 表示这是一个枚举类型
