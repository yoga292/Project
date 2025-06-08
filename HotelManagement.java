import java.sql.*;
import java.util.Scanner;

public class HotelManagement {
    static final String URL = "jdbc:oracle:thin:@//192.168.1.3:1521/xepdb1";
    static final String USER = "system";
    static final String PASS = "yoga22";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            Class.forName("oracle.jdbc.driver.OracleDriver");

            while (true) {
                System.out.println("\n=== HOTEL GUEST MANAGEMENT ===");
                System.out.println("1. Add Guest");
                System.out.println("2. View Guests");
                System.out.println("3. Update Guest Contact");
                System.out.println("4. Delete Guest");
                System.out.println("5. Exit");
                System.out.print("Choose option: ");
                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1 -> {
                        System.out.print("Guest name: ");
                        String name = sc.nextLine();
                        System.out.print("Room number: ");
                        int room = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Contact: ");
                        String contact = sc.nextLine();

                        String insertSQL = "INSERT INTO hotel_guests (name, room_no, contact) VALUES (?, ?, ?)";
                        try (PreparedStatement ps = conn.prepareStatement(insertSQL)) {
                            ps.setString(1, name);
                            ps.setInt(2, room);
                            ps.setString(3, contact);
                            int rows = ps.executeUpdate();
                            System.out.println(rows > 0 ? "Guest added successfully." : "Failed to add guest.");
                        }
                    }
                    case 2 -> {
                        String selectSQL = "SELECT * FROM hotel_guests";
                        try (Statement stmt = conn.createStatement();
                             ResultSet rs = stmt.executeQuery(selectSQL)) {
                            System.out.println("Guest List:");
                            while (rs.next()) {
                                System.out.println("ID: " + rs.getInt("guest_id")
                                        + ", Name: " + rs.getString("name")
                                        + ", Room: " + rs.getInt("room_no")
                                        + ", Contact: " + rs.getString("contact"));
                            }
                        }
                    }
                    case 3 -> {
                        System.out.print("Guest ID to update: ");
                        int id = sc.nextInt();
                        sc.nextLine();
                        System.out.print("New contact: ");
                        String contact = sc.nextLine();

                        String updateSQL = "UPDATE hotel_guests SET contact = ? WHERE guest_id = ?";
                        try (PreparedStatement ps = conn.prepareStatement(updateSQL)) {
                            ps.setString(1, contact);
                            ps.setInt(2, id);
                            int rows = ps.executeUpdate();
                            System.out.println(rows > 0 ? "Contact updated." : "Guest not found.");
                        }
                    }
                    case 4 -> {
                        System.out.print("Guest ID to delete: ");
                        int id = sc.nextInt();

                        String deleteSQL = "DELETE FROM hotel_guests WHERE guest_id = ?";
                        try (PreparedStatement ps = conn.prepareStatement(deleteSQL)) {
                            ps.setInt(1, id);
                            int rows = ps.executeUpdate();
                            System.out.println(rows > 0 ? "Guest deleted." : "Guest not found.");
                        }
                    }
                    case 5 -> {
                        System.out.println("Goodbye!");
                        sc.close();
                        return;
                    }
                    default -> System.out.println("Please enter a valid option.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
