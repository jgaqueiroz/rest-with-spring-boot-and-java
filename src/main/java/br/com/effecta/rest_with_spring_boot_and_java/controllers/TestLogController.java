package br.com.effecta.rest_with_spring_boot_and_java.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test/v1")
public class TestLogController {

    private Logger logger = LoggerFactory.getLogger(TestLogController.class.getName());

    @GetMapping
    public String testLog() {
        logger.debug("This is a DEBUG log");
        logger.info("This is an INFO log");
        logger.warn("This is an WARN log");
        logger.error("This is an ERROR log");
        return "Logs generated sucessfully!";
    }

}
