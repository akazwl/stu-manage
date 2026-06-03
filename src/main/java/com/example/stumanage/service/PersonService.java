package com.example.stumanage.service;

import com.example.stumanage.model.Person;
import com.example.stumanage.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    public Person getPersonById(Integer id){
        return personRepository.findById(id).orElse(null);
    }

    public List<Person> getAllPersons(){
        return personRepository.findAll();
    }

    public Person updatePerson(Person person){
        if(person.getId() == null){
            throw new IllegalArgumentException("请提供人员ID");
        }
        return personRepository.save(person);
    }

    public void deletePerson(Integer id){
        personRepository.deleteById(id);
    }
}

