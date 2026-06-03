package com.example.stumanage.controller;

import com.example.stumanage.model.Person;
import com.example.stumanage.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/person")
@CrossOrigin(origins = "*")
public class PersonController {

    @Autowired
    private PersonService personService;

    //查找个人信息
    @GetMapping("/{id}")
    public Person getPerson(@PathVariable Integer id){
        return personService.getPersonById(id);
    }

    //更新个人信息
    @PutMapping("/update")
    public Person updatePerson(@RequestBody Person person){
        return personService.updatePerson(person);
    }
}
