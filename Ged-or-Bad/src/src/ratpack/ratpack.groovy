import co.timlong.gedorbad.DeviceOrientationHandler
import co.timlong.gedorbad.StreamContainer
import co.timlong.gedorbad.VoterRegister
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
            b.bind(StreamContainer).in(Scopes.SINGLETON)
            b.bind(VoterRegister).in(Scopes.SINGLETON)
        }
    }
    handlers {
        get {
            redirect(302, "vote")
        }
        get("view") {
            render groovyTemplate("view.html")
        }
        get("vote") {
            render groovyTemplate("voter.html")
        }
        get("stream/voter") { VoterRegister voterRegister ->
            // open receiving socket
            WebSockets.websocket(context, new DeviceOrientationHandler(voterRegister))
        }
        get("stream/view") { StreamContainer streamContainer ->
            // open broadcast socket
            WebSockets.websocketBroadcast(context, streamContainer.stream)
        }
        files {
            dir "static"
        }
    }
}