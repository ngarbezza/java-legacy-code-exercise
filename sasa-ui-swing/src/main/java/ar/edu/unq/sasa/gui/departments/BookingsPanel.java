package ar.edu.unq.sasa.gui.departments;

import ar.edu.unq.sasa.gui.util.ToStringConverter;
import ar.edu.unq.sasa.gui.util.tables.ReadOnlyTableModel;
import ar.edu.unq.sasa.model.assignments.Assignment;
import ar.edu.unq.sasa.model.assignments.Booking;
import ar.edu.unq.sasa.model.departments.AssignmentsDepartment;
import ar.edu.unq.sasa.model.items.Classroom;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.stream.Collectors;

import static ar.edu.unq.sasa.gui.util.Dialogs.withConfirmation;

public class BookingsPanel extends AbstractDepartmentPanel<Booking> {

    private static final long serialVersionUID = -408242049018068843L;

    private AssignmentsDepartment department;

    public BookingsPanel(AssignmentsDepartment assignmentsDepartment) {
        department = assignmentsDepartment;
        initialize();
    }

    @Override
    public String getName() {
        return "Reservas";
    }

    @Override
    protected void addColumns(ReadOnlyTableModel<Booking> tableModel) {
        tableModel.addColumn("Aula", "assignableItem", new ToStringConverter<Classroom>() {
            @Override
            public String convert(Classroom aClassroom) {
                return aClassroom.getName();
            }
        });
        tableModel.addColumn("Causa", "cause");
    }

    @Override
    protected void createAddButtonListeners() {
        addButton.addActionListener(anEvent -> new CreateBookingWindow(department));
    }

    @Override
    protected void createDeleteButtonListeners() {
        deleteButton.addActionListener(anEvent ->
                withConfirmation("Eliminar", "¿Desea eliminar la reserva seleccionada?", () ->
                        department.deleteAssignment(selection)));
    }

    @Override
    protected void createModifyButtonListeners() {
        modifyButton.addActionListener(anEvent -> new EditBookingWindow(department, selection));
    }

    @Override
    protected List<Booking> getListModel() {
        return department.getAssignments().stream()
                .filter(Assignment::isBookedAssignment)
                .map(assignment -> (Booking) assignment)
                .collect(Collectors.toList());
    }

    @Override
    protected String getSearchLabelText() {
        return "Búsqueda rápida por causa";
    }

    @Override
    protected Component makeSearchField() {
        JTextField field = new JTextField(10);
        field.addKeyListener(new KeyAdapter() {
            @Override
            @SuppressWarnings("unchecked")
            public void keyReleased(KeyEvent anEvent) {
                String text = ((JTextField) anEvent.getSource()).getText();
                List<Booking> res = BookingsPanel.this.department.searchBookings(text);
                ((ReadOnlyTableModel<Booking>) table.getModel()).setModel(res);
            }
        });
        return field;
    }

    @Override
    protected void registerAsSubscriber() {
        department.getPublisher().addSubscriber("assignmentsChanged", this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void update(String aspect, Object value) {
        ((ReadOnlyTableModel<Booking>) table.getModel()).setModel(getListModel());
    }
}
