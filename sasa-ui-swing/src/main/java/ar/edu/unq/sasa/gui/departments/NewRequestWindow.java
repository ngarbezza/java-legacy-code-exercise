package ar.edu.unq.sasa.gui.departments;

import ar.edu.unq.sasa.gui.period.NewPeriodWindow;
import ar.edu.unq.sasa.gui.util.PeriodHolder;
import ar.edu.unq.sasa.gui.util.combos.EasyComboBoxModel;
import ar.edu.unq.sasa.gui.util.combos.EasyComboBoxRenderer;
import ar.edu.unq.sasa.gui.util.tables.ReadOnlyTableModel;
import ar.edu.unq.sasa.model.academic.Professor;
import ar.edu.unq.sasa.model.academic.Subject;
import ar.edu.unq.sasa.model.departments.RequestsDepartment;
import ar.edu.unq.sasa.model.items.Resource;
import ar.edu.unq.sasa.model.requests.Requirement;
import ar.edu.unq.sasa.model.time.Period;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.*;
import java.util.List;

import static ar.edu.unq.sasa.gui.util.Dialogs.withConfirmation;
import static ar.edu.unq.sasa.gui.util.LabelHelpers.requiredRedStar;

public class NewRequestWindow extends JFrame implements PeriodHolder {

    private static final long serialVersionUID = 6081968814611526766L;

    private JTable requiredResourcesTable, optionalResourcesTable;
    private JComboBox<Resource> resourceCombo;
    private JComboBox<Professor> professorCombo;
    private JComboBox<Subject> subjectCombo;
    private JSpinner amountSelector, capacitySelector;
    private JRadioButton optionalRadioButton, requiredRadioButton;
    private JLabel labelResource, labelAmount, professorLabel, subjectLabel, capacityLabel;
    private JTextArea periodDetailArea;
    private JButton addResourceButton, deleteResourceButton, addPeriodButton, createRequestButton, cancelButton;

    private Period specifiedPeriod;

    private RequestsDepartment department;

    public NewRequestWindow(RequestsDepartment requestsDepartment) {
        department = requestsDepartment;
        SwingUtilities.invokeLater(() -> {
            createWidgets();
            addWidgets();

            setSize(600, 570);
            setLocationRelativeTo(null);
            setTitle("Crear pedido");
            setResizable(false);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setVisible(true);
        });
    }

    protected void createWidgets() {
        requiredResourcesTable = createResourcesTable();
        createRequiredResourcesTableListeners();
        optionalResourcesTable = createResourcesTable();
        createOptionalResourcesTableListeners();
        createAddResourceWidgets();
        createSubjectAndProfessorWidgets();
        createCapacityWidgets();
        createPeriodWidgets();
        createButtons();
    }

    private void createRequiredResourcesTableListeners() {
        requiredResourcesTable.getSelectionModel().addListSelectionListener(anEvent -> {
            Boolean someSelectedItem = requiredResourcesTable.getSelectionModel().isSelectionEmpty();
            deleteResourceButton.setEnabled(!someSelectedItem);
            if (!someSelectedItem)
                optionalResourcesTable.getSelectionModel().clearSelection();
        });
    }

    private void createOptionalResourcesTableListeners() {
        optionalResourcesTable.getSelectionModel().addListSelectionListener(anEvent -> {
            Boolean someSelectedItem = optionalResourcesTable.getSelectionModel().isSelectionEmpty();
            deleteResourceButton.setEnabled(!someSelectedItem);
            if (!someSelectedItem)
                requiredResourcesTable.getSelectionModel().clearSelection();
        });
    }

    private JTable createResourcesTable() {
        ReadOnlyTableModel<ResourceView> tableModel = new ReadOnlyTableModel<>();
        tableModel.addColumn("Nombre", "name");
        tableModel.addColumn("Cantidad", "amount");
        JTable newTable = new JTable(tableModel);
        newTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return newTable;
    }

