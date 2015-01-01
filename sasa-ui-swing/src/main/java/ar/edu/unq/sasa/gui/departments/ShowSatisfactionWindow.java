package ar.edu.unq.sasa.gui.departments;

import ar.edu.unq.sasa.gui.period.PeriodDetailWindow;
import ar.edu.unq.sasa.gui.util.LabelHelpers;
import ar.edu.unq.sasa.gui.util.Pair;
import ar.edu.unq.sasa.gui.util.ToStringConverter;
import ar.edu.unq.sasa.gui.util.tables.ReadOnlyTableModel;
import ar.edu.unq.sasa.model.assignments.ClassroomAssignment;
import ar.edu.unq.sasa.model.items.Resource;
import ar.edu.unq.sasa.model.time.Period;
import uic.layout.HorizontalLayout;
import uic.layout.VerticalLayout;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ShowSatisfactionWindow extends JFrame {

    private static final long serialVersionUID = -3664046632029358042L;

    private ClassroomAssignment assignment;

    private Period selectedPeriod;

    private JScrollPane missingResourcesScrollPane;
    private JScrollPane periodSuperpositionsScrollPane;
    private JTable periodSuperpositionsTable;
    private JButton showPeriodButton;
    private JLabel assignedClassroomTitleLabel;
    private JLabel assignedClassroomLabel;
    private JLabel capacityDifferenceTitleLabel;
    private JLabel capacityDifferenceLabel;
    private JLabel satisfiedRequestTitleLabel;
    private JLabel satisfiedRequestLabel;
    private JButton closeButton;


    public ShowSatisfactionWindow(ClassroomAssignment anAssignment) {
        assignment = anAssignment;

        SwingUtilities.invokeLater(() -> {
            createMissingResourcesTable();
            createPeriodSuperpositionsTable();
            createLabels();
            createButtons();
            organizeComponents();

            setResizable(false);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setTitle("Satisfacción");
            setSize(500, 350);
            setLocationRelativeTo(null);
            setVisible(true);
        });
    }

    private void createMissingResourcesTable() {
        ReadOnlyTableModel<Pair<Resource, Integer>> tableModel = new ReadOnlyTableModel<>(getMissingResources());
        addResourceColumns(tableModel);
        JTable missingResourcesTable = new JTable(tableModel);
        missingResourcesScrollPane = new JScrollPane(missingResourcesTable);
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

    private List<Pair<Resource, Integer>> getMissingResources() {
        Map<Resource, Integer> missingResourcesMap = assignment.getSatisfaction().getResources();
        return resourcesMapToList(missingResourcesMap);
    }

    private List<Pair<Resource, Integer>> resourcesMapToList(Map<Resource, Integer> resourcesMap) {
        List<Pair<Resource, Integer>> resourcesList = new ArrayList<>();
        for (Entry<Resource, Integer> entry : resourcesMap.entrySet())
            resourcesList.add(new Pair<>(entry.getKey(), entry.getValue()));
        return resourcesList;
    }

    private void createPeriodSuperpositionsTable() {
        ReadOnlyTableModel<Pair<Period, Float>> tableModel =
                new ReadOnlyTableModel<>(getPeriodSuperpositionsResources());
        addPeriodSuperpositionsColumns(tableModel);
        periodSuperpositionsTable = new JTable(tableModel);
        periodSuperpositionsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        periodSuperpositionsTable.getSelectionModel().addListSelectionListener(
                ShowSatisfactionWindow.this::whenPeriodTableSelectionChanged);
        periodSuperpositionsScrollPane = new JScrollPane(periodSuperpositionsTable);
    }

    @SuppressWarnings("unchecked")
    protected void whenPeriodTableSelectionChanged(ListSelectionEvent anEvent) {
        DefaultListSelectionModel source = (DefaultListSelectionModel) anEvent.getSource();
        if (source.isSelectionEmpty()) {
            selectedPeriod = null;
            showPeriodButton.setEnabled(false);
        } else {
            int index = source.getMinSelectionIndex();
            List<Pair<Period, Float>> model =
                    ((ReadOnlyTableModel<Pair<Period, Float>>) periodSuperpositionsTable.getModel()).getModel();
            selectedPeriod = (Period) model.get(index).getFirst();
            showPeriodButton.setEnabled(true);
        }
    }

    private List<Pair<Period, Float>> getPeriodSuperpositionsResources() {
        Map<Period, Float> periodSuperpositionsMap = assignment.getSatisfaction().getTimeDifference();
        return periodSuperpositionsMapToList(periodSuperpositionsMap);
    }

    private List<Pair<Period, Float>> periodSuperpositionsMapToList(Map<Period, Float> periodSuperpositionsMap) {
        List<Pair<Period, Float>> periodSuperpositionsList = new ArrayList<>();
        for (Entry<Period, Float> entry : periodSuperpositionsMap.entrySet())
            periodSuperpositionsList.add(new Pair<>(entry.getKey(), entry.getValue()));
        return periodSuperpositionsList;
    }

    private void addPeriodSuperpositionsColumns(ReadOnlyTableModel<Pair<Period, Float>> tableModel) {
        tableModel.addColumn("Tipo", "first", new ToStringConverter<Period>() {
            @Override
            public String convert(Period aPeriod) {
                return assignment.getAssignableItem().getAssignments().get(aPeriod).isBookedAssignment()
                        ? "(Reserva)"
                        : "(Asignación por Pedido)";
            }
        });
        tableModel.addColumn("Cantidad de Horas", "second", new ToStringConverter<Float>() {
            @Override
            public String convert(Float anAmount) {
                return "Superpuesta " + anAmount.toString() + " hs";
            }
        });
    }

    private void createLabels() {
        assignedClassroomTitleLabel = new JLabel("               Aula asignada:  ");
        Font profTFont = assignedClassroomTitleLabel.getFont();
        assignedClassroomTitleLabel.setFont(new Font(profTFont.getFontName(), profTFont.getStyle(), 14));

        assignedClassroomLabel = new JLabel(" " + assignment.getAssignableItem().getName());
        Font profFont = assignedClassroomLabel.getFont();
        assignedClassroomLabel.setFont(new Font(profFont.getFontName(), profFont.getStyle(), 14));

        capacityDifferenceTitleLabel = new JLabel("Diferencia de capacidad:  ");
        Font subTFont = capacityDifferenceTitleLabel.getFont();
        capacityDifferenceTitleLabel.setFont(new Font(subTFont.getFontName(), subTFont.getStyle(), 14));

        String capacityDifferenceShower;
        Integer capacityDifference = assignment.getSatisfaction().getCapacityDifference();
        if (capacityDifference > 0)
            capacityDifferenceShower = "Sobran " + capacityDifference + " lugares";
        else if (capacityDifference == 0)
            capacityDifferenceShower = "No sobran ni faltan asientos";
        else
            capacityDifferenceShower = "Faltan " + capacityDifference * -1 + " lugares";
        capacityDifferenceLabel = new JLabel(" " + capacityDifferenceShower);
        Font subFont = capacityDifferenceLabel.getFont();
        capacityDifferenceLabel.setFont(new Font(subFont.getFontName(), subFont.getStyle(), 14));

        satisfiedRequestTitleLabel = new JLabel("         Pedido satisfecho:  ");
        Font capTFont = satisfiedRequestTitleLabel.getFont();
        satisfiedRequestTitleLabel.setFont(new Font(capTFont.getFontName(), capTFont.getStyle(), 14));

        String satisfied = LabelHelpers.friendlyBoolean(assignment.getSatisfaction().isSatisfied());
        satisfiedRequestLabel = new JLabel(" " + satisfied);
        Font capFont = satisfiedRequestLabel.getFont();
        satisfiedRequestLabel.setFont(new Font(capFont.getFontName(), capFont.getStyle(), 14));
    }

    private void createButtons() {
        closeButton = new JButton("Cerrar");
        createCloseButtonListeners();
        showPeriodButton = new JButton("Detalle del período");
        createShowPeriodButtonListeners();
        showPeriodButton.setEnabled(false);
    }

    private void createCloseButtonListeners() {
        closeButton.addActionListener(anEvent -> dispose());
    }

    private void createShowPeriodButtonListeners() {
        showPeriodButton.addActionListener(anEvent -> new PeriodDetailWindow(selectedPeriod));
    }

    private void organizeComponents() {
        JPanel mainPanel = new JPanel();
        getContentPane().add(mainPanel);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new HorizontalLayout());
        topPanel.setAlignmentX(CENTER_ALIGNMENT);
        missingResourcesScrollPane.setBorder(BorderFactory.createTitledBorder("Recursos que faltaron asignar"));
        topPanel.add(missingResourcesScrollPane);

        JPanel topRightPanel = new JPanel();
        topRightPanel.setLayout(new VerticalLayout());
        topRightPanel.setAlignmentX(CENTER_ALIGNMENT);
        topRightPanel.setBorder(BorderFactory.createTitledBorder("Horas de los períodos superpuestos"));
        topRightPanel.add(periodSuperpositionsScrollPane);
        topRightPanel.add(showPeriodButton);

        topPanel.add(topRightPanel);

        mainPanel.add(topPanel);

        JPanel leftMiddlePanel = new JPanel();
        leftMiddlePanel.setLayout(new GridLayout(4, 1));
        leftMiddlePanel.add(assignedClassroomTitleLabel);
        leftMiddlePanel.add(capacityDifferenceTitleLabel);
        leftMiddlePanel.add(satisfiedRequestTitleLabel);
        leftMiddlePanel.add(new JLabel(" "));

        JPanel rightMiddlePanel = new JPanel();
        rightMiddlePanel.setLayout(new GridLayout(4, 1));
        rightMiddlePanel.add(assignedClassroomLabel);
        rightMiddlePanel.add(capacityDifferenceLabel);
        rightMiddlePanel.add(satisfiedRequestLabel);
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
        closePanel.setLayout(new FlowLayout());
        closePanel.add(closeButton);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.add(closePanel);

        mainPanel.add(bottomPanel);
    }
}
