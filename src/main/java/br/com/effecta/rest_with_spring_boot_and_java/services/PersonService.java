package br.com.effecta.rest_with_spring_boot_and_java.services;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.effecta.rest_with_spring_boot_and_java.exceptions.ResourceNotFoundException;
import br.com.effecta.rest_with_spring_boot_and_java.model.Person;
import br.com.effecta.rest_with_spring_boot_and_java.repositories.PersonRepository;

@Service
public class PersonService {

    @Autowired
    private PersonRepository repository;

    public List<Person> findAll() {
        return repository.findAll();
    }

    public Person findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found with ID: " + id));
    }

    public Person create(Person person) {
        return repository.save(person);
    }

    public Person update(Long id, Person person) {
        Person entity = findById(id);
        if (person.getFirstName() != null)
            entity.setFirstName(person.getFirstName());
        if (person.getLastName() != null)
            entity.setLastName(person.getLastName());
        if (person.getAddress() != null)
            entity.setAddress(person.getAddress());
        if (person.getGender() != null)
            entity.setGender(person.getGender());
        return repository.save(entity);
    }

    public void delete(Long id) {
        findById(id);
        repository.deleteById(id);
    }

}
