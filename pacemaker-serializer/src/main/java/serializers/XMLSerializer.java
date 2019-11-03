package serializers;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class XMLSerializer implements Serializer {
    private Stack stack = new Stack();
    private File file;

    public XMLSerializer(File file) {
        this.file = file;
    }

    public void push(Map o) {
        stack.push(o);
    }

    public Object pop() {
        return stack.pop();
    }

    public Stack getStack() {
        return stack;
    }

    public void read() throws Exception {
        ObjectInputStream is = null;

        try {
            XStream xstream = new XStream(new DomDriver());
            xstream.allowTypesByRegExp(new String[]{".*"});
            is = xstream.createObjectInputStream(new FileReader(file));
//            Object obj = is.readObject();
//            while (obj != null)
//            {
//                stack.push(obj);
//                obj = is.readObject();
//            }
            stack = (Stack) is.readObject();
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public void write() throws Exception {
        ObjectOutputStream os = null;

        try {
            XStream xstream = new XStream(new DomDriver());
            os = xstream.createObjectOutputStream(new FileWriter(file));
//            while (!stack.empty())
//            {
//                os.writeObject(stack.pop());
//            }
            os.writeObject(stack);
        } finally {
            if (os != null) {
                os.close();
            }
        }
    }
}
