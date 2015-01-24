package ar.edu.unq.sasa.gui.departments;

import ar.edu.unq.sasa.gui.period.NewPeriodWindow;
import ar.edu.unq.sasa.gui.util.Pair;
import ar.edu.unq.sasa.gui.util.PeriodHolder;
import ar.edu.unq.sasa.gui.util.ToStringConverter;
import ar.edu.unq.sasa.gui.util.tables.ReadOnlyTableModel;
import ar.edu.unq.sasa.model.academic.ClassroomRequest;
import ar.edu.unq.sasa.model.departments.AssignmentsDepartment;
import ar.edu.unq.sasa.model.items.Classroom;
import ar.edu.unq.sasa.model.items.Resource;
import ar.edu.unq.sasa.model.time.Period;
import uic.layout.VerticalLayout;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ar.edu.unq.sasa.gui.util.Dialogs.withConfirmation;
import static ar.edu.unq.sasa.gui.util.WidgetUtilities.disableAll;

public class AssignRequestWindow extends JFrame implements PeriodHolder {

	private static final long serialVersionUID = -744274987094722160L;

	protected AssignmentsPanel parentPanel;

	protected ClassroomRequest classroomRequestSelection;
	protected Classroom classroomSelection;
	protected Period periodSelection;
	protected List<Pair<Resource, Integer>> resourcesSelection = new ArrayList<>();
	protected List<Pair<Resource, Integer>> oldResourcesSelection = new ArrayList<>();

	protected JLabel searchLabel;
	protected JTextField searchTextField;
	protected JScrollPane classroomsScrollPane;
	protected JTable classroomsTable;
	protected JButton selectPeriod;
	protected JButton selectResources;
	protected JButton asignateRequestInMostSatisfactoryClassroom;
	protected JButton asignateRequestInAClassroom;
	protected JButton asignateClassroomAssignment;
	protected JButton asignateClassroomAssignmentWithDesiredPeriodAndRequiredResources;
	protected JButton cancelButton;
	protected JScrollPane resourcesScrollPane;
	protected JTable resourcesTable;
	protected JScrollPane periodScrollPane;
	protected JTextArea periodTextArea;

	private AssignmentsDepartment department;

	public AssignRequestWindow(AssignmentsDepartment assignmentsDepartment, final AssignmentsPanel assignmentsPanel) {
		department = assignmentsDepartment;
		SwingUtilities.invokeLater(() -> {
            parentPanel = assignmentsPanel;
            classroomRequestSelection = assignmentsPanel.getRequestSelection();
            createSearchComponents();
            createClassroomsTable();
            createButtons();
            createSelectedItemsViewPanel();
            organizeWidgets();

            setResizable(true);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setTitle("Asignación de Pedido");
            setSize(825, 465);
            setLocationRelativeTo(null);
            setVisible(true);
        });
	}

	public AssignmentsPanel getParentPanel() {
		return parentPanel;
	}

	@Override
	public void setPeriod(Period aPeriod) {
		periodSelection = aPeriod;
		periodTextArea.setText(aPeriod.toString());
		validateButtons();
	}

	@Override
	public Period getPeriod() {
		return periodSelection;
	}

	public void setResourcesSelection(List<Pair<Resource, Integer>> resources) {
		resourcesSelection.clear();
		resourcesSelection.addAll(resources);
	}

	public void setOldResourcesSelection(List<Pair<Resource, Integer>> resources) {
		oldResourcesSelection = resources;
	}

	public List<Pair<Resource, Integer>> getResourcesSelection() {
		return resourcesSelection;
	}

	public List<Pair<Resource, Integer>> getOldResourcesSelection() {
		return oldResourcesSelection;
	}

	private void organizeWidgets() {
		JPanel mainPanel = new JPanel();
		getContentPane().add(mainPanel);
		mainPanel.setLayout(new FlowLayout());

		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

		JPanel cancelPanel = new JPanel();
		cancelPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		mainPanel.add(leftPanel);
		mainPanel.add(rightPanel);

		JPanel selectionButtonsPanel = new JPanel();
		JPanel asignationButtonsPanel = new JPanel();
		JPanel classroomsTablePanel = new JPanel();

		classroomsTablePanel.setLayout(new BoxLayout(classroomsTablePanel, BoxLayout.Y_AXIS));
		classroomsTablePanel.setBorder(BorderFactory.createTitledBorder("Seleccione un Aula"));

		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new FlowLayout());
		searchPanel.add(searchLabel);
		searchPanel.add(searchTextField);

