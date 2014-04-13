package org.frtu.simple.tika.model;

import java.io.File;

import lombok.Data;

import org.apache.tika.metadata.Metadata;

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

	private Metadata metadata;

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

	public MediaItem(File file, Metadata metadata) {
		this(file);
		this.metadata = metadata;
	}
}
