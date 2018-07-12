package com.example.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.common.utils.xml.DOM4JXml;

@RunWith(SpringRunner.class)
@SpringBootTest
public class XmlTest {
	@Test
	public void contextLoads() {
		DOM4JXml.analysisXml();
	}
}
