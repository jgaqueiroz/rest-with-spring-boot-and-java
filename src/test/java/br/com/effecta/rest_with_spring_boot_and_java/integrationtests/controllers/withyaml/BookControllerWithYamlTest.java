package br.com.effecta.rest_with_spring_boot_and_java.integrationtests.controllers.withyaml;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.effecta.rest_with_spring_boot_and_java.config.TestConfigs;
import br.com.effecta.rest_with_spring_boot_and_java.integrationtests.dto.BookDTO;
import br.com.effecta.rest_with_spring_boot_and_java.integrationtests.dto.wrappers.xml_and_yaml.PagedModelBook;
import br.com.effecta.rest_with_spring_boot_and_java.integrationtests.testcontainers.AbstractIntegrationTest;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookControllerWithYamlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static YAMLMapper objectMapper;
    private static BookDTO book;

    @BeforeAll
    static void setup() {
        objectMapper = YAMLMapper.builder()
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
            .setConfig(RestAssuredConfig.config()
                .encoderConfig(EncoderConfig.encoderConfig()
                    .encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT)))
            .build();
        
        var content = given(specification)
        .contentType(MediaType.APPLICATION_YAML_VALUE)
        .accept(MediaType.APPLICATION_YAML_VALUE)
        .body(objectMapper.writeValueAsString(book))
		.when()
			.post()
		.then()
			.statusCode(200)
            .contentType(MediaType.APPLICATION_YAML_VALUE)
		.extract()
			.body()
				.asString();

        BookDTO createdBook = objectMapper.readValue(content, BookDTO.class);
        book = createdBook;

        assertNotNull(createdBook.getId());
        assertTrue(createdBook.getId() > 0);

        assertEquals("J. K. Rowling", createdBook.getAuthor());
        assertEquals("26/06/1997", createdBook.getLaunchDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        assertEquals(43.02, createdBook.getPrice(), 0.001);
        assertEquals("Harry Potter e a Pedra Filosofal", createdBook.getTitle());
    }

    @Test
    @Order(2)
    void testUpdate() throws JsonMappingException, JsonProcessingException {
        book.setAuthor("Joanne Rowling");
        
        var content = given(specification)
        .contentType(MediaType.APPLICATION_YAML_VALUE)
        .accept(MediaType.APPLICATION_YAML_VALUE)
        .pathParam("id", book.getId())
        .body(objectMapper.writeValueAsString(book))
		.when()
			.put("{id}")
		.then()
			.statusCode(200)
            .contentType(MediaType.APPLICATION_YAML_VALUE)
		.extract()
			.body()
				.asString();

        BookDTO createdBook = objectMapper.readValue(content, BookDTO.class);
        book = createdBook;

        assertNotNull(createdBook.getId());
        assertTrue(createdBook.getId() > 0);

        assertEquals("Joanne Rowling", createdBook.getAuthor());
        assertEquals("26/06/1997", createdBook.getLaunchDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        assertEquals(43.02, createdBook.getPrice(), 0.001);
        assertEquals("Harry Potter e a Pedra Filosofal", createdBook.getTitle());
    }
    
    @Test
    @Order(3)
    void testFindById() throws JsonMappingException, JsonProcessingException {
        
        var content = given(specification)
        .accept(MediaType.APPLICATION_YAML_VALUE)
        .pathParam("id", book.getId())
		.when()
			.get("{id}")
		.then()
			.statusCode(200)
            .contentType(MediaType.APPLICATION_YAML_VALUE)
		.extract()
			.body()
				.asString();

        BookDTO createdBook = objectMapper.readValue(content, BookDTO.class);
        book = createdBook;

        assertNotNull(createdBook.getId());
        assertTrue(createdBook.getId() > 0);

        assertEquals("Joanne Rowling", createdBook.getAuthor());
        assertEquals("26/06/1997", createdBook.getLaunchDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        assertEquals(43.02, createdBook.getPrice(), 0.001);
        assertEquals("Harry Potter e a Pedra Filosofal", createdBook.getTitle());
    }

    @Test
    @Order(4)
    void testDelete() throws JsonMappingException, JsonProcessingException {
        
        given(specification)
        .pathParam("id", book.getId())
		.when()
			.delete("{id}")
		.then()
			.statusCode(204)
		.extract()
			.body()
				.asString();
    }
    
    @Test
    @Order(5)
    void testFindAll() throws JsonMappingException, JsonProcessingException {
        
        var content = given(specification)
        .accept(MediaType.APPLICATION_YAML_VALUE)
        .queryParams("page", 2, "size", 4, "direction", "asc")
		.when()
			.get()
		.then()
			.statusCode(200)
            .contentType(MediaType.APPLICATION_YAML_VALUE)
		.extract()
			.body()
				.asString();

        PagedModelBook wrapper = objectMapper.readValue(content,  PagedModelBook.class);
        List<BookDTO> books = wrapper.getContent();

        BookDTO bookOne = books.get(0);

        assertNotNull(bookOne.getId());
        assertTrue(bookOne.getId() > 0);

        assertEquals("Eric Evans", bookOne.getAuthor());
        assertEquals("07/11/2017", bookOne.getLaunchDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        assertEquals(92.0, bookOne.getPrice(), 0.001);
        assertEquals("Domain Driven Design", bookOne.getTitle());

        BookDTO bookTwo = books.get(1);

        assertNotNull(bookTwo.getId());
        assertTrue(bookTwo.getId() > 0);

        assertEquals("Roger S. Pressman", bookTwo.getAuthor());
        assertEquals("07/11/2017", bookTwo.getLaunchDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        assertEquals(56.0, bookTwo.getPrice(), 0.001);
        assertEquals("Engenharia de Software: uma abordagem profissional", bookTwo.getTitle());

        BookDTO bookFour = books.get(3);

        assertNotNull(bookFour.getId());
        assertTrue(bookFour.getId() > 0);

        assertEquals("Aguinaldo Aragon Fernandes e Vladimir Ferraz de Abreu", bookFour.getAuthor());
        assertEquals("07/11/2017", bookFour.getLaunchDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        assertEquals(54.0, bookFour.getPrice(), 0.001);
        assertEquals("Implantando a governança de TI", bookFour.getTitle());
    }
    
    private void mockBook() {
        book.setAuthor("J. K. Rowling");
        book.setLaunchDate(LocalDate.parse("26/06/1997", DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        book.setPrice(43.02);
        book.setTitle("Harry Potter e a Pedra Filosofal");
    }
}
