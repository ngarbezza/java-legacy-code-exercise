package ar.edu.unq.sasa.gui.util.tables;

import ar.edu.unq.sasa.gui.util.ToStringConverter;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.List;

import static ar.edu.unq.sasa.gui.util.ReflectionUtils.invokeJavaBean;

/**
 * Clase que facilita el uso de {@link JTable}, mediante una implementación
 * basada en la interfaz que provee {@link TableModel}, a través de la clase
 * {@link AbstractTableModel}.
 */
public class ReadOnlyTableModel<T> extends AbstractTableModel {

	private static final long serialVersionUID = 2709840580806354328L;

	private List<T> model;

	// INVARIANTE DE REPRESENTACIÓN:
	//		las tres listas siguientes tienen el mismo tamaño.

	/**
	 * Son los nombres de los atributos del modelo que será
	 * invocados por reflection bajo la convención JavaBeans.
	 */
	private final List<String> attributes;

	/**
	 * Los nombres que se verán en cada una de las columnas.
	 */
	private final List<String> columnNames;

	/**
	 * Los conversores de Object a String que usa para cada columna.
	 */
	@SuppressWarnings("rawtypes")
	private final List<ToStringConverter> converters;

	public ReadOnlyTableModel() {
		this(new ArrayList<>());
	}

	@SuppressWarnings("rawtypes")
	public ReadOnlyTableModel(List<T> aModel) {
		model = aModel;
		attributes = new ArrayList<>();
		columnNames = new ArrayList<>();
		converters = new ArrayList<>();
	}

	public List<T> getModel() {
		return model;
	}

	public void setModel(List<T> aModel) {
		model = aModel;
		fireTableChanged(new TableModelEvent(this));
	}

	@SuppressWarnings("rawtypes")
	public void addColumn(String columnName, String attribute) {
		addColumn(columnName, attribute, new ToStringConverter());
	}

	@SuppressWarnings("rawtypes")
	public void addColumn(String columnName, String attribute, ToStringConverter converter) {
		columnNames.add(columnName);
		attributes.add(attribute);
		converters.add(converter);
	}

	@Override
	public int getRowCount() {
		return getModel().size();
	}

	@Override
	public String getColumnName(int columnIndex) {
		return columnNames.get(columnIndex);
	}

	@Override
	public int getColumnCount() {
		return columnNames.size();
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	@Override
	@SuppressWarnings("unchecked")
	public String getValueAt(int rowIndex, int columnIndex) {
		T object = getModel().get(rowIndex);
		Object result = invokeJavaBean(object, attributes.get(columnIndex));
		return converters.get(columnIndex).convert(result);
	}
}
