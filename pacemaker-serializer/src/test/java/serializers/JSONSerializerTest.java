package serializers;

import models.Activity;
import models.FileFormatEnum;
import models.User;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static serializers.Fixtures.users;

class JSONSerializerTest {
    private String filePath = Paths.get("").toAbsolutePath() + "/src/test/java/serializers/test";
    private File file = new File(filePath + ".json");
    private JSONSerializer serializer = new JSONSerializer(file);

    @Test
    void testRead() {
        try {
            serializer.read();
            assert serializer.getStack().size() == 3;
            Map<String, Activity> activityMap = (Map<String, Activity>) serializer.pop();
            assert activityMap.isEmpty();
            Map<String, User> emailMap = (Map<String, User>) serializer.pop();
            assert emailMap.size() == 4;
            Map<String, User> userMap = (Map<String, User>) serializer.pop();
            assert userMap.size() == 4;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void testWrite() {
        Map<String, User> userMap = new HashMap<>();
        Map<String, User> emailMap = new HashMap<>();
        for (User user: users) {
            userMap.put(user.getId(), user);
            emailMap.put(user.getEmail(), user);
        }
        serializer.push(userMap);
        serializer.push(emailMap);
        serializer.push(new HashMap<String, Activity>());
        assert serializer.getStack().size() == 3;
        try {
            serializer.write();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
