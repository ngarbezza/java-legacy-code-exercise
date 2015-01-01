package ar.edu.unq.sasa.gui.departments;

import ar.edu.unq.sasa.gui.util.tables.ReadOnlyTableModel;
import ar.edu.unq.sasa.model.departments.ResourcesDepartment;
import ar.edu.unq.sasa.model.items.MobileResource;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import static ar.edu.unq.sasa.gui.util.Dialogs.withConfirmation;

public class MobileResourcesPanel extends AbstractDepartmentPanel<MobileResource> {

    private static final long serialVersionUID = -484671956803489488L;

    protected JButton assignmentsDetailButton;

    private ResourcesDepartment department;

    public MobileResourcesPanel(ResourcesDepartment resourcesDepartment) {
        department = resourcesDepartment;
        initialize();
    }

    @Override
    public String getName() {
        return "Recursos";
    }

    @Override
    protected void addColumns(ReadOnlyTableModel<MobileResource> tableModel) {
        tableModel.addColumn("ID", "id");
        tableModel.addColumn("Nombre", "name");
    }

    @Override
    protected void createAddButtonListeners() {
        addButton.addActionListener(anEvent -> new EditMobileResourceWindow(department));
    }

    @Override
    protected void createDeleteButtonListeners() {
        deleteButton.addActionListener(anEvent -> {
            withConfirmation("Eliminar", "¿Desea eliminar el recurso seleccionado?", () ->
                    department.deleteResource(selection));
        });
    }

    @Override
    protected void createModifyButtonListeners() {
        modifyButton.addActionListener(anEvent -> new EditMobileResourceWindow(department, selection));
    }

    @Override
    protected List<MobileResource> getListModel() {
        return department.getMobileResources();
    }

    @Override
    protected String getSearchLabelText() {
        return "Búsqueda por nombre";
    }

    @Override
    protected Component makeSearchField() {
        JTextField input = new JTextField(10);
        input.addKeyListener(new KeyAdapter() {
            @Override
            @SuppressWarnings("unchecked")
            public void keyReleased(KeyEvent anEvent) {
                String text = ((JTextField) anEvent.getSource()).getText();
                List<MobileResource> res = department.searchResources(text);
                ((ReadOnlyTableModel<MobileResource>) table.getModel()).setModel(res);
            }
        });
        return input;
    }

    @Override
    protected void whenTableSelectionChanged(ListSelectionEvent anEvent) {
        super.whenTableSelectionChanged(anEvent);
        assignmentsDetailButton.setEnabled(selection != null);
    }

    @Override
    protected void addOtherWidgetsToBottomPanel(JPanel bottomPanel) {
        assignmentsDetailButton = new JButton("Ver asignaciones");
        assignmentsDetailButton.setEnabled(false);
        assignmentsDetailButton.addActionListener(anEvent -> new MobileResourceAssignmentsDetailWindow(selection));
        bottomPanel.add(assignmentsDetailButton);
    }

    @Override
    protected void registerAsSubscriber() {
        department.getPublisher().addSubscriber("mobileResources", this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void update(String aspect, Object value) {
        ((ReadOnlyTableModel<MobileResource>) table.getModel())
                .setModel((List<MobileResource>) value);
    }
}
