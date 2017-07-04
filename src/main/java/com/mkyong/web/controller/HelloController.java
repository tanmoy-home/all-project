package com.mkyong.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
/*
 * Instruction to run the test:
 * 
 * terminal#$> mvn jetty:run 
 * URL: http://localhost:8080/spring3
 * Ref: http://www.mkyong.com/spring3/spring-3-mvc-hello-world-example/
 * 
 */
@Controller
public class HelloController {

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String printWelcome(ModelMap model) {

		model.addAttribute("message", "Spring 3 MVC Hello World");
		return "hello";

	}

	/*@RequestMapping(value = "/hello/{name:.+}", method = RequestMethod.GET) :.+ is used for regex */ 
	@RequestMapping(value = "/hello/{name}", method = RequestMethod.GET)
	public ModelAndView hello(@PathVariable("name") String name) {

		ModelAndView model = new ModelAndView();
		model.setViewName("hello");
		model.addObject("msg", name);

		return model;

	}

}