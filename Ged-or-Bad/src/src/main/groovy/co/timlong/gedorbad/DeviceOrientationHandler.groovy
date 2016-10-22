package co.timlong.gedorbad

import com.google.inject.Inject
import ratpack.websocket.WebSocket
import ratpack.websocket.WebSocketClose
import ratpack.websocket.WebSocketHandler
import ratpack.websocket.WebSocketMessage

/**
 * Created by tim on 22/10/2016.
 */
class DeviceOrientationHandler implements WebSocketHandler<String> {
    private final VoterRegister voterRegister
    private String voterUid

    @Inject
    DeviceOrientationHandler(VoterRegister voterRegister)
    {
        this.voterRegister = voterRegister
    }

    @Override
    String onOpen(WebSocket webSocket) throws Exception {
        voterUid = voterRegister.registerVoter(webSocket).getUid()
        return null
    }

    @Override
    void onClose(WebSocketClose<String> close) throws Exception {
        voterRegister.unregisterVoter(voterUid)
    }

    @Override
    void onMessage(WebSocketMessage<String> frame) throws Exception {

    }
}
