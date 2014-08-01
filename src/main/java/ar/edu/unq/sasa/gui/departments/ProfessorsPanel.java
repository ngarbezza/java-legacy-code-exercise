package ar.edu.unq.sasa.gui.departments;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import ar.edu.unq.sasa.gui.util.tables.ReadOnlyTableModel;
import ar.edu.unq.sasa.model.academic.Professor;
import ar.edu.unq.sasa.model.departments.ProfessorsDepartment;

public class ProfessorsPanel extends AbstractDepartmentPanel<Professor> {

	private static final long serialVersionUID = -2846696085344808896L;

	private ProfessorsDepartment department;

	public ProfessorsPanel(ProfessorsDepartment professorsDepartment) {
		department = professorsDepartment;
		initialize();
	}

	@Override
	public String getName() {
		return "Profesores";
	}

	@Override
	@SuppressWarnings("unchecked")
	public void update(String aspect, Object value) {
		((ReadOnlyTableModel<Professor>) table.getModel())
			.setModel((List<Professor>) value);
	}

	@Override
	protected void registerAsSubscriber() {
		department.getPublisher().addSubscriber("professorsChanged", this);
	}

	@Override
	protected void addColumns(ReadOnlyTableModel<Professor> tableModel) {
		tableModel.addColumn("Nombre", "name");
		tableModel.addColumn("Teléfono", "phoneNumber");
		tableModel.addColumn("E-Mail", "mail");
	}

	@Override
	protected List<Professor> getListModel() {
		return department.getProfessors();
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
				List<Professor> res = ProfessorsPanel.this.department.searchProfessor(text);
				((ReadOnlyTableModel<Professor>) table.getModel()).setModel(res);
			}
		});
		return field;
	}

	@Override
	protected void createAddButtonListeners() {
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new EditProfessorWindow(department);
			}
		});
	}

	@Override
	protected void createDeleteButtonListeners() {
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(new JFrame(),
						"¿Desea eliminar el profesor seleccionado?", "Eliminar",
				        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
				      department.deleteProfessor(selection);
			}
		});
	}

	@Override
	protected void createModifyButtonListeners() {
		modifyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new EditProfessorWindow(department, selection);
			}
		});
	}
}