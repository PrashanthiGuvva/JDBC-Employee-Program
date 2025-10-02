import java.sql.*;
import java.util.Scanner;
public class EmployeeDBApp {
    private static final String URL = "jdbc:mysql://localhost:3306/company";
    private static final String USER = "root";
    private static final String PASSWORD = "*****";
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println("Database connected successfully!");
            while (true) {
                System.out.println("\n--- Employee Database Menu ---");
                System.out.println("1. Add Employee");
                System.out.println("2. View Employees");
                System.out.println("3. Update Employee");
                System.out.println("4. Delete Employee");
                System.out.println("5. Exit");
                System.out.print("Enter choice: ");
                int choice = sc.nextInt();
                switch (choice) {
                    case 1 -> addEmployee(conn, sc);
                    case 2 -> viewEmployees(conn);
                    case 3 -> updateEmployee(conn, sc);
                    case 4 -> deleteEmployee(conn, sc);
                    case 5 -> {
                        System.out.println("Exiting");
                        return;
                    }
                    default -> System.out.println("Invalid choice!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addEmployee(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Enter Name: ");
        sc.nextLine();
        String name = sc.nextLine();
        System.out.print("Enter Position: ");
        String position = sc.nextLine();
        System.out.print("Enter Salary: ");
        double salary = sc.nextDouble();
        String sql = "INSERT INTO employee (name, position, salary) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, position);
            pstmt.setDouble(3, salary);
            pstmt.executeUpdate();
            System.out.println("Employee added successfully!");
        }
    }
    private static void viewEmployees(Connection conn) throws SQLException {
        String sql = "SELECT * FROM employee";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\n--- Employee List ---");
            while (rs.next()) {
                System.out.printf("ID: %d | Name: %s | Position: %s | Salary: %.2f%n",
                        rs.getInt("id"), rs.getString("name"),
                        rs.getString("position"), rs.getDouble("salary"));
            }
        }
    }
    private static void updateEmployee(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Enter Employee ID to update: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter New Position: ");
        String position = sc.nextLine();
        System.out.print("Enter New Salary: ");
        double salary = sc.nextDouble();
        String sql = "UPDATE employee SET position = ?, salary = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, position);
            pstmt.setDouble(2, salary);
            pstmt.setInt(3, id);
            int rows = pstmt.executeUpdate();
            if (rows > 0)
                System.out.println("Employee updated successfully!");
            else
                System.out.println("Employee not found.");
        }
    }
    private static void deleteEmployee(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Enter Employee ID to delete: ");
        int id = sc.nextInt();
        String sql = "DELETE FROM employee WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();
            if (rows > 0)
                System.out.println("Employee deleted successfully!");
            else
                System.out.println("Employee not found.");
        }
    }
}
