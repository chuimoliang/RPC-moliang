# RPC相关问题
1. RPC框架？
   * 远程过程调用协议RPC（Remote Procedure Call Protocol)
   * 两个不同的服务器上的服务提供的方法不在一个内存空间，所以，需要通过网络编程才能传递方法调用所需要的参数。
   并且，方法调用的结果也需要通过网络编程来接收。如果我们自己手动网络编程来实现这个调用过程的话工作量是非常大,
   通过 RPC 可以帮助我们调用远程计算机上某个服务的方法，这个过程就像调用本地方法一样简单。
   并且！我们不需要了解底层网络编程的具体细节
   * 主流RPC框架 : gRPC, dubbo,
3. 通信协议
   * 协议头 : 魔数(5个字节) + 版本号(1个字节) + 消息类型(1个字节) + 序列化类型(1个字节) + 数据长度(4个字节)
   * 根据消息长度写入数据长度
4. Netty 粘包拆包 心跳机制
   * 因为NIO的使用比较复杂, Netty封装了NIO的很多细节, 使用起来比较简单, 而且自带编解码器, 和其他NIO框架相比性能比较好
   * 
5. 序列化
   * 将对象序列化成字节数组
   * 实现了两种序列化方式, Kryo和Protobuf
   * 序列化速度 : 
   * 如何选择序列化方式 : 
      * 序列化方式可以分为两类, 一类是文本类序列化方式, 比如Json和xml格式, 可读性好, 但是性能很差, 另一类是二进制序列化方式.
      * 二进制的序列化方式比较 : 
         1. JDK自带的序列化方式, 不支持跨语言调用, 性能较差, 序列化后的字节数组体积较大(记录了完整的类型信息, 其他方式
            往往只记录类的全路径名, 反序列化时根据通过类加载进行加载) , 传输成本增大
         2. kryo的性能比较好, 速度快, 生成的字节码体积小, 对Java支持比较好, 两点优化 : 对long, int等数据类型变长存储, 
            使用类似缓存的结构, 相同对象序列化一次
         3. ProtoStuff支持多种语言, 跨平台, 性能好
   
6. 注册中心
   * 注册中心 : 用于服务端注册服务以及客户端发现服务
   * 用到了注册中心, 使用的Zookeeper作为注册中心
   * Zookeeper 简单了解 :
    
7. 为啥要用动态代理呢？JDK自带的动态代理有啥问题不？
   * 使用动态代理生成代理对象
   * 当调用代理对象的方法时, 由代理进行方法相关信息的组装并发送到服务器进行远程调用, 并由代理接收调用结果并返回.
8. 负载均衡策略
    * 随机法: 
    * 一致性哈希: 
9. 服务动态发现怎么做？
10. 服务提供者如何实现优雅下线？
    * 服务提供开启时通过Runtime.addShutdownHook方法增加触发关闭钩子方法, 清除所有注册的服务
11. 服务预热
    * 启动预热就是让刚启动的服务，不直接承担全部的流量，而是让它随着时间的移动慢慢增加调用次数，
    * 最终让流量缓和运行一段时间后达到正常水平
    * 需要实现基于权重的负载均衡
12. 服务消费者掉调用流程？