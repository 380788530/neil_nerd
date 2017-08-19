package com.neil.impl;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * ******************************************************************
 * <p><b></b><br/>
 * <p><b>备注：</b><br/>
 * ******************************************************************
 *
 * @param @return 设定文件
 * @author lizhiyong
 * @Title:
 * @Description: TODO(这里用一句话描述这个方法的作用)
 * @return String    返回类型
 * @date 2017-08-16 20:10
 * @throws
 */
public class CglibProxy implements MethodInterceptor {

    //要代理的原始对象
    private Object obj;

    public Object createProxy(Object target) {
        this.obj = target;
        Enhancer enhancer = new Enhancer();
        // 设置要代理的目标类，以扩展它的功能
        enhancer.setSuperclass(this.obj.getClass());
        // 设置单一回调对象，在回调中拦截对目标方法的调用
        enhancer.setCallback(this);
        //设置类装载器
        enhancer.setClassLoader(target.getClass().getClassLoader());
        //创建代理对象
        return enhancer.create();
    }

    /**
     * 回调方法:在代理实例上拦截并处理目标方法的调用，返回结果
     *
     * @param proxy 代理类
     * @param method 被代理的方法
     * @param params 该方法的参数数组
     * @param methodProxy
     */
    @Override
    public Object intercept(Object proxy, Method method, Object[] params,
                            MethodProxy methodProxy) throws Throwable {
        Object result = null;
        // 调用之前
        doBefore();
        // 调用目标方法，用methodProxy,
        // 而不是原始的method，以提高性能
        result = methodProxy.invokeSuper(proxy, params);
        // 调用之后
        doAfter();
        return result;
    }

    private void doBefore() {
        System.out.println("before method invoke");
    }

    private void doAfter() {
        System.out.println("after method invoke");
    }

    public static void main(String[] args) {
        CglibProxy cglibProxy = new CglibProxy();
        HelloWorld hw = (HelloWorld) cglibProxy.createProxy(new HelloWorld());
        hw.sayHelloWorld();
    }

}