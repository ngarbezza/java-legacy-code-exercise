package ar.edu.unq.sasa.gui.departments;

import ar.edu.unq.sasa.gui.util.ToStringConverter;
import ar.edu.unq.sasa.gui.util.combos.EasyComboBoxModel;
import ar.edu.unq.sasa.gui.util.combos.EasyComboBoxRenderer;
import ar.edu.unq.sasa.gui.util.tables.ReadOnlyTableModel;
import ar.edu.unq.sasa.model.requests.ClassroomRequest;
import ar.edu.unq.sasa.model.academic.Professor;
import ar.edu.unq.sasa.model.requests.Request;
import ar.edu.unq.sasa.model.academic.Subject;
import ar.edu.unq.sasa.model.assignments.Assignment;
import ar.edu.unq.sasa.model.assignments.ClassroomAssignment;
import ar.edu.unq.sasa.model.departments.AssignmentsDepartment;
import ar.edu.unq.sasa.model.items.Classroom;
import ar.edu.unq.sasa.util.Subscriber;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static ar.edu.unq.sasa.gui.util.Dialogs.withConfirmation;
import static ar.edu.unq.sasa.gui.util.WidgetUtilities.disableAll;
import static ar.edu.unq.sasa.gui.util.WidgetUtilities.enableAll;

public class AssignmentsPanel extends JPanel implements Subscriber {

    private static final long serialVersionUID = -39425443658019906L;
    protected AssignmentsDepartment department;
    protected ClassroomRequest requestSelection;
    protected ClassroomAssignment assignmentSelection;

    protected JTable requestsTable;
    protected JTable assignmentsTable;
    protected JScrollPane requestsScrollPane;
    protected JScrollPane assignmentsScrollPane;
    protected JButton deleteButton;
    protected JButton modifyButton;
    protected JButton assignButton;
    protected JButton viewDetailsButton;
    protected JLabel searchLabel;
    protected JComboBox<Professor> professorsComboBox;

    public AssignmentsPanel(AssignmentsDepartment assignmentsDepartment) {
        department = assignmentsDepartment;
        registerAsSubscriber();
        createSearchComponents();
        createRequestsTable();
        createAssignmentsTable();
        createButtons();
        organizeComponents();
    }

    public AssignmentsDepartment getDepartment() {
        return department;
    }

    @Override
    public String getName() {
        return "Asignaciones";
    }

    public ClassroomRequest getRequestSelection() {
        return requestSelection;
    }

    private List<Request> getRequests() {
        // TODO too much logic to be here
        List<Request> requests = new ArrayList<>();
        for (Request request : getDepartment().getRequestsDepartment().getRequests())
            if (request.isClassroomRequest() && !request.isAssigned())
                if (professorsComboBox.getModel().getSelectedItem() == null)
                    requests.add(request);
                else if (professorsComboBox.getModel().getSelectedItem().equals(request.getProfessor()))
                    requests.add(request);
        return requests;
    }

    protected List<ClassroomAssignment> getAssignments() {
        List<ClassroomAssignment> assignments = new ArrayList<>();
        for (Assignment assignment : getDepartment().getAssignments())
            if (assignment.isClassroomAssignment())
                // TODO refactor next question
                if (professorsComboBox.getModel().getSelectedItem() == null
                        || professorsComboBox.getModel().getSelectedItem().equals(
                        assignment.getRequest().getProfessor()))
                    assignments.add((ClassroomAssignment) assignment);
        return assignments;
    }

    @SuppressWarnings("unchecked")
    private void createSearchComponents() {
        searchLabel = new JLabel("Búsqueda por Profesor");
        professorsComboBox = (JComboBox<Professor>) makeProfessorsComboBox();
    }

    @SuppressWarnings({"unchecked", "serial"})
    private Component makeProfessorsComboBox() {
        EasyComboBoxModel<Professor> comboModel = new EasyComboBoxModel<>(getProfessors());
        JComboBox<Professor> combo = new JComboBox<>(comboModel);
        combo.setRenderer(new EasyComboBoxRenderer<Professor>() {
            @Override
            public String getDisplayName(Professor professor) {
                return professor.getName();
            }
        });
        combo.addActionListener(anEvent -> updateTables());
        combo.setPreferredSize(new Dimension(120, 20));
        return combo;
    }

    private List<Professor> getProfessors() {
        return department.getProfessorsDepartment().getProfessors();
    }

    private void createButtons() {
        assignButton = new JButton("Asignar Pedido");
        createAssignButtonListeners();
        viewDetailsButton = new JButton("Detalle del Pedido Asignado");
        createViewDetailsButtonListeners();
        modifyButton = new JButton("Modificar Asignación");
        createModifyButtonListeners();
        deleteButton = new JButton("Eliminar Asignación");
        createDeleteButtonListeners();
        disableAll(viewDetailsButton, assignButton, deleteButton, modifyButton);
    }

    private void createViewDetailsButtonListeners() {
        viewDetailsButton.addActionListener(anEvent ->
                new RequestViewWindow(department, assignmentSelection.getRequest()));
    }

    private void createAssignButtonListeners() {
        assignButton.addActionListener(anEvent ->
                new AssignRequestWindow(department, AssignmentsPanel.this));
    }

    private void createModifyButtonListeners() {
        modifyButton.addActionListener(anEvent ->
                new EditAssignmentWindow(department, assignmentSelection));
    }

    private void createDeleteButtonListeners() {
        deleteButton.addActionListener(anEvent -> {
            withConfirmation("Eliminar", "¿Desea eliminar la asignación seleccionada?", () -> {
                getDepartment().deleteAssignment(assignmentSelection);
                updateTables();
            });
        });
    }

