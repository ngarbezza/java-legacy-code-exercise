package ar.edu.unq.sasa.gui.handlers;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ar.edu.unq.sasa.gui.util.WidgetUtilities;
import ar.edu.unq.sasa.gui.util.tables.ReadOnlyTableModel;
import ar.edu.unq.sasa.model.exceptions.handlers.ClassroomException;
import ar.edu.unq.sasa.model.handlers.ClassroomHandler;
import ar.edu.unq.sasa.model.items.Classroom;
import ar.edu.unq.sasa.model.items.FixedResource;

/**
 * Ventana de edición de aulas. También sirve para agregar aulas nuevas.
 */
public class EditClassroomWindow extends AbstractEditWindow<Classroom> {

	private static final long serialVersionUID = -238543135195643014L;
	protected JLabel capacityLabel, nameLabel;
	protected JTextField nameField;
	protected JSpinner capacityField;
	protected JPanel resPanel;
	protected JTable resourcesTable;
	protected JLabel resNameLabel, resAmountLabel;
	protected JTextField resNameField;
	protected JSpinner resAmountField;
	protected JButton saveResButton, newResButton, deleteResButton;

	public EditClassroomWindow() {
		this(null);
	}

	public EditClassroomWindow(Classroom c) {
		super(c);
	}

	@Override
	protected void createWidgetsTopPanel() {
		createLabels();
		createFields();
		createResourcesPanel();
	}

