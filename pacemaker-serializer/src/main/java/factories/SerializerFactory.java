package factories;

import serializers.JSONSerializer;
import serializers.Serializer;
import serializers.XMLSerializer;
import serializers.YamlSerializer;

import java.io.File;

public class SerializerFactory {
    public static Serializer getSerializer(String filePath, String fileFormat) {
        File datastore = new File(filePath + "." + fileFormat);
        Serializer serializer;
        switch (fileFormat) {
            case "xml":
                serializer = new XMLSerializer(datastore);
                break;
            case "json":
                serializer = new JSONSerializer(datastore);
                break;
            default:
                serializer = new YamlSerializer(datastore);
                break;
        }
        return serializer;
    }
}
