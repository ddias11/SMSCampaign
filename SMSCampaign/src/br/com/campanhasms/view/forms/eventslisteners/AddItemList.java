package br.com.campanhasms.view.forms.eventslisteners;

import javax.swing.DefaultListModel;

public class AddItemList<E> {

	private E itemToAdd;
	private DefaultListModel<E> listModel;

	public AddItemList(DefaultListModel<E> listModel, E itemToAdd) {
		super();
		this.listModel = listModel;
		this.itemToAdd = itemToAdd;
	}

	public boolean execute() {
		for (int i = 0; i < this.listModel.getSize(); i++) {
			if (this.listModel.getElementAt(i).equals(this.itemToAdd)) {
				return false;
			}
		}
		this.listModel.addElement(this.itemToAdd);
		return true;
	}

}
