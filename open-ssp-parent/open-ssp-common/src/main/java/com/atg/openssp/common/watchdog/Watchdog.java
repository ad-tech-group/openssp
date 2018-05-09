package com.atg.openssp.common.watchdog;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author André Schmer
 * 
 */
public class Watchdog implements Runnable {

	private static final Logger log = LoggerFactory.getLogger(Watchdog.class);

	private String resource;
	private WatchService watchService = null;
	private DynamicLoadable loader;
	private boolean autostart;
	private boolean shuttingDown = false;

	public Watchdog() {}

	/**
	 * Initiates a Watchdog for specific resource. File must be located within the resources location of a dev machine or within
	 * {@code {catalina.home}/properties}
	 * 
	 */
	public Watchdog(final DynamicLoadable loader, final boolean startImmediately) {
		if (loader == null) {
			throw new IllegalArgumentException("loader must be set");
		}
		resource = loader.getResourceFilename();
		if (resource == null) {
			throw new IllegalArgumentException("resource in loader must be set");
		}
		autostart = startImmediately;
		this.loader = loader;
		try {
			watchService = FileSystems.getDefault().newWatchService();
			final String path = loader.getResourceLocation();
			log.info("Watchdog [{}]", path);
			log.info("INFO: Watchdog [{} {}]", path, resource);
			Paths.get(path).register(watchService, ENTRY_MODIFY);
		} catch (final IOException e) {
			log.warn(e.getMessage(), e);
		}
	}

	/**
	 * Launches the Watchdog.
	 */
	@Override
	public void run() {
		log.info("launching watchdog ... [{}]", resource);
		if (autostart) {
			loader.readValues();
		}

		final AtomicBoolean modifyEventFired = new AtomicBoolean();
		modifyEventFired.set(false);
		while (shuttingDown == false) {
			try {
				final WatchKey watchKey = watchService.take();

				for (final WatchEvent<?> event : watchKey.pollEvents()) {
					@SuppressWarnings("unchecked")
					final WatchEvent<Path> watchEvent = (WatchEvent<Path>) event;
					if (resource.equals(watchEvent.context().toString())) {
						final WatchEvent.Kind<?> kind = event.kind();
						// This key is registered only
						// for ENTRY_MODIFY events,
						// but an OVERFLOW event can
						// occur regardless if events
						// are lost or discarded.
						switch (kind.name()) {
						case "ENTRY_MODIFY":
							if (!modifyEventFired.get()) {
								loader.readValues();
								modifyEventFired.set(true);
							}
							break;
						case "ENTRY_DELETE":
							log.info("watchdog recognizes delete event for {}", resource);
							break;
						case "OVERFLOW":
							continue;
						default:
							break;
						}
					}
				}
				// Reset the key -- this step is critical if you want to
				// receive further watch events. If the key is no longer
				// valid, the directory is inaccessible so exit the loop.
				watchKey.reset();
				modifyEventFired.set(false);
			} catch (final InterruptedException e) {
				log.warn(e.getMessage(), e);
			} catch (final ClosedWatchServiceException e) {
				log.warn(e.getMessage(), e);
			}
		}

		Thread.currentThread().interrupt();
	}

	public void shutDown() {
		shuttingDown = true;
		try {
			watchService.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

}
