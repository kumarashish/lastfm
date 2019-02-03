package networkcall;

import android.content.Context;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import interfaces.WebApiResponseCallback;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by Ashish.Kumar on 23-01-2018.
 */

public class WebApiCall {
    OkHttpClient client;
    private  OkHttpClient.Builder client1;
    public static final MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    Context context;


    public WebApiCall(Context context) {
        client = new OkHttpClient();
        this.context = context;
    }

    public void getData(String url, final WebApiResponseCallback callback) {
        client.newBuilder().connectTimeout(60000, TimeUnit.MILLISECONDS).readTimeout(60000, TimeUnit.MILLISECONDS).build();
        final Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(e.fillInStackTrace().toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    if (response != null) {
                        callback.onSucess(response.body().string());
                    } else {
                        if (response.message() != null) {
                            callback.onError(response.message());
                        } else {
                            callback.onError("No data found!");
                        }

                    }
                } else {
                    callback.onError(response.body().string());
                }
            }
        });
    }

}

