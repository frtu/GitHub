package com.github.frtu.simple.mp3agic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;

import com.github.frtu.simple.lucene.IndexHandler;
import com.github.frtu.simple.lucene.IndexHandler.IndexWriterCallback;
import com.github.frtu.simple.lucene.LuceneHandlerFactory;
import com.github.frtu.simple.lucene.SearchHandler;
import com.github.frtu.simple.scan.DirectoryScanner;
import com.github.frtu.simple.scan.FileScannerObserver;
import com.github.frtu.simple.tika.model.AudioItem;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;

public class MusicLoader {
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(MusicLoader.class);

	public static void main(String[] args) throws IOException {
		// File mediaFolder = new File("C:\\_Media_\\Music");
		File mediaFolder = new File("D:\\[_Backup_]\\Music");

		final List<AudioItem> listAudio = scanFolder(mediaFolder);
		File indexDirectory = new File("target/audio_indexes");
		buildIndex(indexDirectory, listAudio);

		final List<AudioItem> readlistAudio = readIndex(indexDirectory);
		System.out.println(readlistAudio);
	}

	public static List<AudioItem> readIndex(File indexDirectory) {
		final List<AudioItem> listAudio = new ArrayList<AudioItem>();

		LuceneHandlerFactory luceneHandlerFactory = new LuceneHandlerFactory(indexDirectory);
		SearchHandler searchHandler = luceneHandlerFactory.buildSearchHandler();
		int maxDoc = searchHandler.getMaxDoc();
		logger.info("There is {} document(s)", maxDoc);
		for (int docId = 0; docId < maxDoc; docId++) {
			Document document = searchHandler.getDocById(docId);

			AudioItem audioItem = new AudioItem(docId, document);
			listAudio.add(audioItem);
		}
		return listAudio;
	}

	public static void buildIndex(File indexDirectory, final List<AudioItem> listAudio) throws IOException {
		LuceneHandlerFactory luceneHandlerFactory = new LuceneHandlerFactory(indexDirectory);
		IndexHandler indexHandler = luceneHandlerFactory.buildIndexHandler(OpenMode.CREATE);
		indexHandler.execute(new IndexWriterCallback() {
			@Override
			public Object doIt(IndexWriter indexWriter) {
				try {
					for (AudioItem audioItem : listAudio) {
						indexWriter.addDocument(audioItem.toLuceneDocument());
					}
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
				return null;
			}
		});
	}

	public static List<AudioItem> scanFolder(File mediaFolder) {
		final List<AudioItem> listAudio = new ArrayList<AudioItem>();

		FileScannerObserver fileScanner = new FileScannerObserver() {
			@Override
			public void scanFile(File file) {
				try {
					logger.info("====== {} ====== {} ======", file.getAbsolutePath(), file.length());
					Mp3File mp3file = new Mp3File(file.getAbsolutePath());
					if (mp3file.hasId3v2Tag()) {
						ID3v2 id3v2Tag = mp3file.getId3v2Tag();

						AudioItem audioItem = new AudioItem(file);
						audioItem.setAlbum(id3v2Tag.getAlbum());// metadata.get(XMPDM.ALBUM));
						audioItem.setTitle(id3v2Tag.getTitle()); // metadata.get(TikaCoreProperties.TITLE));
						audioItem.setArtist(id3v2Tag.getArtist()); // metadata.get(XMPDM.ARTIST));
						audioItem.setComposer(id3v2Tag.getComposer()); // metadata.get(XMPDM.COMPOSER));
						audioItem.setGenre(id3v2Tag.getGenreDescription()); // metadata.get(XMPDM.GENRE));
						audioItem.setDuration(mp3file.getLengthInMilliseconds() + ""); // metadata.get(XMPDM.DURATION));
						audioItem.setAudioChannel(mp3file.getModeExtension()); // metadata.get(XMPDM.AUDIO_CHANNEL_TYPE));
						audioItem.setCompression("mp3");// metadata.get(XMPDM.AUDIO_COMPRESSOR));
						audioItem.setSampleRate(mp3file.getSampleRate() + ""); // metadata.get(XMPDM.AUDIO_SAMPLE_RATE));
						audioItem.setReleaseDate(id3v2Tag.getYear()); // metadata.get(XMPDM.RELEASE_DATE));
						audioItem.setTrackNumber(id3v2Tag.getTrack()); // metadata.get(XMPDM.TRACK_NUMBER));
						audioItem.setComment(id3v2Tag.getComment()); // metadata.get(XMPDM.LOG_COMMENT));

						// byte[] albumImageData = id3v2Tag.getAlbumImage();
						// if (albumImageData != null) {
						// System.out.println("Have album image data, length: " + albumImageData.length + " bytes");
						// System.out.println("Album image mime type: " + id3v2Tag.getAlbumImageMimeType());
						// }

						listAudio.add(audioItem);

						if (logger.isDebugEnabled()) {
							logger.debug(audioItem.toString());
						}
					} else {
						logger.error("File {} doesn't contains any metadata!", file.getAbsolutePath());
					}
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}

			}
		};

		DirectoryScanner directoryScanner = new DirectoryScanner(fileScanner);
		directoryScanner.setFileExtensionToFilter("mp3");
		directoryScanner.scanDirectory(mediaFolder);
		return listAudio;
	}
}
