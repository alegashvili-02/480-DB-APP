package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Manager {

    public static boolean register(int ssn, String name, String email) {

        String sql = "INSERT INTO Manager (SSN, Name, Email) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ssn);
            stmt.setString(2, name);
            stmt.setString(3, email);

            stmt.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean login(int ssn) {

        String sql = "SELECT 1 FROM Manager WHERE SSN = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ssn);
            ResultSet rs = stmt.executeQuery();

            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ResultSet topKClients(int k) {
        try {
            Connection conn = DBConnection.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT c.Email, c.Name, COUNT(b.BookingID) AS booking_count " +
                            "FROM Client c " +
                            "JOIN Booking b ON c.Email = b.Email " +
                            "GROUP BY c.Email, c.Name " +
                            "ORDER BY booking_count DESC " +
                            "LIMIT ?"
            );

            ps.setInt(1, k);

            return ps.executeQuery();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static ResultSet getRoomBookingCounts() {
        try {
            Connection conn = DBConnection.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT r.HotelID, r.RoomNum, COUNT(b.BookingID) AS booking_count " +
                            "FROM Room r " +
                            "LEFT JOIN Booking b " +
                            "ON r.HotelID = b.HotelID AND r.RoomNum = b.RoomNum " +
                            "GROUP BY r.HotelID, r.RoomNum " +
                            "ORDER BY r.HotelID, r.RoomNum"
            );

            return ps.executeQuery();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ResultSet getHotelStats() {
        try {
            Connection conn = DBConnection.getConnection();

            String sql =
                    "SELECT h.HotelID, h.Name, " +
                            "COALESCE(b.total_bookings, 0) AS total_bookings, " +
                            "COALESCE(r.avg_rating, 0) AS avg_rating " +
                            "FROM Hotel h " +


                            "LEFT JOIN ( " +
                            "   SELECT HotelID, COUNT(*) AS total_bookings " +
                            "   FROM Booking " +
                            "   GROUP BY HotelID " +
                            ") b ON h.HotelID = b.HotelID " +


                            "LEFT JOIN ( " +
                            "   SELECT HotelID, AVG(Rating) AS avg_rating " +
                            "   FROM Review " +
                            "   GROUP BY HotelID " +
                            ") r ON h.HotelID = r.HotelID " +

                            "ORDER BY h.HotelID";

            PreparedStatement ps = conn.prepareStatement(sql);
            return ps.executeQuery();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ResultSet getClientsByCities(String c1, String c2) {
        try {
            Connection conn = DBConnection.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT DISTINCT c.Email, c.Name " +
                            "FROM Client c " +
                            "WHERE EXISTS (" +
                            "   SELECT 1 FROM ClientAddress ca " +
                            "   WHERE ca.Email = c.Email AND ca.City = ?" +
                            ") " +
                            "AND EXISTS (" +
                            "   SELECT 1 FROM Booking b " +
                            "   JOIN Hotel h ON b.HotelID = h.HotelID " +
                            "   WHERE b.Email = c.Email AND h.City = ?" +
                            ")"
            );

            ps.setString(1, c1);
            ps.setString(2, c2);

            return ps.executeQuery();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ResultSet getProblematicHotels() {
        try {
            Connection conn = DBConnection.getConnection();

            String sql =
                    "SELECT h.HotelID, h.Name " +
                            "FROM Hotel h " +

                            "LEFT JOIN Review r ON h.HotelID = r.HotelID " +

                            //booking + filter Chicago clients
                            "LEFT JOIN Booking b ON h.HotelID = b.HotelID " +
                            "LEFT JOIN ClientAddress ca ON b.Email = ca.Email " +
                            "WHERE h.City = 'Chicago' " +
                            "AND (ca.City IS NULL OR ca.City <> 'Chicago') " +

                            "GROUP BY h.HotelID, h.Name " +
                            "HAVING " +
                            "COALESCE(AVG(r.Rating), 0) < 2 " +
                            "AND COUNT(DISTINCT b.Email) >= 2";

            PreparedStatement ps = conn.prepareStatement(sql);
            return ps.executeQuery();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ResultSet getClientSpending() {
        try {
            Connection conn = DBConnection.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT c.Name, c.Email, " +
                            "SUM((b.EndDate - b.StartDate) * b.PricePerDay) AS total_spent " +
                            "FROM Client c " +
                            "JOIN Booking b ON c.Email = b.Email " +
                            "GROUP BY c.Name, c.Email " +
                            "ORDER BY total_spent DESC"
            );

            return ps.executeQuery();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}