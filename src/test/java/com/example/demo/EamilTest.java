package com.example.demo;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.common.utils.emial.EmailUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EamilTest {
	
	@Test
	public void contextLoads() {
		try {
			EmailUtil.sendMail("280843837@qq.com", "我是乔纪凯", "老婆我爱你");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
