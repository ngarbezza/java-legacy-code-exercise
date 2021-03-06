package ar.edu.unq.sasa.gui.departments;

import ar.edu.unq.sasa.gui.util.WidgetUtilities;
import ar.edu.unq.sasa.gui.util.tables.ReadOnlyTableModel;
import ar.edu.unq.sasa.model.departments.ClassroomsDepartment;
import ar.edu.unq.sasa.model.items.Classroom;
import ar.edu.unq.sasa.model.items.FixedResource;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static ar.edu.unq.sasa.gui.util.LabelHelpers.requiredRedStar;

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

    private ClassroomsDepartment department;

    public EditClassroomWindow(ClassroomsDepartment classroomsDepartment, Classroom selection) {
        super(selection);
        department = classroomsDepartment;
    }

    public EditClassroomWindow(ClassroomsDepartment classroomsDepartment) {
        this(classroomsDepartment, null);
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
        ReadOnlyTableModel<FixedResource> tableModel = new ReadOnlyTableModel<>();
        tableModel.addColumn("Nombre", "name");
        tableModel.addColumn("Cantidad", "amount");
        resourcesTable = new JTable(tableModel);
        resourcesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resourcesTable.getSelectionModel().addListSelectionListener(
                EditClassroomWindow.this::whenResourcesListSelectionChanged);

        if (inEditMode()) {
            List<FixedResource> copy = item.getResources().stream().collect(Collectors.toCollection(LinkedList::new));
            ((ReadOnlyTableModel<FixedResource>) resourcesTable.getModel()).setModel(copy);
        }
    }

    private void createButtons() {
        saveResButton = new JButton("Guardar");
        saveResButton.setEnabled(false);
        saveResButton.addActionListener(anEvent -> {
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
        });
        newResButton = new JButton("Nuevo");
        newResButton.addActionListener(anEvent -> {
            FixedResource newRes = new FixedResource("", 1);
            getListModel().add(newRes);
            refreshTable();
            Integer index = getListModel().indexOf(newRes);
            resourcesTable.getSelectionModel().setSelectionInterval(index, index);
        });
        deleteResButton = new JButton("Eliminar");
        deleteResButton.setEnabled(false);
        deleteResButton.addActionListener(anEvent -> {
            getListModel().remove(getResourceTableSelection());
            refreshTable();
        });
    }

    @SuppressWarnings("unchecked")
    protected void refreshTable() {
        ((ReadOnlyTableModel<FixedResource>) resourcesTable.getModel()).fireTableDataChanged();
    }

    protected void setResourceChanged(boolean status) {
        saveResButton.setEnabled(status);
    }

    private FixedResource getResourceTableSelection() {
        return getListModel().get(resourcesTable.getSelectionModel().getMinSelectionIndex());
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
        WidgetUtilities.toggleAll(!visibility,
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
        namePanel.add(requiredRedStar());
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
        resNamePanel.add(requiredRedStar());
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
        department.modifyClassroomProperties(item, nameField.getText(), (Integer) capacityField.getValue(), getListModel());
    }

    @Override
    protected void doAcceptInAddingMode() {
        Classroom classroom = department.createClassroom(nameField.getText(), (Integer) capacityField.getValue());
        getListModel().forEach(classroom::addResource);
    }

    @SuppressWarnings("unchecked")
    private List<FixedResource> getListModel() {
        return ((ReadOnlyTableModel<FixedResource>) resourcesTable.getModel()).getModel();
    }

    @Override
    protected Integer getWindowHeight() {
        return 350;
    }

    @Override
    protected Integer getWindowWidth() {
        return 450;
    }

    @Override
    protected String getWindowTitle() {
        return inEditMode() ? "Editar aula" : "Agregar aula";
    }
}
