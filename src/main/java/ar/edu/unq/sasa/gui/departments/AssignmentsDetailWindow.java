package ar.edu.unq.sasa.gui.departments;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ar.edu.unq.sasa.gui.util.ToStringConverter;
import ar.edu.unq.sasa.gui.util.tables.ReadOnlyTableModel;
import ar.edu.unq.sasa.model.academic.Request;
import ar.edu.unq.sasa.model.assignments.AssignmentByRequest;
import ar.edu.unq.sasa.model.items.AssignableItem;
import ar.edu.unq.sasa.model.items.Classroom;
import ar.edu.unq.sasa.model.items.MobileResource;

/**
 * Ventana que muestra el detalle de todas las asignaciones de un
 * {@link AssignableItem}. Es abstracta para que puedan mostrarse
 * {@link Classroom}'s y {@link MobileResource}'s con algunas diferencias.
 */
public abstract class AssignmentsDetailWindow<A extends AssignableItem,
	B extends AssignmentByRequest> extends JFrame {

	private static final long serialVersionUID = 4835555392263678407L;

	protected A assignableItem;
	protected JTable assignmentsTable;
	protected JTextArea periodDetail;

	public AssignmentsDetailWindow(final A item) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				assignableItem = item;
				createWidgets();
				addWidgets();

				setTitle("Detalle de asignaciones");
				setSize(new Dimension(getWindowWidth(), getWindowHeight()));
				setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				setResizable(false);
				setLocationRelativeTo(null);
				setVisible(true);
			}
		});
	}

	protected int getWindowWidth() {
		return 500;
	}

	protected int getWindowHeight() {
		return 500;
	}

	protected void createWidgets() {
		ReadOnlyTableModel<B> model = new ReadOnlyTableModel<B>(getAssigments());
		model.addColumn("Profesor", "request", new ToStringConverter() {
			@Override
			public String convert(Object obj) {
				return ((Request)obj).getProfessor().getName();
			}
		});
		model.addColumn("Materia", "request", new ToStringConverter() {
			@Override
			public String convert(Object obj) {
				return ((Request)obj).getSubject().getName();
			}
		});
		assignmentsTable = new JTable(model);
		assignmentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		assignmentsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				whenAssignmentsTableSelectionChanged(e);
			}
		});
		periodDetail = new JTextArea();
		periodDetail.setPreferredSize(new Dimension(480, 100));
		periodDetail.setEditable(false);
		createOtherWidgets();
	}

	protected abstract void whenAssignmentsTableSelectionChanged(
			ListSelectionEvent e);
	protected abstract void createOtherWidgets();
	protected abstract void addWidgets();
	protected abstract List<B> getAssigments();
}
