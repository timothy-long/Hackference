package co.timlong.gedorbad

import com.google.inject.Inject
import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber
import ratpack.exec.ExecController
import ratpack.service.Service
import ratpack.service.StartEvent
import ratpack.stream.Streams

import java.time.Duration

import static com.google.common.collect.Queues.newArrayDeque
import static groovy.json.JsonOutput.toJson

class StreamContainer implements Service {
    Publisher<String> stream
    private final Deque<Message> queue = newArrayDeque()
    private final ExecController execController

    @Inject
    StreamContainer(ExecController execController) {
        this.execController = execController
    }

    void onStart(StartEvent event) {
        this.stream = Streams.periodically(event.getRegistry(), Duration.ofMillis(10), {
            queue.pollLast() ?: []
        }) map {
            it ? toJson(it) : ""
        } multicast()
    }

    void publish(Message msg) {
        queue.push(msg)
    }
}