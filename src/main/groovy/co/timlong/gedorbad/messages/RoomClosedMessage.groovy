package co.timlong.gedorbad.messages

import co.timlong.gedorbad.Message

/**
 * Created by Tim on 04/03/2017.
 */
class RoomClosedMessage implements Message {
    @Override
    String getType() {
        "room-closed"
    }
}