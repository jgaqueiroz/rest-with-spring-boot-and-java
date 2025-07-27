package br.com.effecta.rest_with_spring_boot_and_java.services;

import static br.com.effecta.rest_with_spring_boot_and_java.mapper.ObjectMapper.parseListObjects;
import static br.com.effecta.rest_with_spring_boot_and_java.mapper.ObjectMapper.parseObject;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Service;

import br.com.effecta.rest_with_spring_boot_and_java.controllers.BookController;
import br.com.effecta.rest_with_spring_boot_and_java.data.dto.BookDTO;
import br.com.effecta.rest_with_spring_boot_and_java.exceptions.RequiredObjectIsNullException;
import br.com.effecta.rest_with_spring_boot_and_java.exceptions.ResourceNotFoundException;
import br.com.effecta.rest_with_spring_boot_and_java.model.Book;
import br.com.effecta.rest_with_spring_boot_and_java.repositories.BookRepository;

@Service
public class BookService {

    private Logger logger = LoggerFactory.getLogger(BookService.class.getName());

    @Autowired
    private BookRepository repository;

    public List<BookDTO> findAll() {
        logger.info("Finding all Books!");

        var books = parseListObjects(repository.findAll(), BookDTO.class);
        books.forEach(this::addHateoasLinks);
        return books;
    }

    public BookDTO findById(Long id) {
        logger.info("Finding one Book!");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + id));
        var dto = parseObject(entity, BookDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public BookDTO create(BookDTO book) {
        if (book == null) throw new RequiredObjectIsNullException();

        logger.info("Creating one Book!");

        var entity = parseObject(book, Book.class);
        var dto = parseObject(repository.save(entity), BookDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public BookDTO update(Long id, BookDTO book) {
        if (book == null) throw new RequiredObjectIsNullException();
        
        logger.info("Updating one Book!");

        Book entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + id));
        if (book.getAuthor() != null)
            entity.setAuthor(book.getAuthor());
        if (book.getLaunchDate() != null)
            entity.setLaunchDate(book.getLaunchDate());
        if (book.getPrice() != null)
            entity.setPrice(book.getPrice());
        if (book.getTitle() != null)
            entity.setTitle(book.getTitle());
        var dto = parseObject(repository.save(entity), BookDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public void delete(Long id) {
        logger.info("Deleting one Book!");

        findById(id);
        repository.deleteById(id);
    }

    private void addHateoasLinks(BookDTO dto) {
        dto.add(linkTo(methodOn(BookController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(BookController.class).findAll()).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(BookController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(BookController.class).update(dto.getId(), dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(BookController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
    }
    
}
