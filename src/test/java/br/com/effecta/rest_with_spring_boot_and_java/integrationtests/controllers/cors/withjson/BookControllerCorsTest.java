package br.com.effecta.rest_with_spring_boot_and_java.integrationtests.controllers.cors.withjson;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.effecta.rest_with_spring_boot_and_java.config.TestConfigs;
import br.com.effecta.rest_with_spring_boot_and_java.integrationtests.dto.BookDTO;
import br.com.effecta.rest_with_spring_boot_and_java.integrationtests.testcontainers.AbstractIntegrationTest;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookControllerCorsTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static BookDTO book;

    @BeforeAll
    static void setup() {
        objectMapper = JsonMapper.builder()
        .addModule(new JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .build();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        RestAssured.config = RestAssured.config().objectMapperConfig(
            new ObjectMapperConfig().jackson2ObjectMapperFactory((cls, charset) -> objectMapper)
        );

        book = new BookDTO();
    }

    @Test
    @Order(1)
    void testCreate() throws JsonMappingException, JsonProcessingException {
        mockBook();

        specification = new RequestSpecBuilder()
            .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_EFFECTA)
                .setBasePath("/api/book/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
            .build();
        
        var content = given(specification)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(book)
		.when()
			.post()
		.then()
			.statusCode(200)
		.extract()
			.body()
				.asString();

        BookDTO createdBook = objectMapper.readValue(content, BookDTO.class);
        book = createdBook;
		
        assertNotNull(createdBook.getId());
        assertNotNull(createdBook.getAuthor());
        assertNotNull(createdBook.getLaunchDate());
        assertNotNull(createdBook.getPrice());
        assertNotNull(createdBook.getTitle());

        assertTrue(createdBook.getId() > 0);

        assertEquals("J. K. Rowling", createdBook.getAuthor());
        assertEquals("26/06/1997", createdBook.getLaunchDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        assertEquals(43.02, createdBook.getPrice(), 0.001);
        assertEquals("Zarry Potter e a Pedra Filosofal", createdBook.getTitle());
    }
    
    @Test
    @Order(2)
    void testCreateWithWrongOrigin() throws JsonMappingException, JsonProcessingException {
        specification = new RequestSpecBuilder()
            .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_PUBLISKO)
                .setBasePath("/api/book/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
            .build();
        
        var content = given(specification)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(book)
		.when()
			.post()
		.then()
			.statusCode(403)
		.extract()
			.body()
				.asString();

        assertEquals("Invalid CORS request", content);
    }
    
    @Test
    @Order(3)
    void testFindById() throws JsonMappingException, JsonProcessingException {
        specification = new RequestSpecBuilder()
            .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCAL)
                .setBasePath("/api/book/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
            .build();
        
        var content = given(specification)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .pathParam("id", book.getId())
		.when()
			.get("{id}")
		.then()
			.statusCode(200)
		.extract()
			.body()
				.asString();

        BookDTO createdBook = objectMapper.readValue(content, BookDTO.class);
        book = createdBook;

        assertNotNull(createdBook.getId());
        assertNotNull(createdBook.getAuthor());
        assertNotNull(createdBook.getLaunchDate());
        assertNotNull(createdBook.getPrice());
        assertNotNull(createdBook.getTitle());

        assertTrue(createdBook.getId() > 0);

        assertEquals("J. K. Rowling", createdBook.getAuthor());
        assertEquals("26/06/1997", createdBook.getLaunchDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        assertEquals(43.02, createdBook.getPrice(), 0.001);
        assertEquals("Zarry Potter e a Pedra Filosofal", createdBook.getTitle());
    }
    
    @Test
    @Order(4)
    void testFindByIdWithWrongOrigin() throws JsonMappingException, JsonProcessingException {
        specification = new RequestSpecBuilder()
            .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_PUBLISKO)
                .setBasePath("/api/book/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
            .build();
        
        var content = given(specification)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .pathParam("id", book.getId())
		.when()
			.get("{id}")
		.then()
			.statusCode(403)
		.extract()
			.body()
				.asString();

        assertEquals("Invalid CORS request", content);
    }
    
    @Test
    void testUpdate() {
        
    }
    
    @Test
    void testFindAll() {
        
    }
    
    private void mockBook() {
        book.setAuthor("J. K. Rowling");
        book.setLaunchDate(LocalDate.parse("26/06/1997", DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        book.setPrice(43.02);
        book.setTitle("Zarry Potter e a Pedra Filosofal");
    }
}
