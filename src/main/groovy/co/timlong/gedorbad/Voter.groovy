package co.timlong.gedorbad

import ratpack.websocket.WebSocket

/**
 * Created by tim on 22/10/2016.
 */
class Voter {
    private final String uid
    private final WebSocket webSocket
    private BigDecimal alpha = BigDecimal.ZERO
    private BigDecimal beta = BigDecimal.ZERO
    private BigDecimal gamma = BigDecimal.ZERO

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

    BigDecimal getAlpha() {
        return alpha
    }

    void setAlpha(BigDecimal alpha) {
        this.alpha = alpha
    }

    BigDecimal getBeta() {
        return beta
    }

    void setBeta(BigDecimal beta) {
        this.beta = beta
    }

    BigDecimal getGamma() {
        return gamma
    }

    void setGamma(BigDecimal gamma) {
        this.gamma = gamma
    }
}
