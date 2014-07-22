package ar.edu.unq.sasa.gui.handlers;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import ar.edu.unq.sasa.gui.util.ObjectToStringConverter;
import ar.edu.unq.sasa.gui.util.tables.ReadOnlyTableModel;
import ar.edu.unq.sasa.model.assignments.Assignment;
import ar.edu.unq.sasa.model.assignments.BookedAssignment;
import ar.edu.unq.sasa.model.handlers.Asignator;
import ar.edu.unq.sasa.model.items.Classroom;

/**
 * Panel para mostrar las Reservas ({@link BookedAssignment}) del Sistema
 */
public class BookedAssignmentsPanel extends AbstractHandlerPanel<BookedAssignment>{

	private static final long serialVersionUID = -408242049018068843L;

	private Asignator getHandler() {
		return Asignator.getInstance();
	}

	@Override
	public String getName() {
		return "Reservas";
	}

	@Override
	protected void addColumns(ReadOnlyTableModel<BookedAssignment> tableModel) {
		tableModel.addColumn("Aula", "assignableItem", new ObjectToStringConverter(){
			@Override
			public String convert(Object obj) {
				return ((Classroom)obj).getName();
			};
		});
		tableModel.addColumn("Causa", "cause");
	}

	@Override
	protected void createAddButtonListeners() {
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new CreateBookedAssignmentWindow();
			}
		});
	}

	@Override
	protected void createDeleteButtonListeners() {
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(new JFrame(),	"¿Desea eliminar la reserva seleccionada?", "Eliminar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
				      getHandler().deleteAssignment(selection);
			}
		});
	}

	@Override
	protected void createModifyButtonListeners() {
		modifyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new EditBookedAssignmentWindow(selection);
			}
		});
	}

	@Override
	protected List<BookedAssignment> getListModel() {
		List<BookedAssignment> bookedAssignments = new ArrayList<BookedAssignment>();
		for (Assignment assignment : getHandler().getInformationManager().getAssignments())
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
			public void keyReleased(KeyEvent e) {
				String text = ((JTextField)e.getSource()).getText();
				List<BookedAssignment> res = BookedAssignmentsPanel.this.getHandler().searchForBookedAssignment(text);
				((ReadOnlyTableModel<BookedAssignment>)table.getModel()).setModel(res);
			}
		});
		return field;
	}

	@Override
	protected void registerAsSubscriber() {
		getHandler().getPublisher().addSubscriber("assignmentsChanged", this);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void update(String aspect, Object value) {
		((ReadOnlyTableModel<BookedAssignment>)table.getModel()).setModel(getListModel());
	}

}
