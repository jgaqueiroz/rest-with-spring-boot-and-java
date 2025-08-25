package br.com.effecta.rest_with_spring_boot_and_java.file.importer.contract;

import java.io.InputStream;
import java.util.List;

import br.com.effecta.rest_with_spring_boot_and_java.data.dto.PersonDTO;

public interface FileImporter {

    List<PersonDTO> importFile(InputStream inputStream) throws Exception;
    
}
