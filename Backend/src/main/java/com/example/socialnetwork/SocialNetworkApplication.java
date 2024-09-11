package com.example.socialnetwork;

import com.example.socialnetwork.config.TokenProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SocialNetworkApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocialNetworkApplication.class, args);

		// Tạo một instance của BCryptPasswordEncoder
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		// Mã hóa một chuỗi
		String originalPassword = "123456";
		String encodedPassword = encoder.encode(originalPassword);

		// In chuỗi đã mã hóa ra màn hình console
		System.out.println("Original Password: " + originalPassword);
		System.out.println("Encoded Password: " + encodedPassword);
	}

}
