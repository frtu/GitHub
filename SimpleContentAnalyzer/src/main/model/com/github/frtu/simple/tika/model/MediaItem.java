package com.github.frtu.simple.tika.model;

import java.io.File;

import lombok.Data;

import org.springframework.util.StringUtils;

/**
 * Basic class that link a File to a Media's metadata.
 * 
 * @author Frederic TU
 */
public @Data
class MediaItem {
	private File file;

	private String fileAbsolutePath;
	private String fileName;
	private long fileSize;

	static final String UNKNOWN = "UNKNOWN";

	public MediaItem(String fileAbsolutePath) {
		this(new File(fileAbsolutePath));
	}

	public MediaItem(File file) {
		super();
		this.file = file;

		this.fileAbsolutePath = file.getAbsolutePath();
		this.fileName = file.getName();
		this.fileSize = file.length();
	}

	protected String getNonNullValue(String value, String defaultValue) {
		return !StringUtils.hasText(value) ? defaultValue : value;
	}
}
