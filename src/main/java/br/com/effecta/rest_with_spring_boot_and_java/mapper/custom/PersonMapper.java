package br.com.effecta.rest_with_spring_boot_and_java.mapper.custom;

import java.util.Date;

import org.springframework.stereotype.Service;

import br.com.effecta.rest_with_spring_boot_and_java.data.dto.v2.PersonDTOv2;
import br.com.effecta.rest_with_spring_boot_and_java.model.Person;

@Service
public class PersonMapper {

    public PersonDTOv2 PersonToDTOv2(Person person) {
        PersonDTOv2 dto = new PersonDTOv2();
        dto.setId(person.getId());
        dto.setFirstName(person.getFirstName());
        dto.setLastName(person.getLastName());
        dto.setAddress(person.getAddress());
        dto.setGender(person.getGender());
        dto.setBirthDate(new Date());
        return dto;
    }

    public Person DTOv2ToPerson(PersonDTOv2 person) {
        Person obj = new Person();
        obj.setId(person.getId());
        obj.setFirstName(person.getFirstName());
        obj.setLastName(person.getLastName());
        obj.setAddress(person.getAddress());
        obj.setGender(person.getGender());
        // obj.setBirthDate(new Date());
        return obj;
    }

}
