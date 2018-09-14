//package com.example.common.remote.rmi;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.remoting.rmi.RmiProxyFactoryBean;
//import org.springframework.stereotype.Component;
//
//import com.example.common.remote.HelloWorldService;
//
//@Component
//public class RmiFactory {
//	
//	@Bean
//	public RmiProxyFactoryBean helloWorldService(){
//		RmiProxyFactoryBean rmiProxy = new RmiProxyFactoryBean();
//		rmiProxy.setServiceUrl("rmi://localhost/HelloWorldService");
//		rmiProxy.setServiceInterface(HelloWorldService.class);
//		return rmiProxy;
//	}
//}
