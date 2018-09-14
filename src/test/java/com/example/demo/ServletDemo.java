package com.example.demo;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
//相当于  --spring.profiles.active=dev
@ActiveProfiles(value="dev")
public class ServletDemo extends HttpServlet {
	
     public void doGet(HttpServletRequest request, HttpServletResponse response)
             throws ServletException, IOException {
 
         response.setStatus(302);//设置服务器的响应状态码
         /**
17          *设置响应头，服务器通过 Location这个头，来告诉浏览器跳到哪里，这就是所谓的请求重定向
18          */
         response.setHeader("Location", "/JavaWeb_HttpProtocol_Study_20140528/1.jsp");
     }
     public void doPost(HttpServletRequest request, HttpServletResponse response)
             throws ServletException, IOException {
         this.doGet(request, response);
     }
     
     @Test
     public void test(){
    	 
     }
 }
