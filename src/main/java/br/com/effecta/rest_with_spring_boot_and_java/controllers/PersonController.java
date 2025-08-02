package br.com.effecta.rest_with_spring_boot_and_java.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.effecta.rest_with_spring_boot_and_java.controllers.docs.PersonControllerDocs;
import br.com.effecta.rest_with_spring_boot_and_java.data.dto.PersonDTO;
import br.com.effecta.rest_with_spring_boot_and_java.services.PersonService;
import io.swagger.v3.oas.annotations.tags.Tag;

//@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/person/v1")
@Tag(name = "People", description = "Endpoints for Managing People")
public class PersonController implements PersonControllerDocs {

    @Autowired
    private PersonService service;

    @Override
    @GetMapping(
        produces = { 
            MediaType.APPLICATION_JSON_VALUE, 
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_YAML_VALUE 
        })
    public List<PersonDTO> findAll() {
        return service.findAll();
    }

    //@CrossOrigin(origins = "http://localhost:8080")
    @Override
    @GetMapping(value = "/{id}", 
        produces = { 
            MediaType.APPLICATION_JSON_VALUE, 
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_YAML_VALUE 
        })
    public PersonDTO findById(@PathVariable Long id) {
        return service.findById(id);
    }

    //@CrossOrigin(origins = {"http://localhost:8080", "https://effecta.com.br"})
    @Override
    @PostMapping(
        consumes = { 
            MediaType.APPLICATION_JSON_VALUE, 
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_YAML_VALUE 
        }, 
        produces = {
            MediaType.APPLICATION_JSON_VALUE, 
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_YAML_VALUE 
        })
    public PersonDTO create(@RequestBody PersonDTO person) {
        return service.create(person);
    }

    @Override
    @PutMapping(value = "/{id}", 
        consumes = { 
            MediaType.APPLICATION_JSON_VALUE, 
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_YAML_VALUE 
        }, 
        produces = { 
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE, 
            MediaType.APPLICATION_YAML_VALUE 
        })
    public PersonDTO update(@PathVariable Long id, @RequestBody PersonDTO person) {
        return service.update(id, person);
    }

    @Override
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping(value = "/{id}", 
        produces = { 
            MediaType.APPLICATION_JSON_VALUE, 
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_YAML_VALUE 
        })
    public PersonDTO disablePerson(@PathVariable("id") Long id) {
        return service.disablePerson(id);
    }

}
