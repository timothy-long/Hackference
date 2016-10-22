package co.timlong.gedorbad

import com.google.inject.Inject
import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j
import ratpack.websocket.WebSocket
import ratpack.websocket.WebSocketClose
import ratpack.websocket.WebSocketHandler
import ratpack.websocket.WebSocketMessage

/**
 * Created by tim on 22/10/2016.
 */
@Slf4j
class DeviceOrientationHandler implements WebSocketHandler<String> {
    private final VoterRegister voterRegister
    private Voter voter
    private JsonSlurper jsonSlurper = new JsonSlurper()

    @Inject
    DeviceOrientationHandler(VoterRegister voterRegister)
    {
        this.voterRegister = voterRegister
    }

    @Override
    String onOpen(WebSocket webSocket) throws Exception {
        voter = voterRegister.registerVoter(webSocket)
        return null
    }

    @Override
    void onClose(WebSocketClose<String> close) throws Exception {
        voterRegister.unregisterVoter(voter)
    }

    @Override
    void onMessage(WebSocketMessage<String> frame) throws Exception {
        def message = jsonSlurper.parseText(frame.text)

        if(message.alpha != null)
            voter.setAlpha(message.alpha)

        if(message.beta != null)
            voter.setBeta(message.beta)

        if(message.gamma != null)
            voter.setGamma(message.gamma)
    }
}
