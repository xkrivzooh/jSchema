# Schema Category

jSchema支持两种类型的schema：

- TypeSchema
- FunctionSchema

## TypeSchema

TypeSchema主要是用来描述Java中的类型的。比如基本类型、数组、集合、自定义类等。

### 生成TypeSchema

生成TypeSchema需要使用`TypeSchema.getSchema`函数。

简单使用办法如下:

```java
public class TypeSchemaTest {

    public static class User {
        String name;
        int age;
    }

    @Test
    public void test1() {
        TypeSchema schema = TypeSchema.getSchema(User.class);
        System.out.println(schema.toString(true));
    }
}
```

### TypeSchema示例

上节中的`User`对象对应的TypeSchema的内容如下：

```json
{
  "type": "record",
  "name": "User",
  "namespace": "ren.wenchao.jschema.TypeSchemaTest",
  "types": {},
  "fields": [
    {
      "name": "age",
      "type": "int"
    },
    {
      "name": "name",
      "type": "String"
    }
  ]
}
```

## FunctionSchema

FunctionSchema主要用来描述java中的函数结构的。

### 生成FunctionSchema

如果在上下文中，我们已经有`java.lang.reflect.Method`类型的对象的话，我们会使用`FunctionSchema.getSchema`
方法来生成FunctionSchema，例如：

```java
public class FunctionSchemaTest {

    static class A {

        boolean f1() {
            return true;
        }

        String f2(int a, long c) {
            return "";
        }
    }

    @Test
    public void test1() throws NoSuchMethodException {
        Method f1 = A.class.getDeclaredMethod("f1");
        System.out.println(FunctionSchema.getSchema(f1).toString(true));
        Assert.assertEquals("{\n" +
                "  \"type\" : \"function\",\n" +
                "  \"namespace\" : \"ren.wenchao.jschema.FunctionSchemaTest\",\n" +
                "  \"name\" : \"A\",\n" +
                "  \"functionName\" : \"f1\",\n" +
                "  \"doc\" : null,\n" +
                "  \"request\" : { },\n" +
                "  \"response\" : \"boolean\"\n" +
                "}", FunctionSchema.getSchema(f1).toString(true));
    }
}
```

但是如果我们上下文中没有`java.lang.reflect.Method`类型的对象时，我们就需要自己来完整的构建FunctionSchema了。
为了方便大家的使用，我提供了`FunctionSchemaBuilder`类来简化操作, 下面是一个简单的示例:

```java
public class FunctionSchemaBuilderTest {


    @Test
    public void test1() {
        Parameter parameter3 = new Parameter();
        parameter3.setName("arg2");
        parameter3.setDoc("arg2Doc");
        parameter3.addProp("arg2K1", "arg2K2");
        parameter3.setSchema(TypeSchema.getSchema(Integer.class));
        parameter3.addConstraint(new NotNull("arg2不能为空"));
        parameter3.setDefaultValue("1");


        FunctionSchemaBuilder builder = FunctionSchemaBuilder.builder()
                .namespace("ren.wenchao.jschema.FunctionSchemaTest")
                .name("A")
                .functionName("f1")
                .doc("doc")
                .requestParameter("arg0", "arg0Doc", TypeSchema.getSchema(int.class))
                .requestParameter("arg1", "arg1Doc", TypeSchema.getSchema(int.class))
                .requestParameter(parameter3)
                .response(TypeSchema.getSchema(int.class));
        FunctionSchema functionSchema = builder.build();
        System.out.println(functionSchema.toString(true));
        Assert.assertEquals("{\n" +
                "  \"type\" : \"function\",\n" +
                "  \"namespace\" : \"ren.wenchao.jschema.FunctionSchemaTest\",\n" +
                "  \"name\" : \"A\",\n" +
                "  \"functionName\" : \"f1\",\n" +
                "  \"doc\" : \"doc\",\n" +
                "  \"request\" : {\n" +
                "    \"arg0\" : {\n" +
                "      \"doc\" : \"arg0Doc\",\n" +
                "      \"props\" : { },\n" +
                "      \"constraints\" : { },\n" +
                "      \"type\" : \"int\",\n" +
                "      \"pos\" : \"0\"\n" +
                "    },\n" +
                "    \"arg1\" : {\n" +
                "      \"doc\" : \"arg1Doc\",\n" +
                "      \"props\" : { },\n" +
                "      \"constraints\" : { },\n" +
                "      \"type\" : \"int\",\n" +
                "      \"pos\" : \"1\"\n" +
                "    },\n" +
                "    \"arg2\" : {\n" +
                "      \"doc\" : \"arg2Doc\",\n" +
                "      \"props\" : {\n" +
                "        \"arg2K1\" : \"arg2K2\"\n" +
                "      },\n" +
                "      \"constraints\" : {\n" +
                "        \"NotNull\" : {\n" +
                "          \"message\" : \"arg2不能为空\"\n" +
                "        }\n" +
                "      },\n" +
                "      \"type\" : \"Integer\",\n" +
                "      \"default\" : \"1\",\n" +
                "      \"pos\" : \"2\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"response\" : \"int\"\n" +
                "}", functionSchema.toString(true));

    }
}
```

