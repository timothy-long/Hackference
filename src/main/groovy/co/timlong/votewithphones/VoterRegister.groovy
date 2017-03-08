package co.timlong.votewithphones

import co.timlong.votewithphones.messages.VoterPositionsMessage

import java.util.concurrent.ConcurrentHashMap

/**
 * Created by tim on 22/10/2016.
 */
class VoterRegister {
    def voters = new ConcurrentHashMap<Voter, Boolean>()

    void register(Voter voter) {
        voters.put(voter, Boolean.TRUE)
    }

    void remove(Voter voter) {
        voters.remove(voter)
    }

    VoterPositionsMessage getVoterPositionsMessage()
    {
        def message = new VoterPositionsMessage()

        voters.forEachKey(Long.MAX_VALUE, message.&add)

        message
    }
}
