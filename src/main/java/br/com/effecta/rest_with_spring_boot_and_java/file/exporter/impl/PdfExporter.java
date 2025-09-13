package br.com.effecta.rest_with_spring_boot_and_java.file.exporter.impl;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import br.com.effecta.rest_with_spring_boot_and_java.data.dto.PersonDTO;
import br.com.effecta.rest_with_spring_boot_and_java.file.exporter.contract.FileExporter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Component
public class PdfExporter implements FileExporter {

    @Override
    public Resource exportFile(List<PersonDTO> people) throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("/templates/people.jrxml");
        if (inputStream == null) {
            throw new RuntimeException("Template file not found: /templates/people.jrxml");
        }

        JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(people);
        Map<String, Object> parameters = new HashMap<>();
        //parameters.put("title", "People Report");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
            return new ByteArrayResource(outputStream.toByteArray());
        } 
    }

}