    @SuppressWarnings({"unchecked", "serial"})
    private void createAddResourceWidgets() {
        labelResource = new JLabel("Recurso");
        labelAmount = new JLabel("Cantidad");
        EasyComboBoxModel<Resource> comboModel = new EasyComboBoxModel<>(
                department.getResourcesDepartment().getAllResources());
        resourceCombo = new JComboBox<>(comboModel);
        resourceCombo.addActionListener(anEvent ->
                addResourceButton.setEnabled(resourceCombo.getSelectedItem() != null));
        resourceCombo.setRenderer(new EasyComboBoxRenderer<Resource>() {
            @Override
            protected String getDisplayName(Resource resource) {
                return resource.getName();
            }
        });
        amountSelector = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
        ButtonGroup resourcesRadioButtonGroup = new ButtonGroup();
        requiredRadioButton = new JRadioButton("Obligatorio");
        requiredRadioButton.setSelected(true); // por defecto
        optionalRadioButton = new JRadioButton("Opcional");
        resourcesRadioButtonGroup.add(requiredRadioButton);
        resourcesRadioButtonGroup.add(optionalRadioButton);
        addResourceButton = new JButton("Agregar");
        addResourceButton.setEnabled(false);
        addResourceButton.addActionListener(anEvent -> {
            ResourceView newRes = new ResourceView(
                    (Resource) resourceCombo.getSelectedItem(),
                    (Integer) amountSelector.getValue());
            List<ResourceView> modelOpt = getListModelFrom(optionalResourcesTable);
            List<ResourceView> modelReq = getListModelFrom(requiredResourcesTable);
            if (requiredRadioButton.isSelected()) {
                addResource(modelReq, modelOpt, newRes);
                ((ReadOnlyTableModel<ResourceView>) requiredResourcesTable.getModel()).setModel(modelReq);
            } else {
                addResource(modelOpt, modelReq, newRes);
                ((ReadOnlyTableModel<ResourceView>) optionalResourcesTable.getModel()).setModel(modelOpt);
            }
        });
        deleteResourceButton = new JButton("Eliminar recurso seleccionado");
        deleteResourceButton.setEnabled(false);
        deleteResourceButton.addActionListener(anEvent -> {
            List<ResourceView> modelOpt = getListModelFrom(optionalResourcesTable);
            List<ResourceView> modelReq = getListModelFrom(requiredResourcesTable);
            if (requiredResourcesTable.getSelectionModel().isSelectionEmpty()) {
                ResourceView selection = getSelectionFrom(optionalResourcesTable);
                modelOpt.remove(selection);
                ((ReadOnlyTableModel<ResourceView>) optionalResourcesTable.getModel()).setModel(modelOpt);
            } else {
                ResourceView selection = getSelectionFrom(requiredResourcesTable);
                modelReq.remove(selection);
                ((ReadOnlyTableModel<ResourceView>) requiredResourcesTable.getModel()).setModel(modelReq);
            }
        });
    }

    protected void addResource(List<ResourceView> model, List<ResourceView> other, ResourceView newRes) {
        if (!containsResource(other, newRes))
            if (containsResource(model, newRes)) {
                model.stream()
                        .filter(resourceView -> resourceView.getResource().equals(newRes.getResource()))
                        .forEach(resourceView -> resourceView.setAmount(resourceView.getAmount() + (Integer) amountSelector.getValue()));
            } else
                model.add(newRes);
    }

    private boolean containsResource(List<ResourceView> model, ResourceView res) {
        for (ResourceView rv : model)
            if (rv.getResource().equals(res.getResource()))
                return true;
        return false;
    }

    protected ResourceView getSelectionFrom(JTable table) {
        return getListModelFrom(table).get(table.getSelectionModel().getMinSelectionIndex());
    }

    @SuppressWarnings("unchecked")
    protected List<ResourceView> getListModelFrom(JTable table) {
        return ((ReadOnlyTableModel<ResourceView>) table.getModel()).getModel();
    }

