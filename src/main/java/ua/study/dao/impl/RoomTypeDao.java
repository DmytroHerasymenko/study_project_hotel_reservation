package ua.study.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.study.dao.AbstractDao;
import ua.study.domain.Dates;
import ua.study.domain.Reservation;
import ua.study.domain.RoomType;
import ua.study.domain.enums.Bedspace;
import ua.study.domain.enums.RoomCategory;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by dima on 28.03.17.
 */
public class RoomTypeDao extends AbstractDao<RoomType> {
    private final Properties properties = new Properties();
    private final Logger LOGGER = LogManager.getLogger(RoomTypeDao.class.getName());

    public RoomTypeDao(){
        init();
    }

    @Override
    public void create() {
        getExecutor().executorUpdate(properties.getProperty("category.room_type"));
        getExecutor().executorUpdate(properties.getProperty("bedspace.room_type"));
        getExecutor().executorUpdate(properties.getProperty("create.room_type"));
    }

    public List<RoomType> get(){
        List<RoomType> roomTypes = new ArrayList<>();
        getExecutor().executorQuery(properties.getProperty("get.room_type"), result -> {
            if(!(result.next())) return null;
            do {
                RoomType roomType = getRoomType(result);
                roomTypes.add(roomType);
            } while (result.next());
            return roomTypes;
        });
        return roomTypes;
    }

    public Map<RoomType, Integer> getFreeRoomTypes(Dates dates){
        String query = properties.getProperty("get.free_room_type");
        Map<RoomType, Integer> freeRoomTypes = new LinkedHashMap<>();
        getExecutor().getFreeRoomTypes(query, dates, result -> {
            if(!(result.next())) return null;
            do {
                RoomType roomType = getRoomType(result);
                freeRoomTypes.put(roomType, result.getInt(5));
            } while (result.next());
            return freeRoomTypes;
        });
        return freeRoomTypes;
    }

    private RoomType getRoomType(ResultSet result) throws SQLException {
        RoomType roomType = new RoomType();
        roomType.setRoomTypeId(result.getInt(1));
        roomType.setRoomCategory(RoomCategory.valueOf(result.getString(2)));
        roomType.setBedspace(Bedspace.valueOf(result.getString(3)));
        roomType.setPrice(result.getDouble(4));
        return roomType;
    }

    private void init(){
        try {
            properties.load(getClass().getResourceAsStream("/sql.properties"));
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }
}
