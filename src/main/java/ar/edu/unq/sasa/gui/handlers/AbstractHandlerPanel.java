package ar.edu.unq.sasa.gui.handlers;

import java.awt.Component;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ar.edu.unq.sasa.gui.util.tables.ReadOnlyTableModel;
import ar.edu.unq.sasa.util.Subscriber;

/**
 * Clase abstracta que contiene las cosas en común de los panels de los
 * handlers.
 */
public abstract class AbstractHandlerPanel<T> extends JPanel implements Subscriber {

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

	public AbstractHandlerPanel() {
		registerAsSubscriber();
		createSearchComponents();
		createTable();
		createButtons();
		addAllWidgets();
	}

	protected int getWindowWidth() {
		return 500;
	}

	protected int getWindowHeight() {
		return 500;
	}

	protected void createSearchComponents() {
		searchLabel = new JLabel(getSearchLabelText());
		searchField = makeSearchField();
	}

	protected void createTable() {
		ReadOnlyTableModel<T> tableModel = new ReadOnlyTableModel<T>(getListModel());
		addColumns(tableModel);
		table = new JTable(tableModel);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				whenTableSelectionChanged(e);
			}
		});
		scrollPane = new JScrollPane(table);
	}

	protected void createButtons() {
		addButton = new JButton("Agregar");
		createAddButtonListeners();
		modifyButton = new JButton("Modificar");
		createModifyButtonListeners();
		deleteButton = new JButton("Eliminar");
		createDeleteButtonListeners();
		deleteButton.setEnabled(false);
		modifyButton.setEnabled(false);
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

	protected void addOtherWidgetsToBottomPanel(JPanel bottomPanel) {}

	@SuppressWarnings("unchecked")
	protected void whenTableSelectionChanged(ListSelectionEvent e) {
		DefaultListSelectionModel source = (DefaultListSelectionModel)e.getSource();
		if (source.isSelectionEmpty()) {
			selection = null;
			deleteButton.setEnabled(false);
			modifyButton.setEnabled(false);
		}
		else {
			int index = source.getMinSelectionIndex();
			List<T> model = ((ReadOnlyTableModel<T>)table.getModel()).getModel();
			selection = model.get(index);
			deleteButton.setEnabled(true);
			modifyButton.setEnabled(true);
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
