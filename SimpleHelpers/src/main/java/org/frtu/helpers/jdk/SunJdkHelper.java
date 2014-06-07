package org.frtu.helpers.jdk;

import java.io.File;
import java.lang.reflect.Field;

public class SunJdkHelper {
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(SunJdkHelper.class);

	private static final String JAVA_LIBRARY_PATH = "java.library.path";
	// is ; or : depending on Windows or Linux
	private static final char SYSTEM_PROP_SEPARATOR = File.pathSeparatorChar;

	public static void appendToJavaLibraryPath(File libraryPath) {
		String currentLibPath = System.getProperty(JAVA_LIBRARY_PATH);
		logger.debug("Currently {}={}", JAVA_LIBRARY_PATH, currentLibPath);

		StringBuilder targetLibPath = new StringBuilder();
		targetLibPath.append(libraryPath.getAbsolutePath());
		if (currentLibPath != null && !"".equals(currentLibPath)) {
			targetLibPath.append(SYSTEM_PROP_SEPARATOR);
			targetLibPath.append(currentLibPath);
		}

		logger.info("Setting new {} to '{}'", JAVA_LIBRARY_PATH, targetLibPath);
		System.setProperty(JAVA_LIBRARY_PATH, targetLibPath.toString());

		resetJavaLibraryPath();
	}

	/**
	 * Delete the cache of "java.library.path". This will force the classloader to recheck the modified value the next time we load
	 * libraries.
	 * 
	 * Attention HACK : This is specific to Sun JVM and may not function on other JVM or other version of JVM... Currently checked with JDK
	 * 1.5.x & 1.6.x
	 */
	private static void resetJavaLibraryPath() {
		synchronized (Runtime.getRuntime()) {
			try {
				Field field = ClassLoader.class.getDeclaredField("usr_paths");
				field.setAccessible(true);
				field.set(null, null);

				field = ClassLoader.class.getDeclaredField("sys_paths");
				field.setAccessible(true);
				field.set(null, null);
			} catch (NoSuchFieldException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
