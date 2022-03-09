import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.sql.*;
class Lesson7Database {

    public static void loadDatabase(Connection connection, String inputFile) throws SQLException, IOException {
       Scanner in = new Scanner(Paths.get(inputFile));
        System.out.println("Populating the database: Done.");

        try( Statement statement = connection.createStatement()){
           while (true) {
               if (!in.hasNextLine()) return;
               String line = in.nextLine().trim();
               if (line.endsWith(";")) { //remove trailing
                   line = line.substring(0, line.length() - 1);
               }
               statement.execute(line);
           }
        }
    }

    public static void displayData(Connection con) throws SQLException {
        String query = "SELECT Lesson_Num, Title FROM Lessons";
        System.out.println("Query of Lessons table results:");

        try (Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            String[] headers = displayHeader(rs);
            while (rs.next()) {

                for (int i = 1; i <= headers.length; i++) {
                    if (i > 1) System.out.print(", ");
                    System.out.print(rs.getString(i));
                }
                System.out.println();
            }
        }
    }

    public static void main(String[] args) throws SQLException, IOException {
        String inputFile = args[0];
        System.out.println("Connecting to the database: Done.");
        Connection connection = DriverManager.getConnection("jdbc:sqlite:./chinook.db");
        loadDatabase(connection, inputFile );
        displayData(connection);
    }

    private static String[] displayHeader(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        String[] headers = new String[columnCount];
        for (int i = 1; i <= columnCount; i++) {
            if (i > 1) System.out.print(", ");
            System.out.print(metaData.getColumnLabel(i));
            headers[i-1] = metaData.getColumnLabel(i);
        }
        System.out.println();
        return headers;
    }
}
