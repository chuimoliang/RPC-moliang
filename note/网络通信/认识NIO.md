# NIO
## 三种IO模型
1. BIO
   * 仅适用于连接数较少的情况, 对服务器资源要求高
   * 同步阻塞, 一个连接一个线程, 如果连接不做任何事情会造成不必要的线程开销
   * 流程 : 
        1. 服务器端启动ServerSocket
        2. 客户端启动Socket连接服务端
        3. 客户端等待服务端响应
        4. 客户端线程等待请求结束后继续执行
   * 问题 :
        1. 每个请求创建独立的线程, 与对应的连接方进行数据Read, Write, 业务处理等
        2. 并发数较大时会创建大量的线程, 系统资源占用较大
        3. 建立连接后, 如果没有数据可读, 线程会阻塞在Read操作上, 造成资源浪费
2. NIO
   * 适用于连接数目比较多, 且连接短的情况
   * JDK1.4开始提供的, 是同步非阻塞的IO方式
   * 三大核心 : Channel, Buffer, Selector
     
     ![img.png](img.png)
3. AIO
   适用于连接数目多且连接较长的情况
## Buffer, Channel, Selector
1. Buffer
    * Buffer是一个父类, 根据写入数据的类型不同有很多子类, 如 ByteBuffer(最常用), CharBuffer, IntBuffer...
    * 四个关键属性
      
      ![img_1.png](img_1.png)
    * ByteBuffer
      
        ```
        ByteBuffer allocate(int capacity) //创建指定容量缓冲区。
        ByteBuffer allocateDirect(int capacity) //创建一个直接缓冲区，这样的ByteBuffer在参与IO操作时性能会更好, 免去了两次复制
        ByteBuffer wrap(byte [] array)
        ByteBuffer wrap(byte [] array, int offset, int length) //把一个byte数组或byte数组的一部分包装成ByteBuffer。
        //从buffer里读一个字节，并把postion移动一位  
        byte get(int index)
        //写模式下，往buffer里写一个字节，并把postion移动一位
        ByteBuffer put(byte b)
        int getInt()  //从ByteBuffer中读出一个int值。
        ByteBuffer putInt(int value)  // 写入一个int值到ByteBuffer中。
      
        Buffer clear()   //标记恢复到初始状态, 不清除数据, 后面写入数据会覆盖原来数据
        Buffer flip() 　 //把limit设为当前position，把position设为0，一般在从Buffer读出数据前调用。
        Buffer rewind()  //把position设为0，limit不变，一般在把数据重写入Buffer前调用。
        mark() & reset() //通过调用Buffer.mark()方法，可以标记Buffer中的一个特定position。之后可以通过调用Buffer.reset()方法恢复到这个position。
        ```
      
2. Channel
    * 与流的区别
        1. 通道是双向的, 读和写都可以进行
        2. 通道不能直接访问数据, 只能通过缓冲区
    * 常用的几种Channel实现
        1. FileChannel 用于文件的读写
        2. DatagramChannel 用于UDP的读写
        3. SocketChannel 用于TCP的读写, 多用于客户端中
        4. ServerSocketChannel 监听TCP连接请求, 每个请求会建立SocketChannel
    * FileChannel
        ```
        //1.创建一个RandomAccessFile（随机访问文件）对象，
        RandomAccessFile raf=new RandomAccessFile("D:\\niodata.txt", "rw");
        //通过RandomAccessFile对象的getChannel()方法。FileChannel是抽象类。
        FileChannel inChannel = raf.getChannel();
        //2.创建一个读数据缓冲区对象
        ByteBuffer buf = ByteBuffer.allocate(48);
        //3.从通道中读取数据
        int bytesRead = inChannel.read(buf);
        //创建一个写数据缓冲区对象
        ByteBuffer buf2 = ByteBuffer.allocate(48);
        //写入数据
        buf2.put("filechannel test".getBytes());
        buf2.flip();
        inChannel.write(buf);
        // 关闭通道
        channel.close();
        ```
    * SocketChannel和ServerSocketChannel
        ```
        //1.通过SocketChannel的open()方法创建一个SocketChannel对象
        SocketChannel socketChannel = SocketChannel.open();
        //2.连接到远程服务器（连接此通道的socket）
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 3333));
        // 3.创建写数据缓存区对象
        ByteBuffer writeBuffer = ByteBuffer.allocate(128);
        writeBuffer.put("hello WebServer this is from WebClient".getBytes());
        writeBuffer.flip();
        socketChannel.write(writeBuffer);
        //创建读数据缓存区对象
        ByteBuffer readBuffer = ByteBuffer.allocate(128);
        socketChannel.read(readBuffer);
        StringBuilder stringBuffer = new StringBuilder();
        //4.将Buffer从写模式变为可读模式
        readBuffer.flip();
        while (readBuffer.hasRemaining()) {
            stringBuffer.append((char) readBuffer.get());
        }
        System.out.println("从服务端接收到的数据："+stringBuffer);
        socketChannel.close();
        ```
        ```
        try {
            //1.通过ServerSocketChannel 的open()方法创建一个ServerSocketChannel对象，open方法的作用：打开套接字通道
            ServerSocketChannel ssc = ServerSocketChannel.open();
            //2.通过ServerSocketChannel绑定ip地址和port(端口号)
            ssc.socket().bind(new InetSocketAddress("127.0.0.1", 3333));
            //通过ServerSocketChannelImpl的accept()方法创建一个SocketChannel对象用户从客户端读/写数据
            SocketChannel socketChannel = ssc.accept();
            //3.创建写数据的缓存区对象
            ByteBuffer writeBuffer = ByteBuffer.allocate(128);
            writeBuffer.put("hello WebClient this is from WebServer".getBytes());
            writeBuffer.flip();
            socketChannel.write(writeBuffer);
            //创建读数据的缓存区对象
            ByteBuffer readBuffer = ByteBuffer.allocate(128);
            //读取缓存区数据
            socketChannel.read(readBuffer);
            StringBuilder stringBuffer=new StringBuilder();
            //4.将Buffer从写模式变为可读模式
            readBuffer.flip();
            while (readBuffer.hasRemaining()) {
                stringBuffer.append((char) readBuffer.get());
            }
            System.out.println("从客户端接收到的数据："+stringBuffer);
            socketChannel.close();
            ssc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ```
      