    @SuppressWarnings({"serial", "unchecked"})
    private void createSubjectAndProfessorWidgets() {
        subjectLabel = new JLabel("Materia");
        professorLabel = new JLabel("Profesor");
        EasyComboBoxModel<Subject> subjectsModel = new EasyComboBoxModel<>();
        subjectCombo = new JComboBox<>(subjectsModel);
        subjectCombo.setRenderer(new EasyComboBoxRenderer<Subject>() {
            @Override
            protected String getDisplayName(Subject subject) {
                return subject.getName();
            }
        });
        EasyComboBoxModel<Professor> professorsModel = new EasyComboBoxModel<>(getProfessors());
        professorCombo = new JComboBox<>(professorsModel);
        professorCombo.setRenderer(new EasyComboBoxRenderer<Professor>() {
            @Override
            protected String getDisplayName(Professor professor) {
                return professor.getName();
            }
        });
        professorCombo.addActionListener(anEvent -> {
            List<Subject> modelToSet = new LinkedList<>();
            if (getSelectedProfessor() != null)
                modelToSet = getSelectedProfessor().getSubjects();
            ((EasyComboBoxModel<Subject>) subjectCombo.getModel()).setModel(modelToSet);
        });
    }

    private List<Professor> getProfessors() {
        return department.getProfessorsDepartment().getProfessors();
    }

    private void createCapacityWidgets() {
        capacityLabel = new JLabel("Capacidad deseada");
        capacitySelector = new JSpinner(new SpinnerNumberModel(10, 1, Integer.MAX_VALUE, 1));
    }

    private void createPeriodWidgets() {
        periodDetailArea = new JTextArea("Ningún período seleccionado");
        periodDetailArea.setEditable(false);
        addPeriodButton = new JButton("Especificar Período");
        addPeriodButton.addActionListener(anEvent -> new NewPeriodWindow(NewRequestWindow.this));
    }

    private void createButtons() {
        createRequestButton = new JButton("Crear Pedido");
        createRequestButton.addActionListener(anEvent -> {
            if (validateCurrentRequest()) {
                department.createClassroomRequest(
                        makeRequirementsFrom(requiredResourcesTable, optionalResourcesTable),
                        specifiedPeriod, getSelectedSubject(),
                        getSelectedProfessor(),
                        (Integer) capacitySelector.getValue());
                dispose();
            }
        });
        cancelButton = new JButton("Cancelar");
        cancelButton.addActionListener(anEvent ->
                withConfirmation("Salir", "¿Desea salir de la ventana y perder los cambios?", this::dispose));
    }

    protected boolean validateCurrentRequest() {
        warningIf(specifiedPeriod == null, "Por favor especifique un período");
        warningIf(getSelectedProfessor() == null, "Por favor especifique un Profesor");
        warningIf(getSelectedSubject() == null, "Por favor especifique una Materia");
        return specifiedPeriod != null && getSelectedProfessor() != null && getSelectedSubject() != null;
    }

