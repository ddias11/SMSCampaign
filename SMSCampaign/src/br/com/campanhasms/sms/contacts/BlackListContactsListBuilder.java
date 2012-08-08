package br.com.campanhasms.sms.contacts;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.ConcurrentSkipListSet;

import org.apache.log4j.Logger;

import br.com.campanhasms.model.Contato;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

public class BlackListContactsListBuilder {

	private static final String CONTACTS_IN_BLACK_LIST_XML = "res/properties/blackListConatcts.xml";
	private static final Logger LOGGER = Logger.getLogger(BlackListContactsListBuilder.class);

	@SuppressWarnings("unchecked")
	public static ConcurrentSkipListSet<Contato> getPersistedContactsInBlackList() {

		try {
			XStream xstream = new XStream(new StaxDriver());
			xstream.autodetectAnnotations(true);
			return (ConcurrentSkipListSet<Contato>) xstream.fromXML(new File(CONTACTS_IN_BLACK_LIST_XML));
		} catch (Exception e) {
			LOGGER.error("Error when reading Black List Contacts from file: " + CONTACTS_IN_BLACK_LIST_XML, e);
			throw e;
		}
	}

	public static void persistContactsInBlackList(ConcurrentSkipListSet<Contato> contatos) throws Exception {

		try {
			XStream xstream = new XStream(new StaxDriver());
			xstream.autodetectAnnotations(true);
			File file = new File(CONTACTS_IN_BLACK_LIST_XML);
			if (!file.exists()) {
				file.createNewFile();
			}
			xstream.toXML(contatos, new FileOutputStream(file));
		} catch (Exception e) {
			LOGGER.error("Error when writing Black List Contacts to file: " + CONTACTS_IN_BLACK_LIST_XML, e);
			throw e;
		}
	}

}