		classroomsTablePanel.add(searchPanel);
		classroomsTablePanel.add(classroomsScrollPane);

		leftPanel.add(classroomsTablePanel);
		leftPanel.add(selectionButtonsPanel);
		leftPanel.add(asignationButtonsPanel);

		selectionButtonsPanel.setLayout(new FlowLayout());
		selectionButtonsPanel.add(selectPeriod);
		selectionButtonsPanel.add(new JLabel("   "));
		selectionButtonsPanel.add(selectResources);

		asignationButtonsPanel.setLayout(new VerticalLayout());
		asignationButtonsPanel.add(asignateRequestInMostSatisfactoryClassroom);
		asignationButtonsPanel.add(asignateRequestInAClassroom);
		asignationButtonsPanel.add(asignateClassroomAssignment);
		asignationButtonsPanel.add(asignateClassroomAssignmentWithDesiredPeriodAndRequiredResources);

		cancelPanel.add(cancelButton);

		JPanel periodViewPanel = new JPanel();
		periodViewPanel.setBorder(BorderFactory.createTitledBorder("Período Elegido"));
		periodViewPanel.add(periodScrollPane);

		JPanel resourcesPanel = new JPanel();
		resourcesPanel.setBorder(BorderFactory.createTitledBorder("Recursos Elegidos"));
		resourcesPanel.add(resourcesScrollPane);

		rightPanel.add(resourcesPanel);
		rightPanel.add(periodViewPanel);
		rightPanel.add(cancelPanel);
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

	private void createButtons() {
		selectPeriod = new JButton("Elegir Período");
		createSelectPeriodListeners();

		selectResources = new JButton("Elegir Recursos");
		createSelectResourcesListeners();

		asignateRequestInMostSatisfactoryClassroom = new JButton("Asignar Pedido en el Aula mas Satisfactoria");
		createAsignateRequestInMostSatisfactoryClassroomListeners();

		asignateRequestInAClassroom = new JButton("Asignar Pedido en Aula Seleccionada");
		createAsignateRequestInAClassroomListeners();

		asignateClassroomAssignment = new JButton("Asignar Pedido en el Aula y Periodo Seleccionados");
		createAsignateClassroomAssignmentListeners();

		asignateClassroomAssignmentWithDesiredPeriodAndRequiredResources =
				new JButton("Asignar Pedido en el Aula, Periodo y Recursos Seleccionados");
		createAsignateClassroomAssignmentWithDesiredPeriodAndRequiredResourcesListeners();

		cancelButton = new JButton("Cancelar");
		createCancelButtonListeners();

		disableAll(asignateRequestInAClassroom, asignateClassroomAssignment,
				asignateClassroomAssignmentWithDesiredPeriodAndRequiredResources);
	}

	private void createCancelButtonListeners() {
		cancelButton.addActionListener(anEvent ->
				withConfirmation("Cancelar asignación", "¿Desea cancelar la asignación?", this::dispose));
	}

	private void createAsignateClassroomAssignmentWithDesiredPeriodAndRequiredResourcesListeners() {
		asignateClassroomAssignmentWithDesiredPeriodAndRequiredResources.addActionListener(anEvent ->
				withConfirmation("Asignación", "¿Desea realizar la asignación elegida?", () -> {
					Map<Resource, Integer> resources = resourcesSelectionToMap(resourcesSelection);
					department.assignClassroomAssignmentWithDesiredPeriodAndRequiredResources(
							classroomRequestSelection, classroomSelection, periodSelection, resources);
					parentPanel.updateTables();
					dispose();
        }));
	}

	private Map<Resource, Integer> resourcesSelectionToMap(List<Pair<Resource, Integer>> aResourcesSelection) {
		Map<Resource, Integer> resources = new HashMap<>();
		for (Pair<Resource, Integer> pair : aResourcesSelection)
			resources.put((Resource) pair.getFirst(), (Integer) pair.getSecond());
		return resources;
	}

	private void createAsignateClassroomAssignmentListeners() {
		asignateClassroomAssignment.addActionListener(anEvent ->
				withConfirmation("Asignación", "¿Desea realizar la asignación elegida?", () -> {
					department.assignClassroomAssignment(classroomRequestSelection, classroomSelection, periodSelection);
					parentPanel.updateTables();
					dispose();
        }));
	}

