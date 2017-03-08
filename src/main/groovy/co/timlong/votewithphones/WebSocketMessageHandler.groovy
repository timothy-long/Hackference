package co.timlong.votewithphones

import ratpack.websocket.WebSocket
import ratpack.websocket.WebSocketClose
import ratpack.websocket.WebSocketHandler
import ratpack.websocket.WebSocketMessage

import static groovy.json.JsonOutput.toJson

/**
 * Created by Tim on 04/03/2017.
 */
abstract class WebSocketMessageHandler implements WebSocketHandler<String> {
    private WebSocket webSocket

    void send(Message message) {
        webSocket.send(toJson(message))
    }

    @Override
    String onOpen(WebSocket webSocket) throws Exception {
        this.webSocket = webSocket
        null
    }

    @Override
    void onMessage(WebSocketMessage<String> frame) throws Exception {}

    @Override
    void onClose(WebSocketClose<String> close) throws Exception {}
}