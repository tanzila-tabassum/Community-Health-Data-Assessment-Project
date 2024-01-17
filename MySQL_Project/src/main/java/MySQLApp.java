import java.sql.*;
import java.util.*;
import java.util.Scanner;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InputMismatchException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.io.StringReader;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;




public class MySQLApp {

    private static final String DB_URL = "jdbc:mysql://68.237.123.87:3306/classData?useSSL=true&requireSSL=true"
            + "&trustCertificateKeyStoreUrl=file:./keystore.jks"
            + "&trustCertificateKeyStorePassword=dbproject711";

    private static final String USERNAME = "'us1'@'%'";
    private static final String PASSWORD = "csci711";
    //private static final String SSL_KEYSTORE_PATH = "file:///Users/tanzilatabassum/Desktop/keystore.jks";
    //private static final String SSL_KEYSTORE_PASSWORD = "dbproject711";


    private static final String CREATE_QUESTION_TABLE =
            "CREATE TABLE IF NOT EXISTS questions (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "question TEXT" +
                    ")";

    private static final String CREATE_QUERY_TABLE =
            "CREATE TABLE IF NOT EXISTS queries (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "question_id INT," +
                    "query TEXT," +
                    "FOREIGN KEY (question_id) REFERENCES questions(id)" +
                    ")";

    private static final String INSERT_QUESTION =
            "INSERT INTO questions (question) VALUES (?)";

    private static final String INSERT_QUERY =
            "INSERT INTO queries (question_id, query) VALUES (?, ?)";


    private static final String GET_QUERY_BY_ID =
            "SELECT * FROM queries WHERE id = ?";

