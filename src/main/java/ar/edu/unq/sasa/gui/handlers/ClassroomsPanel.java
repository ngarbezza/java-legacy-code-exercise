package ar.edu.unq.sasa.gui.handlers;

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
import ar.edu.unq.sasa.model.handlers.ClassroomHandler;
import ar.edu.unq.sasa.model.items.Classroom;

/**
 * Panel para la administración de {@link Classroom}s.
 */
public class ClassroomsPanel extends AbstractHandlerPanel<Classroom> {

	private static final long serialVersionUID = -3693454191114811772L;

	protected JButton assignmentsDetailButton;

	public ClassroomHandler getHandler() {
		return ClassroomHandler.getInstance();
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
				new EditClassroomWindow();
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
					getHandler().deleteClassroom(selection);
			}
		});
	}

	@Override
	protected void createModifyButtonListeners() {
		modifyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new EditClassroomWindow(selection);
			}
		});
	}

	@Override
	protected List<Classroom> getListModel() {
		return getHandler().getInformationManager().getClassrooms();
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
				String text = ((JTextField)e.getSource()).getText();
				List<Classroom> res = getHandler().searchClassroomByName(text);
				((ReadOnlyTableModel<Classroom>)table.getModel()).setModel(res);
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
		getHandler().getPublisher().addSubscriber("classroomsChanged", this);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void update(String aspect, Object value) {
		((ReadOnlyTableModel<Classroom>)table.getModel())
			.setModel((List<Classroom>)value);
	}
}
