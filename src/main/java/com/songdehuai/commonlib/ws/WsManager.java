package com.songdehuai.commonlib.ws;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;


public class WsManager implements IWsManager {
    //重连自增步长
    private final static int RECONNECT_INTERVAL = 10 * 1000;
    //最大重连间隔,只有在设置重连时常自增长的时候才有效
    private final static long RECONNECT_MAX_TIME = 120 * 1000;
    //默认重连间隔
    private final static long RECONNECT_TIME = 8000;
    //是否自增长重连间隔时常，默认false
    private final static boolean NEED_TIME_INCREASE = false;
    private Context mContext;
    private String wsUrl;
    private WebSocket mWebSocket;
    private OkHttpClient mOkHttpClient;
    private Request mRequest;
    //webSocket连接状态
    private int mCurrentStatus = WsStatus.DISCONNECTED;
    //是否需要断线自动重连
    private boolean isNeedReconnect;
    //是否为手动关闭webSocket连接
    private boolean isManualClose = false;
    private WsStatusListener wsStatusListener;
    private Lock mLock;
    private Handler wsMainHandler = new Handler(Looper.getMainLooper());
    //重连次数
    private int reconnectCount = 0;


    private Runnable reconnectRunnable = new Runnable() {
        @Override
        public void run() {
            if (wsStatusListener != null) {
                wsStatusListener.onReconnect();
            }
            buildConnect();
        }
    };

