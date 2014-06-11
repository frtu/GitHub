package com.github.frtu.simple.helpers.object;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@XmlRootElement(name = "testBean")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "id", "name" })
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestBean {
    @XmlElement(name = "id", required = false)
    private String id;

    @XmlElement(name = "name", required = true)
    private String name;

    public TestBean() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}