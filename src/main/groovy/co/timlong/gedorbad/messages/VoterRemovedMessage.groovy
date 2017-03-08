package co.timlong.gedorbad.messages

import co.timlong.gedorbad.Message

/**
 * Created by tim on 22/10/2016.
 */
class VoterRemovedMessage implements Message {
    String uid

    @Override
    String getType() {
        return "voter-removed"
    }
}