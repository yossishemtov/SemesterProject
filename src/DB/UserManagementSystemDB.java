package DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import common.worker.GeneralParkWorker;

public class UserManagementSystemDB {

    private Connection connectionToDatabase;

    public UserManagementSystemDB(Connection connectionToDatabase) {
        this.connectionToDatabase = connectionToDatabase;
    }

    /**
     * Retrieves all employees from the database and returns them as an ArrayList of GeneralParkWorker.
     * 
     * @return An ArrayList of GeneralParkWorker objects representing all employees.
     */
    public ArrayList<GeneralParkWorker> getAllEmployees() {
        String query = "SELECT * FROM `GoNature_employees`";
        ArrayList<GeneralParkWorker> employees = new ArrayList<>();
        
        try (PreparedStatement statement = connectionToDatabase.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                GeneralParkWorker worker = new GeneralParkWorker(
                        resultSet.getInt("workerId"),
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getString("email"),
                        resultSet.getString("role"),
                        resultSet.getString("userName"),
                        resultSet.getString("password"),
                        resultSet.getInt("worksAtPark")
                );
                employees.add(worker);
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }
        
        return employees;
    }
}
