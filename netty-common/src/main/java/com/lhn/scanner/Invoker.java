package com.lhn.scanner;

import lombok.Data;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Data
public class Invoker {

    private Method method;

    private Object targetBean;

//    public static Invoker createInvoker(Method method, Object target) {
//        Invoker invoker = new Invoker();
//        invoker.setMethod(method);
//        invoker.setTargetBean(target);
//        return invoker;
//    }

    public Invoker(Method method, Object target){
        this.method = method;
        this.targetBean = target;
    }

    public Object invoke(Object... params){
        try {
            return method.invoke(targetBean, params);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
