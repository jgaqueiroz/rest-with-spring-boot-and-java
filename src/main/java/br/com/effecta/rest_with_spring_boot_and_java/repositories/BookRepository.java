package br.com.effecta.rest_with_spring_boot_and_java.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.effecta.rest_with_spring_boot_and_java.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
    
}
