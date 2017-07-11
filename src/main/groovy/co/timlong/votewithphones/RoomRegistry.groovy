package co.timlong.votewithphones

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import org.apache.commons.lang3.RandomStringUtils
import ratpack.service.Service

import java.util.concurrent.TimeUnit

/**
 * Created by Tim on 04/03/2017.
 */
class RoomRegistry implements Service {
    private final Cache<String, Room> rooms = CacheBuilder.newBuilder()
            .expireAfterAccess(6, TimeUnit.HOURS)
            .build()

    void register(Room room) {
        def uid = generateUid()

        synchronized (rooms) {
            while(rooms.getIfPresent(uid) != null)
                uid = generateUid()

            rooms.put(uid, room)
        }

        room.uid = uid
    }

    Room getRoom(String uid) {
        rooms.getIfPresent(uid)
    }

    void remove(String uid) {
        rooms.invalidate(uid)
    }

    private static String generateUid() {
        RandomStringUtils.randomAlphanumeric(6).toUpperCase()
    }
}