package co.timlong.votewithphones

import co.timlong.votewithphones.messages.VoterPositionsMessage
import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder

import java.util.concurrent.TimeUnit

/**
 * Created by tim on 22/10/2016.
 */
class VoterRegister {
    private final Cache<Voter, Boolean> voters = CacheBuilder.newBuilder()
            .expireAfterAccess(2, TimeUnit.HOURS)
            .build()

    void register(Voter voter) {
        voters.put(voter, Boolean.TRUE)
    }

    void remove(Voter voter) {
        voters.invalidate(voter)
    }

    VoterPositionsMessage getVoterPositionsMessage()
    {
        def message = new VoterPositionsMessage()

        voters.asMap().keySet().forEach(message.&add)

        message
    }

    Set<Voter> getVoters() {
        voters.asMap().keySet()
    }
}
