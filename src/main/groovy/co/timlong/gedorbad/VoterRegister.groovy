package co.timlong.gedorbad

import co.timlong.gedorbad.messages.VoterPositionsMessage
import co.timlong.gedorbad.messages.VoterRemovedMessage
import com.google.inject.Inject
import ratpack.exec.ExecController
import ratpack.exec.Execution
import ratpack.service.Service
import ratpack.service.StartEvent
import ratpack.service.StopEvent
import ratpack.websocket.WebSocket

import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

/**
 * Created by tim on 22/10/2016.
 */
class VoterRegister implements Service {
    def voters = new ArrayList<Voter>()
    StreamContainer stream

    private volatile ScheduledFuture<?> nextFuture;
    private volatile boolean stopped;
    private ScheduledExecutorService executorService;

    @Inject
    VoterRegister(StreamContainer streamContainer)
    {
        this.stream = streamContainer
    }

    @Override
    void onStart(StartEvent event) throws Exception {
        executorService = event.getRegistry().get(ExecController.class).getExecutor();

        scheduleNext();
    }

    @Override
    public void onStop(StopEvent event) throws Exception {
        stopped = true;
        Optional.ofNullable(nextFuture).ifPresent({it.cancel(true)});
    }

    private void scheduleNext() {
        nextFuture = executorService.schedule(this.&run, 50, TimeUnit.MILLISECONDS);
    }

    private void run() {
        if (stopped) {
            return;
        }

        Execution.fork()
                .onComplete({ scheduleNext() })
                .onError({ it.printStackTrace() })
                .start({
                    stream.publish(getVoterPositionsMessage())
                });
    }

    Voter registerVoter(WebSocket webSocket) {
        def uid = UUID.randomUUID().toString()
        def voter = new Voter(uid, webSocket)

        voters.add(voter)

        return voter
    }

    void unregisterVoter(Voter voter) {
        voters.remove(voter)

        def message = new VoterRemovedMessage()
        message.uid = voter.uid
        stream.publish(message)
    }

    VoterPositionsMessage getVoterPositionsMessage()
    {
        def message = new VoterPositionsMessage()

        voters.forEach(message.&add)

        return message
    }
}
