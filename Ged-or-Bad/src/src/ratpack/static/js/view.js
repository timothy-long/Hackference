if(!window.DeviceOrientationEvent || !window.WebSocket)
{
    alert("This won't work in your browser. Why not try Chrome, or a newer version (if available).");
}

function connectWs() {
    if (window.ws && window.ws.readyState == WebSocket.OPEN) {
        return;
    }

    window.ws = new WebSocket("ws://"+location.host+"/stream/view");

    window.ws.onopen = function(event) {
        console.log("WebSocket opened!", event);
    };

    window.ws.onmessage = function(event) {
        if(!event.data) {
            return;
        }

        var message = JSON.parse(event.data);

        switch(message.type)
        {
            case 'voter-positions':
                updatePositions(message.positions);
                break;
            case 'voter-removed':
                removeVoter(message.uid);
                break;
            default:
                console.warn("Unable to process type: " + message.type)
        }
    }

    window.ws.onclose = function(event) {
        var timer = setTimeout(function() {
            console.log("Retrying connection...");
            connectWs();
            if (window.ws.readyState == WebSocket.OPEN) {
                clearTimeout(timer);
            }
        }, 1000);
    };
}

connectWs();

function updatePositions(positions) {
    console.log(positions[0].beta);
}

function removeVoter(uid) {
    console.log(uid);
}