package br.com.effecta.rest_with_spring_boot_and_java.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.effecta.rest_with_spring_boot_and_java.controllers.docs.BookControllerDocs;
import br.com.effecta.rest_with_spring_boot_and_java.data.dto.BookDTO;
import br.com.effecta.rest_with_spring_boot_and_java.services.BookService;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/book/v1")
@Tag(name = "Books", description = "Endpoints for Managing Books")
public class BookController implements BookControllerDocs {

    @Autowired
    private BookService service;

    @Override
    @GetMapping(
        produces = { 
            MediaType.APPLICATION_JSON_VALUE, 
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_YAML_VALUE 
        })
    public List<BookDTO> findAll() {
        return service.findAll();
    }

    @Override
    @GetMapping(value = "/{id}", 
        produces = { 
            MediaType.APPLICATION_JSON_VALUE, 
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_YAML_VALUE 
        })
    public BookDTO findById(@PathVariable Long id) {
        return service.findById(id);
    }
    
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
    public BookDTO create(@RequestBody BookDTO book) {
        return service.create(book);
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
    public BookDTO update(@PathVariable Long id, @RequestBody BookDTO book) {
        return service.update(id, book);
    }

    @Override
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
    
}
