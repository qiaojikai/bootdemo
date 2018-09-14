package com.example.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.common.remote.HelloWorldService;

@RunWith(SpringRunner.class)
@SpringBootTest
//相当于  --spring.profiles.active=dev
@ActiveProfiles(value="dev")
public class RemoteTest {
	
	@Autowired
	private HelloWorldService helloWorldService;
	
	@Test
	public void remoteTest() {
		String str = "helloWorld";
		helloWorldService.sayHello(str);
	}
}
