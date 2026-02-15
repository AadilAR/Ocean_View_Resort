package oceanviewresort.dao;

import oceanviewresort.model.Room;
import java.util.List;

public interface RoomDAO {

    Room getRoomById(int roomId);

    List<Room> findAll();
}