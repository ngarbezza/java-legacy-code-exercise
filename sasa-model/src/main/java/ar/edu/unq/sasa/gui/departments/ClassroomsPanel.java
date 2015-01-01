package ar.edu.unq.sasa.gui.departments;

import static ar.edu.unq.sasa.gui.util.Dialogs.withConfirmation;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;

import ar.edu.unq.sasa.gui.util.tables.ReadOnlyTableModel;
import ar.edu.unq.sasa.model.departments.ClassroomsDepartment;
import ar.edu.unq.sasa.model.items.Classroom;

public class ClassroomsPanel extends AbstractDepartmentPanel<Classroom> {

	private static final long serialVersionUID = -3693454191114811772L;

	protected JButton assignmentsDetailButton;

	private ClassroomsDepartment department;

	public ClassroomsPanel(ClassroomsDepartment classroomsDepartment) {
		department = classroomsDepartment;
		initialize();
	}

	@Override
	public String getName() {
		return "Aulas";
	}

	@Override
	protected void addColumns(ReadOnlyTableModel<Classroom> tableModel) {
		tableModel.addColumn("Nombre", "name");
		tableModel.addColumn("Capacidad", "capacity");
	}

	@Override
	protected void createAddButtonListeners() {
		addButton.addActionListener(anEvent -> { new EditClassroomWindow(department); });
	}

	@Override
	protected void createDeleteButtonListeners() {
		deleteButton.addActionListener(anEvent -> {
			withConfirmation("Eliminar", "¿Desea eliminar el pedido seleccionado?", () -> {
				department.deleteClassroom(selection);
			});
		});
	}

	@Override
	protected void createModifyButtonListeners() {
		modifyButton.addActionListener(anEvent -> { new EditClassroomWindow(department, selection); });
	}

	@Override
	protected List<Classroom> getListModel() {
		return department.getClassrooms();
	}

	@Override
	protected String getSearchLabelText() {
		return "Búsqueda rápida por nombre";
	}

	@Override
	protected Component makeSearchField() {
		JTextField field = new JTextField(10);
		field.addKeyListener(new KeyAdapter() {
			@Override
			@SuppressWarnings("unchecked")
			public void keyReleased(KeyEvent anEvent) {
				String text = ((JTextField) anEvent.getSource()).getText();
				List<Classroom> res = department.searchClassroomByName(text);
				((ReadOnlyTableModel<Classroom>) table.getModel()).setModel(res);
			}
		});
		return field;
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
		assignmentsDetailButton.addActionListener(anEvent -> {
			new ClassroomAssignmentsDetailWindow(selection);
		});
		bottomPanel.add(assignmentsDetailButton);
	}

	@Override
	protected void registerAsSubscriber() {
		department.getPublisher().addSubscriber("classroomsChanged", this);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void update(String anAspect, Object aValue) {
		((ReadOnlyTableModel<Classroom>) table.getModel())
			.setModel((List<Classroom>) aValue);
	}
}
