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

    private static final String INSERT_SQL = """
            INSERT INTO reservation
            (guest_name, address, contact_number,
             room_id, check_in, check_out, total_amount)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

    private static final String SELECT_BY_ID =
            "SELECT reservation_id, guest_name, address, contact_number, " +
                    "room_id, check_in, check_out, total_amount " +
                    "FROM reservation WHERE reservation_id = ?";

    private static final String SELECT_ALL =
            "SELECT reservation_id, guest_name, address, contact_number, " +
                    "room_id, check_in, check_out, total_amount " +
                    "FROM reservation";

    private static final String SELECT_BY_CONTACT =
            "SELECT reservation_id, guest_name, address, contact_number, " +
                    "room_id, check_in, check_out, total_amount " +
                    "FROM reservation WHERE contact_number = ?";

    private static final String DELETE_SQL =
            "DELETE FROM reservation WHERE reservation_id = ?";

    private static final String CHECK_AVAILABILITY_SQL = """
            SELECT COUNT(*)
            FROM reservation
            WHERE room_id = ?
            AND check_in < ?
            AND check_out > ?
            """;

    // ----------------------------------------
    // SAVE RESERVATION
    // ----------------------------------------

    @Override
    public int save(Reservation reservation) {

        String sql = """
            INSERT INTO reservation
            (guest_name, address, contact_number,
             room_id, check_in, check_out, total_amount)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, reservation.getGuestName());
            stmt.setString(2, reservation.getAddress());
            stmt.setString(3, reservation.getContactNumber());
            stmt.setInt(4, reservation.getRoom().getRoomId());
            stmt.setDate(5, Date.valueOf(reservation.getCheckInDate()));
            stmt.setDate(6, Date.valueOf(reservation.getCheckOutDate()));
            stmt.setDouble(7, reservation.getTotalAmount());

            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1); // return reservation_id
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }


    // ----------------------------------------
    // FIND BY ID
    // ----------------------------------------

    @Override
    public Reservation findById(int reservationId) {

        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID)) {

            stmt.setInt(1, reservationId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapReservation(rs);
                }
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

        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL);
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

        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(CHECK_AVAILABILITY_SQL)) {

            stmt.setInt(1, roomId);
            stmt.setDate(2, Date.valueOf(checkOut));
            stmt.setDate(3, Date.valueOf(checkIn));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0;
                }
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

        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_CONTACT)) {

            stmt.setString(1, contactNumber);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reservations.add(mapReservation(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservations;
    }

    // ----------------------------------------
    // DELETE RESERVATION
    // ----------------------------------------

    @Override
    public boolean deleteById(int reservationId) {

        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {

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
