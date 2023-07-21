package cn.scut.webchat.websocket;

import cn.scut.webchat.webUtil.ConnectionPerson;
import cn.scut.webchat.webUtil.InformToUpdate;
import com.alibaba.fastjson2.JSON;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class WebSocketManager {
    private Map<ConnectionPerson, WebSocketSession> sessions;

    public WebSocketManager() {
        this.sessions = new HashMap<>();
    }

    public void addSession(String sessionId,String name, WebSocketSession session) {
        sessions.put(new ConnectionPerson(sessionId,name), session);
    }

    public void removeSession(String sessionId) {
        ConnectionPerson connectionPerson= null;
        Set<ConnectionPerson> keySet = sessions.keySet();
        for(ConnectionPerson c : keySet){
            if(c.getSessionId().equals(sessionId)){
               connectionPerson = c;
            }
        }

        sessions.remove(connectionPerson);
    }

    public WebSocketSession getSession(String sessionId) {
        Set<ConnectionPerson> keySet = sessions.keySet();
        for(ConnectionPerson c : keySet){
            if(c.getSessionId().equals(sessionId)){
                return sessions.get(c);
            }
        }
        return null;
    }

    public void setNameBySessionId(String sessionId , String name){
        Set<ConnectionPerson> keySet = sessions.keySet();
        for(ConnectionPerson c : keySet){
            if(c.getSessionId().equals(sessionId)){
                c.setUserName(name);
            }
        }
    }

    public void notifyAll(String msg ,String selfName) throws IOException {
        Set<Map.Entry<ConnectionPerson, WebSocketSession>> entrySet = sessions.entrySet();
        for (var e : entrySet){
            if(!e.getKey().getUserName().equals(selfName)) {
                e.getValue().sendMessage(new TextMessage(msg));
            }
        }
    }

    public void notifyToReceive(String toName, String fromName) throws IOException {
        Set<ConnectionPerson> keySet = sessions.keySet();
        for (ConnectionPerson p : keySet){
            if(p.getUserName().equals(toName)){
                String s = JSON.toJSONString(new InformToUpdate("update", fromName));
                sessions.get(p).sendMessage(new TextMessage(s));
            }
        }
    }

    public void notifyAllLeave(String msg , String selfName) throws IOException {
        Set<Map.Entry<ConnectionPerson, WebSocketSession>> entrySet = sessions.entrySet();
        for (var e : entrySet){
            if(!e.getKey().getUserName().equals(selfName)){
                e.getValue().sendMessage(new TextMessage(msg));
            }
        }
    }

    public ArrayList<String> getAllName(){
        ArrayList<String> res = new ArrayList();
        Set<ConnectionPerson> keySet = sessions.keySet();
        for (ConnectionPerson p : keySet){
            res.add(p.getUserName());
        }

        return res;
    }

    public void notifyAllToReceive(String fromName) throws IOException {
        Set<ConnectionPerson> keySet = sessions.keySet();
        for (ConnectionPerson p : keySet){
            if(!p.getUserName().equals(fromName)){
                String s = JSON.toJSONString(new InformToUpdate("update", "group"));
                sessions.get(p).sendMessage(new TextMessage(s));
            }
        }
    }
}

