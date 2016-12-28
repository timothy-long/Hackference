if(!window.DeviceOrientationEvent || !window.WebSocket)
{
    alert("This won't work in your browser. Why not try Chrome, or a newer version (if available).");
}

function connectWs() {
    if (window.ws && window.ws.readyState == WebSocket.OPEN) {
        return;
    }

    window.ws = new WebSocket("wss://"+location.host+"/stream/view");

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
var thumbsSize = 0;
var pauseScore = false;

var dummyThumb = document.createElement('img');
dummyThumb.setAttribute('src', '/img/thumbs.png');

function updatePositions(positions) {
    var votesContainer = document.getElementById('votes');
    var score = 0;

    var bodyWidth = document.getElementsByTagName('body')[0].offsetWidth;
    var innerWidth = document.getElementById('frame').offsetWidth;
    var startPadding = (bodyWidth - innerWidth) / 2;

    for (var i = 0; i < positions.length; i++) {
        var position = positions[i];
        var record = thumbs[position.uid];
        //console.log(position);
        if(record == undefined)
        {
            // new
            var vote = dummyThumb.cloneNode();

            if(thumbsSize > 12)
            {
                vote.style.left = (Math.random() * bodyWidth) + "px";
            }
            else if(thumbsSize > 5)
            {
                vote.style.left = (startPadding + (Math.random() * innerWidth)) + "px";
            }
            else
            {
                vote.style.left = (startPadding + ((thumbsSize / 5) * innerWidth)) + "px";
            }

            votesContainer.appendChild(vote);

            record = {
                element: vote
            };

            thumbsSize++;
            thumbs[position.uid] = record;
        }

        if(position.beta > 90 || position.beta < -90)
        {
            position.alpha = -position.alpha;
        }

        // update rotation
        //record.element.style.transform = 'rotateZ(' + -position.alpha + 'deg) rotateY(' + -position.gamma + 'deg) rotateX(' + (-position.beta + 90) + 'deg)';
        //record.element.style.transform = 'rotateZ(' + -position.alpha + 'deg) rotateY(' + -position.gamma + 'deg)';
        record.element.style.transform = 'rotateZ(' + -position.alpha + 'deg)';

        if(!pauseScore)
        {
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
    }

    if(!pauseScore && positions.length > 0)
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