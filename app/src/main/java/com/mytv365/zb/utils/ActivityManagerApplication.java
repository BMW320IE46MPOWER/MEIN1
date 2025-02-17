package com.mytv365.zb.utils;

import android.app.Activity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2016/11/11 0011.
 *
 */
public class ActivityManagerApplication {

    private static Map<String,Activity> destoryMap = new HashMap<>();

    private ActivityManagerApplication() {
    }

    /**
     * 添加到销毁队列
     *
     * @param activity 要销毁的activity
     */

    public static void addDestoryActivity(Activity activity,String activityName) {
        destoryMap.put(activityName,activity);
    }
    /**
     *销毁指定Activity
     */
    public static void destoryActivity(String activityName) {
        Set<String> keySet=destoryMap.keySet();
        for (String key:keySet){
            destoryMap.get(key).finish();
        }
    }


}
