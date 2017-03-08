package co.timlong.votewithphones

/**
 * Created by tim on 22/10/2016.
 */
class Voter {
    final String uid = UUID.randomUUID().toString()
    VoterWebSocketHandler socketHandler
    private Room room
    def alpha = BigDecimal.ZERO
    def beta = BigDecimal.ZERO
    def gamma = BigDecimal.ZERO

    Voter(Room room) {
        this.room = room
        this.socketHandler = new VoterWebSocketHandler(room.voters, this)
    }

    Room getRoom() {
        room
    }
}