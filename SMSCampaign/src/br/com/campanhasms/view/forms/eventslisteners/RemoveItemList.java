package br.com.campanhasms.view.forms.eventslisteners;

import javax.swing.DefaultListModel;

public class RemoveItemList<E> {

	private E itemToRemove;
	private DefaultListModel<E> listModel;

	public RemoveItemList(DefaultListModel<E> listModel, E itemToRemove) {
		super();
		this.listModel = listModel;
		this.itemToRemove = itemToRemove;
	}

	public boolean execute() {
		for (int i = 0; i < this.listModel.getSize(); i++) {
			if (this.listModel.getElementAt(i).equals(this.itemToRemove)) {
				this.listModel.removeElement(this.itemToRemove);

				return true;
			}
		}
		return false;
	}

}
