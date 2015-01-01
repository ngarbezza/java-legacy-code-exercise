package ar.edu.unq.sasa.gui.departments;

import ar.edu.unq.sasa.gui.period.PeriodDetailWindow;
import ar.edu.unq.sasa.gui.util.Pair;
import ar.edu.unq.sasa.gui.util.ToStringConverter;
import ar.edu.unq.sasa.gui.util.tables.ReadOnlyTableModel;
import ar.edu.unq.sasa.model.requests.ClassroomRequest;
import ar.edu.unq.sasa.model.assignments.Assignment;
import ar.edu.unq.sasa.model.assignments.ClassroomAssignment;
import ar.edu.unq.sasa.model.departments.AssignmentsDepartment;
import ar.edu.unq.sasa.model.items.Classroom;
import ar.edu.unq.sasa.model.items.Resource;
import ar.edu.unq.sasa.model.time.Period;
import uic.layout.HorizontalLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class RequestViewWindow extends JFrame {

    private static final long serialVersionUID = -1212640840819470477L;

    private ClassroomRequest request;
    private ClassroomAssignment requestAssignment;
    private Classroom classroom;

    private JScrollPane requiredResourcesScrollPane;
    private JScrollPane optionalResourcesScrollPane;
    private JLabel professorTitleLabel;
    private JLabel professorLabel;
    private JLabel subjectTitleLabel;
    private JLabel subjectLabel;
    private JLabel capacityTitleLabel;
    private JLabel capacityLabel;
    private JButton showDesiredPeriodButton;
    private JButton showAssignedPeriodButton;
    private JButton showSatisfactionButton;
    private JLabel satisfactionConditionLabel;
    private JButton closeButton;

    public RequestViewWindow(AssignmentsDepartment assignmentsDepartment, ClassroomRequest requestSelection) {
        request = requestSelection;
        if (requestSelection.isAssigned())
            // TODO move this logic out of here
            for (Assignment assignment : assignmentsDepartment.getAssignments())
                if (assignment.isClassroomAssignment())
                    if (assignment.getRequest().equals(requestSelection)) {
                        requestAssignment = (ClassroomAssignment) assignment;
                        classroom = (Classroom) assignment.getAssignableItem();
                        break;
                    }
        SwingUtilities.invokeLater(() -> {
            createRequiredResourcesTable();
            createOptionalResourcesTable();
            createLabels();
            createButtons();
            organizeComponents();

            setResizable(false);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setTitle("Detalle del pedido");
            setSize(530, 410);
            setLocationRelativeTo(null);
            setVisible(true);
        });
    }

    private void createRequiredResourcesTable() {
        List<Pair<Resource, Integer>> requiredResources = resourcesMapToList(request.getRequiredResources());
        ReadOnlyTableModel<Pair<Resource, Integer>> tableModel = new ReadOnlyTableModel<>(requiredResources);
        addResourceColumns(tableModel);
        JTable requiredResourcesTable = new JTable(tableModel);
        requiredResourcesScrollPane = new JScrollPane(requiredResourcesTable);
    }

    private void addResourceColumns(ReadOnlyTableModel<Pair<Resource, Integer>> tableModel) {
        tableModel.addColumn("Recurso", "first", new ToStringConverter<Resource>() {
            @Override
            public String convert(Resource aResource) {
                return aResource.getName();
            }
        });
        tableModel.addColumn("Cantidad", "second", new ToStringConverter<Integer>() {
            @Override
            public String convert(Integer anAmount) {
                return anAmount.toString();
            }
        });
    }

    private void createOptionalResourcesTable() {
        List<Pair<Resource, Integer>> optionalResources = resourcesMapToList(request.getOptionalResources());
        ReadOnlyTableModel<Pair<Resource, Integer>> tableModel = new ReadOnlyTableModel<>(optionalResources);
        addResourceColumns(tableModel);
        JTable optionalResourcesTable = new JTable(tableModel);
        optionalResourcesScrollPane = new JScrollPane(optionalResourcesTable);
    }

    private List<Pair<Resource, Integer>> resourcesMapToList(Map<Resource, Integer> resourcesMap) {
        List<Pair<Resource, Integer>> resourcesList = new ArrayList<>();
        for (Entry<Resource, Integer> entry : resourcesMap.entrySet())
            resourcesList.add(new Pair<>(entry.getKey(), entry.getValue()));
        return resourcesList;
    }

    private void createLabels() {
        professorTitleLabel = new JLabel("              Profesor:   ");
        Font profTFont = professorTitleLabel.getFont();
        professorTitleLabel.setFont(new Font(profTFont.getFontName(), profTFont.getStyle(), 14));

        professorLabel = new JLabel(" " + request.getProfessor().getName());
        Font profFont = professorLabel.getFont();
        professorLabel.setFont(new Font(profFont.getFontName(), profFont.getStyle(), 14));

        subjectTitleLabel = new JLabel("                Materia:   ");
        Font subTFont = subjectTitleLabel.getFont();
        subjectTitleLabel.setFont(new Font(subTFont.getFontName(), subTFont.getStyle(), 14));

        subjectLabel = new JLabel(" " + request.getSubject().getName());
        Font subFont = subjectLabel.getFont();
        subjectLabel.setFont(new Font(subFont.getFontName(), subFont.getStyle(), 14));

        capacityTitleLabel = new JLabel("Capacidad pedida:   ");
        Font capTFont = capacityTitleLabel.getFont();
        capacityTitleLabel.setFont(new Font(capTFont.getFontName(), capTFont.getStyle(), 14));

        capacityLabel = new JLabel(" " + String.valueOf(request.getCapacity()));
        Font capFont = capacityLabel.getFont();
        capacityLabel.setFont(new Font(capFont.getFontName(), capFont.getStyle(), 14));

        satisfactionConditionLabel = new JLabel("(Sólo si el pedido fue asignado)");
        satisfactionConditionLabel.setForeground(Color.GRAY);
        Font satisFont = satisfactionConditionLabel.getFont();
        satisfactionConditionLabel.setFont(new Font(satisFont.getFontName(), satisFont.getStyle(), 11));
    }


    private void createButtons() {
        showDesiredPeriodButton = new JButton("Ver período pedido");
        createDesiredPeriodButtonListeners();
        showAssignedPeriodButton = new JButton("Ver período asignado");
        createAssignedPeriodButtonListeners();
        showSatisfactionButton = new JButton("   Ver satisfacción   ");
        createSatisfactionButtonListeners();
        closeButton = new JButton("Cerrar");
        createCloseButtonListeners();
        if (!request.isAssigned()) {
            showAssignedPeriodButton.setEnabled(false);
            showSatisfactionButton.setEnabled(false);
        }
    }

    private void createDesiredPeriodButtonListeners() {
        showDesiredPeriodButton.addActionListener(anEvent -> new PeriodDetailWindow(request.getDesiredHours()));
    }

    private void createAssignedPeriodButtonListeners() {
        showAssignedPeriodButton.addActionListener(anEvent -> {
            if (request.isAssigned())
                new PeriodDetailWindow(getAssignedPeriod());
        });
    }

    private Period getAssignedPeriod() {
        Period assignedPeriod = null;
        for (Entry<Period, Assignment> entry : classroom.getAssignments().entrySet())
            if (entry.getValue().isClassroomAssignment())
                if (entry.getValue().getRequest().equals(request)) {
                    assignedPeriod = entry.getKey();
                    break;
                }
        return assignedPeriod;
    }

    private void createSatisfactionButtonListeners() {
        showSatisfactionButton.addActionListener(anEvent -> new ShowSatisfactionWindow(requestAssignment));
    }

    private void createCloseButtonListeners() {
        closeButton.addActionListener(anEvent -> dispose());
    }

    private void organizeComponents() {
        JPanel mainPanel = new JPanel();
        getContentPane().add(mainPanel);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new HorizontalLayout());
        topPanel.setAlignmentX(CENTER_ALIGNMENT);
        requiredResourcesScrollPane.setBorder(BorderFactory.createTitledBorder("Recursos obligatorios"));
        topPanel.add(requiredResourcesScrollPane);
        optionalResourcesScrollPane.setBorder(BorderFactory.createTitledBorder("Recursos opcionales"));
        topPanel.add(optionalResourcesScrollPane);

        mainPanel.add(topPanel);

        JPanel topMiddlePanel = new JPanel();
        topMiddlePanel.setLayout(new FlowLayout());
        topMiddlePanel.add(showDesiredPeriodButton);

        mainPanel.add(topMiddlePanel);

        JPanel leftMiddlePanel = new JPanel();
        leftMiddlePanel.setLayout(new GridLayout(4, 1));
        leftMiddlePanel.add(professorTitleLabel);
        leftMiddlePanel.add(subjectTitleLabel);
        leftMiddlePanel.add(capacityTitleLabel);
        leftMiddlePanel.add(new JLabel(" "));

        JPanel rightMiddlePanel = new JPanel();
        rightMiddlePanel.setLayout(new GridLayout(4, 1));
        rightMiddlePanel.add(professorLabel);
        rightMiddlePanel.add(subjectLabel);
        rightMiddlePanel.add(capacityLabel);
        rightMiddlePanel.add(new JLabel(" "));

        JPanel middlePanelLeftBorder = new JPanel();
        middlePanelLeftBorder.setLayout(new BorderLayout());
        middlePanelLeftBorder.add(leftMiddlePanel, BorderLayout.LINE_END);

        JPanel middlePanelRightBorder = new JPanel();
        middlePanelRightBorder.setLayout(new BorderLayout());
        middlePanelRightBorder.add(rightMiddlePanel, BorderLayout.LINE_START);

        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new GridLayout(1, 1));
        middlePanel.add(middlePanelLeftBorder);
        middlePanel.add(middlePanelRightBorder);

        mainPanel.add(middlePanel);

        JPanel closePanel = new JPanel();
        closePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        closePanel.add(closeButton);

        JPanel assignedRequestPanel = new JPanel();
        assignedRequestPanel.setLayout(new FlowLayout());
        assignedRequestPanel.add(showSatisfactionButton);
        assignedRequestPanel.add(showAssignedPeriodButton);

        JPanel assignedRequestLabelPanel = new JPanel();
        assignedRequestLabelPanel.setLayout(new FlowLayout());
        assignedRequestLabelPanel.add(satisfactionConditionLabel);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.add(assignedRequestPanel);
        bottomPanel.add(assignedRequestLabelPanel);
        bottomPanel.add(closePanel);

        mainPanel.add(bottomPanel);
    }
}
