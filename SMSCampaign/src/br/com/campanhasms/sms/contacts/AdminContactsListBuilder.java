package br.com.campanhasms.sms.contacts;

import java.io.File;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import br.com.campanhasms.model.Contato;

public class AdminContactsListBuilder {

	private static final String ADMIN_CONTACTS_XML = "/adminContacts.xml";
	private static final Logger LOGGER = Logger.getLogger(AdminContactsListBuilder.class);

	public static Contato[] getAdminContacts() {
		
		try {
			TreeSet<Contato> treeSet = new TreeSet<Contato>();
			File fXmlFile = new File(AdminContactsListBuilder.class.getResource(ADMIN_CONTACTS_XML).getFile());
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

			NodeList contactsNodeList = doc.getElementsByTagName("Contact");
			for (int i = 0; i < contactsNodeList.getLength(); i++) {
				String readedValue = contactsNodeList.item(i).getChildNodes().item(0).getNodeValue();
				treeSet.add(new Contato(readedValue));
			}

			return treeSet.toArray(new Contato[treeSet.size()]);
		} catch (Exception e) {
			LOGGER.info("Error when importing admin contact from file: " + ADMIN_CONTACTS_XML, e);
			return null;
		}
	}
}
