package com.commonlib.utils;

import android.app.Activity;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * @author songdehuai
 */
public class FreeSync {

    /**
     * 默认key，自定义key避免使用此名称
     */
    private static final String DEFAULTFREESYNCNAME = "FREESYNC_DEFAULTFREESYNCNAME";

    private ConcurrentHashMap<String, CopyOnWriteArrayList<FreeSyncCallback>> freeSyncCallbackHashMap = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, FreeSync> freeSyncArrayMap = new ConcurrentHashMap<>();
    private static FreeSync freeSync = new FreeSync();

    private FreeSync() {

    }

    public static FreeSync defaultFreeSync() {
        freeSyncArrayMap.put(DEFAULTFREESYNCNAME, freeSync);
        return freeSync;
    }

    public static FreeSync freeSyncWithName(String name) {
        if (freeSyncArrayMap.containsKey(name)) {
            return freeSyncArrayMap.get(name);
        } else {
            freeSyncArrayMap.put(name, new FreeSync());
            return freeSyncArrayMap.get(name);
        }
    }

    public synchronized <T> void addCall(Activity activity, FreeSyncCallback<T> freeSyncCallback) {
        addCall(activity.getLocalClassName(), freeSyncCallback);
    }

    public synchronized <T> void addOneCallOnly(Activity activity, FreeSyncCallback<T> freeSyncCallback) {
        addOneCallOnly(activity.getLocalClassName(), freeSyncCallback);
    }

    public synchronized <T> void addCall(String name, FreeSyncCallback<T> freeSyncCallback) {
        boolean isHas = false;
        for (String s : freeSyncCallbackHashMap.keySet()) {
            if (name.equals(s)) {
                freeSyncCallbackHashMap.get(s).add(freeSyncCallback);
                isHas = true;
            }
        }
        if (!isHas) {
            CopyOnWriteArrayList<FreeSyncCallback> callBackList = new CopyOnWriteArrayList<>();
            callBackList.add(freeSyncCallback);
            freeSyncCallbackHashMap.put(name, callBackList);
        }
    }

    public synchronized <T> void addOneCallOnly(String name, FreeSyncCallback<T> freeSyncCallback) {
        for (String s : freeSyncCallbackHashMap.keySet()) {
            if (name.equals(s)) {
                freeSyncCallbackHashMap.remove(s);
            }
        }
        CopyOnWriteArrayList<FreeSyncCallback> callBackList = new CopyOnWriteArrayList<>();
        callBackList.add(freeSyncCallback);
        freeSyncCallbackHashMap.put(name, callBackList);
    }


    public synchronized void call(Activity activity) {
        call(activity.getLocalClassName());
    }

    public synchronized void call(Activity activity, Object obj) {
        call(activity.getLocalClassName(), obj);
    }

    public synchronized void call(String name, Object obj) {
        CopyOnWriteArrayList<FreeSyncCallback> callBackList;
        for (String s : freeSyncCallbackHashMap.keySet()) {
            if (name.equals(s)) {
                callBackList = freeSyncCallbackHashMap.get(s);
                for (int i = 0; i < callBackList.size(); i++) {
                    callBackList.get(i).onCall(name, obj);
                }
            }
        }
    }

    public synchronized void call(String name) {
        CopyOnWriteArrayList<FreeSyncCallback> callBackList;
        for (String s : freeSyncCallbackHashMap.keySet()) {
            if (name.equals(s)) {
                callBackList = freeSyncCallbackHashMap.get(s);
                for (int i = 0; i < callBackList.size(); i++) {
                    callBackList.get(i).onCall(name, "");
                }
            }
        }
    }

    public synchronized void removeCall(Activity activity) {
        removeCall(activity.getLocalClassName());
    }

    public synchronized void removeCall(String name) {
        for (String s : freeSyncCallbackHashMap.keySet()) {
            if (name.equals(s)) {
                freeSyncCallbackHashMap.remove(name);
            }
        }
    }

    public synchronized void removeFreeSync(String name) {
        for (String s : freeSyncArrayMap.keySet()) {
            if (name.equals(s)) {
                freeSyncArrayMap.remove(name);
            }
        }
    }

    public synchronized void clearAllDefault() {
        freeSyncCallbackHashMap.clear();
    }

    public synchronized void clearAllWithName(String name) {
        FreeSync freeSync = freeSyncWithName(name);
        if (freeSync != null) {
            freeSync.freeSyncCallbackHashMap.clear();
        }
    }

    public interface FreeSyncCallback<T> {

        void onCall(String name, T obj);
    }
}
