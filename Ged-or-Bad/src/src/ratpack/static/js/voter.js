if(!window.DeviceOrientationEvent || !window.WebSocket)
{
    alert("This won't work in your browser. Why not try Chrome, or a newer version (if available).");
}

screen.lockOrientationUniversal = screen.lockOrientation || screen.mozLockOrientation || screen.msLockOrientation;

if (!(screen.lockOrientationUniversal && screen.lockOrientationUniversal("portrait-primary")) && !(getOrientation() && getOrientation().angle !== undefined)) {
  // orientation lock failed
  alert("We recommend locking the rotation of your screen to portrait for accurate voting.")
}

var alphaOffset = 0;
var lastRecordedData;

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

        if(e.alpha == null && e.beta == null && e.gamma == null)
        {
            return;
        }

        //console.log(e.alpha, e.beta, e.gamma);
        var orientation = getOrientation();

        if(orientation && orientation.angle)
        {
            e.alpha += orientation.angle;
        }

        lastRecordedData = {
            alpha: (e.alpha + alphaOffset) % 360,
            beta: e.beta,
            gamma: e.gamma
        }

        window.ws.send(JSON.stringify(lastRecordedData))
    }

    window.addEventListener('deviceorientation', onMotionHandler, false);
}

connectWs();

function getOrientation() {
    return screen.orientation || screen.mozOrientation || screen.msOrientation;
}

function calibrate() {
    if(!lastRecordedData)
    {
        return;
    }

    alphaOffset = lastRecordedData.alpha - alphaOffset + lastRecordedData.alpha;
}