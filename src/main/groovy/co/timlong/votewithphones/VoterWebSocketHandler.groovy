package co.timlong.votewithphones

import co.timlong.votewithphones.messages.VoterRemovedMessage
import groovy.json.JsonSlurper
import ratpack.websocket.WebSocketClose
import ratpack.websocket.WebSocketMessage

/**
 * Created by Tim on 04/03/2017.
 */
class VoterWebSocketHandler extends WebSocketMessageHandler {
    private VoterRegister voterRegister
    private Voter voter
    private JsonSlurper jsonSlurper = new JsonSlurper()

    VoterWebSocketHandler(VoterRegister voterRegister, Voter voter) {
        this.voterRegister = voterRegister
        this.voter = voter
    }

    @Override
    void onClose(WebSocketClose<String> close) throws Exception {
        voterRegister.remove(voter)

        def message = new VoterRemovedMessage()
        message.uid = voter.uid
        voter.room.socketHandler.send(message)
    }

    @Override
    void onMessage(WebSocketMessage<String> frame) throws Exception{
        def message = jsonSlurper.parseText(frame.text)

        if(message.alpha != null)
            voter.setAlpha(message.alpha)

        if(message.beta != null)
            voter.setBeta(message.beta)

        if(message.gamma != null)
            voter.setGamma(message.gamma)
    }
}