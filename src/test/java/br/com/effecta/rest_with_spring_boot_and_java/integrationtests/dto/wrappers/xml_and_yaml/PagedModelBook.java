package br.com.effecta.rest_with_spring_boot_and_java.integrationtests.dto.wrappers.xml_and_yaml;

import java.io.Serializable;
import java.util.List;

import br.com.effecta.rest_with_spring_boot_and_java.integrationtests.dto.BookDTO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PagedModelBook implements Serializable {

    private static final Long serialVersionUID = 1L;

    @XmlElement(name = "content")
    public List<BookDTO> content;

    public PagedModelBook() {
    }

    public static Long getSerialversionuid() {
        return serialVersionUID;
    }

    public List<BookDTO> getContent() {
        return content;
    }

    public void setContent(List<BookDTO> content) {
        this.content = content;
    }
    
}
