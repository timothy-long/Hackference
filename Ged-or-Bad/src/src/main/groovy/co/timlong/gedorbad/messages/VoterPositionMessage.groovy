package co.timlong.gedorbad.messages

import co.timlong.gedorbad.Message

/**
 * Created by tim on 22/10/2016.
 */
class VoterPositionMessage implements Message {
    String uid
    int alpha
    int beta
    int gamma

    @Override
    String getType() {
        return "voter-position"
    }
}
