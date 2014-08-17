package ar.edu.unq.sasa.gui.departments;

import static ar.edu.unq.sasa.gui.util.Dialogs.withConfirmation;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextField;

import ar.edu.unq.sasa.gui.util.ToStringConverter;
import ar.edu.unq.sasa.gui.util.tables.ReadOnlyTableModel;
import ar.edu.unq.sasa.model.assignments.Assignment;
import ar.edu.unq.sasa.model.assignments.BookedAssignment;
import ar.edu.unq.sasa.model.departments.AssignmentsDepartment;
import ar.edu.unq.sasa.model.items.Classroom;

public class BookedAssignmentsPanel extends AbstractDepartmentPanel<BookedAssignment> {

	private static final long serialVersionUID = -408242049018068843L;

	private AssignmentsDepartment department;

	public BookedAssignmentsPanel(AssignmentsDepartment assignmentsDepartment) {
		department = assignmentsDepartment;
		initialize();
	}

	@Override
	public String getName() {
		return "Reservas";
	}

	@Override
	protected void addColumns(ReadOnlyTableModel<BookedAssignment> tableModel) {
		tableModel.addColumn("Aula", "assignableItem", new ToStringConverter<Classroom>() {
			@Override
			public String convert(Classroom aClassroom) {
				return aClassroom.getName();
			};
		});
		tableModel.addColumn("Causa", "cause");
	}

	@Override
	protected void createAddButtonListeners() {
		addButton.addActionListener(anEvent -> { new CreateBookedAssignmentWindow(department); });
	}

	@Override
	protected void createDeleteButtonListeners() {
		deleteButton.addActionListener(anEvent -> {
			withConfirmation("Eliminar", "¿Desea eliminar la reserva seleccionada?", () -> {
				department.deleteAssignment(selection);
			});
		});
	}

	@Override
	protected void createModifyButtonListeners() {
		modifyButton.addActionListener(anEvent -> { new EditBookedAssignmentWindow(department, selection); });
	}

	@Override
	protected List<BookedAssignment> getListModel() {
		List<BookedAssignment> bookedAssignments = new ArrayList<BookedAssignment>();
		for (Assignment assignment : department.getAssignments())
			if (assignment.isBookedAssignment())
				bookedAssignments.add((BookedAssignment) assignment);
		return bookedAssignments;
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
				List<BookedAssignment> res = BookedAssignmentsPanel.this.department.searchForBookedAssignment(text);
				((ReadOnlyTableModel<BookedAssignment>) table.getModel()).setModel(res);
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
		((ReadOnlyTableModel<BookedAssignment>) table.getModel()).setModel(getListModel());
	}
}