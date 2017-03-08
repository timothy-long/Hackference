package co.timlong.votewithphones

import org.apache.commons.lang3.RandomStringUtils
import ratpack.service.Service

import java.util.concurrent.ConcurrentHashMap

/**
 * Created by Tim on 04/03/2017.
 */
class RoomRegistry implements Service {
    private final rooms = new ConcurrentHashMap<String, Room>()

    void register(Room room) {
        def uid = generateUid()

        synchronized (rooms) {
            while(rooms.containsKey(uid))
                uid = generateUid()

            rooms.put(uid, room)
        }

        room.uid = uid
    }

    Room getRoom(String uid) {
        rooms.get(uid)
    }

    void remove(String uid) {
        rooms.remove(uid)
    }

    private static String generateUid() {
        RandomStringUtils.randomAlphanumeric(6).toUpperCase()
    }
}