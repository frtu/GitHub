package com.github.frtu.simple.infra.reflect;

import static org.junit.Assert.*;
import org.junit.Test;

import com.github.frtu.simple.infra.reflect.BeanGenerator;

public class BeanGeneratorTest {
    @Test
    public void testJSON() {
        String jsonSampleFromString = BeanGenerator.getJSONSampleFromString(TestBean.class);
        assertNotNull(jsonSampleFromString);
        assertEquals("{\"id\":\"id\",\"name\":\"name\"}", jsonSampleFromString);
    }

    @Test
    public void testXML() {
        String xmlSampleFromString = BeanGenerator.getXMLSampleFromString(TestBean.class);
        assertNotNull(xmlSampleFromString);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><testBean><id>id</id><name>name</name></testBean>",
                     xmlSampleFromString);
    }
}
