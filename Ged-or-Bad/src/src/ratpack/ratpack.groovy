import co.timlong.gedorbad.DeviceOrientationHandler
import co.timlong.gedorbad.StreamContainer
import co.timlong.gedorbad.VoterRegister
import com.google.inject.Scopes
import ratpack.groovy.template.TextTemplateModule
import ratpack.websocket.WebSockets

import static ratpack.groovy.Groovy.groovyTemplate
import static ratpack.groovy.Groovy.ratpack

ratpack {
    bindings {
        module TextTemplateModule
        binder { b ->
            b.bind(StreamContainer).in(Scopes.SINGLETON)
            b.bind(VoterRegister).in(Scopes.SINGLETON)
        }
    }
    handlers {
        get { ctx ->
            ctx.redirect(302, "vote")
        }
        get("view") {
            render groovyTemplate("view.html")
        }
        get("vote") {
            render groovyTemplate("voter.html")
        }
        get("stream/voter") { ctx ->
            def voterRegister = ctx.get(VoterRegister)

            // open receiving socket
            WebSockets.websocket(ctx, new DeviceOrientationHandler(voterRegister))
        }
        get("stream/view") { ctx ->
            def streamContainer = ctx.get(StreamContainer)

            // open broadcast socket
            def stream = streamContainer.stream
            WebSockets.websocketBroadcast(ctx, stream)
        }
        files {
            dir "static"
        }
    }
}