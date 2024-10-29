package database;
//OKAY NEW DAY I SHOULD ADD HOW IT INTERACTS TO THE OTHER PARTS OF THE CODE AS WELL
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//Import statements: Import classes from the `java.sql` package to enable MySQL database connectivity and SQL operations.
//- `Connection`: Manages the connection to the database.
//- `DriverManager`: Establishes a connection to the database using specified credentials.
//- `PreparedStatement`: Prepares and executes SQL statements securely.
//- `ResultSet`: Stores results retrieved from executing SQL queries.
//- `SQLException`: Catches SQL-related exceptions.
public class UserDatabase {
    private static final String URL = "jdbc:mysql://localhost:3306/Brainrot";
    private static final String USER = "root";
    private static final String PASSWORD = "mha@auU,taA+0J!";
    // Class declaration: Defines 'UserDatabase', a utility class responsible for database operations related to user management.
    // Benefit: Encapsulates all database logic in one place, improving maintainability and reusability across the application.


    // Establish a connection to the MySQL database
    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    // Private method (connect): Establishes and returns a `Connection` object to the database using `DriverManager.getConnection()`.
    // - Benefit: Centralizes database connection logic, allowing reuse of the connection method in other database methods.
    // - Interaction: Used by both `addUser()` and `validateUser()` to set up database connections.


    // Add a new user to the database
    public static void addUser(String username, String password) {
        String query = "INSERT INTO user (name, password) VALUES (?, ?)";
        // Public method (addUser): Takes `username` and `password` as parameters and inserts them into the `user` table.
        // - SQL Query: Uses a parameterized `INSERT INTO` SQL query to prevent SQL injection attacks and improve security.
        // - Benefit: Encapsulates the user insertion logic, allowing easy registration of new users.

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
        	// `try-with-resources` block: Ensures resources (connection and statement) are automatically closed after use.
            // `Connection` object: Created by calling `connect()`, enabling interaction with the database.
            // `PreparedStatement` object: Compiles the SQL query with parameters, enhancing security and performance.
            
            statement.setString(1, username);
            statement.setString(2, password);
            // `setString` method: Binds values to the placeholders in the SQL statement (`?`), improving query security.
            // Benefit: Prevents SQL injection by treating user inputs as values rather than executable code.

            statement.executeUpdate();
            // Executes the `INSERT` SQL query, updating the database by adding the new user record.

            System.out.println("User registered successfully in the database!");
            // Feedback message: Confirms the successful addition of the user to the database, aiding in debugging and user feedback.

        } catch (SQLException e) {
            System.err.println("Error adding user to database: " + e.getMessage());
        }
        // Exception handling: Catches SQL exceptions, providing an error message for any issues encountered during insertion.
        // Benefit: Increases the robustness of the code by catching and addressing potential database-related issues.
    }

    // Validate if a user exists with the given username and password
    public static boolean validateUser(String username, String password) {
        String query = "SELECT * FROM user WHERE name = ? AND password = ?";
        // Public method (validateUser): Takes `username` and `password` as parameters, checks if they exist in the `user` table.
        // - SQL Query: Uses a parameterized `SELECT` query, enhancing security by preventing SQL injection.
        // - Benefit: Centralizes validation logic for user credentials, supporting modularity and reusability.

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
        	// `try-with-resources` block: Automatically closes resources after use.
            // `Connection` object: Created by calling `connect()`, allowing communication with the database.
            // `PreparedStatement` object: Prepares the SQL query, enabling parameterized input for secure validation.
        	
            statement.setString(1, username);
            statement.setString(2, password);
            // Binds `username` and `password` values to the placeholders in the query, improving security and maintainability.

            ResultSet resultSet = statement.executeQuery();
            // Executes the `SELECT` query and stores the result in a `ResultSet` object.
            // - `ResultSet`: Allows retrieval of query results row by row, if they exist.
            
            return resultSet.next(); // Returns true if a match is found, else false. But the more detailed version for my future self is right below.
            // Checks if the query returned a result by calling `next()`, which returns `true` if a match is found.
            // Benefit: Provides a simple boolean response for credential validation, facilitating integration with other code (e.g., LoginPage).

        } catch (SQLException e) {
            System.err.println("Error validating user: " + e.getMessage());
            return false;
        }
        // Exception handling: Catches SQL exceptions and prints an error message in case of database validation issues.
        // Benefit: Improves reliability by handling errors gracefully, reducing the risk of application crashes.
    }
    public static void testConnection() { // This method should test our connection, duh. More specifically.
    	// Public method (testConnection): Tests the database connection by attempting to connect and handling potential exceptions.
        // Benefit: Provides an initial test to verify the database is accessible before running other operations.

        try (Connection connection = connect()) {
            System.out.println("Database connection successful!");
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            e.printStackTrace(); // This prints the full error stack for more details
            // Exception handling: Catches SQL exceptions during connection attempts and outputs detailed error information.
            // - `e.printStackTrace()`: Prints the stack trace to the console, aiding in debugging by showing where the error occurred.
        }
    }
}