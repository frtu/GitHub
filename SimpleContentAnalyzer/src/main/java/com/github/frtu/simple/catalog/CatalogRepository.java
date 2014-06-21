package com.github.frtu.simple.catalog;

import java.io.File;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import lombok.Getter;
import lombok.NoArgsConstructor;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;

import com.github.frtu.simple.lucene.IndexHandler;
import com.github.frtu.simple.lucene.LuceneHandlerFactory;
import com.github.frtu.simple.lucene.SearchHandler;
import com.github.frtu.simple.tika.model.AudioItem;

@Repository
@NoArgsConstructor
public class CatalogRepository {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CatalogRepository.class);

    private LinkedMultiValueMap<String, AudioItem> itemsRepository = new LinkedMultiValueMap<String, AudioItem>();
    // private HashMap<String, ArrayList<AudioItem>> itemsRepository = new HashMap<String, ArrayList<AudioItem>>();

    @Getter
    private HashMap<String, HashSet<String>> albumNames = new HashMap<String, HashSet<String>>();
    @Getter
    private HashMap<String, HashSet<String>> artistNames = new HashMap<String, HashSet<String>>();

    private LuceneHandlerFactory luceneHandlerFactory;
    private IndexHandler indexHandler;

    public CatalogRepository(File indexDirectory) {
        this(indexDirectory, false);
    }

    public CatalogRepository(File indexDirectory, boolean restartFromZero) {
        this();
        if (indexDirectory != null) {
            logger.info("Create LuceneHandlerFactory with indexDirectory={} and restartFromZero={}",
                        indexDirectory.getAbsolutePath(), restartFromZero);
            luceneHandlerFactory = new LuceneHandlerFactory(indexDirectory);

            OpenMode mode = (restartFromZero) ? OpenMode.CREATE : OpenMode.CREATE_OR_APPEND;
            indexHandler = luceneHandlerFactory.buildIndexHandler(mode);
        }
    }

    @PostConstruct
    public void loadAllItems() {
        if (luceneHandlerFactory != null) {
            SearchHandler searchHandler = luceneHandlerFactory.buildSearchHandler();
            int maxDoc = searchHandler.getMaxDoc();
            logger.info("Loaded {} document(s)", maxDoc);

            for (int docId = 0; docId < maxDoc; docId++) {
                Document document = searchHandler.getDocById(docId);

                // reader.isDeleted(i);

                AudioItem audioItem = new AudioItem(docId, document);
                logger.debug("Loaded document docId={} audioItem={}", docId, audioItem);
                addToCatalogMaps(audioItem);
                addItemToCatalogList(audioItem);
            }
        } else {
            logger.warn("This repository need to be construct with indexDirectory to work!");
        }
    }

    public void addAudioItem(AudioItem audioItem) {
        addToCatalogMaps(audioItem);
        addItemToCatalogList(audioItem);

        // Persistence on?
        if (indexHandler != null) {
            indexHandler.addDocument(audioItem.toLuceneDocument());
        }
    }

    private void addToCatalogMaps(AudioItem audioItem) {
        String artist = audioItem.getArtist();
        String albumName = audioItem.getAlbum();

        addElement(albumNames, albumName, artist);
        addElement(artistNames, artist, albumName);
    }

    private void addElement(HashMap<String, HashSet<String>> catalog, String key, String uniqValue) {
        HashSet<String> itemList = catalog.get(key);
        if (itemList == null) {
            itemList = new HashSet<String>();
            catalog.put(key, itemList);
        }
        itemList.add(uniqValue);
    }

    private String getAlbumKey(String artist, String albumName) {
        String albumKey = artist + albumName;
        return albumKey;
    }

    private void addItemToCatalogList(AudioItem audioItem) {
        String artist = audioItem.getArtist();
        String albumName = audioItem.getAlbum();

        String albumKey = getAlbumKey(artist, albumName);
        itemsRepository.add(albumKey, audioItem);
        // List<AudioItem> itemList = getItems(albumKey);
        // if (itemList == null) {
        // itemList = new ArrayList<AudioItem>();
        // itemsRepository.put(albumKey, itemList);
        // }
        // itemList.add(audioItem);
    }

    public List<AudioItem> getItems(String artist, String albumName) {
        String albumKey = getAlbumKey(artist, albumName);
        return getItems(albumKey);
    }

    public List<AudioItem> getItems(String albumKey) {
        List<AudioItem> itemList = itemsRepository.get(albumKey);
        return itemList;
    }

    public void printCatalog(PrintStream printStream) {
        printCatalog(printStream, "\n");
    }

    public void printCatalog(PrintStream printStream, String skipLine) {
        HashMap<String, HashSet<String>> catalogNames = getArtistNames();
        for (Iterator<String> iterator = catalogNames.keySet().iterator(); iterator.hasNext();) {
            String key = iterator.next();
            Set<String> keySet = catalogNames.get(key);

            for (String value : keySet) {
                printStream.print("==== " + key + " - " + value + "====" + skipLine);

                List<AudioItem> items = getItems(key, value);
                for (AudioItem audioItem : items) {
                    printStream.print(audioItem.toString() + skipLine);
                }
            }
        }
    }
}