3. Selector
    * Selector能够检测多个注册的Channel上是否有事件发生, 只有在通道真正有读写发生时才会进行读写, 避免了多线程之间切换上下文的开销
    * 工作流程 : Channel注册到Selector中, 调用Selector的方法将可进行IO操作的通道的SelectionKey加入到一个集合中, 对集合中每个对象进行处理
    * Selector 方法
        * public static Selector open() / Selector s = Selector.open() : 获取一个Selector对象
        * public int select(long timeout) : 监控所有注册的Channel, 其中有IO操作可以进行时, 将对应的SelectionKey加入内部集合中, timeout为超时时间
        * public Set<SelectionKey> selectedKeys() : 获取集合中所有SelectionKey对象
    * SelectionKey : Selector和Channel的注册关系
        * int OP_ACCEPT 接受
        * int OP_CONNECT 连接
        * int OP_READ 读
        * int OP_WRITE 写
        * 还有几个方法, 无非就是 selector(), channel(), isReadable()....作用一目了然
    * 注册Channel
        ```
        channel.configureBlocking(false);
        //通道必须是非阻塞类型
        SelectionKey key = channel.register(selector, Selectionkey.OP_READ); //可以用'|'添加多个监听类型
        ```
    ```
    public static void main(String[] args) {
        try {
            ServerSocketChannel s = ServerSocketChannel.open();
            s.socket().bind(new InetSocketAddress("127.0.0.1", 8888));
            s.configureBlocking(false);
            Selector selector = Selector.open();
            s.register(selector, SelectionKey.OP_ACCEPT);

            ByteBuffer readBuff = ByteBuffer.allocate(1024);
            ByteBuffer writeBuff = ByteBuffer.allocate(128);
            writeBuff.put("服务端:)".getBytes());
            writeBuff.flip();
            while (true) {
                int nReady = selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> it = keys.iterator();
                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    it.remove();
                    if (key.isAcceptable()) {
                        SocketChannel socketChannel = s.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                    }
                    else if (key.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        readBuff.clear();
                        socketChannel.read(readBuff);
                        readBuff.flip();
                        System.out.println(new String(readBuff.array()));
                        key.interestOps(SelectionKey.OP_WRITE);
                    }
                    else if (key.isWritable()) {
                        writeBuff.rewind();
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        socketChannel.write(writeBuff);
                        key.interestOps(SelectionKey.OP_READ);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    ```
    ```
    public static void main(String[] args) {
        try {
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress("127.0.0.1", 8888));

            ByteBuffer writeBuffer = ByteBuffer.allocate(32);
            ByteBuffer readBuffer = ByteBuffer.allocate(32);

            writeBuffer.put("客户端:)".getBytes());
            writeBuffer.flip();

            while (true) {
                writeBuffer.rewind();
                socketChannel.write(writeBuffer);
                readBuffer.clear();
                socketChannel.read(readBuffer);
                System.out.println(new String(readBuffer.array()));
                readBuffer.clear();
                TimeUnit.SECONDS.sleep(3);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    ```
## 零拷贝
    