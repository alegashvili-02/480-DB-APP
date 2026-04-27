package org.example;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Client {

    // ================= REGISTER =================
    public static boolean registerFull(
            String email,
            String name,
            String city,
            String street,
            int number,
            String cardNumber,
            String billCity,
            String billStreet,
            int billNumber) {

        try (Connection conn = DBConnection.getConnection()) {

            conn.setAutoCommit(false);

            // Client
            PreparedStatement ps1 = conn.prepareStatement(
                    "INSERT INTO Client (Email, Name) VALUES (?, ?)");
            ps1.setString(1, email);
            ps1.setString(2, name);
            ps1.executeUpdate();

            // Address (home)
            PreparedStatement ps2 = conn.prepareStatement(
                    "INSERT INTO Address (City, StreetName, Number) VALUES (?, ?, ?) " +
                            "ON CONFLICT DO NOTHING");
            ps2.setString(1, city);
            ps2.setString(2, street);
            ps2.setInt(3, number);
            ps2.executeUpdate();

            // ClientAddress
            PreparedStatement ps3 = conn.prepareStatement(
                    "INSERT INTO ClientAddress (Email, City, StreetName, Number) VALUES (?, ?, ?, ?)");
            ps3.setString(1, email);
            ps3.setString(2, city);
            ps3.setString(3, street);
            ps3.setInt(4, number);
            ps3.executeUpdate();

            // Billing address
            PreparedStatement ps4 = conn.prepareStatement(
                    "INSERT INTO Address (City, StreetName, Number) VALUES (?, ?, ?) " +
                            "ON CONFLICT DO NOTHING");
            ps4.setString(1, billCity);
            ps4.setString(2, billStreet);
            ps4.setInt(3, billNumber);
            ps4.executeUpdate();

            // Credit card
            PreparedStatement ps5 = conn.prepareStatement(
                    "INSERT INTO CreditCard (CardNumber, Email, City, StreetName, Number) VALUES (?, ?, ?, ?, ?)");
            ps5.setString(1, cardNumber);
            ps5.setString(2, email);
            ps5.setString(3, billCity);
            ps5.setString(4, billStreet);
            ps5.setInt(5, billNumber);
            ps5.executeUpdate();

            conn.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ================= LOGIN =================
    public static boolean login(String email) {

        String sql = "SELECT 1 FROM Client WHERE Email = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ================= UPDATE (FIXED: NO DUPLICATE) =================
    public static boolean updateAll(
            String email,
            String name,
            String city,
            String street,
            int number,
            String cardNumber) {

        try (Connection conn = DBConnection.getConnection()) {

            conn.setAutoCommit(false);

            // update name
            if (name != null && !name.isEmpty()) {
                PreparedStatement ps = conn.prepareStatement(
                        "UPDATE Client SET Name = ? WHERE Email = ?");
                ps.setString(1, name);
                ps.setString(2, email);
                ps.executeUpdate();
            }

            // address insert
            PreparedStatement addr = conn.prepareStatement(
                    "INSERT INTO Address (City, StreetName, Number) VALUES (?, ?, ?) " +
                            "ON CONFLICT DO NOTHING");
            addr.setString(1, city);
            addr.setString(2, street);
            addr.setInt(3, number);
            addr.executeUpdate();

            // replace client-address relation
            PreparedStatement del1 = conn.prepareStatement(
                    "DELETE FROM ClientAddress WHERE Email = ?");
            del1.setString(1, email);
            del1.executeUpdate();

            PreparedStatement ins1 = conn.prepareStatement(
                    "INSERT INTO ClientAddress (Email, City, StreetName, Number) VALUES (?, ?, ?, ?)");
            ins1.setString(1, email);
            ins1.setString(2, city);
            ins1.setString(3, street);
            ins1.setInt(4, number);
            ins1.executeUpdate();


            PreparedStatement del2 = conn.prepareStatement(
                    "DELETE FROM CreditCard WHERE Email = ?");
            del2.setString(1, email);
            del2.executeUpdate();

            PreparedStatement ins2 = conn.prepareStatement(
                    "INSERT INTO CreditCard (CardNumber, Email, City, StreetName, Number) VALUES (?, ?, ?, ?, ?)");
            ins2.setString(1, cardNumber);
            ins2.setString(2, email);
            ins2.setString(3, city);
            ins2.setString(4, street);
            ins2.setInt(5, number);
            ins2.executeUpdate();

            conn.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    // ================= searchAvailableRooms =================
    public static ResultSet searchAvailableRooms(Date start, Date end) {

        String sql =
                "SELECT h.Name, h.HotelID, r.RoomNum " +
                        "FROM Room r " +
                        "JOIN Hotel h ON r.HotelID = h.HotelID " +
                        "WHERE NOT EXISTS (" +
                        "  SELECT 1 FROM Booking b " +
                        "  WHERE b.HotelID = r.HotelID " +
                        "    AND b.RoomNum = r.RoomNum " +
                        "    AND NOT (b.EndDate < ? OR b.StartDate > ?)" +
                        ")";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setDate(1, start);
            ps.setDate(2, end);

            return ps.executeQuery();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ================= bookRoom =================
    public static boolean bookRoom(
            String email,
            int hotelId,
            int roomNum,
            Date start,
            Date end) {

        String checkSql =
                "SELECT 1 FROM Booking b " +
                        "WHERE b.HotelID=? AND b.RoomNum=? " +
                        "AND NOT (b.EndDate < ? OR b.StartDate > ?)";

        String insertSql =
                "INSERT INTO Booking (BookingID, PricePerDay, RoomNum, HotelID, Email, StartDate, EndDate) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {

            PreparedStatement check = conn.prepareStatement(checkSql);
            check.setInt(1, hotelId);
            check.setInt(2, roomNum);
            check.setDate(3, start);
            check.setDate(4, end);

            ResultSet rs = check.executeQuery();

            if (rs.next()) {
                return false;
            }

            PreparedStatement insert = conn.prepareStatement(insertSql);

            int bookingId = (int) (System.currentTimeMillis() % 1000000);

            insert.setInt(1, bookingId);
            insert.setInt(2, 100);
            insert.setInt(3, roomNum);
            insert.setInt(4, hotelId);
            insert.setString(5, email);
            insert.setDate(6, start);
            insert.setDate(7, end);

            insert.executeUpdate();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    // ================= autoBook =================
    public static String autoBook(String email, String hotelName, Date start, Date end) {

        try (Connection conn = DBConnection.getConnection()) {

            conn.setAutoCommit(false);

            try {
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT r.RoomNum, h.HotelID " +
                                "FROM Room r JOIN Hotel h ON r.HotelID = h.HotelID " +
                                "WHERE h.Name = ? AND NOT EXISTS (" +
                                "  SELECT 1 FROM Booking b " +
                                "  WHERE b.HotelID = r.HotelID AND b.RoomNum = r.RoomNum " +
                                "  AND NOT (b.EndDate < ? OR b.StartDate > ?))"
                );

                ps.setString(1, hotelName);
                ps.setDate(2, start);
                ps.setDate(3, end);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    int room = rs.getInt("RoomNum");
                    int hotelId = rs.getInt("HotelID");


                    PreparedStatement insert = conn.prepareStatement(
                            "INSERT INTO Booking VALUES (?, ?, ?, ?, ?, ?, ?)");

                    int bookingId = (int) (System.currentTimeMillis() % 100000);

                    insert.setInt(1, bookingId);
                    insert.setInt(2, 100);
                    insert.setInt(3, room);
                    insert.setInt(4, hotelId);
                    insert.setString(5, email);
                    insert.setDate(6, start);
                    insert.setDate(7, end);

                    insert.executeUpdate();

                    conn.commit();

                    return "Booked!\nHotel: " + hotelName +
                            "\nRoom: " + room +
                            "\nDate: " + start + " → " + end;

                } else {
                    PreparedStatement alt = conn.prepareStatement(
                            "SELECT DISTINCT h.Name " +
                                    "FROM Room r JOIN Hotel h ON r.HotelID = h.HotelID " +
                                    "WHERE NOT EXISTS (" +
                                    "  SELECT 1 FROM Booking b " +
                                    "  WHERE b.HotelID = r.HotelID AND b.RoomNum = r.RoomNum " +
                                    "  AND NOT (b.EndDate < ? OR b.StartDate > ?))"
                    );

                    alt.setDate(1, start);
                    alt.setDate(2, end);

                    ResultSet rs2 = alt.executeQuery();

                    StringBuilder sb = new StringBuilder("No room available.\n\nAlternative hotels:\n");

                    while (rs2.next()) {
                        sb.append("- ").append(rs2.getString("Name")).append("\n");
                    }

                    conn.commit();

                    return sb.toString();
                }

            } catch (Exception e) {
                conn.rollback();
                throw e;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }


    // ================= ClientBookings =================
    public static ResultSet getBookings(String email) {

        try {
            Connection conn = DBConnection.getConnection();

            String sql =
                    "SELECT b.StartDate, b.EndDate, " +
                            "b.PricePerDay, " +
                            "(b.PricePerDay * (b.EndDate - b.StartDate)) AS TotalPrice, " +
                            "h.Name AS HotelName, r.RoomNum " +
                            "FROM Booking b " +
                            "JOIN Hotel h ON b.HotelID = h.HotelID " +
                            "JOIN Room r ON b.HotelID = r.HotelID AND b.RoomNum = r.RoomNum " +
                            "WHERE b.Email = ? " +
                            "ORDER BY b.StartDate";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);

            return ps.executeQuery();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    // ================= canReview =================

    public static boolean canReview(String email, int hotelId) {

        String sql =
                "SELECT 1 FROM Booking " +
                        "WHERE Email = ? AND HotelID = ? " +
                        "LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setInt(2, hotelId);

            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ================= submitReview =================
    public static boolean submitReview(String email, int hotelId, int rating, String message) {

        try (Connection conn = DBConnection.getConnection()) {


            if (!canReview(email, hotelId)) {
                return false;
            }


            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO Review (ReviewID, HotelID, Rating, Email, Message) " +
                            "VALUES (?, ?, ?, ?, ?)");

            int reviewId = (int) (System.currentTimeMillis() % 100000);

            ps.setInt(1, reviewId);
            ps.setInt(2, hotelId);
            ps.setInt(3, rating);
            ps.setString(4, email);
            ps.setString(5, message);

            ps.executeUpdate();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}