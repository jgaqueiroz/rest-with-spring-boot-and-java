package br.com.effecta.rest_with_spring_boot_and_java.services;

import static br.com.effecta.rest_with_spring_boot_and_java.mapper.ObjectMapper.parseObject;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.effecta.rest_with_spring_boot_and_java.controllers.PersonController;
import br.com.effecta.rest_with_spring_boot_and_java.data.dto.PersonDTO;
import br.com.effecta.rest_with_spring_boot_and_java.exceptions.BadRequestException;
import br.com.effecta.rest_with_spring_boot_and_java.exceptions.FileStorageException;
import br.com.effecta.rest_with_spring_boot_and_java.exceptions.RequiredObjectIsNullException;
import br.com.effecta.rest_with_spring_boot_and_java.exceptions.ResourceNotFoundException;
import br.com.effecta.rest_with_spring_boot_and_java.file.importer.contract.FileImporter;
import br.com.effecta.rest_with_spring_boot_and_java.file.importer.factory.FileImporterFactory;
import br.com.effecta.rest_with_spring_boot_and_java.model.Person;
import br.com.effecta.rest_with_spring_boot_and_java.repositories.PersonRepository;
import jakarta.transaction.Transactional;

@Service
public class PersonService {

    private Logger logger = LoggerFactory.getLogger(PersonService.class.getName());

    @Autowired
    private PersonRepository repository;
    
    @Autowired
    private FileImporterFactory importer;

    @Autowired
    PagedResourcesAssembler<PersonDTO> assembler;

    public PagedModel<EntityModel<PersonDTO>> findAll(Pageable pageable) {
        logger.info("Finding all People!");

        var people = repository.findAll(pageable);
        return buildPagedModel(pageable, people);
    }
    
    public PagedModel<EntityModel<PersonDTO>> findByName(String firstName, Pageable pageable) {
        logger.info("Finding People by name!");

        var people = repository.findPeopleByName(firstName, pageable);
        return buildPagedModel(pageable, people);
    }

    public PersonDTO findById(Long id) {
        logger.info("Finding one Person!");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found with ID: " + id));
        var dto = parseObject(entity, PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public PersonDTO create(PersonDTO person) {
        if (person == null) throw new RequiredObjectIsNullException();

        logger.info("Creating one Person!");

        var entity = parseObject(person, Person.class);
        var dto = parseObject(repository.save(entity), PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public List<PersonDTO> createWithFile(MultipartFile file) {
        logger.info("Importing People from file!");

        if (file.isEmpty()) throw new BadRequestException("Please set a Valid file!");

        try (InputStream inputStream = file.getInputStream()) {
            String fileName = Optional.ofNullable(file.getOriginalFilename())
                .orElseThrow(() -> new BadRequestException("File name cannot be null"));
            FileImporter importer = this.importer.getImporter(fileName);

            List<Person> entities = importer.importFile(inputStream).stream()
                .map(dto -> repository.save(parseObject(dto, Person.class)))
                .toList();

            return entities.stream()
                .map(entity -> {
                    var dto = parseObject(entity, PersonDTO.class);
                    addHateoasLinks(dto);
                    return dto;
                }).toList();
        } catch (Exception e) {
            throw new FileStorageException("Error processing the file! ");
        }
    }

    public PersonDTO update(Long id, PersonDTO person) {
        if (person == null) throw new RequiredObjectIsNullException();
        
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
        var dto = parseObject(repository.save(entity), PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    @Transactional
    public PersonDTO disablePerson(Long id) {
        logger.info("Disabling one Person!");

        findById(id);
        repository.disablePerson(id);

        var entity = repository.findById(id).get();
        var dto = parseObject(entity, PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public void delete(Long id) {
        logger.info("Deleting one Person!");

        findById(id);
        repository.deleteById(id);
    }

    private PagedModel<EntityModel<PersonDTO>> buildPagedModel(Pageable pageable, Page<Person> people) {
        var peopleWithLinks = people.map(person -> {
            var dto = parseObject(repository.save(person), PersonDTO.class);
            addHateoasLinks(dto);
            return dto;
        });

        Link findAllLink = WebMvcLinkBuilder.linkTo(
            WebMvcLinkBuilder.methodOn(PersonController.class)
                .findAll(
                    pageable.getPageNumber(), 
                    pageable.getPageSize(), 
                    String.valueOf(pageable.getSort())))
                .withSelfRel();

        return assembler.toModel(peopleWithLinks, findAllLink);
    }

    private void addHateoasLinks(PersonDTO dto) {
        dto.add(linkTo(methodOn(PersonController.class).findAll(1, 12, "asc")).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).findByName("", 1, 12, "asc")).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(PersonController.class).update(dto.getId(), dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(PersonController.class).disablePerson(dto.getId())).withRel("disable").withType("PATCH"));
        dto.add(linkTo(methodOn(PersonController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
    }

}
