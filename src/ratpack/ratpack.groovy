import co.timlong.votewithphones.Room
import co.timlong.votewithphones.RoomFactory
import co.timlong.votewithphones.RoomRegistry
import co.timlong.votewithphones.VoterFactory
import com.google.inject.Scopes
import ratpack.groovy.template.TextTemplateModule
import ratpack.websocket.WebSockets

import static ratpack.groovy.Groovy.groovyTemplate
import static ratpack.groovy.Groovy.ratpack

ratpack {
    serverConfig {
        env()
    }
    bindings {
        module TextTemplateModule
        binder { b ->
            b.bind(RoomFactory).in(Scopes.SINGLETON)
            b.bind(VoterFactory).in(Scopes.SINGLETON)
            b.bind(RoomRegistry).in(Scopes.SINGLETON)
        }
    }
    handlers {
        get {
            render groovyTemplate("index.html")
        }

        prefix("view") {
            get {
                render groovyTemplate("view.html")
            }
            get("socket") { RoomFactory roomFactory ->
                def room = roomFactory.newRoom()

                WebSockets.websocket(context, room.socketHandler)
            }
        }

        prefix("vote") {
            get {
                redirect(302, "/")
            }
            prefix(":roomUid") {
                all { RoomRegistry roomRegistry ->
                    def room = roomRegistry.getRoom(pathTokens.roomUid)

                    if (room == null)
                        clientError(404)
                    else
                        next(single(room))
                }
                get { Room room ->
                    render groovyTemplate("voter.html", room: room)
                }
                get("socket") { VoterFactory voterFactory, Room room ->
                    def voter = voterFactory.newVoterFor(room)

                    WebSockets.websocket(context, voter.socketHandler)
                }
            }
        }

        files {
            dir "static"
        }
    }
}