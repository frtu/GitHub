package org.frtu.simple.tika.model;

import java.io.File;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.metadata.XMPDM;
import org.hibernate.search.annotations.Indexed;
import org.springframework.util.StringUtils;

@Indexed(index = "indexes/audio")
@Data
@EqualsAndHashCode(callSuper = true)
public class AudioItem extends MediaItem {
	private static final String UNKNOWN = "UNKNOWN";

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
	private String duration;

	// xmpDM:audioChannelType
	private String audioChannel;
	// xmpDM:audioCompressor
	private String compression;
	// xmpDM:audioSampleRate
	private String sampleRate;

	// xmpDM:releaseDate
	private String releaseDate;
	// xmpDM:trackNumber
	private String trackNumber;
	// xmpDM:logComment
	private String comment;

	public AudioItem(File file, Metadata metadata) {
		super(file, metadata);
		this.album = metadata.get(XMPDM.ALBUM);
		this.title = metadata.get(TikaCoreProperties.TITLE);
		this.artist = metadata.get(XMPDM.ARTIST);
		this.composer = metadata.get(XMPDM.COMPOSER);
		this.genre = metadata.get(XMPDM.GENRE);
		this.duration = metadata.get(XMPDM.DURATION);
		this.audioChannel = metadata.get(XMPDM.AUDIO_CHANNEL_TYPE);
		this.compression = metadata.get(XMPDM.AUDIO_COMPRESSOR);
		this.sampleRate = metadata.get(XMPDM.AUDIO_SAMPLE_RATE);
		this.releaseDate = metadata.get(XMPDM.RELEASE_DATE);
		this.trackNumber = metadata.get(XMPDM.TRACK_NUMBER);
		this.comment = metadata.get(XMPDM.LOG_COMMENT);
	}

	public AudioItem(Document document) {
		super(document.get("fileAbsolutePath"));

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
		return !StringUtils.hasText(album) ? UNKNOWN : album;
	}

	public String getTitle() {
		return !StringUtils.hasText(title) ? UNKNOWN : ("no title".equals(title) ? UNKNOWN : title);
	}

	public String getArtist() {
		return !StringUtils.hasText(artist) ? UNKNOWN : artist;
	}

	public String getComposer() {
		return !StringUtils.hasText(composer) ? UNKNOWN : composer;
	}

	public String getGenre() {
		return !StringUtils.hasText(genre) ? UNKNOWN : genre;
	}

	public String getDuration() {
		return !StringUtils.hasText(duration) ? UNKNOWN : duration;
	}

	public String getAudioChannel() {
		return !StringUtils.hasText(audioChannel) ? UNKNOWN : audioChannel;
	}

	public String getCompression() {
		return !StringUtils.hasText(compression) ? UNKNOWN : compression;
	}

	public String getSampleRate() {
		return !StringUtils.hasText(sampleRate) ? UNKNOWN : sampleRate;
	}

	public String getReleaseDate() {
		return !StringUtils.hasText(releaseDate) ? UNKNOWN : releaseDate;
	}

	public String getTrackNumber() {
		return !StringUtils.hasText(trackNumber) ? UNKNOWN : trackNumber;
	}

	public String getComment() {
		return !StringUtils.hasText(comment) ? UNKNOWN : comment;
	}
}
