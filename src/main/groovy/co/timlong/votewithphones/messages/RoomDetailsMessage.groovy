package co.timlong.votewithphones.messages

import co.timlong.votewithphones.Message
import co.timlong.votewithphones.Room

/**
 * Created by Tim on 05/03/2017.
 */
class RoomDetailsMessage implements Message {
    private String uid

    @Override
    String getType() {
        return "room-details"
    }

    RoomDetailsMessage(Room room) {
        uid = room.uid
    }

    String getUid() {
        uid
    }
}