    protected void warningIf(Boolean condition, String aMessage) {
        if (condition)
            JOptionPane.showMessageDialog(this, aMessage, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }

    protected Subject getSelectedSubject() {
        return (Subject) subjectCombo.getSelectedItem();
    }

    protected Professor getSelectedProfessor() {
        return (Professor) professorCombo.getSelectedItem();
    }

    protected Set<Requirement> makeRequirementsFrom(JTable requiredTable, JTable optionalTable) {
        Set<Requirement> requirements = new HashSet<>();
        for (ResourceView rv : getListModelFrom(requiredTable))
            requirements.add(new Requirement(rv.getResource(), rv.getAmount(), false));
        for (ResourceView rv : getListModelFrom(optionalTable))
            requirements.add(new Requirement(rv.getResource(), rv.getAmount(), true));
        return requirements;
    }

    protected void addWidgets() {
        JPanel main = new JPanel();
        add(main);
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        JPanel top = new JPanel();
        JPanel mainButtons = new JPanel();
        main.add(top);
        main.add(mainButtons);
        top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));
        JPanel resourcesPanel = new JPanel();
        JPanel right = new JPanel();
        top.add(resourcesPanel);
        top.add(right);
        JPanel addResourcePanel = new JPanel();
        JPanel professorAndSubject = new JPanel();
        JPanel periodPanel = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.add(addResourcePanel);
        right.add(professorAndSubject);
        right.add(periodPanel);
        JPanel requiredPanel = new JPanel();
        JPanel optionalPanel = new JPanel();
        resourcesPanel.setLayout(new FlowLayout());
        requiredPanel.setBorder(BorderFactory.createTitledBorder("Recursos Obligatorios"));
        optionalPanel.setBorder(BorderFactory.createTitledBorder("Recursos Opcionales"));
        resourcesPanel.add(requiredPanel);
        resourcesPanel.add(optionalPanel);
        JScrollPane reqScrollPane = new JScrollPane(requiredResourcesTable);
        JScrollPane optScrollPane = new JScrollPane(optionalResourcesTable);
        reqScrollPane.setPreferredSize(new Dimension(200, 180));
        optScrollPane.setPreferredSize(new Dimension(200, 180));
        requiredPanel.setLayout(new FlowLayout());
        requiredPanel.add(reqScrollPane);
        optionalPanel.setLayout(new FlowLayout());
        optionalPanel.add(optScrollPane);
        addResourcePanel.setBorder(BorderFactory.createTitledBorder("Agregar recurso"));
        addResourcePanel.setLayout(new BoxLayout(addResourcePanel, BoxLayout.Y_AXIS));
        JPanel resourceDetails = new JPanel();
        JPanel optionalOrRequired = new JPanel();
        JPanel buttonsPanel = new JPanel();
        addResourcePanel.add(resourceDetails);
        addResourcePanel.add(optionalOrRequired);
        addResourcePanel.add(buttonsPanel);
        resourceDetails.setLayout(new BoxLayout(resourceDetails, BoxLayout.Y_AXIS));
        JPanel resPanel = new JPanel();
        JPanel amountPanel = new JPanel();
        resourceDetails.add(resPanel);
        resourceDetails.add(amountPanel);
        resPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        amountPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        resPanel.add(labelResource);
        resPanel.add(resourceCombo);
        amountPanel.add(labelAmount);
        amountPanel.add(amountSelector);
        optionalOrRequired.setLayout(new FlowLayout());
        optionalOrRequired.add(requiredRadioButton);
        optionalOrRequired.add(optionalRadioButton);
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.add(deleteResourceButton);
        buttonsPanel.add(addResourceButton);
        professorAndSubject.setBorder(BorderFactory.createTitledBorder("Profesor/Materia/Capacidad"));
        periodPanel.setBorder(BorderFactory.createTitledBorder("Condiciones de tiempo"));
        professorAndSubject.setLayout(new BoxLayout(professorAndSubject, BoxLayout.Y_AXIS));
        JPanel professorPanel = new JPanel();
        JPanel subjectPanel = new JPanel();
        JPanel capacityPanel = new JPanel();
        professorAndSubject.add(professorPanel);
        professorAndSubject.add(subjectPanel);
        professorAndSubject.add(capacityPanel);
        professorPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        professorPanel.add(requiredRedStar());
        professorPanel.add(professorLabel);
        professorPanel.add(professorCombo);
        subjectPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        subjectPanel.add(requiredRedStar());
        subjectPanel.add(subjectLabel);
        subjectPanel.add(subjectCombo);
        capacityPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        capacityPanel.add(requiredRedStar());
        capacityPanel.add(capacityLabel);
        capacityPanel.add(capacitySelector);
        periodPanel.setLayout(new BoxLayout(periodPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPanePeriod = new JScrollPane(periodDetailArea);
        scrollPanePeriod.setPreferredSize(new Dimension(350, 100));
        periodPanel.add(scrollPanePeriod);
        JPanel aux = new JPanel();
        aux.setLayout(new FlowLayout(FlowLayout.RIGHT));
        aux.add(requiredRedStar());
        aux.add(addPeriodButton);
        periodPanel.add(aux);
        mainButtons.setLayout(new FlowLayout(FlowLayout.RIGHT));
        mainButtons.add(createRequestButton);
        mainButtons.add(cancelButton);
    }

    @Override
    public Period getPeriod() {
        return null;
    }

    @Override
    public void setPeriod(Period aPeriod) {
        specifiedPeriod = aPeriod;
        periodDetailArea.setText(specifiedPeriod.toString());
    }
}
