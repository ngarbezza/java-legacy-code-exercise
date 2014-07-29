package ar.edu.unq.sasa.gui.departments;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ar.edu.unq.sasa.gui.util.tables.ReadOnlyTableModel;
import ar.edu.unq.sasa.model.assignments.BookedAssignment;
import ar.edu.unq.sasa.model.assignments.ClassroomAssignment;
import ar.edu.unq.sasa.model.items.Classroom;

/**
 * Ventana para el detalle de asignaciones de {@link Classroom}'s.
 */
public class ClassroomAssignmentsDetailWindow extends AssignmentsDetailWindow<Classroom, ClassroomAssignment> {

	private static final long serialVersionUID = -6375987318161190231L;

	protected JTable bookedAssignments;

	public ClassroomAssignmentsDetailWindow(Classroom item) {
		super(item);
	}

	@Override
	protected void addWidgets() {
		JPanel main = new JPanel();
		add(main);
		main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
		JPanel topPanel = new JPanel();
		JPanel leftPanel = new JPanel();
		JPanel rightPanel = new JPanel();
		JPanel bottomPanel = new JPanel();
		main.add(topPanel);
		main.add(bottomPanel);
		topPanel.setLayout(new FlowLayout());
		topPanel.add(leftPanel);
		topPanel.add(rightPanel);
		leftPanel.setBorder(BorderFactory.createTitledBorder("Asignaciones"));
		leftPanel.setLayout(new FlowLayout());
		JScrollPane assignmentsScrollPane = new JScrollPane(assignmentsTable);
		assignmentsScrollPane.setPreferredSize(new Dimension(300, 250));
		leftPanel.add(assignmentsScrollPane);
		rightPanel.setBorder(BorderFactory.createTitledBorder("Reservas"));
		rightPanel.setLayout(new FlowLayout());
		JScrollPane bookedsScrollPane = new JScrollPane(bookedAssignments);
		bookedsScrollPane.setPreferredSize(new Dimension(150, 250));
		rightPanel.add(bookedsScrollPane);
		bottomPanel.setBorder(BorderFactory.createTitledBorder("Detalle del tiempo"));
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
		bottomPanel.add(new JScrollPane(periodDetail));
	}

	@Override
	protected int getWindowWidth() {
		return 520;
	}

	@Override
	protected int getWindowHeight() {
		return 450;
	}

	@Override
	protected List<ClassroomAssignment> getAssigments() {
		return assignableItem.getClassroomAssignments();
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void whenAssignmentsTableSelectionChanged(
			ListSelectionEvent e) {
		DefaultListSelectionModel selection = (DefaultListSelectionModel) e.getSource();
		if (selection.isSelectionEmpty()) {
			if (bookedAssignments.getSelectionModel().isSelectionEmpty())
				periodDetail.setText("");
		} else {
			((DefaultListSelectionModel) bookedAssignments.getSelectionModel()).clearSelection();
			List<ClassroomAssignment> listModel =
					((ReadOnlyTableModel<ClassroomAssignment>) assignmentsTable.getModel()).getModel();
			int index = selection.getMinSelectionIndex();
			periodDetail.setText(assignableItem.searchPeriod(listModel.get(index)).toString());
		}
	}


	@Override
	protected void createOtherWidgets() {
		ReadOnlyTableModel<BookedAssignment> model =
			new ReadOnlyTableModel<BookedAssignment>(assignableItem.getBookedAssignments());
		model.addColumn("Causa", "cause");
		bookedAssignments = new JTable(model);
		bookedAssignments.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			@SuppressWarnings("unchecked")
			public void valueChanged(ListSelectionEvent e) {
				DefaultListSelectionModel selection = (DefaultListSelectionModel) e.getSource();
				if (selection.isSelectionEmpty()) {
					if (assignmentsTable.getSelectionModel().isSelectionEmpty())
						periodDetail.setText("");
				} else {
					((DefaultListSelectionModel) assignmentsTable.getSelectionModel()).clearSelection();
					List<BookedAssignment> listModel =
							((ReadOnlyTableModel<BookedAssignment>) bookedAssignments.getModel()).getModel();
					int index = selection.getMinSelectionIndex();
					periodDetail.setText(assignableItem.searchPeriod(listModel.get(index)).toString());
				}
			}
		});
	}
}
