package org.frtu.simple.tika.catalog;

import java.util.HashMap;
import java.util.HashSet;

import lombok.Data;

import org.frtu.simple.tika.model.AudioItem;

public @Data class CatalogRepository {
	private HashMap<String, HashSet<String>> albumNames = new HashMap<String, HashSet<String>>();
	private HashMap<String, HashSet<String>> artistNames = new HashMap<String, HashSet<String>>();
	
	public void addAudioItem(AudioItem audioItem) {
		String artist = audioItem.getArtist();
		String albumName = audioItem.getAlbum();
		
		addItem(albumNames, albumName, artist);
		addItem(artistNames, artist, albumName);
	}

	private void addItem(HashMap<String, HashSet<String>> catalog, String key, String uniqValue) {
		HashSet<String> itemList = catalog.get(key);
		if (itemList == null) {
			itemList = new HashSet<String>();
			catalog.put(key, itemList);
		}
		itemList.add(uniqValue);
	}
}
