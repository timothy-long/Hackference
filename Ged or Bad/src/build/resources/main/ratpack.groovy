import com.google.inject.Scopes
import ratpack.form.Form
import ratpack.websocket.WebSockets

import static ratpack.groovy.Groovy.groovyTemplate
import static ratpack.groovy.Groovy.ratpack

ratpack {
    bindings {
        binder { b ->
            b.bind(StreamContainer).in(Scopes.SINGLETON)
        }
    }
    handlers {
        get {
            render groovyTemplate("websocket-sample.html")
        }
        get("stream") { ctx ->
            def streamContainer = ctx.get(StreamContainer)
            def stream = streamContainer.stream
            WebSockets.websocketBroadcast(ctx, stream)
        }
        post("stream") { ctx ->
            def form = ctx.parse(Form)
            def msg = form.msg as String
            def streamContainer = ctx.get(StreamContainer)
            streamContainer.publish(msg)
            ctx.response.status(202)
            ctx.response.send()
        }
    }
}