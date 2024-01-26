package cn.reghao.jutil.jdk.serializer;

import java.io.*;

/**
 * JDK 对象序列化器
 *
 * @author reghao
 * @date 2022-05-21 13:49:18
 */
public class JdkSerializer {
    public static byte[] serialize(Object object) {
        if (object == null) {
            return null;
        } else {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            try {
                (new ObjectOutputStream(stream)).writeObject(object);
            } catch (IOException var3) {
                throw new IllegalArgumentException("Could not serialize object of type: " + object.getClass(), var3);
            }

            return stream.toByteArray();
        }
    }

    public static Object deserialize(byte[] bytes) {
        if (bytes == null) {
            return null;
        } else {
            try {
                return deserialize(new ObjectInputStream(new ByteArrayInputStream(bytes)));
            } catch (IOException var2) {
                throw new IllegalArgumentException("Could not deserialize object", var2);
            }
        }
    }

    public static Object deserialize(ObjectInputStream stream) {
        if (stream == null) {
            return null;
        } else {
            try {
                return stream.readObject();
            } catch (IOException var2) {
                throw new IllegalArgumentException("Could not deserialize object", var2);
            } catch (ClassNotFoundException var3) {
                throw new IllegalStateException("Could not deserialize object type", var3);
            }
        }
    }
}
