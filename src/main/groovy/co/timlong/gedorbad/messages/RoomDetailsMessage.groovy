package co.timlong.gedorbad.messages

import co.timlong.gedorbad.Message
import co.timlong.gedorbad.Room

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