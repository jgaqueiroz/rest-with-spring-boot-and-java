package br.com.effecta.rest_with_spring_boot_and_java.file.exporter.impl;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import br.com.effecta.rest_with_spring_boot_and_java.data.dto.PersonDTO;
import br.com.effecta.rest_with_spring_boot_and_java.file.exporter.contract.FileExporter;

@Component
public class XlsxExporter  implements FileExporter {

    @Override
    public Resource exportFile(List<PersonDTO> people) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'importFile'");
    }

}
