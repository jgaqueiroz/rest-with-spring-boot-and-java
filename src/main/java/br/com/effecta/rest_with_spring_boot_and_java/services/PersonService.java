package br.com.effecta.rest_with_spring_boot_and_java.services;

import static br.com.effecta.rest_with_spring_boot_and_java.mapper.ObjectMapper.parseListObjects;
import static br.com.effecta.rest_with_spring_boot_and_java.mapper.ObjectMapper.parseObject;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.effecta.rest_with_spring_boot_and_java.data.dto.v1.PersonDTO;
import br.com.effecta.rest_with_spring_boot_and_java.data.dto.v2.PersonDTOv2;
import br.com.effecta.rest_with_spring_boot_and_java.exceptions.ResourceNotFoundException;
import br.com.effecta.rest_with_spring_boot_and_java.mapper.custom.PersonMapper;
import br.com.effecta.rest_with_spring_boot_and_java.model.Person;
import br.com.effecta.rest_with_spring_boot_and_java.repositories.PersonRepository;

@Service
public class PersonService {

    private Logger logger = LoggerFactory.getLogger(PersonService.class.getName());

    @Autowired
    private PersonRepository repository;

    @Autowired
    private PersonMapper converter;

    public List<PersonDTO> findAll() {
        logger.info("Finding all People!");

        return parseListObjects(repository.findAll(), PersonDTO.class);
    }

    public PersonDTO findById(Long id) {
        logger.info("Finding one Person!");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found with ID: " + id));
        return parseObject(entity, PersonDTO.class);
    }

    public PersonDTO create(PersonDTO person) {
        logger.info("Creating one Person!");

        var entity = parseObject(person, Person.class);
        return parseObject(repository.save(entity), PersonDTO.class);
    }

    public PersonDTOv2 createV2(PersonDTOv2 person) {
        logger.info("Creating one Person V2!");

        var entity = converter.DTOv2ToPerson(person);
        return converter.PersonToDTOv2(repository.save(entity));
    }

    public PersonDTO update(Long id, PersonDTO person) {
        logger.info("Updating one Person!");

        Person entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found with ID: " + id));
        if (person.getFirstName() != null)
            entity.setFirstName(person.getFirstName());
        if (person.getLastName() != null)
            entity.setLastName(person.getLastName());
        if (person.getAddress() != null)
            entity.setAddress(person.getAddress());
        if (person.getGender() != null)
            entity.setGender(person.getGender());
        return parseObject(repository.save(entity), PersonDTO.class);
    }

    public void delete(Long id) {
        logger.info("Deleting one Person!");

        findById(id);
        repository.deleteById(id);
    }

}
