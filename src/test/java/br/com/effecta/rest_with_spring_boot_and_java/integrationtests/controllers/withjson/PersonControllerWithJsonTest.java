package br.com.effecta.rest_with_spring_boot_and_java.integrationtests.controllers.withjson;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.effecta.rest_with_spring_boot_and_java.config.TestConfigs;
import br.com.effecta.rest_with_spring_boot_and_java.integrationtests.dto.PersonDTO;
import br.com.effecta.rest_with_spring_boot_and_java.integrationtests.dto.wrappers.WrapperPersonDTO;
import br.com.effecta.rest_with_spring_boot_and_java.integrationtests.testcontainers.AbstractIntegrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerWithJsonTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static PersonDTO person;

    @BeforeAll
    static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        person = new PersonDTO();
    }

    @Test
    @Order(1)
    void testCreate() throws JsonMappingException, JsonProcessingException {
        mockPerson();

        specification = new RequestSpecBuilder()
            .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_EFFECTA)
                .setBasePath("/api/person/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
            .build();
        
        var content = given(specification)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(person)
		.when()
			.post()
		.then()
			.statusCode(200)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
		.extract()
			.body()
				.asString();

        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        person = createdPerson;
		
        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);

        assertEquals("Linus", createdPerson.getFirstName());
        assertEquals("Torvalds", createdPerson.getLastName());
        assertEquals("Helsinki - Finland", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());
    }

    @Test
    @Order(2)
    void testUpdate() throws JsonMappingException, JsonProcessingException {
        person.setLastName("Benedict Torvalds");
        
        var content = given(specification)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .pathParam("id", person.getId())
        .body(person)
		.when()
			.put("{id}")
		.then()
			.statusCode(200)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
		.extract()
			.body()
				.asString();

        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        person = createdPerson;
		
        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);

        assertEquals("Linus", createdPerson.getFirstName());
        assertEquals("Benedict Torvalds", createdPerson.getLastName());
        assertEquals("Helsinki - Finland", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());
    }
    
    @Test
    @Order(3)
    void testFindById() throws JsonMappingException, JsonProcessingException {
        
        var content = given(specification)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .pathParam("id", person.getId())
		.when()
			.get("{id}")
		.then()
			.statusCode(200)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
		.extract()
			.body()
				.asString();

        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        person = createdPerson;

        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);

        assertEquals("Linus", createdPerson.getFirstName());
        assertEquals("Benedict Torvalds", createdPerson.getLastName());
        assertEquals("Helsinki - Finland", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());
    }

    @Test
    @Order(4)
    void testDisable() throws JsonMappingException, JsonProcessingException {
        
        var content = given(specification)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .pathParam("id", person.getId())
		.when()
			.patch("{id}")
		.then()
			.statusCode(200)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
		.extract()
			.body()
				.asString();

        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        person = createdPerson;

        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);

        assertEquals("Linus", createdPerson.getFirstName());
        assertEquals("Benedict Torvalds", createdPerson.getLastName());
        assertEquals("Helsinki - Finland", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertFalse(createdPerson.getEnabled());
    }

    @Test
    @Order(5)
    void testDelete() throws JsonMappingException, JsonProcessingException {
        
        given(specification)
        .pathParam("id", person.getId())
		.when()
			.delete("{id}")
		.then()
			.statusCode(204)
		.extract()
			.body()
				.asString();
    }
    
    @Test
    @Order(6)
    void testFindAll() throws JsonMappingException, JsonProcessingException {
        
        var content = given(specification)
        .accept(MediaType.APPLICATION_JSON_VALUE)
        .queryParams("page", 3, "size", 12, "direction", "asc")
		.when()
			.get()
		.then()
			.statusCode(200)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
		.extract()
			.body()
				.asString();

        WrapperPersonDTO wrapper = objectMapper.readValue(content,  WrapperPersonDTO.class);
        List<PersonDTO> people = wrapper.getEmbedded().getPeople();

        PersonDTO personOne = people.get(0);

        assertNotNull(personOne.getId());
        assertTrue(personOne.getId() > 0);

        assertEquals("Alva", personOne.getFirstName());
        assertEquals("Duberry", personOne.getLastName());
        assertEquals("8460 Russell Park", personOne.getAddress());
        assertEquals("Male", personOne.getGender());
        assertTrue(personOne.getEnabled());

        PersonDTO personFive = people.get(4);

        assertNotNull(personFive.getId());
        assertTrue(personFive.getId() > 0);

        assertEquals("Amelina", personFive.getFirstName());
        assertEquals("Masterson", personFive.getLastName());
        assertEquals("4 Caliangt Point", personFive.getAddress());
        assertEquals("Female", personFive.getGender());
        assertFalse(personFive.getEnabled());

        PersonDTO personEight = people.get(7);

        assertNotNull(personEight.getId());
        assertTrue(personEight.getId() > 0);

        assertEquals("Anabelle", personEight.getFirstName());
        assertEquals("Antyshev", personEight.getLastName());
        assertEquals("46 Corscot Lane", personEight.getAddress());
        assertEquals("Female", personEight.getGender());
        assertTrue(personEight.getEnabled());
    }
    
    private void mockPerson() {
        person.setFirstName("Linus");
        person.setLastName("Torvalds");
        person.setAddress("Helsinki - Finland");
        person.setGender("Male");
        person.setEnabled(true);
    }
}
