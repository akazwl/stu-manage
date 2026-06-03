package com.example.stumanage.repository;

import com.example.stumanage.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PersonRepository extends JpaRepository<Person, Integer>{

}
