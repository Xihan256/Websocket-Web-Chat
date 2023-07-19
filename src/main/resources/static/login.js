const socket = new WebSocket('ws://8.134.143.81:11500/ws');
const stompClient = Stomp.over(socket);

socket.onmessage = function (message) {
    const json = message.data;
    const data = JSON.parse(json);
    switch (data.type){
        case "register":
            processRegister(data.status);
            break;
        case "login":
            processLogin(data.status);
            break;
    }

}
function processLogin(status){
    switch (status){
        case "success":
            localStorage.setItem("name",document.getElementById('username').value);
            const targetPageUrl = './main.html';
            window.location.href = targetPageUrl;
            break;
        case "invalid":
            const paragraph = document.getElementById("registrationStatus");
            paragraph.innerText = "账号或密码错误";
            break;
        case "fail":
            const paragraph1 = document.getElementById("registrationStatus");
            paragraph1.innerText = "不要传递空参数";
            break;
        default:
            break;
    }
}

function processRegister(status){
    switch (status){
        case "success":
            localStorage.setItem("name",document.getElementById('username').value);
            const targetPageUrl = './main.html';
            window.location.href = targetPageUrl;
            break;
        case "wrongInfo":
            const paragraph = document.getElementById("registrationStatus");
            paragraph.innerText = "账号或密码不合理";
            break;
        case "fail":
            const paragraph1 = document.getElementById("registrationStatus");
            paragraph1.innerText = "不要传递空参数";
            break;
        default:
            break;
    }
    console.log('Registration Status:', status);
}

function registerUser() {
    event.preventDefault();
    const name = document.getElementById('username').value;
    const pwd = document.getElementById('password').value;

    const registrationRequest = {
        type: 'registration',
        username: name,
        password: pwd

    };
    const msg = JSON.stringify(registrationRequest);
    stompClient.send(msg);
}

function login() {
    event.preventDefault();
    const name = document.getElementById('username').value;
    const pwd = document.getElementById('password').value;

    const loginRequest = {
        type: 'login',
        username: name,
        password: pwd

    };
    const msg = JSON.stringify(loginRequest);
    stompClient.send(msg);
}