package ar.edu.unq.sasa.gui.departments;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
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
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new EditClassroomWindow(department);
			}
		});
	}

	@Override
	protected void createDeleteButtonListeners() {
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(new JFrame(),
						"¿Desea eliminar el pedido seleccionado?", "Eliminar",
				        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
					department.deleteClassroom(selection);
			}
		});
	}

	@Override
	protected void createModifyButtonListeners() {
		modifyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new EditClassroomWindow(department, selection);
			}
		});
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
			public void keyReleased(KeyEvent e) {
				String text = ((JTextField) e.getSource()).getText();
				List<Classroom> res = department.searchClassroomByName(text);
				((ReadOnlyTableModel<Classroom>) table.getModel()).setModel(res);
			}
		});
		return field;
	}

	@Override
	protected void whenTableSelectionChanged(ListSelectionEvent e) {
		super.whenTableSelectionChanged(e);
		assignmentsDetailButton.setEnabled(selection != null);
	}

	@Override
	protected void addOtherWidgetsToBottomPanel(JPanel bottomPanel) {
		assignmentsDetailButton = new JButton("Ver asignaciones");
		assignmentsDetailButton.setEnabled(false);
		assignmentsDetailButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new ClassroomAssignmentsDetailWindow(selection);
			}
		});
		bottomPanel.add(assignmentsDetailButton);
	}

	@Override
	protected void registerAsSubscriber() {
		department.getPublisher().addSubscriber("classroomsChanged", this);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void update(String aspect, Object value) {
		((ReadOnlyTableModel<Classroom>) table.getModel())
			.setModel((List<Classroom>) value);
	}
}
