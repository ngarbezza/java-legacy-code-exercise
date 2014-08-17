package ar.edu.unq.sasa.gui.util.lists;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;

public class EasyListModel<T> extends AbstractListModel<T> {

	private static final long serialVersionUID = 4881231145326705206L;

	private List<T> model;

	public EasyListModel() {
		this(new ArrayList<T>());
	}

	public EasyListModel(List<T> aModel) {
		model = aModel;
	}

	public List<T> getModel() {
		return model;
	}

	public void setModel(List<T> aModel) {
		model = aModel;
		fireContentsChanged(this, 0, getSize());
	}

	public void addItem(T anItem) {
		model.add(anItem);
		fireIntervalAdded(this, getSize() - 1, getSize());
	}

	public void removeItem(T anItem) {
		model.remove(anItem);
		fireIntervalRemoved(this, 0, getSize());
	}

	@Override
	public T getElementAt(int anIndex) {
		return getModel().get(anIndex);
	}

	@Override
	public int getSize() {
		return getModel().size();
	}
}
