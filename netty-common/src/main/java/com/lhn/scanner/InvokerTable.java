package com.lhn.scanner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InvokerTable {

    private static ConcurrentHashMap<String /*module */, Map<String /*cmd*/,Invoker>> invokerMap =
            new ConcurrentHashMap<>();

    public static void addInvoker(String module, String cmd, Invoker invoker){
        Map<String /*cmd*/,Invoker> map = invokerMap.get(module);
        if (map == null) {
            // 如果能查不到module不在缓存中，那么先将module放到缓存
            map = new HashMap<>();
            invokerMap.put(module, map);
        }
        map.put(cmd,invoker); // 存不存在都在这里对module的缓存值进行更改
    }

    public static Invoker getInvoker(String module, String cmd) {
        Map<String /*cmd*/, Invoker> map = invokerMap.get(module);
        if(map != null) {
            return map.get(cmd);
        }
        return null;
    }
}
