package br.com.campanhasms.persistence;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;
import org.prevayler.Transaction;

import br.com.campanhasms.persistence.model.SystemPrevaylerModel;
import br.com.campanhasms.properties.PrevaylerProperties;

public class SystemPrevayler {

	private static final Logger LOGGER = Logger.getLogger(SystemPrevayler.class);
	private static Prevayler systemPrevayler = null;

	private SystemPrevayler() {
	}

	public static void execute(Transaction transaction) {
		LOGGER.info("Executing persistence transaction");
		getPrevaylerInstance().execute(transaction);
		takeSnapShot();
	}

	private static void expurgeFiles(FileFilter filter) {
		File[] listFilteredFiles = new File(PrevaylerProperties.getString("SystemPrevayler.PREVAYLER_BASE_DIR")).listFiles(filter); //$NON-NLS-1$

		for (int i = 0; i < listFilteredFiles.length - 1; i++) {
			LOGGER.info("Expurge file: " + listFilteredFiles[i].getName());
			listFilteredFiles[i].delete();
		}
	}

	private static void expurgeJournalFiles() {
		expurgeFiles(new FileFilter() {
			@Override
			public boolean accept(File arg0) {
				return arg0.getName().matches(".*\\.journal");
			}
		});
	}

	private static void expurgeSnapshotFiles() {
		expurgeFiles(new FileFilter() {
			@Override
			public boolean accept(File arg0) {
				return arg0.getName().matches(".*\\.snapshot");
			}
		});
	}

	public static Prevayler getPrevaylerInstance() {
		if (systemPrevayler == null) {
			PrevaylerFactory factory = new PrevaylerFactory();
			factory.configureTransactionFiltering(false);
			factory.configurePrevalenceDirectory(PrevaylerProperties.getString("SystemPrevayler.PREVAYLER_BASE_DIR")); //$NON-NLS-1$
			factory.configurePrevalentSystem(new SystemPrevaylerModel());
			factory.configureJournalFileSizeThreshold(new Integer(PrevaylerProperties
					.getString("SystemPrevayler.PREVAYLER_JOURNAL_FILE_SIZE"))); //$NON-NLS-1$
			try {
				systemPrevayler = factory.create();
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}

		return systemPrevayler;
	}

	public static SystemPrevaylerModel getSystemPrevaylerModel() {
		return (SystemPrevaylerModel) getPrevaylerInstance().prevalentSystem();
	}

	public static void takeSnapShot() {
		try {
			LOGGER.info("Taking Snaphhot");
			getPrevaylerInstance().takeSnapshot();

			expurgeSnapshotFiles();

			expurgeJournalFiles();

		} catch (IOException e) {
			LOGGER.error("Error when taking Snapshot", e);
		}
	}

}