	private void createAsignateRequestInAClassroomListeners() {
		asignateRequestInAClassroom.addActionListener(anEvent ->
				withConfirmation("Asignación", "¿Desea realizar la asignación elegida?", () -> {
					department.assignRequestInAClassroom(classroomRequestSelection, classroomSelection);
					parentPanel.updateTables();
					dispose();
				}));
	}

	private void createAsignateRequestInMostSatisfactoryClassroomListeners() {
		asignateRequestInMostSatisfactoryClassroom.addActionListener(anEvent ->
				withConfirmation("Asignación", "¿Desea realizar la asignación elegida?", () -> {
					department.assignRequestInMostSatisfactoryClassroom(classroomRequestSelection);
					parentPanel.updateTables();
					dispose();
				}));
	}

	private void createSelectResourcesListeners() {
		selectResources.addActionListener(anEvent -> new ResourcesSelectionWindow(department, AssignRequestWindow.this));
	}

	private void createSelectPeriodListeners() {
		selectPeriod.addActionListener(anEvent -> new NewPeriodWindow(AssignRequestWindow.this));
	}

	private void createClassroomsTable() {
		ReadOnlyTableModel<Classroom> tableModel = new ReadOnlyTableModel<>(
				department.getClassroomsDepartment().getClassrooms());
		addClassroomColumns(tableModel);
		classroomsTable = new JTable(tableModel);
		classroomsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		classroomsTable.getSelectionModel().addListSelectionListener(AssignRequestWindow.this::whenClassroomsTableSelectionChanged);
		classroomsScrollPane = new JScrollPane(classroomsTable);
		classroomsScrollPane.setPreferredSize(new Dimension(200, 200));
	}

	private void addClassroomColumns(ReadOnlyTableModel<Classroom> tableModel) {
		tableModel.addColumn("Aula", "name");
		tableModel.addColumn("Capacidad", "capacity");
	}

	@SuppressWarnings("unchecked")
	protected void whenClassroomsTableSelectionChanged(ListSelectionEvent anEvent) {
		DefaultListSelectionModel source = (DefaultListSelectionModel) anEvent.getSource();
		if (source.isSelectionEmpty()) {
			classroomSelection = null;
			disableAll(asignateRequestInAClassroom, asignateClassroomAssignment,
					asignateClassroomAssignmentWithDesiredPeriodAndRequiredResources);
		} else {
			int index = source.getMinSelectionIndex();
			List<Classroom> model = ((ReadOnlyTableModel<Classroom>) classroomsTable.getModel()).getModel();
			classroomSelection = model.get(index);
			asignateRequestInAClassroom.setEnabled(true);
			if (periodSelection != null) {
				asignateClassroomAssignment.setEnabled(true);
				asignateClassroomAssignmentWithDesiredPeriodAndRequiredResources.setEnabled(true);
			}
		}
	}

	private void createSelectedItemsViewPanel() {
		createResourcesTable();
		periodTextArea = new JTextArea("Todavía no se eligió ningun Periodo");
		periodScrollPane = new JScrollPane(periodTextArea);
		periodScrollPane.setPreferredSize(new Dimension(455, 100));
	}

	private void createResourcesTable() {
		ReadOnlyTableModel<Pair<Resource, Integer>> tableModel = new ReadOnlyTableModel<>(resourcesSelection);
		addResourcesColumns(tableModel);
		resourcesTable = new JTable(tableModel);
		resourcesScrollPane = new JScrollPane(resourcesTable);
		resourcesScrollPane.setPreferredSize(new Dimension(300, 200));
	}

	private void addResourcesColumns(ReadOnlyTableModel<Pair<Resource, Integer>> tableModel) {
		tableModel.addColumn("Recurso", "first", new ToStringConverter<Resource>() {
			@Override
			public String convert(Resource aResource) {
				return aResource.getName();
			};
		});
		tableModel.addColumn("Cantidad", "second", new ToStringConverter<Integer>() {
			@Override
			public String convert(Integer anAmount) {
				return anAmount.toString();
			};
		});
	}

	public void validateButtons() {
		if (classroomSelection != null) {
			asignateRequestInAClassroom.setEnabled(true);
			if (periodSelection != null) {
				asignateClassroomAssignment.setEnabled(true);
				asignateClassroomAssignmentWithDesiredPeriodAndRequiredResources.setEnabled(true);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void updateResourcesTable() {
		((ReadOnlyTableModel<Pair<Resource, Integer>>) resourcesTable.getModel()).setModel(resourcesSelection);
	}
}
