package cn.scut.webchat.webUtil;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class HandShakeMsg {
    private String type;
    private String name;
}