    private WebSocketListener mWebSocketListener = new WebSocketListener() {
        @Override
        public void onOpen(WebSocket webSocket, final Response response) {
            mWebSocket = webSocket;
            setCurrentStatus(WsStatus.CONNECTED);
            connected();
            if (wsStatusListener != null) {
                if (Looper.myLooper() != Looper.getMainLooper()) {
                    wsMainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            wsStatusListener.onOpen(response);
                        }
                    });
                } else {
                    wsStatusListener.onOpen(response);
                }
            }
        }

        @Override
        public void onMessage(WebSocket webSocket, final ByteString bytes) {
            if (wsStatusListener != null) {
                if (Looper.myLooper() != Looper.getMainLooper()) {
                    wsMainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            wsStatusListener.onMessage(bytes);
                        }
                    });
                } else {
                    wsStatusListener.onMessage(bytes);
                }
            }
        }

        @Override
        public void onMessage(WebSocket webSocket, final String text) {
            if (wsStatusListener != null) {
                if (Looper.myLooper() != Looper.getMainLooper()) {
                    wsMainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            wsStatusListener.onMessage(text);
                        }
                    });
                } else {
                    wsStatusListener.onMessage(text);
                }
            }
        }

        @Override
        public void onClosing(WebSocket webSocket, final int code, final String reason) {
            if (wsStatusListener != null) {
                if (Looper.myLooper() != Looper.getMainLooper()) {
                    wsMainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            wsStatusListener.onClosing(code, reason);
                        }
                    });
                } else {
                    wsStatusListener.onClosing(code, reason);
                }
            }
        }

        @Override
        public void onClosed(WebSocket webSocket, final int code, final String reason) {
            if (wsStatusListener != null) {
                if (Looper.myLooper() != Looper.getMainLooper()) {
                    wsMainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            wsStatusListener.onClosed(code, reason);
                        }
                    });
                } else {
                    wsStatusListener.onClosed(code, reason);
                }
            }
        }

        @Override
        public void onFailure(WebSocket webSocket, final Throwable t, final Response response) {
            tryReconnect();
            if (wsStatusListener != null) {
                if (Looper.myLooper() != Looper.getMainLooper()) {
                    wsMainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            wsStatusListener.onFailure(t, response);
                        }
                    });
                } else {
                    wsStatusListener.onFailure(t, response);
                }
            }
        }
    };

    public WsManager(Builder builder) {
        mContext = builder.mContext;
        wsUrl = builder.wsUrl;
        isNeedReconnect = builder.needReconnect;
        mOkHttpClient = builder.mOkHttpClient;
        this.mLock = new ReentrantLock();
    }

    private void initWebSocket() {
        if (mOkHttpClient == null) {
            mOkHttpClient = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .build();
        }
        if (mRequest == null) {
            mRequest = new Request.Builder()
                    .url(wsUrl)
                    .build();
        }
        mOkHttpClient.dispatcher().cancelAll();
        try {
            mLock.lockInterruptibly();
            try {
                mOkHttpClient.newWebSocket(mRequest, mWebSocketListener);
            } finally {
                mLock.unlock();
            }
        } catch (InterruptedException e) {
        }
    }

    @Override
    public WebSocket getWebSocket() {
        return mWebSocket;
    }


    @Override
    public synchronized boolean isWsConnected() {
        return mCurrentStatus == WsStatus.CONNECTED;
    }

    @Override
    public synchronized int getCurrentStatus() {
        return mCurrentStatus;
    }

    @Override
    public synchronized void setCurrentStatus(int currentStatus) {
        this.mCurrentStatus = currentStatus;
    }

    @Override
    public void startConnect() {
        isManualClose = false;
        buildConnect();
    }

    @Override
    public void stopConnect() {
        isManualClose = true;
        disconnect();
    }

    private void tryReconnect() {
        if (!isNeedReconnect | isManualClose) {
            return;
        }

        if (!isNetworkConnected(mContext)) {
            setCurrentStatus(WsStatus.DISCONNECTED);
            return;
        }

        setCurrentStatus(WsStatus.RECONNECT);

        long delay = RECONNECT_TIME;

        if (NEED_TIME_INCREASE) {
            delay = reconnectCount * RECONNECT_INTERVAL;
        }

        wsMainHandler
                .postDelayed(reconnectRunnable, delay > RECONNECT_MAX_TIME ? RECONNECT_MAX_TIME : delay);
        reconnectCount++;
    }

    private void cancelReconnect() {
        wsMainHandler.removeCallbacks(reconnectRunnable);
        reconnectCount = 0;
    }

    private void connected() {
        cancelReconnect();
    }

    private void disconnect() {
        if (mCurrentStatus == WsStatus.DISCONNECTED) {
            return;
        }
        cancelReconnect();
        if (mOkHttpClient != null) {
            mOkHttpClient.dispatcher().cancelAll();
        }
        if (mWebSocket != null) {
            boolean isClosed = mWebSocket.close(WsStatus.CODE.NORMAL_CLOSE, WsStatus.TIP.NORMAL_CLOSE);
            //非正常关闭连接
            if (!isClosed) {
                if (wsStatusListener != null) {
                    wsStatusListener.onClosed(WsStatus.CODE.ABNORMAL_CLOSE, WsStatus.TIP.ABNORMAL_CLOSE);
                }
            }
        }
        setCurrentStatus(WsStatus.DISCONNECTED);
    }

    private synchronized void buildConnect() {
        if (!isNetworkConnected(mContext)) {
            setCurrentStatus(WsStatus.DISCONNECTED);
            return;
        }
        switch (getCurrentStatus()) {
            case WsStatus.CONNECTED:
            case WsStatus.CONNECTING:
                break;
            default:
                setCurrentStatus(WsStatus.CONNECTING);
                initWebSocket();
        }
    }

    //发送消息
    @Override
    public boolean sendMessage(String msg) {
        return send(msg);
    }

    @Override
    public boolean sendMessage(ByteString byteString) {
        return send(byteString);
    }

    private boolean send(Object msg) {
        boolean isSend = false;
        if (mWebSocket != null && mCurrentStatus == WsStatus.CONNECTED) {
            if (msg instanceof String) {
                isSend = mWebSocket.send((String) msg);
            } else if (msg instanceof ByteString) {
                isSend = mWebSocket.send((ByteString) msg);
            }
            //发送消息失败，尝试重连
            if (!isSend) {
                tryReconnect();
            }
        }
        return isSend;
    }

    /**
     * 检测网络状态,无视网络状态,直接调用重连
     *
     * @param context
     * @return
     */
    private boolean isNetworkConnected(Context context) {
//        if (context != null) {
//            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
//                    .getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
//            if (mNetworkInfo != null) {
//                return mNetworkInfo.isAvailable();
//            }
//        }
        return true;
    }

    public void setWsStatusListener(WsStatusListener wsStatusListener) {
        this.wsStatusListener = wsStatusListener;
    }

    public WsStatusListener getWsStatusListener() {
        return wsStatusListener;
    }

    public static final class Builder {

        private Context mContext;
        private String wsUrl;
        private boolean needReconnect = true;
        private OkHttpClient mOkHttpClient;

        public Builder(Context val) {
            mContext = val;
        }

        public Builder wsUrl(String url) {
            wsUrl = url;
            return this;
        }

        public Builder client(OkHttpClient client) {
            mOkHttpClient = client;
            return this;
        }

        public Builder needReconnect(boolean isRe) {
            needReconnect = isRe;
            return this;
        }

        public WsManager build() {
            return new WsManager(this);
        }
    }
}
