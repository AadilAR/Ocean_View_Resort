package oceanviewresort.dao;

import oceanviewresort.model.Room;
import oceanviewresort.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomDAOImpl implements RoomDAO {

    private static final String SELECT_BY_ID =
            "SELECT room_id, room_type, price_per_night FROM room WHERE room_id = ?";

    private static final String SELECT_ALL =
            "SELECT room_id, room_type, price_per_night FROM room";

    @Override
    public Room getRoomById(int roomId) {

        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID)) {

            stmt.setInt(1, roomId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRoom(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public List<Room> findAll() {

        List<Room> rooms = new ArrayList<>();

        String sql = "SELECT room_id, room_type, price_per_night FROM room";

        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                rooms.add(new Room(
                        rs.getInt("room_id"),
                        rs.getString("room_type"),
                        rs.getDouble("price_per_night")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rooms;
    }

    @Override
    public double getPriceByRoomId(int roomId) {

        double price = 0;

        String sql = "SELECT price_per_night FROM room WHERE room_id = ?";

        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, roomId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                price = rs.getDouble("price_per_night");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return price;
    }

    // ----------------------------------------
    // MAP RESULTSET TO ROOM
    // ----------------------------------------

    private Room mapRoom(ResultSet rs) throws SQLException {
        return new Room(
                rs.getInt("room_id"),
                rs.getString("room_type"),
                rs.getDouble("price_per_night")
        );
    }


}