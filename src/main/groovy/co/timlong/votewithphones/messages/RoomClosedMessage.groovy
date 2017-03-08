package co.timlong.votewithphones.messages

import co.timlong.votewithphones.Message

/**
 * Created by Tim on 04/03/2017.
 */
class RoomClosedMessage implements Message {
    @Override
    String getType() {
        "room-closed"
    }
}