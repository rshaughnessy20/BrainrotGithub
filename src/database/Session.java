package database;

public class Session {
    private static Session instance; // Singleton instance
    private int currentUserId; // Store the logged-in user ID

    // Private constructor to prevent direct instantiation
    private Session() {}

    // Get the singleton instance of the session
    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    // Getter and setter for currentUserId
    public int getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(int userId) {
        this.currentUserId = userId;
    }
}
