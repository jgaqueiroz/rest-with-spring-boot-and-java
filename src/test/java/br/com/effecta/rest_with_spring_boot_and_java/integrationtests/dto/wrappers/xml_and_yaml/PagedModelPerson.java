package br.com.effecta.rest_with_spring_boot_and_java.integrationtests.dto.wrappers.xml_and_yaml;

import java.io.Serializable;
import java.util.List;

import br.com.effecta.rest_with_spring_boot_and_java.integrationtests.dto.PersonDTO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PagedModelPerson implements Serializable {

    private static final Long serialVersionUID = 1L;

    @XmlElement(name = "content")
    public List<PersonDTO> content;

    public PagedModelPerson() {
    }

    public static Long getSerialversionuid() {
        return serialVersionUID;
    }

    public List<PersonDTO> getContent() {
        return content;
    }

    public void setContent(List<PersonDTO> content) {
        this.content = content;
    }
    
}