	@SuppressWarnings("unchecked")
	protected void createResourcesPanel() {
		resPanel = new JPanel();
		resPanel.setBorder(BorderFactory.createTitledBorder("Recursos fijos"));
		resNameLabel = new JLabel("Nombre");
		resNameLabel.setEnabled(false);
		resAmountLabel = new JLabel("Cantidad");
		resAmountLabel.setEnabled(false);
		resNameField = new JTextField(14);
		resNameField.setEnabled(false);
		resAmountField = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
		resAmountField.setEnabled(false);
		createButtons();
		ReadOnlyTableModel<FixedResource> tableModel = new ReadOnlyTableModel<FixedResource>();
		tableModel.addColumn("Nombre", "name");
		tableModel.addColumn("Cantidad", "amount");
		resourcesTable = new JTable(tableModel);
		resourcesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		resourcesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				whenResourcesListSelectionChanged(e);
			}
		});

		if (inEditMode()) {
			List<FixedResource> copy = new LinkedList<FixedResource>();
			for (FixedResource fr : item.getResources())
				copy.add(fr);
			((ReadOnlyTableModel<FixedResource>)resourcesTable.getModel()).setModel(copy);
		}
	}

	private void createButtons() {
		saveResButton = new JButton("Guardar");
		saveResButton.setEnabled(false);
		saveResButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FixedResource res = getResourceTableSelection();
				String text = resNameField.getText();
				if (text.equals(""))
					JOptionPane.showMessageDialog(EditClassroomWindow.this,
							"Falta especificar el nombre al recurso",
							"Advertencia", JOptionPane.WARNING_MESSAGE);
				res.setName(text);
				res.setAmount((Integer) resAmountField.getValue());
				setResourceChanged(false);
				refreshTable();
			}
		});
		newResButton = new JButton("Nuevo");
		newResButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FixedResource newRes = new FixedResource("", 1);
				getListModel().add(newRes);
				refreshTable();
				int index = getListModel().indexOf(newRes);
				resourcesTable.getSelectionModel().setSelectionInterval(index, index);
			}
		});
		deleteResButton = new JButton("Eliminar");
		deleteResButton.setEnabled(false);
		deleteResButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getListModel().remove(getResourceTableSelection());
				refreshTable();
			}
		});
	}

	@SuppressWarnings("unchecked")
	protected void refreshTable() {
		((ReadOnlyTableModel<FixedResource>) resourcesTable.getModel()).fireTableDataChanged();
	}

	protected void setResourceChanged(boolean b) {
		saveResButton.setEnabled(b);
	}

	private FixedResource getResourceTableSelection() {
		int index = resourcesTable.getSelectionModel().getMinSelectionIndex();
		return getListModel().get(index);
	}

	protected void whenResourcesListSelectionChanged(ListSelectionEvent e) {
		int row = resourcesTable.getSelectedRow();
		boolean visibility = row <= -1;
		if (visibility) {
			resNameField.setText("");
			resAmountField.setValue(1);
		} else {
			String selectedName = (String) resourcesTable.getValueAt(row, 0);
			String selectedAmount = (String) resourcesTable.getValueAt(row, 1);
			resNameField.setText(selectedName);
			resAmountField.setValue(Integer.parseInt(selectedAmount));
		}
		WidgetUtilities.enableOrDisableWidgets(!visibility,
				resAmountField, resAmountLabel, resNameField, resNameLabel,
				saveResButton, deleteResButton);
	}

	protected void createFields() {
		nameField = new JTextField(14);
		capacityField = new JSpinner(new SpinnerNumberModel(20, 1, Integer.MAX_VALUE, 1));

		if (inEditMode()) {
			nameField.setText(item.getName());
			capacityField.setValue(item.getCapacity());
		}
	}

	protected void createLabels() {
		nameLabel = new JLabel("Nombre  ");
		capacityLabel = new JLabel("Capacidad");
	}

	@Override
	protected void organizeTopPanelWidgets(JPanel topPanel) {
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		JPanel namePanel = new JPanel();
		JPanel capacityPanel = new JPanel();
		topPanel.add(namePanel);
		topPanel.add(capacityPanel);
		topPanel.add(resPanel);
		namePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel label1 = new JLabel("*");
		label1.setForeground(Color.RED);
		namePanel.add(label1);
		namePanel.add(nameLabel);
		namePanel.add(nameField);
		capacityPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		capacityPanel.add(capacityLabel);
		capacityPanel.add(capacityField);
		resPanel.setLayout(new BoxLayout(resPanel, BoxLayout.X_AXIS));
		JPanel modifyResPanel = new JPanel();
		modifyResPanel.setBorder(BorderFactory.createTitledBorder("Detalle Recurso fijo"));
		resPanel.add(new JScrollPane(resourcesTable));
		resPanel.add(modifyResPanel);
		modifyResPanel.setLayout(new BoxLayout(modifyResPanel, BoxLayout.Y_AXIS));
		JPanel resNamePanel = new JPanel();
		JPanel resAmountPanel = new JPanel();
		JPanel resButtonsPanel = new JPanel();
		modifyResPanel.add(resNamePanel);
		modifyResPanel.add(resAmountPanel);
		modifyResPanel.add(resButtonsPanel);
		resNamePanel.setLayout(new FlowLayout());
		JLabel label2 = new JLabel("*");
		label2.setForeground(Color.RED);
		resNamePanel.add(label2);
		resNamePanel.add(resNameLabel);
		resNamePanel.add(resNameField);
		resAmountPanel.setLayout(new FlowLayout());
		resAmountPanel.add(resAmountLabel);
		resAmountPanel.add(resAmountField);
		resButtonsPanel.setLayout(new FlowLayout());
		resButtonsPanel.add(newResButton);
		resButtonsPanel.add(saveResButton);
		resButtonsPanel.add(deleteResButton);
	}

	@Override
	protected void doAcceptActionInEditMode() {
		ClassroomHandler.getInstance().modifyClassroomProperties(item, nameField.getText(),
				(Integer) capacityField.getValue(),	getListModel());
	}

	@Override
	protected void doAcceptInAddingMode() {
		try {
			Classroom c = ClassroomHandler.getInstance().createClassroom(
					nameField.getText(), (Integer) capacityField.getValue());
			for (FixedResource r : getListModel())
				c.addResource(r);
		} catch (ClassroomException e) {
			JOptionPane.showMessageDialog(this, "Hubo un error al crear el aula",
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	@SuppressWarnings("unchecked")
	private List<FixedResource> getListModel() {
		return ((ReadOnlyTableModel<FixedResource>) resourcesTable.getModel()).getModel();
	}

	@Override
	protected int getWindowHeight() {
		return 350;
	}

	@Override
	protected int getWindowWidth() {
		return 450;
	}

	@Override
	protected String getWindowTitle() {
		return inEditMode()? "Edición de un Aula" : "Agregar Aula";
	}
}
