package oceanviewresort.dao;

import oceanviewresort.model.Reservation;
import oceanviewresort.model.Room;
import oceanviewresort.util.DBUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAOImpl implements ReservationDAO {

    private static final String TABLE = "reservation";

    // ----------------------------------------
    // SAVE RESERVATION
    // ----------------------------------------

    @Override
    public boolean save(Reservation reservation) {

        String sql = """
                INSERT INTO reservation
                (guest_name, address, contact_number,
                 room_id, check_in, check_out, total_amount)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, reservation.getGuestName());
            stmt.setString(2, reservation.getAddress());
            stmt.setString(3, reservation.getContactNumber());
            stmt.setInt(4, reservation.getRoom().getRoomId());
            stmt.setDate(5, Date.valueOf(reservation.getCheckInDate()));
            stmt.setDate(6, Date.valueOf(reservation.getCheckOutDate()));
            stmt.setDouble(7, reservation.getTotalAmount());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // ----------------------------------------
    // FIND BY ID
    // ----------------------------------------

    @Override
    public Reservation findById(int reservationId) {

        String sql = "SELECT * FROM reservation WHERE reservation_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reservationId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapReservation(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // ----------------------------------------
    // FIND ALL
    // ----------------------------------------

    @Override
    public List<Reservation> findAll() {

        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservation";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                reservations.add(mapReservation(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservations;
    }

    // ----------------------------------------
    // PREVENT DOUBLE BOOKING
    // ----------------------------------------

    @Override
    public boolean isRoomAvailable(int roomId,
                                   LocalDate checkIn,
                                   LocalDate checkOut) {

        String sql = """
                SELECT COUNT(*)
                FROM reservation
                WHERE room_id = ?
                AND check_in < ?
                AND check_out > ?
                """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, roomId);
            stmt.setDate(2, Date.valueOf(checkOut));
            stmt.setDate(3, Date.valueOf(checkIn));

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println("Overlap count: " + count);
                return count == 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // ----------------------------------------
    // SEARCH BY MOBILE NUMBER
    // ----------------------------------------

    @Override
    public List<Reservation> findByContactNumber(String contactNumber) {

        List<Reservation> reservations = new ArrayList<>();

        String sql = "SELECT * FROM reservation WHERE contact_number = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, contactNumber);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                reservations.add(mapReservation(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservations;
    }

    @Override
    public boolean deleteById(int reservationId) {

        String sql = "DELETE FROM reservation WHERE reservation_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reservationId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // ----------------------------------------
    // MAP RESULTSET TO OBJECT
    // ----------------------------------------

    private Reservation mapReservation(ResultSet rs) throws SQLException {

        Room room = new Room();
        room.setRoomId(rs.getInt("room_id"));

        Reservation reservation = new Reservation();
        reservation.setReservationId(rs.getInt("reservation_id"));
        reservation.setGuestName(rs.getString("guest_name"));
        reservation.setAddress(rs.getString("address"));
        reservation.setContactNumber(rs.getString("contact_number"));
        reservation.setRoom(room);
        reservation.setCheckInDate(rs.getDate("check_in").toLocalDate());
        reservation.setCheckOutDate(rs.getDate("check_out").toLocalDate());
        reservation.setTotalAmount(rs.getDouble("total_amount"));

        return reservation;
    }
}