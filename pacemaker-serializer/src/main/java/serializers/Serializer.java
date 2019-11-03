package serializers;

import java.util.Map;

public interface Serializer {
    void push(Map o);

    Object pop();

    void write() throws Exception;

    void read() throws Exception;
}
