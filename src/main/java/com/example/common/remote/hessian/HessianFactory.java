package com.example.common.remote.hessian;

import org.springframework.context.annotation.Bean;
import org.springframework.remoting.caucho.HessianProxyFactoryBean;
import org.springframework.stereotype.Component;

import com.example.common.remote.HelloWorldService;

@Component
public class HessianFactory {
	
	@Bean
	public HessianProxyFactoryBean helloWorldService(){
		HessianProxyFactoryBean proxyFactoryBean = new HessianProxyFactoryBean();
		proxyFactoryBean.setServiceUrl("http://localhost:8080/personone/helloServer");
		proxyFactoryBean.setServiceInterface(HelloWorldService.class);
		return proxyFactoryBean;
	}
}