    public static void main(String[] args) {
        Connection connection = null;

        //String url = DB_URL + "&clientCertificateKeyStoreUrl=file:/Users/tanzilatabassum/Desktop/Database/Final%20Project/MySQL_Project/out/artifacts/MySQL_Project_jar/keystore.jks" +
                //"&clientCertificateKeyStorePassword=dbproject711";
        String url = DB_URL.replace("./keystore.jks", "./out/artifacts/MySQL_Project_jar/keystore.jks");
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Set the SSL keystore properties in the URL
            /*String url = DB_URL + "&clientCertificateKeyStoreUrl=" + SSL_KEYSTORE_PATH +
                    "&clientCertificateKeyStorePassword=" + SSL_KEYSTORE_PASSWORD;*/

            // Establish the connection with SSL keystore
            connection = DriverManager.getConnection(url, USERNAME, PASSWORD);

            initializeDatabase(connection);
            runApplication(connection);
        } catch (ClassNotFoundException e) {
            System.err.println("Error loading the MySQL JDBC driver: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.err.println("Error closing the database connection: " + e.getMessage());
                }
            }
        }
    }


    private static void runApplication(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            displayMenu();
            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character

                switch (choice) {
                    case 1:
                        submitQuestion(connection, scanner);
                        break;
                    case 2:
                        submitQuery(connection, scanner);
                        break;
                    case 3:
                        displayRunnableQueries(connection);
                        break;
                    case 4:
                        runQuery(connection, scanner);
                        break;
                    case 5:
                        System.out.println("Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            } else {
                String invalidChoice = scanner.nextLine();
                System.out.println("'" + invalidChoice + "' is not a valid choice. Please enter a valid integer.");
            }
        }
    }


    private static void initializeDatabase(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(CREATE_QUESTION_TABLE);
            statement.executeUpdate(CREATE_QUERY_TABLE);
        }
    }

    private static void displayMenu() {
        System.out.println("\nWelcome to the Spring 2023 Database class final project!\n");
        System.out.println("1. Specify a problem");
        System.out.println("2. Contribute a SQL");
        System.out.println("3. Display a list of runnable query");
        System.out.println("4. Run a query");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void submitQuestion(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("\nEnter your SQL question: ");
        String question = scanner.nextLine();

        try (PreparedStatement statement = connection.prepareStatement(INSERT_QUESTION, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, question);
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int questionId = generatedKeys.getInt(1);
                System.out.println("Successfully submitted the question.");

                System.out.print("Do you want to submit a query related to this question? (y/n): ");
                String choice = scanner.nextLine();

                if (choice.equalsIgnoreCase("y")) {
                    submitQuery(connection, scanner, questionId);
                    System.out.println("Related query submitted.");
                } else if (choice.equalsIgnoreCase("n")) {
                    System.out.println("No related query submitted.");
                } else {
                    System.out.println("Invalid choice. Please enter 'y' or 'n'.");
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid choice.");
        }
    }

    private static void submitQuery(Connection connection, Scanner scanner, int questionId) throws SQLException {
        // Show a sample query to the user
        System.out.println("\nSample Query: SELECT count(A.Q17) as CountOfHealthyAnd100k " +
                "from (SELECT Q17, Q16, Q47 " +
                "from chad_encoded_data " +
                "where Q16 >=4 " +
                "AND Q17>=4 AND Q47 BETWEEN 0 AND 3) A;");
        System.out.println("Please provide your query based on the above example.");


        // Get the query from the user
        System.out.print("\nEnter the SQL query: ");
        String query = scanner.nextLine();

        // Replace parameter placeholders in the query with parameter values
        /*query = replaceParameterPlaceholders(query, parameterValues);*/

        // Submit the query
        submitQueryToDatabase(connection, questionId, query);

        System.out.println("Successfully submitted the query.");
    }

    private static void submitQueryToDatabase(Connection connection, int questionId, String query) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, questionId);
            statement.setCharacterStream(2, new StringReader(query));
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int queryId = generatedKeys.getInt(1);
                System.out.println("Query ID: " + queryId);
            }
        }
    }


    private static void submitQuery(Connection connection, Scanner scanner) throws SQLException {
        // Display the list of questions without corresponding queries
        boolean hasQuestionsWithoutQueries = displayQuestionsWithoutQueries(connection);

        if (!hasQuestionsWithoutQueries) {
            System.out.println("All the questions have corresponding queries.");
            return;
        }

        // Get the question ID from the user
        System.out.print("\nEnter the ID of the question you want to submit a query for: ");
        int questionId = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        // Validate the question ID
        if (!isValidQuestionId(connection, questionId)) {
            System.out.println("Invalid question ID. Please try again.");
            return;
        }

        // Show a sample query to the user
        System.out.println("\nSample Query: SELECT count(A.Q17) as CountOfHealthyAnd100k " +
                "from (SELECT Q17, Q16, Q47 " +
                "from chad_encoded_data " +
                "where Q16 >=4 " +
                "AND Q17>=4 AND Q47 BETWEEN 0 AND 3) A;");
        System.out.println("Please provide your query based on the above example.");



        // Get the query from the user
        System.out.print("\nEnter the SQL query: ");
        String query = scanner.nextLine();
        // Check if the query matches the sample query structure
       /*if (!isQuerySimilarToSample(query)) {
           throw new IllegalArgumentException("Error: The provided query does not match the expected structure.");
       }*/


        // Submit the query
        submitQuery(connection, questionId, query);

        System.out.println("Successfully submitted the query.");
    }




    /*private static boolean isQuerySimilarToSample(String query) {
        String sampleQuery = "SELECT count(A.Q17) as CountOfHealthyAnd100k " +
                "from (SELECT Q17, Q16, Q47 " +
                "from chad_encoded_data " +
                "where Q16 >=4 " +
                "AND Q17>=4 AND Q47 BETWEEN 0 AND 3) A;";
        return query.trim().equalsIgnoreCase(sampleQuery.trim());*/
    //}

    private static boolean displayQuestionsWithoutQueries(Connection connection) throws SQLException {
        String query = "SELECT q.id, q.question " +
                "FROM Questions q " +
                "LEFT JOIN Queries qu ON q.id = qu.question_id " +
                "WHERE qu.question_id IS NULL";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            System.out.println("Questions without Queries:");
            System.out.println("------------------------------------");
            System.out.println("| Question ID | Question            |");
            System.out.println("------------------------------------");

            boolean hasQuestions = false;

            while (resultSet.next()) {
                hasQuestions = true;
                int questionId = resultSet.getInt("id");
                String question = resultSet.getString("question");

                System.out.printf("| %-11d | %-19s |%n", questionId, question);
            }

            System.out.println("------------------------------------");

            return hasQuestions;
        }
    }

    private static boolean isValidQuestionId(Connection connection, int questionId) throws SQLException {
        String query = "SELECT id FROM Questions WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, questionId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }





    private static void submitQuery(Connection connection, int questionId, String query) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_QUERY)) {
            statement.setInt(1, questionId);
            statement.setString(2, query);
            statement.executeUpdate();
        }
    }


    private static void displayRunnableQueries(Connection connection) throws SQLException {
        String getRunnableQueriesQuery = "SELECT q.id AS question_id, q.question, qs.id AS query_id, qs.query " +
                "FROM questions q LEFT JOIN queries qs " +
                "ON q.id = qs.question_id";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(getRunnableQueriesQuery)) {

            System.out.println("Runnable Queries:");
            System.out.println("-----------------------------------------------------------");
            System.out.println("| Question ID | Question           | Query ID | Query            |");
            System.out.println("-----------------------------------------------------------");

            while (resultSet.next()) {
                int questionId = resultSet.getInt("question_id");
                String question = resultSet.getString("question");
                int queryId = resultSet.getInt("query_id");
                String query = resultSet.getString("query");

                System.out.printf("| %-11d | %-18s | %-8d | %-18s |%n", questionId, question, queryId, query);
            }

            System.out.println("-----------------------------------------------------------");
        }
    }


    private static void displayRunnableQueriesWithSelection(Connection connection) throws SQLException {
        String getRunnableQueriesQuery = "SELECT q.id AS query_id, q.query, qs.id AS question_id, qs.question " +
                "FROM Queries q INNER JOIN Questions qs " +
                "ON q.question_id = qs.id";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(getRunnableQueriesQuery)) {

            System.out.println("Runnable Queries:");
            System.out.println("-----------------------------------------------------------");
            System.out.println("| Query ID | Query              | Question ID | Question       |");
            System.out.println("-----------------------------------------------------------");

            while (resultSet.next()) {
                int queryId = resultSet.getInt("query_id");
                String query = resultSet.getString("query");
                int questionId = resultSet.getInt("question_id");
                String question = resultSet.getString("question");

                System.out.printf("| %-8d | %-18s | %-11d | %-14s |%n", queryId, query, questionId, question);
            }

            System.out.println("-----------------------------------------------------------");
        }
    }




    private static void runQuery(Connection connection, Scanner scanner) throws SQLException {
        displayRunnableQueriesWithSelection(connection);

        System.out.print("\nEnter the ID of the query you want to run (0 to exit): ");
        int queryId = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        if (queryId == 0) {
            return;
        }

        try (PreparedStatement statement = connection.prepareStatement(GET_QUERY_BY_ID)) {
            statement.setInt(1, queryId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String query = resultSet.getString("query"); // Assuming column name is "query"

                // Get parameter values from the user
                System.out.println("\nEnter the parameter values -");
                Map<String, String> parameterValues = new HashMap<>();
                String paramName;
                while (true) {
                    System.out.print("Enter the parameter name (or 'done' to finish): ");
                    paramName = scanner.nextLine();
                    if (paramName.equalsIgnoreCase("done")) {
                        break;
                    }
                    System.out.print("Enter the value for Column Name '" + paramName + "': ");
                    String paramValue = scanner.nextLine();
                    parameterValues.put(paramName, paramValue);
                }


                try (PreparedStatement queryStatement = connection.prepareStatement(query)) {
                    // Execute the modified query
                    try {
                        ResultSet queryResult = queryStatement.executeQuery();
                        ResultSetMetaData metaData = queryResult.getMetaData();
                        int columnCount = metaData.getColumnCount();

                        System.out.println("\nQuery Result:");
                        // Print column names
                        for (int i = 1; i <= columnCount; i++) {
                            System.out.print(metaData.getColumnName(i) + "\t");
                        }
                        System.out.println();

                        // Print query result
                        while (queryResult.next()) {
                            for (int i = 1; i <= columnCount; i++) {
                                System.out.print(queryResult.getString(i) + "\t");
                            }
                            System.out.println();
                        }
                    } catch (SQLException e) {
                        System.out.println("Error executing the query: " + e.getMessage());
                    }
                }
            } else {
                System.out.println("Query not found for the specified ID.");
            }
        }
    }


}






