package br.com.effecta.rest_with_spring_boot_and_java.unittests.mapper.mocks;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import br.com.effecta.rest_with_spring_boot_and_java.data.dto.BookDTO;
import br.com.effecta.rest_with_spring_boot_and_java.model.Book;

public class MockBook {

    public Book mockEntity() {
        return mockEntity(0);
    }

    public BookDTO mockDTO() {
        return mockDTO(0);
    }

    public List<Book> mockEntityList() {
        List<Book> books = new ArrayList<Book>();
        for (int i = 0; i < 14; i++) {
            books.add(mockEntity(i));
        }
        return books;
    }

    public List<BookDTO> mockDTOList() {
        List<BookDTO> books = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            books.add(mockDTO(i));
        }
        return books;
    }

    public Book mockEntity(Integer number) {
        Book book = new Book();
        book.setId(number.longValue());
        book.setAuthor("Author Test" + number);
        book.setLaunchDate(LocalDate.parse("25/12/1987", DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        book.setPrice(((number % 2) == 0) ? 200.0 : 100.0);
        book.setTitle("Title Test" + number);
        return book;
    }

    public BookDTO mockDTO(Integer number) {
        BookDTO book = new BookDTO();
        book.setId(number.longValue());
        book.setAuthor("Author Test" + number);
        book.setLaunchDate(LocalDate.parse("25/12/1987", DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        book.setPrice(((number % 2) == 0) ? 200.0 : 100.0);
        book.setTitle("Title Test" + number);
        return book;
    }

}