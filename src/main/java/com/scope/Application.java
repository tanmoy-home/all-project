package com.scope;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
 
public class Application {
 public static final Logger LOG = LoggerFactory.getLogger(Application.class);
 
 public static void main(String[] args) {
 
 ApplicationContext context = new GenericXmlApplicationContext("application-context.xml");
 
 /*Singleton singleton = context.getBean("singleton", Singleton.class);
 LOG.debug("singleton bean message = "+singleton.getMessage());
 
 singleton.setMessage("new message");
 
 Singleton singleton1 = context.getBean("singleton", Singleton.class);
 LOG.debug("singleton1 bean message = "+singleton1.getMessage());

 Prototype prototype1 = singleton.getPrototype();
 LOG.debug("prototype1 bean message = "+prototype1.getMessage());
 
 prototype1.setMessage("new prototype");
 
 Prototype prototype2 = singleton.getPrototype();
 LOG.debug("prototype2 bean message = "+prototype2.getMessage());
 
 LOG.debug((prototype1 == prototype2) ? "Same Instance" : "Different Instances");*/
 
 /*Prototype prototype = context.getBean("prototype", Prototype.class);
 LOG.debug("prototype bean message = "+prototype.getMessage());
 prototype.setMessage("new prototype");
 Prototype new_prototype = context.getBean("prototype", Prototype.class);
 LOG.debug("new_prototype bean message = "+new_prototype.getMessage());
 
 Singleton singleton = context.getBean("singleton", Singleton.class);
 LOG.debug("singleton bean message = "+singleton.getMessage());
 singleton.setMessage("new singleton");
 Singleton new_singleton = context.getBean("singleton", Singleton.class);
 LOG.debug("new_singleton bean message = "+new_singleton.getMessage());*/
 
 ((AbstractApplicationContext) context).close();
 }
}
