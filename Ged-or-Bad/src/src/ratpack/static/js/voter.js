if(!window.DeviceOrientationEvent || !window.WebSocket)
{
    alert("This won't work in your browser. Why not try Chrome, or a newer version (if available).");
}

function connectWs() {
    if (window.ws && window.ws.readyState == WebSocket.OPEN) {
        return;
    }

    window.ws = new WebSocket("ws://"+location.host+"/stream/voter");

    window.ws.onopen = function(event) {
        console.log("WebSocket opened!", event);
    };

    window.ws.onclose = function(event) {
        window.removeEventListener('deviceorientation', onMotionHandler);

        var timer = setTimeout(function() {
            console.log("Retrying connection...");
            connectWs();
            if (window.ws.readyState == WebSocket.OPEN) {
                clearTimeout(timer);
            }
        }, 1000);
    };

    var onMotionHandler = function(e) {
        if(window.ws.readyState != WebSocket.OPEN)
        {
            return;
        }

        //console.log(e.alpha, e.beta, e.gamma);

        var data = {
            alpha: e.alpha,
            beta: e.beta,
            gamma: e.gamma
        }

        window.ws.send(JSON.stringify(data))
    }

    window.addEventListener('deviceorientation', onMotionHandler, false);
}

connectWs();