package cn.scut.webchat.DAO;

import cn.scut.webchat.PO.ChatRecord;
import cn.scut.webchat.PO.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;

@Repository
public interface UserDAO {

    @Insert("""
            insert into web_chat.user(name,password)
            values
            (#{name},#{pwd});
            """)
    void addUser(String name, String pwd);


    @Select("""
            select count(*) from web_chat.user
            where name = #{name};
            """)
    Integer getNumOfUser(String name);


    @Select("""
            select web_chat.user.password from web_chat.user
            where name = #{name};
            """)
    @ResultType(ArrayList.class)
    ArrayList<String> getUsersByName(String name);

    @Insert("""
            insert into web_chat.record(from_name, to_name, content, send_time)
            values
            (#{fromName},#{toName},#{content},#{time});
            """)
    void addChatRecord(String fromName, String toName, String content, Timestamp time);

    @Select("""
            select *
            from web_chat.record
            where (from_name = #{myName} OR from_name = #{otherName})
             AND (to_name = #{myName} OR to_name = #{otherName})
            order by send_time
            limit 100;
            """)
    @ResultType(ArrayList.class)
    ArrayList<ChatRecord> get100Record(String myName, String otherName);

}
