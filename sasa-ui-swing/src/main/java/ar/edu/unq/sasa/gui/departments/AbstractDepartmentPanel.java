package ar.edu.unq.sasa.gui.departments;

import ar.edu.unq.sasa.gui.util.tables.ReadOnlyTableModel;
import ar.edu.unq.sasa.util.Subscriber;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.util.List;

import static ar.edu.unq.sasa.gui.util.WidgetUtilities.disableAll;
import static ar.edu.unq.sasa.gui.util.WidgetUtilities.enableAll;

public abstract class AbstractDepartmentPanel<T> extends JPanel implements Subscriber {

	private static final long serialVersionUID = 411643177304647624L;

	// el elemento seleccionado de la tabla.
	protected T selection;

	protected JTable table;
	protected JButton addButton, deleteButton, modifyButton;
	protected JScrollPane scrollPane;
	protected JLabel searchLabel;
	// el componente que se usará para buscar. Es Component porque algunas
	// clases usan InputField, y otras ComboBox
	protected Component searchField;

	public void initialize() {
		// TODO find better way to initialize
		registerAsSubscriber();
		createSearchComponents();
		createTable();
		createButtons();
		addAllWidgets();
	}

	protected void createSearchComponents() {
		searchLabel = new JLabel(getSearchLabelText());
		searchField = makeSearchField();
	}

	protected void createTable() {
		ReadOnlyTableModel<T> tableModel = new ReadOnlyTableModel<>(getListModel());
		addColumns(tableModel);
		table = new JTable(tableModel);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getSelectionModel().addListSelectionListener(AbstractDepartmentPanel.this::whenTableSelectionChanged);
		scrollPane = new JScrollPane(table);
	}

	protected void createButtons() {
		addButton = new JButton("Agregar");
		createAddButtonListeners();
		modifyButton = new JButton("Modificar");
		createModifyButtonListeners();
		deleteButton = new JButton("Eliminar");
		createDeleteButtonListeners();
		disableAll(deleteButton, modifyButton);
	}

	protected void addAllWidgets() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel topPanel = new JPanel();
		JPanel bottomPanel = new JPanel();
		add(topPanel);
		add(scrollPane);
		add(bottomPanel);
		topPanel.setLayout(new FlowLayout());
		topPanel.add(searchLabel);
		topPanel.add(searchField);
		bottomPanel.setLayout(new FlowLayout());
		addOtherWidgetsToBottomPanel(bottomPanel);
		bottomPanel.add(addButton);
		bottomPanel.add(modifyButton);
		bottomPanel.add(deleteButton);
	}

	protected void addOtherWidgetsToBottomPanel(JPanel bottomPanel) { }

	@SuppressWarnings("unchecked")
	protected void whenTableSelectionChanged(ListSelectionEvent anEvent) {
		DefaultListSelectionModel source = (DefaultListSelectionModel) anEvent.getSource();
		if (source.isSelectionEmpty()) {
			selection = null;
			disableAll(deleteButton, modifyButton);
		} else {
			List<T> model = ((ReadOnlyTableModel<T>) table.getModel()).getModel();
			selection = model.get(source.getMinSelectionIndex());
			enableAll(deleteButton, modifyButton);
		}
	}

	// para registrar observers del modelo
	protected abstract void registerAsSubscriber();
	// listeners de los botones
	protected abstract void createModifyButtonListeners();
	protected abstract void createDeleteButtonListeners();
	protected abstract void createAddButtonListeners();

	protected abstract String getSearchLabelText();
	protected abstract Component makeSearchField();
	protected abstract List<T> getListModel();

	// para especificar qué columnas se deben agregar en cada caso
	protected abstract void addColumns(ReadOnlyTableModel<T> tableModel);
}
