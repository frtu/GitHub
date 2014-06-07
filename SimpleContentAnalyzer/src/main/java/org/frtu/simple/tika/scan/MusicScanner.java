package org.frtu.simple.tika.scan;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.metadata.XMPDM;
import org.frtu.simple.lucene.IndexHandler;
import org.frtu.simple.lucene.IndexHandler.IndexWriterCallback;
import org.frtu.simple.lucene.LuceneHandlerFactory;
import org.frtu.simple.lucene.SearchHandler;
import org.frtu.simple.tika.model.AudioItem;

import com.github.frtu.simple.scan.DirectoryScanner;
import com.github.frtu.simple.scan.FileScannerObserver;

public class MusicScanner {
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(MusicScanner.class);

	public static void main(String[] args) throws IOException {
		// File mediaFolder = new File("M:\\[iTunes Library]\\Music");
		 File mediaFolder = new File("C:\\_Media_\\Music");
//		File mediaFolder = new File("C:\\_Media_\\_Classe_");
//		File mediaFolder = new File("\\\\NAS-FRED\\Multimedia\\Music");
//		File mediaFolder = new File("D:\\[_Backup_]\\Music");
		 
		 
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

	private static AudioItem toAudioItem(File file, Metadata metadata) {
		AudioItem audioItem = new AudioItem(file);
		audioItem.setAlbum(metadata.get(XMPDM.ALBUM));
		audioItem.setTitle(metadata.get(TikaCoreProperties.TITLE));
		audioItem.setArtist(metadata.get(XMPDM.ARTIST));
		audioItem.setComposer(metadata.get(XMPDM.COMPOSER));
		audioItem.setGenre(metadata.get(XMPDM.GENRE));
		audioItem.setDuration(metadata.get(XMPDM.DURATION));
		audioItem.setAudioChannel(metadata.get(XMPDM.AUDIO_CHANNEL_TYPE));
		audioItem.setCompression(metadata.get(XMPDM.AUDIO_COMPRESSOR));
		audioItem.setSampleRate(metadata.get(XMPDM.AUDIO_SAMPLE_RATE));
		audioItem.setReleaseDate(metadata.get(XMPDM.RELEASE_DATE));
		audioItem.setTrackNumber(metadata.get(XMPDM.TRACK_NUMBER));
		audioItem.setComment(metadata.get(XMPDM.LOG_COMMENT));
		return audioItem;
	}
	
	public static List<AudioItem> scanFolder(File mediaFolder) {
		final List<AudioItem> listAudio = new ArrayList<AudioItem>();

		FileScannerObserver fileScanner = new FileScannerObserver() {
			@Override
			public void scanFile(File file) {
				try {
					logger.info("====== {} ====== {} ======", file.getAbsolutePath(), file.length());
					InputStream input = new FileInputStream(file);
					Metadata metadata = new Metadata();
					new Tika().parse(input, metadata);
					
					AudioItem audioItem = toAudioItem(file, metadata);
					listAudio.add(audioItem);

					if (logger.isDebugEnabled()) {
						String[] names = metadata.names();
						for (String metadataName : names) {
							String metadataValue = metadata.get(metadataName);
							logger.debug("{} : {}", metadataName, metadataValue);
						}
						logger.debug(audioItem.toString());
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
