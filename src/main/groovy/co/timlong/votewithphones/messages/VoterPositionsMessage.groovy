package co.timlong.votewithphones.messages

import co.timlong.votewithphones.Message
import co.timlong.votewithphones.Voter

/**
 * Created by tim on 22/10/2016.
 */
class VoterPositionsMessage implements Message {
    class VoterPosition
    {
        String uid
        BigDecimal alpha
        BigDecimal beta
        BigDecimal gamma
    }

    def positions = new ArrayList<VoterPosition>()

    @Override
    String getType() {
        return "voter-positions"
    }

    void add(Voter voter)
    {
        def position = new VoterPosition()
        position.uid = voter.uid
        position.alpha = voter.alpha
        position.beta = voter.beta
        position.gamma = voter.gamma
        positions.add(position)
    }
}