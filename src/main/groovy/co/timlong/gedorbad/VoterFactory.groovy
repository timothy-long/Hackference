package co.timlong.gedorbad

/**
 * Created by Tim on 04/03/2017.
 */
class VoterFactory {
    Voter newVoterFor(Room room) {
        def voter = new Voter(room)
        room.voters.register(voter)
        voter
    }
}