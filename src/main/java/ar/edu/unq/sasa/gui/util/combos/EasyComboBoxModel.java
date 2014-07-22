package ar.edu.unq.sasa.gui.util.combos;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

import ar.edu.unq.sasa.gui.util.ObjectToStringConverter;

/**
 * Model para {@link JComboBox} que facilita su uso.
 */
public class EasyComboBoxModel<T> extends AbstractListModel<T> implements ComboBoxModel<T> {

	private static final long serialVersionUID = -7637136543588118644L;

	protected List<T> model;
	protected T selection;
	protected ObjectToStringConverter converter;
	
	public EasyComboBoxModel() {
		this(new ArrayList<T>());
	}
 	
	public EasyComboBoxModel(List<T> theModel) {		
		this(theModel, new ObjectToStringConverter());
	}
	
	public EasyComboBoxModel(List<T> theModel, ObjectToStringConverter conv) {
		this.model = theModel;
		this.converter = conv;
	}

	public List<T> getModel() {
		return model;
	}
	
	public void setModel(List<T> theModel) {
		model = theModel;
		fireContentsChanged(theModel, 0, getSize());
	}
	
	@Override
	public T getSelectedItem() {
		return selection;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void setSelectedItem(Object anItem) {
		selection = (T)anItem;
	}

	@Override
	public T getElementAt(int index) {
		// null indica "ningún elemento seleccionado"
		// y no forma parte de la lista "model"
		return (index == 0)? null : model.get(index - 1);
	}

	@Override
	public int getSize() {
		return model.size() + 1;
	}
}
