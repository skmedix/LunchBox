package org.apache.commons.lang;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

public class SerializationUtils {

    public static Object clone(Serializable object) {
        return deserialize(serialize(object));
    }

    public static void serialize(Serializable obj, OutputStream outputStream) {
        if (outputStream == null) {
            throw new IllegalArgumentException("The OutputStream must not be null");
        } else {
            ObjectOutputStream out = null;

            try {
                out = new ObjectOutputStream(outputStream);
                out.writeObject(obj);
            } catch (IOException ioexception) {
                throw new SerializationException(ioexception);
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException ioexception1) {
                    ;
                }

            }

        }
    }

    public static byte[] serialize(Serializable obj) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(512);

        serialize(obj, baos);
        return baos.toByteArray();
    }

    public static Object deserialize(InputStream inputStream) {
        if (inputStream == null) {
            throw new IllegalArgumentException("The InputStream must not be null");
        } else {
            ObjectInputStream in = null;

            Object ex;

            try {
                in = new ObjectInputStream(inputStream);
                ex = in.readObject();
            } catch (ClassNotFoundException classnotfoundexception) {
                throw new SerializationException(classnotfoundexception);
            } catch (IOException ioexception) {
                throw new SerializationException(ioexception);
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException ioexception1) {
                    ;
                }

            }

            return ex;
        }
    }

    public static Object deserialize(byte[] objectData) {
        if (objectData == null) {
            throw new IllegalArgumentException("The byte[] must not be null");
        } else {
            ByteArrayInputStream bais = new ByteArrayInputStream(objectData);

            return deserialize((InputStream) bais);
        }
    }
}
