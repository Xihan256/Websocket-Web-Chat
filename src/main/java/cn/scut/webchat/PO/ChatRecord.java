package cn.scut.webchat.PO;

import lombok.*;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Data
public class ChatRecord {
    private String fromName;
    private String toName;
    private String content;
    private Timestamp sendTime;
}
