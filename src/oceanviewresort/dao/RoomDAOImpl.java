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

    @Override
    public Room getRoomById(int roomId) {

        String sql = "SELECT * FROM room WHERE room_id = ?";
        Room room = null;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, roomId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                room = new Room(
                        rs.getInt("room_id"),
                        rs.getString("room_type"),
                        rs.getDouble("price_per_night")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return room;
    }

    @Override
    public List<Room> getAllRooms() {

        String sql = "SELECT * FROM room";
        List<Room> rooms = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Room room = new Room(
                        rs.getInt("room_id"),
                        rs.getString("room_type"),
                        rs.getDouble("price_per_night")
                );
                rooms.add(room);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rooms;
    }
}
