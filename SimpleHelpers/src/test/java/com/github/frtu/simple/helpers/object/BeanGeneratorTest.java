package com.github.frtu.simple.helpers.object;

import static org.junit.Assert.*;
import org.junit.Test;

public class BeanGeneratorTest {
    @Test
    public void testJSON() {
        String jsonSampleFromString = BeanGenerator.getJSONSampleFromString(TestBean.class);
        assertEquals("{\"id\":\"id\",\"name\":\"name\"}", jsonSampleFromString);
    }

    @Test
    public void testXML() {
        String xmlSampleFromString = BeanGenerator.getXMLSampleFromString(TestBean.class);
        // <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
        // <testBean>
        // <id>id</id>
        // <name>name</name>
        // </testBean>
        assertNotNull(xmlSampleFromString);
    }
}
