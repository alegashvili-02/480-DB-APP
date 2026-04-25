package org.example;

import java.sql.Connection;
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
}