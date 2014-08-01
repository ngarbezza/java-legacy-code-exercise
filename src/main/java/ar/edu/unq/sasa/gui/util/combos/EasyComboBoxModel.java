package ar.edu.unq.sasa.gui.util.combos;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import ar.edu.unq.sasa.gui.util.ToStringConverter;

public class EasyComboBoxModel<T> extends AbstractListModel<T> implements ComboBoxModel<T> {

	private static final long serialVersionUID = -7637136543588118644L;

	protected List<T> model;
	protected T selection;
	protected ToStringConverter<T> converter;

	public EasyComboBoxModel() {
		this(new ArrayList<T>());
	}

	public EasyComboBoxModel(List<T> theModel) {
		this(theModel, new ToStringConverter<T>());
	}

	public EasyComboBoxModel(List<T> aModel, ToStringConverter<T> aConverter) {
		model = aModel;
		converter = aConverter;
	}

	public List<T> getModel() {
		return model;
	}

	public void setModel(List<T> aModel) {
		model = aModel;
		fireContentsChanged(aModel, 0, getSize());
	}

	@Override
	public T getSelectedItem() {
		return selection;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void setSelectedItem(Object anItem) {
		selection = (T) anItem;
	}

	@Override
	public T getElementAt(int anIndex) {
		// null indica "ningún elemento seleccionado"
		// y no forma parte de la lista "model"
		return (anIndex == 0) ? null : model.get(anIndex - 1);
	}

	@Override
	public int getSize() {
		return model.size() + 1;
	}
}
