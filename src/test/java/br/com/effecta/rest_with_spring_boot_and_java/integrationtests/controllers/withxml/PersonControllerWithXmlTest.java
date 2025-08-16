package br.com.effecta.rest_with_spring_boot_and_java.integrationtests.controllers.withxml;

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
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import br.com.effecta.rest_with_spring_boot_and_java.config.TestConfigs;
import br.com.effecta.rest_with_spring_boot_and_java.integrationtests.dto.PersonDTO;
import br.com.effecta.rest_with_spring_boot_and_java.integrationtests.dto.wrappers.xml_and_yaml.PagedModelPerson;
import br.com.effecta.rest_with_spring_boot_and_java.integrationtests.testcontainers.AbstractIntegrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerWithXmlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static XmlMapper objectMapper;
    private static PersonDTO person;

    @BeforeAll
    static void setup() {
        objectMapper = new XmlMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

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
        .contentType(MediaType.APPLICATION_XML_VALUE)
        .accept(MediaType.APPLICATION_XML_VALUE)
        .body(person)
		.when()
			.post()
		.then()
			.statusCode(200)
            .contentType(MediaType.APPLICATION_XML_VALUE)
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
        .contentType(MediaType.APPLICATION_XML_VALUE)
        .accept(MediaType.APPLICATION_XML_VALUE)
        .pathParam("id", person.getId())
        .body(person)
		.when()
			.put("{id}")
		.then()
			.statusCode(200)
            .contentType(MediaType.APPLICATION_XML_VALUE)
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
        .accept(MediaType.APPLICATION_XML_VALUE)
        .pathParam("id", person.getId())
		.when()
			.get("{id}")
		.then()
			.statusCode(200)
            .contentType(MediaType.APPLICATION_XML_VALUE)
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
        .accept(MediaType.APPLICATION_XML_VALUE)
        .pathParam("id", person.getId())
		.when()
			.patch("{id}")
		.then()
			.statusCode(200)
            .contentType(MediaType.APPLICATION_XML_VALUE)
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
        .accept(MediaType.APPLICATION_XML_VALUE)
        .queryParams("page", 3, "size", 12, "direction", "asc")
		.when()
			.get()
		.then()
			.statusCode(200)
            .contentType(MediaType.APPLICATION_XML_VALUE)
		.extract()
			.body()
				.asString();

        PagedModelPerson wrapper = objectMapper.readValue(content,  PagedModelPerson.class);
        List<PersonDTO> people = wrapper.getContent();

        PersonDTO personOne = people.get(0);

        assertNotNull(personOne.getId());
        assertTrue(personOne.getId() > 0);

        assertEquals("Alex", personOne.getFirstName());
        assertEquals("Brewood", personOne.getLastName());
        assertEquals("0 Orin Pass", personOne.getAddress());
        assertEquals("Male", personOne.getGender());
        assertFalse(personOne.getEnabled());

        PersonDTO personFive = people.get(4);

        assertNotNull(personFive.getId());
        assertTrue(personFive.getId() > 0);

        assertEquals("Aliza", personFive.getFirstName());
        assertEquals("Pringell", personFive.getLastName());
        assertEquals("62 4th Place", personFive.getAddress());
        assertEquals("Female", personFive.getGender());
        assertTrue(personFive.getEnabled());

        PersonDTO personEight = people.get(7);

        assertNotNull(personEight.getId());
        assertTrue(personEight.getId() > 0);

        assertEquals("Allissa", personEight.getFirstName());
        assertEquals("Millard", personEight.getLastName());
        assertEquals("43 Green Avenue", personEight.getAddress());
        assertEquals("Female", personEight.getGender());
        assertTrue(personEight.getEnabled());
    }

    @Test
    @Order(7)
    void testFindByName() throws JsonMappingException, JsonProcessingException {
        
        var content = given(specification)
        .accept(MediaType.APPLICATION_XML_VALUE)
        .pathParam("firstName", "and")
        .queryParams("page", 2, "size", 3, "direction", "asc")
		.when()
			.get("findPeopleByName/{firstName}")
		.then()
			.statusCode(200)
            .contentType(MediaType.APPLICATION_XML_VALUE)
		.extract()
			.body()
				.asString();

        PagedModelPerson wrapper = objectMapper.readValue(content,  PagedModelPerson.class);
        List<PersonDTO> people = wrapper.getContent();

        PersonDTO personOne = people.get(0);

        assertNotNull(personOne.getId());
        assertTrue(personOne.getId() > 0);

        assertEquals("Brande", personOne.getFirstName());
        assertEquals("Aiken", personOne.getLastName());
        assertEquals("3 Toban Lane", personOne.getAddress());
        assertEquals("Female", personOne.getGender());
        assertTrue(personOne.getEnabled());

        PersonDTO personTwo = people.get(1);

        assertNotNull(personTwo.getId());
        assertTrue(personTwo.getId() > 0);

        assertEquals("Brandise", personTwo.getFirstName());
        assertEquals("Stockey", personTwo.getLastName());
        assertEquals("26 Crescent Oaks Street", personTwo.getAddress());
        assertEquals("Female", personTwo.getGender());
        assertFalse(personTwo.getEnabled());

        PersonDTO personThree = people.get(2);

        assertNotNull(personThree.getId());
        assertTrue(personThree.getId() > 0);

        assertEquals("Candida", personThree.getFirstName());
        assertEquals("McKeever", personThree.getLastName());
        assertEquals("72 Dennis Pass", personThree.getAddress());
        assertEquals("Female", personThree.getGender());
        assertFalse(personThree.getEnabled());
    }
    
    private void mockPerson() {
        person.setFirstName("Linus");
        person.setLastName("Torvalds");
        person.setAddress("Helsinki - Finland");
        person.setGender("Male");
        person.setEnabled(true);
    }
}
