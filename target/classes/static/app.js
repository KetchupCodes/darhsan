var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/greetings', function (greeting) {
            showGreeting(JSON.parse(greeting.body).num1);
        });
       
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    stompClient.send("/app/hello", {}, JSON.stringify({'num1': $("#num1").val(),'num2':$("#num2").val()}));
}

function showGreeting(message) {
    $("#result").append("<tr><td>" + message + "</td></tr>");
}
function showResults(message) {
    stompClient.send("/app/results", {}, JSON.stringify({'num1': $("#num1").val(),'num2':$("#num2").val()}));
  
}
$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#showResults").click(function () { showResults(); });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
});

