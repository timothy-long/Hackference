package co.timlong.votewithphones

import co.timlong.votewithphones.messages.RoomDetailsMessage
import ratpack.exec.ExecController
import ratpack.websocket.WebSocket
import ratpack.websocket.WebSocketClose

import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

/**
 * Created by Tim on 04/03/2017.
 */
class RoomWebSocketHandler extends WebSocketMessageHandler {
    private Room room
    private RoomRegistry roomRegistry
    private ExecController execController
    private ScheduledFuture updateCycle

    RoomWebSocketHandler(RoomRegistry roomRegistry, ExecController execController, Room room) {
        this.execController = execController
        this.room = room
        this.roomRegistry = roomRegistry
    }

    @Override
    String onOpen(WebSocket webSocket) throws Exception {
        super.onOpen(webSocket)

        def roomSocket = room.socketHandler

        updateCycle = execController.executor.scheduleWithFixedDelay({
            roomSocket.send(room.voters.voterPositionsMessage)
        }, 0, 50, TimeUnit.MILLISECONDS)

        send(new RoomDetailsMessage(room))

        null
    }

    @Override
    void onClose(WebSocketClose<String> close) throws Exception {
        updateCycle.cancel(true)
        room.close()
        roomRegistry.remove(room.uid)
    }
}