# protostuff使用
## 引入依赖
```
<dependency>
    <groupId>io.protostuff</groupId>
    <artifactId>protostuff-core</artifactId>
    <version>1.7.2</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>io.protostuff</groupId>
    <artifactId>protostuff-runtime</artifactId>
    <version>1.7.2</version>
</dependency>
```
## 简单使用
1. 序列化
   ```
    @Override
    public byte[] serialize(Object obj) {
        LinkedBuffer BUFFER = protoThreadLocal.get();
        Class<?> clazz = obj.getClass();
        Schema schema = RuntimeSchema.getSchema(clazz);
        byte[] bytes;
        try {
            bytes = ProtostuffIOUtil.toByteArray(obj, schema, BUFFER);
        } finally {
            BUFFER.clear();
        }
        return bytes;
    }
   ```
   方法挺简单的, 一目了然, 需要注意的一点是, 在网上通常流传的工具类中, 缓冲区都是这么写的
   ```
    private static final LinkedBuffer BUFFER = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
   ```
   多线程下会抛出异常, 因为传入System.arraycopy()中的起止位置与实际不相同, 因此我把这里借助了ThreadLocal
   ```
    private final ThreadLocal<LinkedBuffer> protoThreadLocal = ThreadLocal.withInitial(() -> {
        LinkedBuffer BUFFER = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        return BUFFER;
    });
   ```
2. 反序列化
   ```
   public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        Schema<T> schema = RuntimeSchema.getSchema(clazz);
        T obj = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(bytes, obj, schema);
        return obj;
    }
   ```