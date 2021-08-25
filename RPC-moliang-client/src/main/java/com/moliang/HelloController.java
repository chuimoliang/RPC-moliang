package com.moliang;

import com.moliang.annotation.RpcReference;
import com.moliang.api.Hello;
import com.moliang.api.HelloService;
import com.moliang.context.RpcContext;
import com.moliang.entity.RpcResponse;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author smile2coder
 */
@Component
public class HelloController {


    @RpcReference(version = "1.1", group = "h1")
    private HelloService helloService;

    @RpcReference(version = "1.2", group = "h2", async = true)
    private HelloService helloService2;

    public void asyncTest() throws InterruptedException, ExecutionException {
        for (int i = 0;i < 100;i++) {
            CompletableFuture<RpcResponse<Object>> res = (CompletableFuture) helloService2.hello(new Hello("async", "hello"));
            res.whenComplete((ret, exception) -> {
                if (exception == null) {
                    System.out.println(ret.getData());
                } else {
                    exception.printStackTrace();
                }
            });
            //CompletableFuture<RpcResponse<String>> future = RpcContext.getContext().getCompletableFuture();
            //System.out.println(future.get().getData());
            /**
            future.whenComplete((ret, exception) -> {
                if (exception == null) {
                    System.out.println(ret.getData());
                } else {
                    exception.printStackTrace();
                }
            });
             **/
        }
    }

    public void test() throws InterruptedException {
        String hello = (String) this.helloService.hello(new Hello("111", "222"));
        //如需使用 assert 断言，需要在 VM options 添加参数：-ea
        for (int i = 0; i < 10000; i++) {
            System.out.println(helloService.hello(new Hello("111", "222")));
        }
    }
}