    private void createAssignmentsTable() {
        ReadOnlyTableModel<ClassroomAssignment> tableModel = new ReadOnlyTableModel<>(getAssignments());
        addAssignmentColumns(tableModel);
        assignmentsTable = new JTable(tableModel);
        assignmentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        assignmentsTable.getSelectionModel().addListSelectionListener(
                AssignmentsPanel.this::whenAssignmentsTableSelectionChanged);
        assignmentsScrollPane = new JScrollPane(assignmentsTable);
    }

    @SuppressWarnings("unchecked")
    private void whenAssignmentsTableSelectionChanged(ListSelectionEvent anEvent) {
        DefaultListSelectionModel source = (DefaultListSelectionModel) anEvent.getSource();
        if (source.isSelectionEmpty()) {
            assignmentSelection = null;
            disableAll(viewDetailsButton, deleteButton, modifyButton);
        } else {
            int index = source.getMinSelectionIndex();
            // TODO there are a lot of casts like this. try to avoid them
            List<ClassroomAssignment> model =
                    ((ReadOnlyTableModel<ClassroomAssignment>) assignmentsTable.getModel()).getModel();
            assignmentSelection = model.get(index);
            enableAll(viewDetailsButton, deleteButton, modifyButton);
        }
    }

    private void addAssignmentColumns(ReadOnlyTableModel<ClassroomAssignment> tableModel) {
        tableModel.addColumn("Profesor", "request", new ToStringConverter<Request>() {
            @Override
            public String convert(Request aRequest) {
                return aRequest.getProfessor().getName();
            }
        });
        tableModel.addColumn("Materia", "request", new ToStringConverter<Request>() {
            @Override
            public String convert(Request aRequest) {
                return aRequest.getSubject().getName();
            }
        });
        tableModel.addColumn("Aula", "assignableItem", new ToStringConverter<Classroom>() {
            @Override
            public String convert(Classroom aClassroom) {
                return aClassroom.getName();
            }
        });
    }

    private void createRequestsTable() {
        ReadOnlyTableModel<Request> tableModel = new ReadOnlyTableModel<>(getRequests());
        addRequestsColumns(tableModel);
        requestsTable = new JTable(tableModel);
        requestsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        requestsTable.getSelectionModel().addListSelectionListener(AssignmentsPanel.this::whenRequestsTableSelectionChanged);
        requestsScrollPane = new JScrollPane(requestsTable);
    }

    @SuppressWarnings("unchecked")
    protected void whenRequestsTableSelectionChanged(ListSelectionEvent anEvent) {
        DefaultListSelectionModel source = (DefaultListSelectionModel) anEvent.getSource();
        if (source.isSelectionEmpty()) {
            requestSelection = null;
            assignButton.setEnabled(false);
        } else {
            int index = source.getMinSelectionIndex();
            List<ClassroomRequest> model = ((ReadOnlyTableModel<ClassroomRequest>) requestsTable.getModel()).getModel();
            requestSelection = model.get(index);
            assignButton.setEnabled(true);
        }
    }

    private void addRequestsColumns(ReadOnlyTableModel<Request> aTableModel) {
        aTableModel.addColumn("Profesor", "professor", new ToStringConverter<Professor>() {
            @Override
            public String convert(Professor aProfessor) {
                return aProfessor.getName();
            }
        });
        aTableModel.addColumn("Materia", "subject", new ToStringConverter<Subject>() {
            @Override
            public String convert(Subject aSubject) {
                return aSubject.getName();
            }
        });
    }

    private void registerAsSubscriber() {
        getDepartment().getPublisher().addSubscriber("requestsChanged", this);
        getDepartment().getPublisher().addSubscriber("assignmentsChanged", this);
    }

    private void organizeComponents() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createTitledBorder("Pedidos sin Asignar"));
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createTitledBorder("Pedidos Asignados"));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));

        JPanel comboBoxPanel = new JPanel();
        comboBoxPanel.setLayout(new FlowLayout());
        comboBoxPanel.add(searchLabel);
        comboBoxPanel.add(professorsComboBox);

        add(comboBoxPanel);
        add(bottomPanel);

        bottomPanel.add(leftPanel);
        bottomPanel.add(rightPanel);

        JPanel viewDetailsButtonPanel = new JPanel();
        viewDetailsButtonPanel.setLayout(new FlowLayout());
        viewDetailsButtonPanel.add(viewDetailsButton);

        JPanel assignButtonPanel = new JPanel();
        assignButtonPanel.setLayout(new FlowLayout());
        assignButtonPanel.add(assignButton);

        JPanel modifyButtonPanel = new JPanel();
        modifyButtonPanel.setLayout(new FlowLayout());
        modifyButtonPanel.add(modifyButton);

        JPanel deleteButtonPanel = new JPanel();
        deleteButtonPanel.setLayout(new FlowLayout());
        deleteButtonPanel.add(deleteButton);

        leftPanel.add(requestsScrollPane);
        leftPanel.add(assignButtonPanel);

        rightPanel.add(assignmentsScrollPane);
        rightPanel.add(viewDetailsButtonPanel);
        rightPanel.add(modifyButtonPanel);
        rightPanel.add(deleteButtonPanel);
    }

    @SuppressWarnings("unchecked")
    public void updateTables() {
        ((ReadOnlyTableModel<ClassroomAssignment>) assignmentsTable.getModel())
                .setModel(getAssignments());
        ((ReadOnlyTableModel<Request>) requestsTable.getModel())
                .setModel(getRequests());
    }

    @Override
    public void update(String aspect, Object value) {
        updateTables();
    }
}
