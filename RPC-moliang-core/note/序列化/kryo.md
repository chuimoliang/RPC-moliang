# kryo的使用
## 引入依赖
```
  <dependency>
        <groupId>com.esotericsoftware</groupId>
        <artifactId>kryo</artifactId>
        <version>4.0.2</version>
    </dependency>
```
如果与spring之间发生ASM依赖版本冲突,则使用下面的依赖
```
  <dependency>
        <groupId>com.esotericsoftware</groupId>
        <artifactId>kryo</artifactId>
        <version>4.0.2</version>
    </dependency>
```
## kryo的三种读写方式
1. 知道class字节码, 且对象不为空
   ```
   kryo.writeObject(output, someObject);
   SomeClass someObject = kryo.readObject(input, SomeClass.class);
   ```
2. 知道class字节码, 对象可能为空
   ```
   kryo.writeObjectOrNull(output, someObject);
   SomeClass someObject = kryo.readObjectOrNull(input, SomeClass.class);
   ```
3. 不知道class字节码, 对象可能为空
   ```
   kryo.writeClassAndObject(output, object);
   Object object = kryo.readClassAndObject(input);
   if (object instanceof SomeClass) {     
       // ...
   }
   ```
## 注意
1. 两个参数
    ```
    kryo.setRegistrationRequired(false);//关闭注册行为
    kryo.setReferences(true);//支持循环引用 
    ```
    Kryo支持对注册行为，如kryo.register(SomeClazz.class);
    这会赋予该Class一个从0开始的编号，
    但Kryo使用注册行为最大的问题在于，
    其不保证同一个Class每一次注册的号码相同，
    这与注册的顺序有关，也就意味着在不同的机器、同一个机器重启前后都有可能拥有不同的编号，
    这会导致序列化产生问题，所以在分布式项目中，一般关闭注册行为。

    第二个注意点在于循环引用，
    Kryo为了追求高性能，可以关闭循环引用的支持。
    大多数情况下，都不会关闭它。
   
2. Kryo不支持Bean中增删字段。如果使用Kryo序列化了一个类，存入了Redis，对类进行了修改，会导致反序列化的异常
3. 不支持不包含无参构造器类的反序列化
4. Kryo是线程不安全的，通常借助ThreadLocal来维护以保证其线程安全。