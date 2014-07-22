package ar.edu.unq.sasa.gui.handlers;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;

import ar.edu.unq.sasa.gui.util.tables.ReadOnlyTableModel;
import ar.edu.unq.sasa.model.assignments.ClassroomAssignment;
import ar.edu.unq.sasa.model.assignments.ResourceAssignment;
import ar.edu.unq.sasa.model.items.MobileResource;

/**
 * Ventana que muestra las asignaciones de un recurso móvil.
 */
public class MobileResourceAssignmentsDetailWindow extends
		AssignmentsDetailWindow<MobileResource, ResourceAssignment> {

	private static final long serialVersionUID = -6062321212214573838L;

	public MobileResourceAssignmentsDetailWindow(MobileResource item) {
		super(item);
	}

	@Override
	protected List<ResourceAssignment> getAssigments() {
		return assignableItem.getResourceAssignments();
	}

	@Override
	protected void addWidgets() {
		JPanel main = new JPanel();
		add(main);
		main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
		JPanel topPanel = new JPanel();
		JPanel leftPanel = new JPanel();
		JPanel bottomPanel = new JPanel();
		main.add(topPanel);
		main.add(bottomPanel);
		topPanel.setLayout(new FlowLayout());
		topPanel.add(leftPanel);
		leftPanel.setBorder(BorderFactory.createTitledBorder("Asignaciones"));
		leftPanel.setLayout(new FlowLayout());
		JScrollPane assignmentsScrollPane = new JScrollPane(assignmentsTable);
		assignmentsScrollPane.setPreferredSize(new Dimension(300, 250));
		leftPanel.add(assignmentsScrollPane);
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
	protected void createOtherWidgets() {}

	@Override
	@SuppressWarnings("unchecked")
	protected void whenAssignmentsTableSelectionChanged(ListSelectionEvent e) {
		DefaultListSelectionModel selection = (DefaultListSelectionModel)e.getSource();
		if (selection.isSelectionEmpty())
			periodDetail.setText("");
		else {
			List<ClassroomAssignment> listModel = ((ReadOnlyTableModel<ClassroomAssignment>) assignmentsTable.getModel()).getModel();
			int index = selection.getMinSelectionIndex();
			periodDetail.setText(assignableItem.searchPeriod(listModel.get(index)).toString());
		}
	}
}
