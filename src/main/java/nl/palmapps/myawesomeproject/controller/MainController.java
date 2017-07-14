package nl.palmapps.myawesomeproject.controller;

import java.security.Principal;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nl.palmapps.myawesomeproject.model.Greeting;
import nl.palmapps.myawesomeproject.model.Treating;

/**
 * Example controller to test security calls
 */
@RestController
@RequestMapping(value = "/JWT-Test")
public class MainController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();


    @RequestMapping(value = "/treating", method = RequestMethod.POST)
    public Treating treating(@RequestBody Treating treating) {

    	/*Treating treating = new Treating();
    	treating.setAddress("address");
    	treating.setAge(22L);
    	treating.setEid("eid");
    	treating.setName("name");
    	treating.setStatus("status");*/
    	return treating;
    }
    
    
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {

        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Greeting homePage(@RequestParam(value = "name", defaultValue = "World") String name) {

        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }

    @RequestMapping(value = {"/user", "/me"}, method = RequestMethod.POST)
    public ResponseEntity<?> user(Principal principal) {
        return ResponseEntity.ok(principal);
    }
}