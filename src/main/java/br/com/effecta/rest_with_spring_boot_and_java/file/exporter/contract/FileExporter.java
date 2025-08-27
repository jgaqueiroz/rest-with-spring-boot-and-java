package br.com.effecta.rest_with_spring_boot_and_java.file.exporter.contract;

import java.util.List;

import org.springframework.core.io.Resource;

import br.com.effecta.rest_with_spring_boot_and_java.data.dto.PersonDTO;

public interface FileExporter {

    Resource exportFile(List<PersonDTO> people) throws Exception;
    
}
