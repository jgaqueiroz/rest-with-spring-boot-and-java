package br.com.effecta.rest_with_spring_boot_and_java.integrationtests.dto.wrappers;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.effecta.rest_with_spring_boot_and_java.integrationtests.dto.PersonDTO;

public class PersonEmbeddedDTO implements Serializable {

    private static final Long serialVersionUID = 1L;

    @JsonProperty("people")
    private List<PersonDTO> people;

    public PersonEmbeddedDTO() {}

    public static Long getSerialversionuid() {
        return serialVersionUID;
    }

    public List<PersonDTO> getPeople() {
        return people;
    }

    public void setPeople(List<PersonDTO> people) {
        this.people = people;
    }

}
