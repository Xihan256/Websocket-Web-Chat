package cn.scut.webchat.services;

import cn.scut.webchat.DAO.UserDAO;
import cn.scut.webchat.PO.ChatRecord;
import cn.scut.webchat.PO.User;
import cn.scut.webchat.exceptions.InvalidInformationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RegistryAndLoginService {

    private final UserDAO dao;

    @Autowired
    public RegistryAndLoginService(UserDAO dao) {
        this.dao = dao;
    }

    public void registry(String name , String pwd) throws InvalidInformationException {
        String nameRegex = "^[A-Za-z]{3,10}$";
        Pattern namePattern = Pattern.compile(nameRegex);

        String pwdRegex = "^[A-Za-z0-9]{5,10}$";
        Pattern pwdPattern = Pattern.compile(pwdRegex);

        Matcher nameMatcher = namePattern.matcher(name);
        Matcher pwdMatcher = pwdPattern.matcher(pwd);

        if(nameMatcher.matches() && pwdMatcher.matches()){
            dao.addUser(name, pwd);
        }

        if(!nameMatcher.matches()){
            throw new InvalidInformationException("账号不合规,需要3到10位数字或字母,");
        }

        if(!pwdMatcher.matches()){
            throw new InvalidInformationException("密码不合规,需要5到10位数字或字母,");
        }
    }

    public boolean login(String name, String password) {
        Integer count = dao.getNumOfUser(name);

        if (count != 0) {
            ArrayList<String> pwds = dao.getUsersByName(name);

            for (String s : pwds) {
                if (s.equals(password)) {
                    return true;
                }
            }
        }
        return false;

    }

    public void addChatRecord(String fromName, String toName, String content) {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        dao.addChatRecord(fromName,toName,content,time);

    }

    public ArrayList<ChatRecord> get100ChatRecord(String myName, String otherName) {
        return dao.get100Record(myName,otherName);
    }
}
