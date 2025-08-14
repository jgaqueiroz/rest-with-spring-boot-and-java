package br.com.effecta.rest_with_spring_boot_and_java.integrationtests.dto.wrappers.json;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.effecta.rest_with_spring_boot_and_java.integrationtests.dto.BookDTO;

public class BookEmbeddedDTO implements Serializable {

    private static final Long serialVersionUID = 1L;

    @JsonProperty("books")
    private List<BookDTO> books;

    public BookEmbeddedDTO() {}

    public static Long getSerialversionuid() {
        return serialVersionUID;
    }

    public List<BookDTO> getBooks() {
        return books;
    }

    public void setBooks(List<BookDTO> books) {
        this.books = books;
    }

}
