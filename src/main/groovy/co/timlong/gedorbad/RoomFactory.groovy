package co.timlong.gedorbad

import com.google.inject.Inject
import ratpack.exec.ExecController

/**
 * Created by Tim on 04/03/2017.
 */
class RoomFactory {
    private RoomRegistry registry
    private ExecController execController

    @Inject
    RoomFactory(RoomRegistry roomRegistry, ExecController execController) {
        this.execController = execController
        this.registry = roomRegistry
    }

    Room newRoom() {
        def room = new Room()
        def roomSocketHandler = new RoomWebSocketHandler(registry, execController, room)

        registry.register(room)
        room.socketHandler = roomSocketHandler

        room
    }
}