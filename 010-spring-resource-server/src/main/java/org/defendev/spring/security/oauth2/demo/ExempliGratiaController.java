package org.defendev.spring.security.oauth2.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;



@Controller
public class ExempliGratiaController {

    @RequestMapping(method = GET, path = "api/hello")
    public ResponseEntity<Map<String, String>> hello() {
        return ResponseEntity.ok(Map.of("hehe", "lololOO"));
    }

}
