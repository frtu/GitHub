package org.frtu.simple.tika.model;

import java.io.File;

import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.hibernate.search.annotations.Indexed;

@Indexed(index = "indexes/audio")
@Data
@EqualsAndHashCode(callSuper = true)
public class AudioItem extends MediaItem {
	@Id
	// Lucene persistence id
	private int docId;

	// xmpDM:album
	private String album;
	// title
	private String title;
	// xmpDM:artist
	private String artist;

	// xmpDM:composer
	private String composer;

	// xmpDM:genre
	private String genre;

	// xmpDM:duration
	/** Duration time in millisec */
	private String duration;

	// xmpDM:audioChannelType
	private String audioChannel;
	// xmpDM:audioCompressor
	private String compression;
	// xmpDM:audioSampleRate
	/** In Hz */
	private String sampleRate;

	// xmpDM:releaseDate
	private String releaseDate;
	// xmpDM:trackNumber
	private String trackNumber;
	// xmpDM:logComment
	private String comment;

	public AudioItem(File file) {
		super(file);
	}

	public AudioItem(int docId, Document document) {
		super(document.get("fileAbsolutePath"));

		this.docId = docId;
		this.album = document.get("album");
		this.title = document.get("title");
		this.artist = document.get("artist");
		this.composer = document.get("composer");
		this.genre = document.get("genre");
		this.duration = document.get("duration");
		this.audioChannel = document.get("audioChannel");
		this.compression = document.get("compression");
		this.sampleRate = document.get("sampleRate");
		this.releaseDate = document.get("releaseDate");
		this.trackNumber = document.get("trackNumber");
		this.comment = document.get("comment");
	}

	public Document toLuceneDocument() {
		Document doc = new Document();
		doc.add(new StringField("fileAbsolutePath", getFileAbsolutePath(), Field.Store.YES));
		doc.add(new StringField("fileName", getFileName(), Field.Store.YES));
		doc.add(new StringField("fileSize", Long.toString(getFileSize()), Field.Store.YES));
		doc.add(new TextField("album", getAlbum(), Field.Store.YES));
		doc.add(new TextField("title", getTitle(), Field.Store.YES));
		doc.add(new TextField("artist", getArtist(), Field.Store.YES));
		doc.add(new TextField("composer", getComposer(), Field.Store.YES));
		doc.add(new TextField("genre", getGenre(), Field.Store.YES));
		doc.add(new StringField("duration", getDuration(), Field.Store.YES));
		doc.add(new StringField("audioChannel", getAudioChannel(), Field.Store.YES));
		doc.add(new TextField("compression", getCompression(), Field.Store.YES));
		doc.add(new StringField("sampleRate", getSampleRate(), Field.Store.YES));
		doc.add(new TextField("releaseDate", getReleaseDate(), Field.Store.YES));
		doc.add(new TextField("trackNumber", getTrackNumber(), Field.Store.YES));
		doc.add(new TextField("comment", getComment(), Field.Store.YES));

		return doc;
	}

	public String getAlbum() {
		return getNonNullValue(album, MediaItem.UNKNOWN);
	}

	public String getTitle() {
		if ("no title".equals(title)) {
			return MediaItem.UNKNOWN;
		}
		return getNonNullValue(title, MediaItem.UNKNOWN);
	}

	public String getArtist() {
		return getNonNullValue(artist, MediaItem.UNKNOWN);
	}

	public String getComposer() {
		return getNonNullValue(composer, MediaItem.UNKNOWN);
	}

	public String getGenre() {
		return getNonNullValue(genre, MediaItem.UNKNOWN);
	}

	public String getDuration() {
		return getNonNullValue(duration, MediaItem.UNKNOWN);
	}

	public String getAudioChannel() {
		return getNonNullValue(audioChannel, MediaItem.UNKNOWN);
	}

	public String getCompression() {
		return getNonNullValue(compression, MediaItem.UNKNOWN);
	}

	public String getSampleRate() {
		return getNonNullValue(sampleRate, MediaItem.UNKNOWN);
	}

	public String getReleaseDate() {
		return getNonNullValue(releaseDate, MediaItem.UNKNOWN);
	}

	public String getTrackNumber() {
		return getNonNullValue(trackNumber, MediaItem.UNKNOWN);
	}

	public String getComment() {
		return getNonNullValue(comment, MediaItem.UNKNOWN);
	}
}
