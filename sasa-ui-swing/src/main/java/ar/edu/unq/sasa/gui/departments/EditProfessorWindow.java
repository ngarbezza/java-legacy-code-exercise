package ar.edu.unq.sasa.gui.departments;

import ar.edu.unq.sasa.gui.util.combos.EasyComboBoxModel;
import ar.edu.unq.sasa.gui.util.lists.EasyListModel;
import ar.edu.unq.sasa.model.academic.Professor;
import ar.edu.unq.sasa.model.academic.Subject;
import ar.edu.unq.sasa.model.departments.ProfessorsDepartment;
import ar.edu.unq.sasa.model.departments.SubjectsDepartment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static ar.edu.unq.sasa.gui.util.LabelHelpers.requiredRedStar;

public class EditProfessorWindow extends AbstractEditWindow<Professor> {

    private static final long serialVersionUID = -252220491365344826L;

    protected JLabel nameLabel, phoneLabel, mailLabel;
    protected JTextField mailField, phoneField, nameField;

    protected JPanel subjectsPanel;
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
            public void keyReleased(KeyEvent anEvent) {
                doValidations();
            }
        });
        phoneField = new JTextField(12);
        phoneField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent anEvent) {
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
        allSubjectsCombo = new JComboBox<>(new EasyComboBoxModel<>(allSubjects));
        allSubjectsCombo.addActionListener(anEvent -> {
            Subject subject = (Subject) allSubjectsCombo.getSelectedItem();
            currentSubjectName = (subject == null) ? "" : subject.getName();
            validateCurrentSubjectName();
        });
        newSubjectField = new JTextField(24);
        newSubjectField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent anEvent) {
                currentSubjectName = newSubjectField.getText();
                validateCurrentSubjectName();
            }
        });
        subjectList = new JList<>(new EasyListModel<>());
        subjectList.addListSelectionListener(anEvent ->
                deleteSubject.setEnabled(!subjectList.getSelectionModel().isSelectionEmpty()));
        subjectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        createSubjectButtons();
        createSubjectRadioButtons();

        if (inEditMode()) {
            List<Subject> copy = item.getSubjects().stream()
                    .collect(Collectors.toCollection(LinkedList::new));
            ((EasyListModel<Subject>) subjectList.getModel()).setModel(copy);
        }
    }

    private void createSubjectRadioButtons() {
        ButtonGroup subjectCreating = new ButtonGroup();
        chooseExistingSubject = new JRadioButton("Existente", true);
        newSubjectField.setEnabled(!chooseExistingSubject.isSelected());
        chooseExistingSubject.addActionListener(anEvent -> {
            Subject subjectSelected = (Subject) allSubjectsCombo.getSelectedItem();
            currentSubjectName = (subjectSelected != null) ? getName() : "";
            newSubjectField.setEnabled(!chooseExistingSubject.isSelected());
            newSubjectField.setText("");
            allSubjectsCombo.setEnabled(chooseExistingSubject.isSelected());
            validateCurrentSubjectName();
        });
        chooseNewSubject = new JRadioButton("Nueva");
        chooseNewSubject.addActionListener(anEvent -> {
            currentSubjectName = newSubjectField.getText();
            allSubjectsCombo.setEnabled(!chooseNewSubject.isSelected());
            allSubjectsCombo.setSelectedIndex(0);
            newSubjectField.setEnabled(chooseNewSubject.isSelected());
            validateCurrentSubjectName();
        });
        subjectCreating.add(chooseExistingSubject);
        subjectCreating.add(chooseNewSubject);
    }

    private void createSubjectButtons() {
        addSubject = new JButton("Agregar");
        addSubject.setEnabled(false);
        addSubject.addActionListener(anEvent -> {
            SubjectsDepartment sh = department.getSubjectsDepartment();
            if (!sh.existSubjectNamed(currentSubjectName)) {
                Subject subjectToAdd;
                if (!sh.existSubjectNamed(currentSubjectName))
                    subjectToAdd = sh.createSubject(currentSubjectName);
                else
                    subjectToAdd = sh.getSubjectNamed(currentSubjectName);
                ((EasyListModel<Subject>) subjectList.getModel()).addItem(subjectToAdd);
                ((EasyComboBoxModel<Subject>) allSubjectsCombo.getModel()).setModel(sh.getSubjects());
                cleanSubjectFields();
            }
        });
        deleteSubject = new JButton("Eliminar Materia seleccionada");
        deleteSubject.setEnabled(false);
        deleteSubject.addActionListener(anEvent -> {
            Subject selection = subjectList.getSelectedValue();
            if (selection != null)
                ((EasyListModel<Subject>) subjectList.getModel()).removeItem(selection);
        });
    }

    protected void cleanSubjectFields() {
        allSubjectsCombo.setSelectedIndex(0);
        newSubjectField.setText("");
        currentSubjectName = "";
    }

    @Override
    protected String getWindowTitle() {
        return (inEditMode()) ? "Editar datos del profesor" : "Agregar Profesor";
    }

    @Override
    protected Integer getWindowHeight() {
        return 380;
    }

    @Override
    protected Integer getWindowWidth() {
        return 420;
    }

    @Override
    protected Boolean mustBeResizable() {
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
        namePanel.add(requiredRedStar());
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

    // TODO reuse this in other places
    protected boolean validateName() {
        String text = nameField.getText();
        for (Character c : text.toCharArray())
            if (!Character.isLetterOrDigit(c) && !Character.isWhitespace(c))
                return false;
        return text.toCharArray().length > 0;
    }
}
