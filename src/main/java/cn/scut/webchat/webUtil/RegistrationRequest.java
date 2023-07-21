package cn.scut.webchat.webUtil;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class RegistrationRequest {
    private String username;
    private String password;
}
