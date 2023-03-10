package com.dailycodebuffer.springbootmongodb.controller;

import com.dailycodebuffer.springbootmongodb.dto.PersonDto;
import com.dailycodebuffer.springbootmongodb.model.Person;
import com.dailycodebuffer.springbootmongodb.service.PersonService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {
    @Autowired
    PersonService personService;

    @PostMapping
    public String addPerson(@RequestBody PersonDto personDto){
        return personService.save(personDto);
    }

    @GetMapping
    List<Person> getPerson(@RequestParam String name){
        return personService.getPerson(name);
    }

    @DeleteMapping("/{id}")
    public String DeleteUserById(@PathVariable("id") String id){
        return personService.DeleteUserById(id);
    }

    @GetMapping("/age")
    public List<Person> getPersonBetweenAge(@RequestParam int minAge,
                                            @RequestParam int maxAge){
//        return new ArrayList<Person>();
        return personService.getPersonBetweenAge(minAge,maxAge);
    }

    @GetMapping("/search")
    public Page<Person> searchPerson(@RequestParam(required = false) String name,
                                     @RequestParam(required = false) Integer minAge,
                                     @RequestParam(required = false) Integer maxAge,
                                     @RequestParam(required = false) String city,
                                     @RequestParam(defaultValue = "0") Integer page,
                                     @RequestParam(defaultValue = "5") Integer size){
        Pageable pageable=PageRequest.of(page,size);
        return personService.searchPerson(name,minAge,maxAge,city,pageable);
    }

    @GetMapping("/oldestPerson")
    public List<Document> getOldestPersonByCity(){
        return personService.getOldestPersonByCity();
    }

    @GetMapping("/populationByCity")
    public List<Document> getPopulationByCity(){
        return personService.getPopulationByCity();
    }
}
