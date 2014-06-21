package com.github.frtu.simple.infra.jdk;

import java.lang.management.ManagementFactory;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

public class JdkAttributeHelper {
    public static final String PLATFORM_32BITS = "x86";
    public static final String PLATFORM_64BITS = "amd64";

    protected MBeanServerConnection mbeanServerConnection;

    public JdkAttributeHelper(MBeanServerConnection mbeanServerConnection) {
        super();
        this.mbeanServerConnection = mbeanServerConnection;
    }

    public Object getAttribute(String objName, String attributeName) {
        ObjectName objectName = buildObjectName(objName);
        try {
            return mbeanServerConnection.getAttribute(objectName, attributeName);
        } catch (AttributeNotFoundException e) {
            throw new IllegalArgumentException("Attribute cannot be found att=" + attributeName, e);
        } catch (InstanceNotFoundException e) {
            throw new IllegalArgumentException("Instance not found! Instance=" + objName, e);
        } catch (Exception e) {
            throw new IllegalStateException("An error occurred when searching for objName=" + objName + " att=" + attributeName, e);
        }
    }

    public ObjectName buildObjectName(String objName) {
        ObjectName objectName = null;
        try {
            objectName = new ObjectName(objName);
        } catch (MalformedObjectNameException e) {
            throw new IllegalArgumentException("Parameter objName is not correctly formatted!! objName=" + objName, e);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Parameter objName cannot be null!", e);
        }
        return objectName;
    }

    public Integer getThreadCount() {
        Integer threadCount = (Integer) getAttribute(ManagementFactory.THREAD_MXBEAN_NAME, "ThreadCount");
        return threadCount;
    }

    public short getOsArch() {
        String strOS = (String) getAttribute(ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME, "Arch");
        if (PLATFORM_32BITS.equals(strOS)) {
            return (short) 32;
        } else {
            return (short) 64;
        }
    }

    public String getOsName() {
        String name = (String) getAttribute(ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME, "Name");
        return name;
    }

    public String getJVM() {
        String vmVendor = (String) getAttribute(ManagementFactory.RUNTIME_MXBEAN_NAME, "VmVendor");
        String vmVersion = (String) getAttribute(ManagementFactory.RUNTIME_MXBEAN_NAME, "VmVersion");
        return vmVendor + " " + vmVersion;
    }

    public Integer getAvailableProc() {
        Integer availableProc = (Integer) getAttribute(ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME, "AvailableProcessors");
        return availableProc;
    }

}