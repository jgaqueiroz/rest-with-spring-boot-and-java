package br.com.effecta.rest_with_spring_boot_and_java.unittests.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.effecta.rest_with_spring_boot_and_java.data.dto.BookDTO;
import br.com.effecta.rest_with_spring_boot_and_java.exceptions.RequiredObjectIsNullException;
import br.com.effecta.rest_with_spring_boot_and_java.model.Book;
import br.com.effecta.rest_with_spring_boot_and_java.repositories.BookRepository;
import br.com.effecta.rest_with_spring_boot_and_java.services.BookService;
import br.com.effecta.rest_with_spring_boot_and_java.unittests.mapper.mocks.MockBook;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    MockBook input;

    @InjectMocks
    private BookService service;

    @Mock
    BookRepository repository;

    @BeforeEach
    void setUp() {
        input = new MockBook();
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    void testFindById() {
        Book book = input.mockEntity(1);
        book.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(book));
        
        var result = service.findById(1L);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assert(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("self")
            && link.getHref().endsWith("/api/book/v1/1")
            && link.getType().equals("GET")
            )
        );

        assert(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("findAll")
            && link.getHref().endsWith("/api/book/v1?page=1&size=12&direction=asc")
            && link.getType().equals("GET")
            )
        );

        assert(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("create")
            && link.getHref().endsWith("/api/book/v1")
            && link.getType().equals("POST")
            )
        );

        assert(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("update")
            && link.getHref().endsWith("/api/book/v1/1")
            && link.getType().equals("PUT")
            )
        );

        assert(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("delete")
            && link.getHref().endsWith("/api/book/v1/1")
            && link.getType().equals("DELETE")
            )
        );

        assertEquals("Author Test1", result.getAuthor());
        assertEquals("25/12/1987", result.getLaunchDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        assertEquals(100.0, result.getPrice());
        assertEquals("Title Test1", result.getTitle());
    }

    @Test
    void testCreate() {
        Book book = input.mockEntity(1);
        Book persisted = book;
        persisted.setId(1L);

        BookDTO dto = input.mockDTO(1);

        when(repository.save(book)).thenReturn(persisted);

        var result = service.create(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assert(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("self")
            && link.getHref().endsWith("/api/book/v1/1")
            && link.getType().equals("GET")
            )
        );

        assert(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("findAll")
            && link.getHref().endsWith("/api/book/v1?page=1&size=12&direction=asc")
            && link.getType().equals("GET")
            )
        );

        assert(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("create")
            && link.getHref().endsWith("/api/book/v1")
            && link.getType().equals("POST")
            )
        );

        assert(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("update")
            && link.getHref().endsWith("/api/book/v1/1")
            && link.getType().equals("PUT")
            )
        );

        assert(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("delete")
            && link.getHref().endsWith("/api/book/v1/1")
            && link.getType().equals("DELETE")
            )
        );

        assertEquals("Author Test1", result.getAuthor());
        assertEquals("25/12/1987", result.getLaunchDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        assertEquals(100.0, result.getPrice());
        assertEquals("Title Test1", result.getTitle());
    }

    @Test
    void testCreateWithNullPerson() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            service.create(null);
        });
        
        String expectedMessage = "It is not allowed to persist a null object";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testUpdate() {
        Book book = input.mockEntity(1);
        Book persisted = book;
        persisted.setId(1L);

        BookDTO dto = input.mockDTO(1);

        when(repository.findById(1L)).thenReturn(Optional.of(book));
        when(repository.save(book)).thenReturn(persisted);

        var result = service.update(dto.getId(), dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assert(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("self")
            && link.getHref().endsWith("/api/book/v1/1")
            && link.getType().equals("GET")
            )
        );

        assert(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("findAll")
            && link.getHref().endsWith("/api/book/v1?page=1&size=12&direction=asc")
            && link.getType().equals("GET")
            )
        );

        assert(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("create")
            && link.getHref().endsWith("/api/book/v1")
            && link.getType().equals("POST")
            )
        );

        assert(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("update")
            && link.getHref().endsWith("/api/book/v1/1")
            && link.getType().equals("PUT")
            )
        );

        assert(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("delete")
            && link.getHref().endsWith("/api/book/v1/1")
            && link.getType().equals("DELETE")
            )
        );

        assertEquals("Author Test1", result.getAuthor());
        assertEquals("25/12/1987", result.getLaunchDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        assertEquals(100.0, result.getPrice());
        assertEquals("Title Test1", result.getTitle());

    }

    @Test
    void testUpdateWithNullPerson() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            service.update(1L, null);
        });
        
        String expectedMessage = "It is not allowed to persist a null object";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testDelete() {
        Book book = input.mockEntity(1);
        book.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(book));

        service.delete(1L);
        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).deleteById(anyLong());
        verifyNoMoreInteractions(repository);
    }

    @Test
    @Disabled("REASON: Still under development")
    void testFindAll() {
        List<Book> list = input.mockEntityList();
        when(repository.findAll()).thenReturn(list);
        List<BookDTO> books = new ArrayList<>(); //service.findAll(pageable);

        assertNotNull(books);
        assertEquals(14, books.size());

        var bookOne = books.get(1);

        assertNotNull(bookOne);
        assertNotNull(bookOne.getId());
        assertNotNull(bookOne.getLinks());

        assert(bookOne.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("self")
            && link.getHref().endsWith("/api/book/v1/1")
            && link.getType().equals("GET")
            )
        );

        assert(bookOne.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("findAll")
            && link.getHref().endsWith("/api/book/v1")
            && link.getType().equals("GET")
            )
        );

        assert(bookOne.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("create")
            && link.getHref().endsWith("/api/book/v1")
            && link.getType().equals("POST")
            )
        );

        assert(bookOne.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("update")
            && link.getHref().endsWith("/api/book/v1/1")
            && link.getType().equals("PUT")
            )
        );

        assert(bookOne.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("delete")
            && link.getHref().endsWith("/api/book/v1/1")
            && link.getType().equals("DELETE")
            )
        );

        assertEquals("Author Test1", bookOne.getAuthor());
        assertEquals("25/12/1987", bookOne.getLaunchDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        assertEquals(100.0, bookOne.getPrice());
        assertEquals("Title Test1", bookOne.getTitle());

        var bookFour = books.get(4);
        
        assertNotNull(bookFour);
        assertNotNull(bookFour.getId());
        assertNotNull(bookFour.getLinks());

        assert(bookFour.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("self")
            && link.getHref().endsWith("/api/book/v1/4")
            && link.getType().equals("GET")
            )
        );

        assert(bookFour.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("findAll")
            && link.getHref().endsWith("/api/book/v1")
            && link.getType().equals("GET")
            )
        );

        assert(bookFour.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("create")
            && link.getHref().endsWith("/api/book/v1")
            && link.getType().equals("POST")
            )
        );

        assert(bookFour.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("update")
            && link.getHref().endsWith("/api/book/v1/4")
            && link.getType().equals("PUT")
            )
        );

        assert(bookFour.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("delete")
            && link.getHref().endsWith("/api/book/v1/4")
            && link.getType().equals("DELETE")
            )
        );

        assertEquals("Author Test4", bookFour.getAuthor());
        assertEquals("25/12/1987", bookFour.getLaunchDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        assertEquals(200.0, bookFour.getPrice());
        assertEquals("Title Test4", bookFour.getTitle());

        var bookSeven = books.get(7);
        
        assertNotNull(bookSeven);
        assertNotNull(bookSeven.getId());
        assertNotNull(bookSeven.getLinks());

        assert(bookSeven.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("self")
            && link.getHref().endsWith("/api/book/v1/7")
            && link.getType().equals("GET")
            )
        );

        assert(bookSeven.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("findAll")
            && link.getHref().endsWith("/api/book/v1")
            && link.getType().equals("GET")
            )
        );

        assert(bookSeven.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("create")
            && link.getHref().endsWith("/api/book/v1")
            && link.getType().equals("POST")
            )
        );

        assert(bookSeven.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("update")
            && link.getHref().endsWith("/api/book/v1/7")
            && link.getType().equals("PUT")
            )
        );

        assert(bookSeven.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("delete")
            && link.getHref().endsWith("/api/book/v1/7")
            && link.getType().equals("DELETE")
            )
        );

        assertEquals("Author Test7", bookSeven.getAuthor());
        assertEquals("25/12/1987", bookSeven.getLaunchDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        assertEquals(100.0, bookSeven.getPrice());
        assertEquals("Title Test7", bookSeven.getTitle());
    }
}
