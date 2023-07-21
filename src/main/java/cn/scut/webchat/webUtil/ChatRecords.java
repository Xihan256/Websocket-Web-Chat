package cn.scut.webchat.webUtil;

import cn.scut.webchat.PO.ChatRecord;
import java.util.ArrayList;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ChatRecords {
    private String type;
    private ArrayList<ChatRecord>records;

}
