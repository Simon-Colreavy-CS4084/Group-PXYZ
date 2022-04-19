package com.example.recipes.util.callback;

import java.util.WeakHashMap;

   

public class CallbackManager {

          private static final WeakHashMap<Object, IGlobalCallback> CALLBACKS = new WeakHashMap<>();
    public String VPN_STATE = null;
    public String VPN_TIME = null;

          private CallbackManager(){}

      

                   

          private volatile static CallbackManager instance = null;

          public static CallbackManager getInstance() {
        if(instance==null) {
            synchronized (CallbackManager.class) {
                if(instance==null)
                    instance = new CallbackManager();
            }
        }
        return instance;
    }

       
    public CallbackManager addCallback(Object tag, IGlobalCallback callback) {
        CALLBACKS.put(tag, callback);
        return this;
    }

       
    public IGlobalCallback getCallback(Object tag) {
        return CALLBACKS.get(tag);
    }
}
