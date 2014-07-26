package ar.edu.unq.sasa.gui.departments;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ar.edu.unq.sasa.gui.util.combos.EasyComboBoxModel;
import ar.edu.unq.sasa.gui.util.lists.EasyListModel;
import ar.edu.unq.sasa.model.academic.Professor;
import ar.edu.unq.sasa.model.academic.Subject;
import ar.edu.unq.sasa.model.departments.ProfessorsDepartment;
import ar.edu.unq.sasa.model.departments.SubjectsDepartment;

/**
 * Panel que sirve para la edición de datos de {@link Professor}s. También sirve
 * para agregar Profesores nuevos.
 */
public class EditProfessorWindow extends AbstractEditWindow<Professor> {

	private static final long serialVersionUID = -252220491365344826L;

	protected JLabel nameLabel, phoneLabel, mailLabel;
	protected JTextField mailField, phoneField, nameField;

	// materias
	protected JPanel subjectsPanel, leftBottomPanel;
	protected JComboBox<Subject> allSubjectsCombo;
	protected JList<Subject> subjectList;
	protected JTextField newSubjectField;
	protected JButton addSubject, deleteSubject;
	protected JRadioButton chooseExistingSubject, chooseNewSubject;
	protected JLabel addSubjectLabel;

	// el nombre de la materia que se está por agregar
	// sirve para saber cuando agrega cómo crear la materia
	// (o simplemente buscarla si ya existe)
	protected String currentSubjectName;

	private ProfessorsDepartment department;

	public EditProfessorWindow(ProfessorsDepartment professorsDepartment) {
		this(professorsDepartment, null);
	}

	public EditProfessorWindow(ProfessorsDepartment professorsDepartment, Professor professor) {
		super(professor);
		department = professorsDepartment;
	}

	private List<Subject> getSubjectList() {
		return ((EasyListModel<Subject>) subjectList.getModel()).getModel();
	}

	@Override
	protected void createWidgetsTopPanel() {
		createLabels();
		createFields();
		createSubjectsPanel();
	}

	protected void createLabels() {
		nameLabel = new JLabel("Nombre:");
		phoneLabel = new JLabel(" Teléfono:");
		mailLabel = new JLabel("     E-Mail:");
	}

