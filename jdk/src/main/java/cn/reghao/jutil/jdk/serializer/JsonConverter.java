package cn.reghao.jutil.jdk.serializer;

import cn.reghao.jutil.jdk.text.TextFile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JSON 序列化/反序列化
 *
 * @author reghao
 * @date 2020-11-11 16:57:04
 */
public class JsonConverter {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();
    private static final JsonParser jsonParser = new JsonParser();

    /**
     * 对象转换为 JSON
     *
     * @param
     * @return
     * @date 2020-11-11 下午5:10
     */
    public static String objectToJson(Object object) {
        return gson.toJson(object);
    }

    /**
     * JSON 转换为对象
     * TODO <T> 的含义
     *
     * @param
     * @return
     * @date 2020-11-11 下午5:11
     */
    public static <T> T jsonToObject(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    /**
     * 对泛型数据的反序列化
     *
     * @param
     * @return
     * @date 2021-05-26 下午3:38
     */
    public static <T> T jsonToObject(String json, Type type) {
        return gson.fromJson(json, type);
    }

    public static <T> T jsonFileToObject(File jsonFile, Type type) {
        try {
            JsonReader reader = gson.newJsonReader(new InputStreamReader(new FileInputStream(jsonFile)));
            return gson.fromJson(reader, type);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T jsonFileToObject(InputStreamReader streamReader, Type type) {
        JsonReader reader = gson.newJsonReader(streamReader);
        return gson.fromJson(reader, type);
    }

    public static <T> List<T> jsonToObjects(String json, Class<T> clazz) {
        JsonParser parser = new JsonParser();
        List<T> list = new ArrayList<>();
        parser.parse(json).getAsJsonArray().forEach(ele -> {
            list.add(gson.fromJson(ele, clazz));
        });

        return list;
    }

    /**
     * JSON 文件转换为对象
     *
     * @param
     * @return
     * @date 2020-11-11 下午5:11
     */
    public static JsonElement jsonToJsonElement(File jsonFile) {
        String content = new TextFile().readFile(jsonFile.getAbsolutePath()).replace(System.lineSeparator(), "");
        return jsonParser.parse(content);
    }

    public static JsonElement jsonToJsonElement(String json) {
        return jsonParser.parse(json);
    }

    /**
     * 对象集合转换为 JSON
     *
     * @param
     * @return
     * @date 2020-11-11 下午5:10
     */
    public static String listToJson(List<Object> objects) {
        return gson.toJson(objects);
    }
}
