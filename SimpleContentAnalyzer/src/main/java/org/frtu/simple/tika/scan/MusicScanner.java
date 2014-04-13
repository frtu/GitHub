package org.frtu.simple.tika.scan;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.frtu.simple.lucene.IndexHandler;
import org.frtu.simple.lucene.IndexHandler.IndexWriterCallback;
import org.frtu.simple.lucene.LuceneHandlerFactory;
import org.frtu.simple.scan.DirectoryScanner;
import org.frtu.simple.scan.FileScannerObserver;
import org.frtu.simple.tika.model.AudioItem;

public class MusicScanner {
	public static void main(String[] args) throws IOException {
		// File mediaFolder = new File("M:\\[iTunes Library]\\Music");
		File mediaFolder = new File("C:\\_Media_\\Music");

		final List<AudioItem> listAudio = new ArrayList<AudioItem>();

		FileScannerObserver fileScanner = new FileScannerObserver() {
			@Override
			public void scanFile(File file) {
				try {
					System.out.println("======" + file.getAbsolutePath() + "======" + file.length() + "======");
					InputStream input = new FileInputStream(file);
					Metadata metadata = new Metadata();
					new Tika().parse(input, metadata);

					AudioItem audioItem = new AudioItem(file, metadata);
					listAudio.add(audioItem);
					// String[] names = metadata.names();
					// for (String metadataName : names) {
					// String metadataValue = metadata.get(metadataName);
					// System.out.println(metadataName+":"+metadataValue);
					// }
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		};

		DirectoryScanner directoryScanner = new DirectoryScanner(fileScanner);
		directoryScanner.setFileExtensionToFilter("mp3");
		directoryScanner.scanDirectory(mediaFolder);

		File file = new File("target/audio_indexes");
		LuceneHandlerFactory luceneHandlerFactory = new LuceneHandlerFactory(file);
		IndexHandler indexHandler = luceneHandlerFactory.buildIndexHandler(OpenMode.CREATE);
		indexHandler.execute(new IndexWriterCallback() {
			@Override
			public Object doIt(IndexWriter indexWriter) {
				try {
					for (AudioItem audioItem : listAudio) {
						indexWriter.addDocument(audioItem.toLuceneDocument());
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}
		});
		System.out.println(file.getAbsolutePath());
	}
}
