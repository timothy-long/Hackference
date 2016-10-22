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
    var score = 0;

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
        record.element.style.transform = 'rotate3d(0, 0, -1, ' + position.alpha + 'deg)';

        // calc score
        if(position.alpha < 50)
        {
            score += 1;
        }
        else if(position.alpha < 110)
        {
            score += 0;
        }
        else if(position.alpha < 230)
        {
            score -= 1;
        }
        else if(position.alpha < 300)
        {
            score += 0;
        }
        else
        {
            score += 1;
        }
    }

    if(positions.length > 0)
    {
        score += positions.length;
        var percentage = Math.floor((100 / (positions.length * 2)) * score);

        document.getElementById('value').innerHTML = percentage;
    }
}

function removeVoter(uid) {
    var record = thumbs[uid];

    if(record == undefined)
    {
        return;
    }

    record.element.parentNode.removeChild(record.element);
    delete thumbs[uid];
}