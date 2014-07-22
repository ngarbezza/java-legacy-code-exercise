package ar.edu.unq.sasa.gui.util.tables;

import static ar.edu.unq.sasa.gui.util.ReflectionUtils.invokeJavaBean;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import ar.edu.unq.sasa.gui.util.ObjectToStringConverter;

/**
 * Clase que facilita el uso de {@link JTable}, mediante una implementación
 * basada en la interfaz que provee {@link TableModel}, a través de la clase
 * {@link AbstractTableModel}.
 */
public class ReadOnlyTableModel<T> extends AbstractTableModel {

	private static final long serialVersionUID = 2709840580806354328L;

	private List<T> model;

	// INVARIANTE DE REPRESENTACION:
	//		las tres listas siguientes tienen el mismo tamaño.

	/**
	 * Son los nombres de los atributos del modelo que será
	 * invocados por reflection bajo la convención JavaBeans.
	 */
	private List<String> attributes;

	/**
	 * Los nombres que se verán en cada una de las columnas.
	 */
	private List<String> columnNames;

	/**
	 * Los convertidores de Object a String que usa para cada columna.
	 */
	private List<ObjectToStringConverter> converters;

	public ReadOnlyTableModel() {
		this(new ArrayList<T>());
	}

	public ReadOnlyTableModel(List<T> model) {
		this.model = model;
		this.attributes = new ArrayList<String>();
		this.columnNames = new ArrayList<String>();
		this.converters = new ArrayList<ObjectToStringConverter>();
	}

	public List<String> getAttributes() {
		return attributes;
	}

	public List<String> getColumnNames() {
		return columnNames;
	}

	public List<ObjectToStringConverter> getConverters() {
		return converters;
	}

	public List<T> getModel() {
		return model;
	}

	public void setModel(List<T> model) {
		this.model = model;
		this.fireTableChanged(new TableModelEvent(this));
	}

	public void addColumn(String columnName, String attribute) {
		this.addColumn(columnName, attribute, new ObjectToStringConverter());
	}

	public void addColumn(String columnName, String attribute,
			ObjectToStringConverter converter) {
		this.getColumnNames().add(columnName);
		this.getAttributes().add(attribute);
		this.getConverters().add(converter);
	}

	@Override
	public int getRowCount() {
		return this.getModel().size();
	}

	@Override
	public String getColumnName(int columnIndex) {
		return this.getColumnNames().get(columnIndex);
	}

	@Override
	public int getColumnCount() {
		return this.getColumnNames().size();
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	@Override
	public String getValueAt(int rowIndex, int columnIndex) {
		T object = this.getModel().get(rowIndex);
		Object result = invokeJavaBean(object, this.getAttributes().get(columnIndex));
		return this.getConverters().get(columnIndex).convert(result);
	}
}