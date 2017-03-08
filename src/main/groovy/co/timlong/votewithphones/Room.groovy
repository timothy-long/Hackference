package co.timlong.votewithphones

import co.timlong.votewithphones.messages.RoomClosedMessage

/**
 * Created by Tim on 04/03/2017.
 */
class Room {
    RoomWebSocketHandler socketHandler
    String uid
    private VoterRegister voters

    Room() {
        this.voters = new VoterRegister()
    }

    VoterRegister getVoters() {
        voters
    }

    void close() {
        RoomClosedMessage message = new RoomClosedMessage()
        voters.voters.forEachKey(20, {v -> v.socketHandler.send(message)})
    }
}