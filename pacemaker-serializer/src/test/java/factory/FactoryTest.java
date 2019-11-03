package factory;

import factories.SerializerFactory;
import org.junit.jupiter.api.Test;
import serializers.JSONSerializer;
import serializers.Serializer;
import serializers.XMLSerializer;
import serializers.YamlSerializer;

class FactoryTest {
    private SerializerFactory factory = new SerializerFactory();
    @Test
    void testGetSerializer() {
        String fileFormat = "xml";
        Serializer serializer = SerializerFactory.getSerializer("test", fileFormat);
        assert serializer instanceof XMLSerializer;
        fileFormat = "json";
        serializer = SerializerFactory.getSerializer("test", fileFormat);
        assert serializer instanceof JSONSerializer;
        fileFormat = "yaml";
        serializer = SerializerFactory.getSerializer("test", fileFormat);
        assert serializer instanceof YamlSerializer;
        fileFormat = "abc";
        serializer = SerializerFactory.getSerializer("test", fileFormat);
        assert serializer instanceof YamlSerializer;
    }
}
