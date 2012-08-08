package br.com.campanhasms.sms.contacts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.concurrent.ConcurrentSkipListSet;

import org.apache.log4j.Logger;

import br.com.campanhasms.model.Contato;
import br.com.campanhasms.sms.contacts.normalization.model.ContactFactory;

public class ContactsListBuilder {

	private static final Logger LOGGER = Logger.getLogger(ContactsListBuilder.class);

	public static Contato[] getContactsFromFile(File file) {
		try {
			LOGGER.info("Importing contact from file: " + file.getAbsolutePath());
			ConcurrentSkipListSet<Contato> concurrentSkipListSet = new ConcurrentSkipListSet<Contato>();
			InputStreamReader isr = new InputStreamReader(new FileInputStream(file), Charset.defaultCharset());
			BufferedReader bufReader = new BufferedReader(isr);
			String readedLine;
			while ((readedLine = bufReader.readLine()) != null) {
				if (!"".equals(readedLine)) {
					try {
						concurrentSkipListSet.add(ContactFactory.getInstance().createContact(Long.valueOf(readedLine)));
					} catch (NumberFormatException e) {
						LOGGER.error("Error when creating admin contact ", e);
					}
				}
			}
			bufReader.close();
			isr.close();
			return concurrentSkipListSet.toArray(new Contato[concurrentSkipListSet.size()]);
		} catch (Exception e) {
			LOGGER.info("Error when importing contact from file: " + file.getAbsolutePath(), e);
		}
		return null;
	}
}
