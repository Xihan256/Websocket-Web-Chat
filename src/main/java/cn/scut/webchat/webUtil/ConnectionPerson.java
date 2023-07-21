package cn.scut.webchat.webUtil;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ConnectionPerson {
    private String sessionId;
    private String userName;
}
