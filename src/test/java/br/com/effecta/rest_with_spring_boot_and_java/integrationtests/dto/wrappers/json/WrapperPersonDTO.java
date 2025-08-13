package br.com.effecta.rest_with_spring_boot_and_java.integrationtests.dto.wrappers.json;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WrapperPersonDTO implements Serializable {

    private static final Long serialVersionUID = 1L;

    @JsonProperty("_embedded")
    private PersonEmbeddedDTO embedded;

    public WrapperPersonDTO() {}

    public static Long getSerialversionuid() {
        return serialVersionUID;
    }

    public PersonEmbeddedDTO getEmbedded() {
        return embedded;
    }

    public void setEmbedded(PersonEmbeddedDTO embedded) {
        this.embedded = embedded;
    }

}
