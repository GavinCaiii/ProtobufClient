package com.caitou.protobufdemo;

import android.app.Application;
import android.content.Context;

/**
 * The application context.
 * @author swallow
 * @since 2015.4.1
 */
public class AppContext extends Application {
    public static final String TERMINAL_TYPE = "OBD_E300";
    public static final String VERSION_NO = "version";
    public static final String SERVER_HOST = "10.1.1.227";
    public static final int SERVER_PORT = 10086;

    private static AppContext instance = null;
    public static AppContext getInstance(){
        return instance;
    }

    public Context getContext(){
        if (instance == null)
            return null;
        return instance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

}
