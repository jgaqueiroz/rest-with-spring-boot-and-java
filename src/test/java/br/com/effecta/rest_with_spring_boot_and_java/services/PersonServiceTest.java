package br.com.effecta.rest_with_spring_boot_and_java.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.mockito.junit.jupiter.MockitoExtension;

import br.com.effecta.rest_with_spring_boot_and_java.model.Person;
import br.com.effecta.rest_with_spring_boot_and_java.repositories.PersonRepository;
import br.com.effecta.rest_with_spring_boot_and_java.unittests.mapper.mocks.MockPerson;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    MockPerson input;

    @InjectMocks
    private PersonService service;

    @Mock
    PersonRepository repository;

    @BeforeEach
    void setUp() {
        input = new MockPerson();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById() {
        Person person = input.mockEntity(1);
        person.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(person));
        
        var result = service.findById(1L);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assert(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("self")
            && link.getHref().endsWith("/api/person/v1/1")
            && link.getType().equals("GET")
            )
        );

        assert(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("findAll")
            && link.getHref().endsWith("/api/person/v1")
            && link.getType().equals("GET")
            )
        );

        assert(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("create")
            && link.getHref().endsWith("/api/person/v1")
            && link.getType().equals("POST")
            )
        );

        assert(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("update")
            && link.getHref().endsWith("/api/person/v1/1")
            && link.getType().equals("PUT")
            )
        );

        assert(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("delete")
            && link.getHref().endsWith("/api/person/v1/1")
            && link.getType().equals("DELETE")
            )
        );

        assertEquals("Address Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());
    }
    
    @Test
    void testCreate() {

    }

    @Test
    void testDelete() {

    }
    
    @Test
    void testUpdate() {
        
    }
    
    @Test
    void testFindAll() {

    }
}
