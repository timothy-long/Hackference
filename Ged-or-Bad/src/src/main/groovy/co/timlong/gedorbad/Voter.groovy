package co.timlong.gedorbad

import ratpack.websocket.WebSocket

/**
 * Created by tim on 22/10/2016.
 */
class Voter {
    private final String uid
    private final WebSocket webSocket

    Voter(String uid, WebSocket webSocket)
    {
        this.uid = uid
        this.webSocket = webSocket
    }

    String getUid() {
        return uid
    }

    WebSocket getWebSocket() {
        return webSocket
    }
}
