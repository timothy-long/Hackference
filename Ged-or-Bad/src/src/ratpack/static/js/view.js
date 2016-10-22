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

var thumbs = {};

function updatePositions(positions) {
    var votesContainer = document.getElementById('votes');

    for (var i = 0; i < positions.length; i++) {
        var position = positions[i];
        var record = thumbs[position.uid];

        if(record == undefined)
        {
            // new
            var vote = document.createElement('img');
            vote.setAttribute('src', '/img/thumbs.png')

            votesContainer.appendChild(vote);

            record = {
                element: vote
            };

            thumbs[position.uid] = record;
        }

        // update rotation
        //record.element.style.transform = 'rotate3d(' + position.alpha + ',' + position.beta + ',' + position.gamma + ', 0deg)';
        record.element.style.transform = 'rotate3d(0, 0, 1, ' + position.gamma + 'deg)';

        // calc score
    }
}

function removeVoter(uid) {
    console.log(uid);
    var record = thumbs[uid];

    if(record == undefined)
    {
        return;
    }

    record.element.parentNode.removeChild(record.element);
    delete thumbs[uid];
}