package cn.scut.webchat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.socket.TextMessage;

@SpringBootTest
class WebChatApplicationTests {

	@Test
	void contextLoads() {
		TextMessage msg = new TextMessage("fail");
		System.out.println(msg);
	}

}
