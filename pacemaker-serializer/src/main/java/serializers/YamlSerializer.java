package serializers;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;

import java.io.*;
import java.util.Map;
import java.util.Stack;

public class YamlSerializer implements Serializer {

    private Stack stack = new Stack();
    private File file;

    public YamlSerializer(File file) {
        this.file = file;
    }

    public Stack getStack() {
        return stack;
    }

    public void push(Map map) {
        stack.push(map);
    }

    public Object pop() {
        return stack.pop();
    }

    public void read() throws Exception {

        YamlReader reader = new YamlReader(new FileReader(file));
        stack = reader.read(Stack.class);
    }

    public void write() throws Exception {
        YamlWriter writer = new YamlWriter(new FileWriter(file));
        writer.write(stack);
        writer.close();
    }

}
