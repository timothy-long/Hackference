package co.timlong.gedorbad

import groovy.util.logging.Slf4j
import ratpack.websocket.WebSocket

/**
 * Created by tim on 22/10/2016.
 */
class VoterRegister {
    def voters = new HashMap<String, Voter>()

    Voter registerVoter(WebSocket webSocket) {
        def uid = UUID.randomUUID().toString()
        def voter = new Voter(uid, webSocket)

        voters[uid] = voter

        return voter
    }

    void unregisterVoter(String uid) {
        voters.remove(uid)
    }
}
