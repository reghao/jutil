package cn.reghao.jutil.jdk.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JSON 数组反序列化
 *
 * @author reghao
 * @date 2020-11-11 16:57:04
 */
public class JsonArrayDeserializer<T> {
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    /**
     * JSON 数组转换为对象集合
     *
     * @param
     * @return
     * @date 2020-11-11 下午5:14
     */
    public List<T> fromJsonArray(String json, Class<T> clazz) {
        JsonParser parser = new JsonParser();
        List<T> list = new ArrayList<>();
        parser.parse(json).getAsJsonArray().forEach(ele -> {
            list.add(gson.fromJson(ele, clazz));
        });

        return list;
    }
}
