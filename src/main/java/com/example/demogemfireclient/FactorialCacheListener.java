package com.example.demogemfireclient;

import com.gemstone.gemfire.cache.EntryEvent;
import com.gemstone.gemfire.cache.util.CacheListenerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by derrickwong on 29/5/2017.
 */
@Slf4j
public class FactorialCacheListener extends CacheListenerAdapter<Long, Long> {
	@Override
	public void afterCreate(EntryEvent e) {
		log.info("Created " + e.getNewValue());
	}

	@Override
 	public void afterUpdate(EntryEvent e) {
		log.info("Updated " + e.getOldValue());
	}

}
