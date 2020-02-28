package com.cegeka.cnads.simpleproject;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
public class HelloController {

    private final HelloUserRepository helloUserRepository;

    public HelloController(HelloUserRepository helloUserRepository) {
        this.helloUserRepository = helloUserRepository;
    }

    @GetMapping("/hello/{name}")
    public String hello(@PathVariable("name") String name){
        helloUserRepository.save(new HelloUser(name));
        return "Hello "+name;
    }

    @GetMapping("/users")
    public Collection<String> getUsers(){
        return helloUserRepository.findAll().stream()
                .map(HelloUser::getUsername)
                .collect(Collectors.toList());
    }
}