	protected void createFields() {
		nameField = new JTextField(12);
		nameField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				doValidations();
			}
		});
		phoneField = new JTextField(12);
		phoneField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				doValidations();
			}
		});
		mailField = new JTextField(12);
		if (inEditMode()) {
			nameField.setText(item.getName());
			phoneField.setText(item.getPhoneNumber());
			mailField.setText(item.getMail());
		}
	}

	protected void createSubjectsPanel() {
		subjectsPanel = new JPanel();
		subjectsPanel.setBorder(BorderFactory.createTitledBorder("Materias"));

		addSubjectLabel = new JLabel("Agregar Materia");
		List<Subject> allSubjects = department.getSubjectsDepartment().getSubjects();
		allSubjectsCombo = new JComboBox<Subject>(new EasyComboBoxModel<Subject>(allSubjects));
		allSubjectsCombo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Subject s = (Subject) allSubjectsCombo.getSelectedItem();
				currentSubjectName = (s == null) ? "" : s.getName();
				validateCurrentSubjectName();
			}
		});
		newSubjectField = new JTextField(24);
		newSubjectField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				currentSubjectName = newSubjectField.getText();
				validateCurrentSubjectName();
			}
		});
		subjectList = new JList<Subject>(new EasyListModel<Subject>());
		subjectList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				boolean visibility = subjectList.getSelectionModel().isSelectionEmpty();
				deleteSubject.setEnabled(!visibility);
			}
		});
		subjectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		createSubjectButtons();
		createSubjectRadioButtons();

		if (inEditMode()) {
			List<Subject> copy = new LinkedList<Subject>();
			for (Subject s : item.getSubjects())
				copy.add(s);
			((EasyListModel<Subject>) subjectList.getModel()).setModel(copy);
		}
	}

	private void createSubjectRadioButtons() {
		ButtonGroup subjectCreating = new ButtonGroup();
		chooseExistingSubject = new JRadioButton("Existente", true);
		newSubjectField.setEnabled(!chooseExistingSubject.isSelected());
		chooseExistingSubject.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Subject subjectSelected = (Subject) allSubjectsCombo
						.getSelectedItem();
				currentSubjectName = (subjectSelected != null) ? getName() : "";
				newSubjectField.setEnabled(!chooseExistingSubject.isSelected());
				newSubjectField.setText("");
				allSubjectsCombo.setEnabled(chooseExistingSubject.isSelected());
				validateCurrentSubjectName();
			}
		});
		chooseNewSubject = new JRadioButton("Nueva");
		chooseNewSubject.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentSubjectName = newSubjectField.getText();
				allSubjectsCombo.setEnabled(!chooseNewSubject.isSelected());
				allSubjectsCombo.setSelectedIndex(0);
				newSubjectField.setEnabled(chooseNewSubject.isSelected());
				validateCurrentSubjectName();
			}
		});
		subjectCreating.add(chooseExistingSubject);
		subjectCreating.add(chooseNewSubject);
	}

	private void createSubjectButtons() {
		addSubject = new JButton("Agregar");
		addSubject.setEnabled(false);
		addSubject.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SubjectsDepartment sh = department.getSubjectsDepartment();
				if (!sh.existSubjectNamed(getSubjectList(), currentSubjectName)) {
					Subject subjectToAdd;
					if (!sh.existSubjectNamed(currentSubjectName))
						subjectToAdd = sh.createSubject(currentSubjectName);
					else
						subjectToAdd = sh.getSubjectNamed(currentSubjectName);
					((EasyListModel<Subject>) subjectList.getModel()).addItem(subjectToAdd);
					((EasyComboBoxModel<Subject>) allSubjectsCombo.getModel()).setModel(sh.getSubjects());
					cleanSubjectFields();
				}
			}
		});
		deleteSubject = new JButton("Eliminar Materia seleccionada");
		deleteSubject.setEnabled(false);
		deleteSubject.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Subject selection = subjectList.getSelectedValue();
				if (selection != null)
					((EasyListModel<Subject>) subjectList.getModel()).removeItem(selection);
			}
		});
	}

	protected void cleanSubjectFields() {
		allSubjectsCombo.setSelectedIndex(0);
		newSubjectField.setText("");
		currentSubjectName = "";
	}

	@Override
	protected String getWindowTitle() {
		return (inEditMode()) ? "Editar datos del profesor"	: "Agregar Profesor";
	}

	@Override
	protected int getWindowHeight() {
		return 380;
	}

	@Override
	protected int getWindowWidth() {
		return 420;
	}

	@Override
	protected boolean mustBeResizable() {
		return true;
	}

	@Override
	protected void setAnotherConfigurations() {
		setMinimumSize(new Dimension(getWindowWidth(), getWindowHeight()));
	}

	@Override
	protected void organizeTopPanelWidgets(JPanel topPanel) {
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel phonePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel mailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		topPanel.add(namePanel);
		topPanel.add(phonePanel);
		topPanel.add(mailPanel);
		topPanel.add(subjectsPanel);
		JLabel label1 = new JLabel("*");
		label1.setForeground(Color.RED);
		namePanel.add(label1);
		namePanel.add(nameLabel);
		namePanel.add(nameField);
		phonePanel.add(phoneLabel);
		phonePanel.add(phoneField);
		mailPanel.add(mailLabel);
		mailPanel.add(mailField);
		subjectsPanel.setLayout(new BoxLayout(subjectsPanel, BoxLayout.Y_AXIS));
		JPanel subjectLabelPanel = new JPanel();
		JPanel existingSubjectPanel = new JPanel();
		JPanel newSubjectPanel = new JPanel();
		JPanel buttonsPanel = new JPanel();
		subjectsPanel.add(new JScrollPane(subjectList));
		subjectsPanel.add(subjectLabelPanel);
		subjectsPanel.add(existingSubjectPanel);
		subjectsPanel.add(newSubjectPanel);
		subjectsPanel.add(buttonsPanel);
		subjectLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		subjectLabelPanel.add(addSubjectLabel);
		existingSubjectPanel.setLayout(new FlowLayout());
		existingSubjectPanel.add(chooseExistingSubject);
		existingSubjectPanel.add(allSubjectsCombo);
		newSubjectPanel.setLayout(new FlowLayout());
		newSubjectPanel.add(chooseNewSubject);
		newSubjectPanel.add(newSubjectField);
		buttonsPanel.setLayout(new FlowLayout());
		buttonsPanel.add(addSubject);
		buttonsPanel.add(deleteSubject);
	}

	@Override
	protected void doAcceptActionInEditMode() {
		department.modifyProfessor(item, nameField.getText(), phoneField.getText(), mailField.getText(), getSubjectList());
	}

	@Override
	protected void doAcceptInAddingMode() {
		department.createProfessor(nameField.getText(), phoneField.getText(), mailField.getText(), getSubjectList());
	}

	protected void validateCurrentSubjectName() {
		boolean visibility = currentSubjectName.equals("");
		addSubject.setEnabled(!visibility);
	}

	@Override
	protected void doValidations() {
		acceptButton.setEnabled(validateName());
	}

	protected boolean validateName() {
		String text = nameField.getText();
		for (Character c : text.toCharArray())
			if (!Character.isLetterOrDigit(c) && !Character.isWhitespace(c))
				return false;
		return text.toCharArray().length > 0;
	}
}