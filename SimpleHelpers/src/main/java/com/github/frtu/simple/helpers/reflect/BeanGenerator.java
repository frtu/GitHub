package com.github.frtu.simple.helpers.reflect;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

public class BeanGenerator {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(BeanGenerator.class);

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T generateBean(Class<T> clazz) {
        T object;
        try {
            object = ConstructorUtils.invokeConstructor(clazz, (Object[]) null);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(
                                               "The class passed MUST have an empty constructor OR not be an embedded class ! Class="
                                                       + clazz.getCanonicalName(), e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("The empty constructor MUST be public ! Class=" + clazz.getCanonicalName(), e);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error in creating instance of class=" + clazz.getCanonicalName(), e);
        }
        return object;
    }

    public static <T> T generatePopulatedBean(Class<T> clazz) {
        final T object = generateBean(clazz);

        FieldCallback fc = new FieldCallback() {
            @Override
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                Class<?> classField = field.getType();
                // Assign String with its field name
                if (String.class.equals(classField)) {
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, object, field.getName());
                } else {
                    logger.error("Cannot set field '{}' that is from class '{}'!", field.getName(), classField);
                }
            }
        };
        ReflectionUtils.doWithFields(clazz, fc);
        return object;
    }

    public static <T> String getJSONSampleFromString(Class<T> clazz) {
        return getJSONSampleFromString(clazz, false);
    }

    public static <T> String getJSONSampleFromString(Class<T> clazz, boolean prettyPrint) {
        T classSampleObj = generatePopulatedBean(clazz);
        try {
            String result;
            if (prettyPrint) {
                ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
                result = objectWriter.writeValueAsString(classSampleObj);
            } else {
                result = objectMapper.writeValueAsString(classSampleObj);
            }
            return result;
        } catch (IOException e) {
            throw new IllegalArgumentException("Impossible to serialize to JSON class=" + clazz.getCanonicalName(), e);
        }
    }

    public static <T> String getXMLSampleFromString(Class<T> clazz) {
        return getXMLSampleFromString(clazz, false);
    }

    public static <T> String getXMLSampleFromString(Class<T> clazz, boolean prettyPrint) {
        T classSampleObj = generatePopulatedBean(clazz);
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            if (prettyPrint) {
                // output pretty printed
                jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            }
            StringWriter stringWriter = new StringWriter();
            jaxbMarshaller.marshal(classSampleObj, stringWriter);

            return stringWriter.toString();
        } catch (JAXBException e) {
            throw new IllegalArgumentException("Impossible to serialize to XML class=" + clazz.getCanonicalName(), e);
        }
    }
}
