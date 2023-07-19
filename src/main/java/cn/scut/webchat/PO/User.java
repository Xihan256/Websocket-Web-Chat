package cn.scut.webchat.PO;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Data
public class User {
    private Integer id;
    private String name;
    private String password;
}
