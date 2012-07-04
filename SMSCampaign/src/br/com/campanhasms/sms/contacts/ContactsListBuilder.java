package br.com.campanhasms.sms.contacts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.TreeSet;

import br.com.campanhasms.model.Contato;

public class ContactsListBuilder {

	public static Contato[] getContactsFromFile(File file) {
		try {
			TreeSet<Contato> treeSet = new TreeSet<Contato>();
			InputStreamReader isr = new InputStreamReader(new FileInputStream(file));
			BufferedReader bufReader = new BufferedReader(isr);
			String readedLine;
			while ((readedLine = bufReader.readLine()) != null) {
				if (!"".equals(readedLine)) {
					treeSet.add(new Contato(readedLine));
				}
			}
			bufReader.close();
			isr.close();
			return treeSet.toArray(new Contato[treeSet.size()]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
