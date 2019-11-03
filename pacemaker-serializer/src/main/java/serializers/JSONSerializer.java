package serializers;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

import java.io.*;
import java.util.Map;
import java.util.Stack;

public class JSONSerializer implements Serializer {
    private Stack stack = new Stack();
    private File file;

    public JSONSerializer(File file) {
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

            //set up an array of classes that can be loaded.
//            Class<?>[] classes = new Class[] { User.class, Activity.class, Location.class };

            XStream xstream = new XStream(new JettisonMappedXmlDriver());
            //set up permissions for loading via xstream
// clear out existing permissions and set own ones
//            xstream.addPermission(NoTypePermission.NONE);
//// allow some basics
//            xstream.addPermission(NullPermission.NULL);
//            xstream.addPermission(PrimitiveTypePermission.PRIMITIVES);
//            xstream.allowTypeHierarchy(Collection.class);
//// allow any type from the same package
//            xstream.allowTypesByWildcard(new String[] {
//                    "models.*"
//            });

            is = xstream.createObjectInputStream(new FileReader(file));
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
            XStream xstream = new XStream(new JettisonMappedXmlDriver());
            os = xstream.createObjectOutputStream(new FileWriter(file));
            os.writeObject(stack);

        } finally {
            if (os != null) {
                os.close();
            }
        }
    }
}
