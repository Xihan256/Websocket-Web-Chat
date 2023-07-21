package cn.scut.webchat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan(basePackages = "cn.scut.webchat")
public class WebChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebChatApplication.class, args);
	}

}
