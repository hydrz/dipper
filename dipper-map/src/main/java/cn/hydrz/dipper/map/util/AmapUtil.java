package cn.hydrz.dipper.map.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author hydrz
 */
public class AmapUtil {
    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();

    /**
     * 获取高德web服务key
     *
     * @return String
     */
    public static String getWebKey() {
        return "ab0a91dc4f4161172b7e841dd37eddff";
    }


    /**
     * 发起请求
     *
     * @param url 请求地址
     * @return API返回结果
     */
    public static Map<String, Object> get(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = client.newCall(request).execute();

            Type type = new TypeToken<Map<String, Object>>() {
            }.getType();

            return gson.fromJson(response.body().string(), type);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
