/*
 * Copyright (c) 2018.
 * Author：Zhao
 * Email：joeyzhao1005@gmail.com
 */

package com.kit.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.util.LruCache;
import android.util.Log;


import com.kit.utils.StringUtils;
import com.kit.utils.intent.BundleData;
import com.kit.utils.log.Zog;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class BroadcastCenter {
    private LocalBroadcastManager localBroadcastManager;

    private CopyOnWriteArrayList<WeakReference<BroadcastReceiver>> broadcastReceiverList = new CopyOnWriteArrayList<WeakReference<BroadcastReceiver>>();
    private static ConcurrentHashMap<String, BundleData> map;
    private static BroadcastCenter singleBroadcast = new BroadcastCenter();

    private LruCache<String, BundleData> dataMap;
    private Intent intent;
    private String action;
    private BundleData data;


    public static BroadcastCenter getInstance() {
        return singleBroadcast;
    }

    public boolean init(Context context) {
        Zog.d("init | Enter");

        if (localBroadcastManager != null) {
            Zog.e("init | localBroadcastManager is null");
            return false;
        }

        if (null == context) {
            Zog.e("init | context is null");
            return false;
        }

        localBroadcastManager = LocalBroadcastManager.getInstance(context);
        map = new ConcurrentHashMap<String, BundleData>(10);
        dataMap = new LruCache<String, BundleData>(20);
        broadcastReceiverList = new CopyOnWriteArrayList<WeakReference<BroadcastReceiver>>();
        Zog.d("init | Leave");
        return true;
    }


    public BroadcastCenter intent(Intent intent) {
        this.intent = intent;
        return this;
    }


    public BroadcastCenter action(String action) {
        this.action = action;
        return this;
    }


    /**
     * 从压入的数据取出数据
     *
     * @param key
     */
    public <T> T get(String key) {
        if (action == null) {
            Zog.e("action must init first,you can call like action(xxx).get(yyy)");
        }
        return dataMap.get(action).get(key);
    }


    /**
     * 往item中压入数据 无敌的方法
     *
     * @param key
     * @param value
     */
    public <T> BroadcastCenter put(String key, T value) {
        getData().put(key, value);
        return this;
    }

    public BroadcastCenter extras(BundleData bundleData) {
        this.data = bundleData;
        return this;
    }

    private BundleData getData() {
        if (data == null) {
            data = new BundleData();
        }
        return data;
    }

    public void broadcast() {
        if (null == localBroadcastManager) {
            Zog.e("localBroadcastManager is null, is the BroadcastCenter inited?");
            return;
        }

        if (intent == null) {
            if (!StringUtils.isEmptyOrNullStr(action)) {
                intent = new Intent(action);
            }
            Zog.d("intent created with action");
            if (!StringUtils.isEmptyOrNullStr(action)) {
                intent = new Intent(action);
            }
            if (data != null) {
                map.put(action, data);
                Zog.d("intent created with data");
            } else {
                map.remove(action);
            }
        }

        if (intent == null) {
            Zog.e("intent create failed");
            return;
        }

        if (action != null && data != null) {
            dataMap.put(action, data);
        }

        localBroadcastManager.sendBroadcast(intent);
        Zog.d("broadcast | sendBroadcast finished");
        intent = null;
        action = null;
        data = null;
    }

    public void registerReceiver(BroadcastReceiver br, String... actions) {
        if (null == br || null == actions || null == localBroadcastManager) {
            Zog.e("registerReceiver | param is null or localBroadcastManager is null");
            return;
        }

        IntentFilter iFilter = new IntentFilter();
        if (actions != null) {
            for (String ac : actions) {
                iFilter.addAction(ac);
            }
        }
        localBroadcastManager.registerReceiver(br, iFilter);
        broadcastReceiverList.add(new WeakReference<>(br));
    }

    public boolean checkBroadcastReceiverRegistered(String receiverClassName) {

        if (broadcastReceiverList == null || broadcastReceiverList.isEmpty()) {
            return false;
        }
        Iterator<WeakReference<BroadcastReceiver>> iterator = broadcastReceiverList.iterator();
        while (iterator.hasNext()) {
            WeakReference<BroadcastReceiver> key = iterator.next();
            if (key == null || key.get() == null) {
                continue;
            }
            BroadcastReceiver receiver = key.get();

            if (receiver.getClass().getName().equals(receiverClassName)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkBroadcastReceiverRegistered(Class receiverClass) {
        return checkBroadcastReceiverRegistered(receiverClass.getClass().getName());
    }

    /**
     * @param br
     * @param actions 至少传入一个
     */
    public void unregisterReceiver(BroadcastReceiver br, @NonNull String... actions) {
        if (null == br || null == localBroadcastManager) {
            Zog.e("unregisterReceiver | param is null or localBroadcastManager is null");
            return;
        }

        if (actions != null) {
            for (String ac : actions) {
                map.remove(ac);
            }
        }
        try {
            localBroadcastManager.unregisterReceiver(br);
        } catch (Exception e) {

        }

        Iterator<WeakReference<BroadcastReceiver>> iterator = broadcastReceiverList.iterator();
        while (iterator.hasNext()) {
            WeakReference<BroadcastReceiver> key = iterator.next();
            if (key == null || key.get() == null) {
                continue;
            }
            BroadcastReceiver receiver = key.get();

            if (receiver == br) {
                broadcastReceiverList.remove(key);
            }
        }
    }

    public void unregisterAllReceiver() {
        if (null == localBroadcastManager) {
            Zog.e("unregisterAllReceiver | localBroadcastManager is null");
            return;
        }

        Iterator<WeakReference<BroadcastReceiver>> iterator = broadcastReceiverList.iterator();
        while (iterator.hasNext()) {
            WeakReference<BroadcastReceiver> key = iterator.next();
            if (key == null || key.get() == null) {
                continue;
            }

            localBroadcastManager.unregisterReceiver(key.get());
        }
        map.clear();
        broadcastReceiverList.clear();
    }


}
