package oceanviewresort.dao;

import oceanviewresort.model.Reservation;
import oceanviewresort.model.Room;
import oceanviewresort.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // ============================
    // SAVE RESERVATION
    // ============================

    @Override
    public int save(Reservation reservation) {

        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

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
                return keys.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    // ============================
    // FIND BY ID
    // ============================

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

    // ============================
    // FIND ALL
    // ============================

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

    // ============================
    // CHECK ROOM AVAILABILITY
    // ============================

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

    // ============================
    // SEARCH BY CONTACT NUMBER
    // ============================

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

    // ============================
    // DELETE RESERVATION
    // ============================

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

    // ======================================================
    // REPORT METHODS (EXCELLENT CRITERIA – DECISION SUPPORT)
    // ======================================================

    @Override
    public List<Map<String, Object>> getMonthlyRevenue() {

        List<Map<String, Object>> revenueList = new ArrayList<>();

        String sql = """
            SELECT YEAR(check_in) AS year,
                   MONTH(check_in) AS month,
                   SUM(total_amount) AS total_revenue
            FROM reservation
            GROUP BY YEAR(check_in), MONTH(check_in)
            ORDER BY year DESC, month DESC
        """;

        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("year", rs.getInt("year"));
                row.put("month", rs.getInt("month"));
                row.put("totalRevenue", rs.getDouble("total_revenue"));
                revenueList.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return revenueList;
    }

    @Override
    public List<Map<String, Object>> getMonthlyOccupancy() {

        List<Map<String, Object>> occupancyList = new ArrayList<>();

        String sql = """
            SELECT MONTH(check_in) AS month,
                   COUNT(*) AS total_bookings
            FROM reservation
            GROUP BY MONTH(check_in)
            ORDER BY month
        """;

        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("month", rs.getInt("month"));
                row.put("totalBookings", rs.getInt("total_bookings"));
                occupancyList.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return occupancyList;
    }

    @Override
    public Map<String, Object> getMostBookedRoom() {

        String sql = """
            SELECT room_id, COUNT(*) AS total_bookings
            FROM reservation
            GROUP BY room_id
            ORDER BY total_bookings DESC
            LIMIT 1
        """;

        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                Map<String, Object> result = new HashMap<>();
                result.put("roomId", rs.getInt("room_id"));
                result.put("totalBookings", rs.getInt("total_bookings"));
                return result;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // ============================
    // MAP RESULTSET TO OBJECT
    // ============================

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