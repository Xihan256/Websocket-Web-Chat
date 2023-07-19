package cn.scut.webchat.websocket;

import cn.scut.webchat.PO.ChatRecord;
import cn.scut.webchat.exceptions.InvalidInformationException;
import cn.scut.webchat.services.RegistryAndLoginService;
import cn.scut.webchat.webUtil.*;
import com.alibaba.fastjson2.JSON;
import jakarta.websocket.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
public class MyWebSocketHandler extends TextWebSocketHandler {
    @Autowired
    private WebSocketManager webSocketManager;
    @Autowired
    private RegistryAndLoginService rlService;

    HashMap<String , WebSocketSession>sessionsMap;

    public MyWebSocketHandler() {
        sessionsMap = new HashMap<>();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        session.sendMessage(new TextMessage("你好"));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        webSocketManager.removeSession(session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();

        if(!payload.contains("{")){
            System.out.println("这并不是一个有效的json");
            return;
        }

        try {
            payload = payload.substring(payload.indexOf("{"),payload.indexOf("}") + 1);
        } catch (Exception e) {
            System.out.println("json解析崩了");
        }
        Map<String, String> messageMap = JSON.parseObject(payload,Map.class);
        String messageType = messageMap.get("type");

        switch (messageType){
            case "registration": {
                String username = messageMap.get("username");
                String password = messageMap.get("password");
                RegistrationResponse response = this.registerUser(username, password, session);
                System.out.println(session.getId() + "第二个");
                session.sendMessage(new TextMessage(JSON.toJSONString(response)));

                System.out.println("发完了");
                break;
            }
            case "login": {
                String username = messageMap.get("username");
                String password = messageMap.get("password");
                String res = this.userLogin(username,password,session);
                String login = JSON.toJSONString(new RegistrationResponse(res, "login"));
                session.sendMessage(new TextMessage(login));
                break;
            }
            case "handShake":{
                String username = messageMap.get("username");
                String sId = session.getId();
                webSocketManager.addSession(sId,username,session);
                HandShakeMsg msg = new HandShakeMsg("hello",username);

                webSocketManager.notifyAll(JSON.toJSONString(msg),username);

                ArrayList<String> allName = webSocketManager.getAllName();
                String online = JSON.toJSONString(new OnLineList("online", allName));
                session.sendMessage(new TextMessage(online));
                break;
            }
            case "message":{
                System.out.println("说话了");
                String content = messageMap.get("content");
                String fromName = messageMap.get("fromName");
                String toName = messageMap.get("toName");
                rlService.addChatRecord(fromName,toName,content);
                webSocketManager.notifyToReceive(toName,fromName);
                break;
            }
            case "fetch":{
                String myName = messageMap.get("myName");
                String otherName = messageMap.get("otherName");
                ArrayList<ChatRecord> chatRecord = rlService.get100ChatRecord(myName, otherName);
                ChatRecords records = new ChatRecords("record",chatRecord);
                String json = JSON.toJSONString(records);
                session.sendMessage(new TextMessage(json));
                break;
            }
            case "leave":{
                String myName = messageMap.get("myName");
                HandShakeMsg msg = new HandShakeMsg("leave",myName);

                webSocketManager.notifyAllLeave(JSON.toJSONString(msg),myName);
            }
            default:
                break;
        }
    }

    public String userLogin(String name, String password, WebSocketSession session){
        if(name != null && password != null){
            boolean isValid = rlService.login(name , password);
            return (isValid ? "success" : "invalid");
        }else{
            return "fail";
        }
    }

    public RegistrationResponse registerUser(String name, String password , WebSocketSession session) {
        if(name != null && password != null){
            try {
                rlService.registry(name , password);//core
            } catch (InvalidInformationException e) {
                return new RegistrationResponse("wrongInfo","register");
            }
            return new RegistrationResponse("success","register");
        }else{
            return new RegistrationResponse("fail","register");
        }

    }

    private boolean isHandshakeMessage(String message) {
        // 这里可以根据消息内容来判断是否为握手消息
        // 例如，判断是否包含某个特定的字段或格式
        return message.contains("handshake");
    }
}