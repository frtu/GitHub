package com.github.frtu.simple.tika.model;

import java.io.File;

import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.hibernate.search.annotations.Indexed;

@Indexed(index = "indexes/video")
@Data
@EqualsAndHashCode(callSuper = true)
public class VideoItem extends MediaItem {
	@Id
	// Lucene persistence id
	private int docId;

	// title
	private String title;

	private String duration;

	private String releaseDate;
	private String comment;

	public VideoItem(File file) {
		super(file);
	}
}
