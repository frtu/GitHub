package com.github.frtu.maven.plugin.logger;

import org.apache.maven.plugin.logging.Log;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.spi.LoggerFactoryBinder;

/**
 * Singleton LoggerFactory that auto register himself.
 * 
 * http://www.slf4j.org/faq.html#slf4j_compatible
 * 
 * @author frtu
 */
public class MavenLoggerFactory implements ILoggerFactory, LoggerFactoryBinder {
	private Log innerLogger;

	private static MavenLoggerFactory INSTANCE;

	public static Logger registerMavenLoggerAndGet(Log innerLogger, Class<?> clazz) {
		return registerMavenLoggerAndGet(innerLogger, clazz.getCanonicalName());
	}

	public static Logger registerMavenLoggerAndGet(Log innerLogger, String name) {
		registerMavenLogger(innerLogger);
		return INSTANCE.getLogger(name);
	}

	/**
	 * Provide the Log implementation to this factory. It is usually done with {@link org.apache.maven.plugin.Mojo#getLog()}.
	 * 
	 * @param innerLogger
	 */
	public static void registerMavenLogger(Log innerLogger) {
		if (INSTANCE == null) {
			INSTANCE = new MavenLoggerFactory(innerLogger);
		}

		if (INSTANCE.innerLogger != innerLogger) {
			throw new IllegalArgumentException(
					"You've previously register a org.apache.maven.plugin.logging.Log that is NOT the one that you're passing !");
		}
	}

	private MavenLoggerFactory(Log innerLogger) {
		super();
		this.innerLogger = innerLogger;
	}

	@Override
	public Logger getLogger(String name) {
		return new MavenLogToSlf4jAdapter(innerLogger, name);
	}

	/**
	 * Allow to register himself
	 */
	@Override
	public ILoggerFactory getLoggerFactory() {
		if (INSTANCE == null) {
			throw new IllegalStateException(
					"You NEED to call MavenLoggerFactory.registerMavenLogger(mojo.getLog()) before being able to use the wrapper!");
		}
		return INSTANCE;
	}

	/**
	 * Allow to register himself
	 */
	@Override
	public String getLoggerFactoryClassStr() {
		return MavenLoggerFactory.class.getCanonicalName();
	}
}
