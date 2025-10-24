package cn.reghao.jutil.tool.json;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Gson 序列化/反序列化 Java8 LocalDateTime
 *
 * @author reghao
 * @date 2021-03-04 19:34:31
 */
public class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final DateTimeFormatter formatter1 = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public JsonElement serialize(LocalDateTime localDateTime, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(localDateTime.format(formatter));
    }

    @Override
    public LocalDateTime deserialize(JsonElement element, Type type, JsonDeserializationContext context)
            throws JsonParseException {
        String timestamp = element.getAsJsonPrimitive().getAsString();
        return LocalDateTime.parse(timestamp, formatter);
    }
}
