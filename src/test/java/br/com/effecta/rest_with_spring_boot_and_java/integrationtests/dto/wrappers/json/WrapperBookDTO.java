package br.com.effecta.rest_with_spring_boot_and_java.integrationtests.dto.wrappers.json;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WrapperBookDTO implements Serializable {

    private static final Long serialVersionUID = 1L;

    @JsonProperty("_embedded")
    private BookEmbeddedDTO embedded;

    public WrapperBookDTO() {}

    public static Long getSerialversionuid() {
        return serialVersionUID;
    }

    public BookEmbeddedDTO getEmbedded() {
        return embedded;
    }

    public void setEmbedded(BookEmbeddedDTO embedded) {
        this.embedded = embedded;
    }

}
