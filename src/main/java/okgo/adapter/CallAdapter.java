package okgo.adapter;

public interface CallAdapter<T, R> {

    /** call执行的代理方法 */
    R adapt(Call<T> call, AdapterParam param);
}
