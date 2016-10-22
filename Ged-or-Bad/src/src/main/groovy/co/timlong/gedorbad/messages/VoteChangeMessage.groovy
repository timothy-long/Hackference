package co.timlong.gedorbad.messages

import co.timlong.gedorbad.Message

/**
 * Created by tim on 22/10/2016.
 */
class VoteChangeMessage implements Message{
    int vote

    @Override
    String getType() {
        return "vote-change"
    }
}
