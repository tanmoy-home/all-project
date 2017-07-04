package com.scope;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
 
public class Singleton implements ApplicationContextAware {
 
 private Prototype prototype;
 
 private ApplicationContext context;
 
 public Prototype getPrototype() {
 return context.getBean("prototype", Prototype.class);
 }
 
 public void setPrototype(Prototype prototype) {
 this.prototype = prototype;
 }
 
 @Override
 public void setApplicationContext(ApplicationContext context) throws BeansException {
 
 this.context = context;
 
 }
 
 
 String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
