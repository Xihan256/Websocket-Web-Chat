const chatArea = document.getElementById('chat-area');
const name = localStorage.getItem("name");
const sendBtn = document.getElementById("send-button");
const messageInput = document.getElementById('message-input');

const socket = new WebSocket('ws://8.134.143.81:11500/ws');
const stompClient = Stomp.over(socket);

socket.onopen = function (event){
    setTimeout(function () {
        const helloMsg = {
            type: 'handShake',
            username: name
        };
        const msg = JSON.stringify(helloMsg);
        stompClient.send(msg);
    } , 1000)
}

function removeFriendToSidebar(name) {
    const friendListContainer = document.querySelector('.friend-list-container');
    const friendItems = friendListContainer.querySelectorAll('.friend-item');

    friendItems.forEach(item => {
        if (item.innerHTML === name) {
            item.remove();
        }
    });
}

socket.onmessage = function (message) {
    const json = message.data;
    const data = JSON.parse(json);
    switch (data.type){
        case "hello":
            addFriendToSidebar(data.name);
            break;
        case "online":
            const nameArray = data.names;
            nameArray.forEach(item => {
                if(!(item === name)){
                    addFriendToSidebar(item);
                }
            });
            break;
        case "update":
            const friendName = document.getElementById('selected-friend').innerText;
            if(friendName === data.fromName){
                fetchAllChatRecord(name,data.fromName);
            }else{
                alert(data.fromName + "来消息啦！");
            }
            break;
        case "record":
            const chatArray = data.records;
            chatArray.forEach(item => {
                if(item.fromName === name){
                    addMyMessage(item.content);
                }else if(item.fromName === document.getElementById('selected-friend').innerText){
                    addFriendMessage(item.content);
                }
            });
            break;
        case "leave":
            removeFriendToSidebar(data.name);
            break;
        default:
            break;
    }

}

function addFriendMessage( message) {
    const messageDiv = document.createElement('div');
    messageDiv.classList.add('message');
    messageDiv.textContent = message;
    chatArea.appendChild(messageDiv);
    scrollToBottom();
}

function addMyMessage(message) {
    const messageDiv = document.createElement('div');
    messageDiv.classList.add('message', 'my-message');
    messageDiv.textContent = message;
    chatArea.appendChild(messageDiv);
    scrollToBottom();
}

function scrollToBottom() {
    chatArea.scrollTop = chatArea.scrollHeight;
}

function addFriendToSidebar(friendName) {
    const friendListContainer = document.querySelector('.friend-list-container');

    // 创建一个新的好友项
    const friendItem = document.createElement('div');
    friendItem.classList.add('friend-item');
    friendItem.textContent = friendName;
    friendItem.onclick = function() {
        showFriendName(friendName);
    };

    friendListContainer.appendChild(friendItem);
}

function showFriendName(friendName) {
    // 将好友的名字显示在顶部
    const friendNameElement = document.getElementById('selected-friend');
    friendNameElement.innerText = friendName;
    fetchAllChatRecord(name,friendName);
}

sendBtn.onclick = function() {
    const message = messageInput.value;
    const speakMsg = {
        type: 'message',
        content: message,
        fromName: name,
        toName: document.getElementById('selected-friend').innerText

    };
    const msg = JSON.stringify(speakMsg);
    stompClient.send(msg);
    // 清空输入框内容
    messageInput.value = '';
    addMyMessage(message);
}

function fetchAllChatRecord(myName,otherName){
    const chatArea = document.getElementById("chat-area");
    chatArea.innerHTML = '';
    const fetchInfo = {
        type: 'fetch',
        myName: name,
        otherName: document.getElementById('selected-friend').innerText

    };
    const msg = JSON.stringify(fetchInfo);
    stompClient.send(msg);
}

window.addEventListener('beforeunload', function (event) {
    const leaveInfo = {
        type: 'leave',
        myName: name

    };
    const msg = JSON.stringify(leaveInfo);
    stompClient.send(msg);
    socket.close();
});
