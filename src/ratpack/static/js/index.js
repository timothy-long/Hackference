document.getElementById("join-room-form").onsubmit = function() {
    location = "/vote/" + this.test.value.toUpperCase();
    return false;
};