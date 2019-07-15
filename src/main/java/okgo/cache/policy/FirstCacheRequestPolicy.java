package okgo.cache.policy;

import okgo.cache.CacheEntity;
import okgo.callback.Callback;
import okgo.model.Response;
import okgo.request.base.Request;

public class FirstCacheRequestPolicy<T> extends BaseCachePolicy<T> {
    public FirstCacheRequestPolicy(Request<T, ? extends Request> request) {
        super(request);
    }

    @Override
    public void onSuccess(final Response<T> success) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCallback.onSuccess(success);
                mCallback.onFinish();
            }
        });
    }

    @Override
    public void onError(final Response<T> error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCallback.onError(error);
                mCallback.onFinish();
            }
        });
    }

    @Override
    public Response<T> requestSync(CacheEntity<T> cacheEntity) {
        try {
            prepareRawCall();
        } catch (Throwable throwable) {
            return Response.error(false, rawCall, null, throwable);
        }
        //同步请求，不能返回两次，只返回正确的数据
        Response<T> response;
        if (cacheEntity != null) {
            response = Response.success(true, cacheEntity.getData(), rawCall, null);
        }
        response = requestNetworkSync();
        if (!response.isSuccessful() && cacheEntity != null) {
            response = Response.success(true, cacheEntity.getData(), rawCall, response.getRawResponse());
        }
        return response;
    }

    @Override
    public void requestAsync(final CacheEntity<T> cacheEntity, Callback<T> callback) {
        mCallback = callback;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCallback.onStart(request);

                try {
                    prepareRawCall();
                } catch (Throwable throwable) {
                    Response<T> error = Response.error(false, rawCall, null, throwable);
                    mCallback.onError(error);
                    return;
                }
                if (cacheEntity != null) {
                    Response<T> success = Response.success(true, cacheEntity.getData(), rawCall, null);
                    mCallback.onCacheSuccess(success);
                }
                requestNetworkAsync();
            }
        });
    }
}
