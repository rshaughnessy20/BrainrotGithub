import java.util.HashMap;
import java.util.Map;

public class UserDatabase {
    private static Map<String, String> users = new HashMap<>();

    public static void addUser(String username, String password) {
        users.put(username, password);
    }

    public static boolean validateUser(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }
}