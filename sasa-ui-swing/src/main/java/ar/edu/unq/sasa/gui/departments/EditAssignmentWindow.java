package ar.edu.unq.sasa.gui.departments;

import ar.edu.unq.sasa.gui.period.NewPeriodWindow;
import ar.edu.unq.sasa.gui.util.PeriodHolder;
import ar.edu.unq.sasa.gui.util.tables.ReadOnlyTableModel;
import ar.edu.unq.sasa.model.assignments.ClassroomAssignment;
import ar.edu.unq.sasa.model.departments.AssignmentsDepartment;
import ar.edu.unq.sasa.model.items.Classroom;
import ar.edu.unq.sasa.model.time.Period;
import uic.layout.VerticalLayout;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import static ar.edu.unq.sasa.gui.util.Dialogs.withConfirmation;

public class EditAssignmentWindow extends JFrame implements PeriodHolder {

    private static final long serialVersionUID = 4976218465593759722L;

    private ClassroomAssignment assignment;

    private Classroom classroomSelected;
    private Period period;

    private JLabel searchLabel;
    private JTextField searchTextField;
    private JScrollPane classroomsTableScrollPane;
    private JTable classroomsTable;
    private JButton changePeriodButton;
    private JButton moveAssignmentOfClassroomButton;
    private JButton moveAssignmentOfPeriodButton;
    private JButton cancelButton;

    private AssignmentsDepartment department;

    public EditAssignmentWindow(AssignmentsDepartment assignmentsDepartment, ClassroomAssignment selection) {
        department = assignmentsDepartment;
        assignment = selection;
        period = selection.getPeriod();

        SwingUtilities.invokeLater(() -> {
            createSearchComponents();
            createClassroomsTable();
            createButtons();
            organizeComponents();

            setResizable(false);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setTitle("Modificar asignación");
            setSize(350, 450);
            setLocationRelativeTo(null);
            setVisible(true);
        });
    }

    @Override
    public Period getPeriod() {
        return period;
    }

    @Override
    public void setPeriod(Period aPeriod) {
        period = aPeriod;
        moveAssignmentOfPeriodButton.setEnabled(true);
    }

    private void createSearchComponents() {
        searchLabel = new JLabel("Búsqueda por nombre");
        searchTextField = new JTextField(10);
        createSearchTextFieldListeners();
    }

    private void createSearchTextFieldListeners() {
        searchTextField.addKeyListener(new KeyAdapter() {
            @Override
            @SuppressWarnings("unchecked")
            public void keyReleased(KeyEvent anEvent) {
                String text = ((JTextField) anEvent.getSource()).getText();
                List<Classroom> res = department.getClassroomsDepartment().searchClassroomByName(text);
                ((ReadOnlyTableModel<Classroom>) classroomsTable.getModel()).setModel(res);
            }
        });
    }

    private void createClassroomsTable() {
        ReadOnlyTableModel<Classroom> tableModel =
                new ReadOnlyTableModel<>(department.getClassroomsDepartment().searchClassroomByName(""));
        addClassroomsColumns(tableModel);
        classroomsTable = new JTable(tableModel);
        classroomsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        classroomsTable.getSelectionModel().addListSelectionListener(
                EditAssignmentWindow.this::whenClassroomsTableSelectionChanged);
        classroomsTableScrollPane = new JScrollPane(classroomsTable);
    }

    private void addClassroomsColumns(ReadOnlyTableModel<Classroom> tableModel) {
        tableModel.addColumn("Aula", "name");
        tableModel.addColumn("Capacidad", "capacity");
    }

    @SuppressWarnings("unchecked")
    protected void whenClassroomsTableSelectionChanged(ListSelectionEvent anEvent) {
        DefaultListSelectionModel source = (DefaultListSelectionModel) anEvent.getSource();
        if (source.isSelectionEmpty()) {
            classroomSelected = null;
            moveAssignmentOfClassroomButton.setEnabled(false);
        } else {
            int index = source.getMinSelectionIndex();
            List<Classroom> model = ((ReadOnlyTableModel<Classroom>) classroomsTable.getModel()).getModel();
            classroomSelected = model.get(index);
            moveAssignmentOfClassroomButton.setEnabled(!classroomSelected.equals(assignment.getAssignableItem()));
        }
    }

    private void createButtons() {
        changePeriodButton = new JButton("Cambiar período");
        createChangePeriodButtonListeners();
        moveAssignmentOfClassroomButton = new JButton("Cambiar asignación al aula seleccionada");
        createMoveAssignmentOfClassroomButtonButtonListeners();
        moveAssignmentOfClassroomButton.setEnabled(false);
        moveAssignmentOfPeriodButton = new JButton("Cambiar asignación de período");
        createMoveAssignmentOfPeriodButtonButtonListeners();
        moveAssignmentOfPeriodButton.setEnabled(false);
        cancelButton = new JButton("Cancelar");
        createCancelButtonListeners();
    }

    private void createChangePeriodButtonListeners() {
        changePeriodButton.addActionListener(anEvent -> new NewPeriodWindow(EditAssignmentWindow.this));
    }

    private void createMoveAssignmentOfClassroomButtonButtonListeners() {
        moveAssignmentOfClassroomButton.addActionListener(anEvent ->
                withConfirmation("Cambio de aula", "¿Desea cambiar la asignación al aula elegida?", () -> {
                    department.moveAssignmentOfClassroom(assignment, classroomSelected);
                    dispose();
                }));
    }

    private void createMoveAssignmentOfPeriodButtonButtonListeners() {
        moveAssignmentOfPeriodButton.addActionListener(anEvent ->
                withConfirmation("Cambio de período", "¿Desea cambiar el período de la asignación?", () -> {
                    department.moveAssignmentOfPeriod(assignment, period);
                    dispose();
                }));
    }

    private void createCancelButtonListeners() {
        cancelButton.addActionListener(anEvent ->
                withConfirmation("Cancelar", "¿Desea cancelar los cambios?", this::dispose));
    }

    private void organizeComponents() {
        JPanel mainPanel = new JPanel();
        getContentPane().add(mainPanel);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel classroomsTablePanel = new JPanel();
        classroomsTablePanel.setLayout(new BoxLayout(classroomsTablePanel, BoxLayout.Y_AXIS));
        classroomsTablePanel.setBorder(BorderFactory.createTitledBorder("Seleccione un aula"));
        JPanel periodSelectionPanel = new JPanel();
        JPanel moveButtonsPanel = new JPanel();
        JPanel cancelPanel = new JPanel();
        cancelPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout());
        searchPanel.add(searchLabel);
        searchPanel.add(searchTextField);

        classroomsTablePanel.add(searchPanel);
        classroomsTablePanel.add(classroomsTableScrollPane);

        mainPanel.add(classroomsTablePanel);
        mainPanel.add(periodSelectionPanel);
        mainPanel.add(moveButtonsPanel);
        mainPanel.add(cancelPanel);

        periodSelectionPanel.setLayout(new FlowLayout());
        periodSelectionPanel.add(changePeriodButton);

        moveButtonsPanel.setLayout(new VerticalLayout());
        moveButtonsPanel.add(moveAssignmentOfClassroomButton);
        moveButtonsPanel.add(moveAssignmentOfPeriodButton);

        cancelPanel.add(cancelButton);
    }
}
