# Netty的简单使用
## Netty是什么
1. Netty是一个基于NIO的client-server框架, 使用Netty可以快速简单的开发网络应用程序
2. 它极⼤地简化并优化了 TCP 和 UDP 套接字服务器等⽹络编程,并且性能以及安全性等很多
   ⽅⾯甚⾄都要更好。
3. ⽀持多种协议 如 FTP，SMTP，HTTP 以及各种⼆进制和基于⽂本的传统协议。 
   
用官方的总结就是：Netty 成功地找到了⼀种在不妥协可维护性和性能的情况下实现易于开发，
   性能，稳定性和灵活性的⽅法。
* 用到Netty的框架 : Dubbo, RocketMQ, grpc, Elasticsearch
## Netty的优点
1. API使用简单，学习成本低。 
2. 功能强大，内置了多种解码编码器，支持多种协议, 自带编码器解码器, 解决TCP粘包/拆包问题
3. 性能高，对比其他主流的NIO框架，Netty的性能最优。
4. 社区活跃，发现BUG会及时修复，迭代版本周期短，不断加入新的功能。 
5. Dubbo、Elasticsearch都采用了Netty，质量得到验证。
## Netty结构
![](../images/Netty结构.png)