package br.com.effecta.rest_with_spring_boot_and_java.file.importer.impl;

import java.io.InputStream;
import java.util.List;

import br.com.effecta.rest_with_spring_boot_and_java.data.dto.PersonDTO;
import br.com.effecta.rest_with_spring_boot_and_java.file.importer.contract.FileImporter;

public class CsvImporter implements FileImporter {

    @Override
    public List<PersonDTO> importFile(InputStream inputStream) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'importFile'");
    }
    
}
