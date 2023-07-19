package cn.scut.webchat.webUtil;

import java.util.ArrayList;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class OnLineList {
    private String type;
    private ArrayList<String>names;
}
