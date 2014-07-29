package ar.edu.unq.sasa.gui.util.lists;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.JList;

/**
 * Modelo sencillo de lista para usar con las {@link JList}.
 */
public class EasyListModel<T> extends AbstractListModel<T> {

	private static final long serialVersionUID = 4881231145326705206L;

	private List<T> model;

	public EasyListModel() {
		this(new ArrayList<T>());
	}

	public EasyListModel(List<T> model) {
		this.model = model;
	}

	public List<T> getModel() {
		return model;
	}

	public void setModel(List<T> model) {
		this.model = model;
		fireContentsChanged(this, 0, getSize());
	}

	public void addItem(T item) {
		this.model.add(item);
		fireIntervalAdded(this, getSize() - 1, getSize());
	}

	public void removeItem(T item) {
		this.model.remove(item);
		fireIntervalRemoved(this, 0, getSize());
	}

	@Override
	public T getElementAt(int index) {
		return this.getModel().get(index);
	}

	@Override
	public int getSize() {
		return this.getModel().size();
	}
}
