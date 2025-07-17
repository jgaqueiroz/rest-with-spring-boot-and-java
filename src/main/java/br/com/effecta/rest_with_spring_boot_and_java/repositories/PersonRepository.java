package br.com.effecta.rest_with_spring_boot_and_java.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.effecta.rest_with_spring_boot_and_java.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {

}
