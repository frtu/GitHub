package com.github.frtu.simple.infra.reflect;

import java.lang.annotation.Annotation;
import java.util.Set;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

public class ClassPathScanningAnnotationProvider extends ClassPathScanningCandidateComponentProvider {
    public ClassPathScanningAnnotationProvider() {
        super(false);
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isIndependent();
    }

    public static Set<BeanDefinition> findCandidateComponents(Class<? extends Annotation> annotationType) {
        return findCandidateComponents(annotationType, "");
    }
    public static Set<BeanDefinition> findCandidateComponents(Class<? extends Annotation> annotationType, String basePackage) {
        ClassPathScanningAnnotationProvider classPathScanningCandidateComponentProvider = new ClassPathScanningAnnotationProvider();
        classPathScanningCandidateComponentProvider.addIncludeFilter(new AnnotationTypeFilter(annotationType));
        
        Set<BeanDefinition> findCandidateComponents = classPathScanningCandidateComponentProvider.findCandidateComponents(basePackage);
        return findCandidateComponents;
    }
}