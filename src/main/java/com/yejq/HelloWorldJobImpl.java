package com.yejq;

import org.springframework.stereotype.Service;

@Service("helloWorldJob")
public class HelloWorldJobImpl implements HelloWorldJob {
	
	public void sayHello() {
		System.out.println("HelloWorldJobImpl is running...");
		if (5 >= 3) {
			throw new RuntimeException("this job has exceptions....");
		}
	}

}
