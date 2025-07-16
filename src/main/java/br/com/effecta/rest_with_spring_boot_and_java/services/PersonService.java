package br.com.effecta.rest_with_spring_boot_and_java.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import br.com.effecta.rest_with_spring_boot_and_java.model.Person;

@Service
public class PersonService {

    private final AtomicLong counter = new AtomicLong();

    public List<Person> findAll() {
        List<Person> list = new ArrayList<>();
        for (int i = 0; i <= 9; i++) {
            list.add(findById(String.valueOf(i)));
        }
        return list;
    }

    public Person findById(String id) {
        Person person = new Person();
        person.setId(counter.incrementAndGet());
        person.setFirstName("José");
        person.setLastName("Queiroz");
        person.setAddress("Boa Viagem - Recife/PE");
        person.setGender("Male");
        return person;
    }

}
