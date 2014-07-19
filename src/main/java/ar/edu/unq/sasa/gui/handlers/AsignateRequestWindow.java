package ar.edu.unq.sasa.gui.handlers;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ar.edu.unq.sasa.gui.period.NewPeriodWindow;
import ar.edu.unq.sasa.gui.util.ObjectToStringConverter;
import ar.edu.unq.sasa.gui.util.Pair;
import ar.edu.unq.sasa.gui.util.PeriodHolder;
import ar.edu.unq.sasa.gui.util.tables.ReadOnlyTableModel;
import ar.edu.unq.sasa.model.academic.ClassroomRequest;
import ar.edu.unq.sasa.model.data.InformationManager;
import ar.edu.unq.sasa.model.exceptions.handlers.ResourceException;
import ar.edu.unq.sasa.model.exceptions.time.PeriodException;
import ar.edu.unq.sasa.model.handlers.Asignator;
import ar.edu.unq.sasa.model.handlers.ClassroomHandler;
import ar.edu.unq.sasa.model.items.Classroom;
import ar.edu.unq.sasa.model.items.Resource;
import ar.edu.unq.sasa.model.time.Period;

/**
 * Ventana para Asignar un {@link ClassroomRequest}
 * 
 * @author Cristian Suarez
 *
 */
public class AsignateRequestWindow extends JFrame implements PeriodHolder{
	protected AssignmentsPanel parentPanel; 
	
	protected Asignator handler = Asignator.getInstance();
	protected ClassroomRequest classroomRequestSelection;
	protected Classroom classroomSelection;
	protected Period periodSelection;
	protected List<Pair<Resource, Integer>> resourcesSelection = new ArrayList<Pair<Resource,Integer>>();
	protected List<Pair<Resource, Integer>> oldResourcesSelection = new ArrayList<Pair<Resource,Integer>>();
	
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
	
	public AsignateRequestWindow(final AssignmentsPanel assignmentsPanel){
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				parentPanel = assignmentsPanel;
				classroomRequestSelection = assignmentsPanel.getRequestSelection();
				createSearchComponents();
				createClassroomsTable();
				createButtons();
				createSelectedItemsViewPanel();
				organizeWidgets();

				setResizable(true);
				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				setTitle("Asignacion de Pedido");
				setSize(825, 465);
				setLocationRelativeTo(null);
				setVisible(true);
			}

		});
	}
	
	public AssignmentsPanel getParentPanel() {
		return parentPanel;
	}
	
	@Override
	public void setPeriod(Period period){
		periodSelection = period;
		periodTextArea.setText(period.toString());
		validateButtons();
		
	}
	
	public Period getPeriod() {
		return periodSelection;
	}
	
	public void setResourcesSelection(List<Pair<Resource, Integer>> resources){
		resourcesSelection.clear();
		resourcesSelection.addAll(resources);
	}
	
	public void setOldResourcesSelection(List<Pair<Resource, Integer>> resources){
		oldResourcesSelection = resources;
	}
	
	public List<Pair<Resource, Integer>> getResourcesSelection() {
		return resourcesSelection;
	}
	
	public List<Pair<Resource, Integer>> getOldResourcesSelection() {
		return oldResourcesSelection;
	}
	
	public Asignator getHandler(){
		return handler;
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
		periodViewPanel.setBorder(BorderFactory.createTitledBorder("Periodo Elegido"));
		periodViewPanel.add(periodScrollPane);
		
		JPanel resourcesPanel = new JPanel();
		resourcesPanel.setBorder(BorderFactory.createTitledBorder("Recursos Elegidos"));
		resourcesPanel.add(resourcesScrollPane);
		
		rightPanel.add(resourcesPanel);
		rightPanel.add(periodViewPanel);
		rightPanel.add(cancelPanel);
	}
	
	private void createSearchComponents() {
		searchLabel = new JLabel("Busqueda por nombre");
		searchTextField = new JTextField(10);
		createSearchTextFieldListeners();
	}

	private void createSearchTextFieldListeners() {
		searchTextField.addKeyListener(new KeyAdapter() {
			@Override
			@SuppressWarnings("unchecked")
			public void keyReleased(KeyEvent e) {
				String text = ((JTextField)e.getSource()).getText();
				List<Classroom> res = ClassroomHandler.getInstance().searchClassroomByName(text);
				((ReadOnlyTableModel<Classroom>)classroomsTable.getModel()).setModel(res);
			}
		});
	}

	private void createButtons() {
		selectPeriod = new JButton("Elegir Periodo");
		createSelectPeriodListeners();
		
		selectResources = new JButton("Elegir Recursos");
		createSelectResourcesListeners();
		
		asignateRequestInMostSatisfactoryClassroom = new JButton("Asignar Pedido en el Aula mas Satisfactoria");
		createAsignateRequestInMostSatisfactoryClassroomListeners();
		
		asignateRequestInAClassroom = new JButton("Asignar Pedido en Aula Seleccionada");
		createAsignateRequestInAClassroomListeners();
		
		asignateClassroomAssignment = new JButton("Asignar Pedido en el Aula y Periodo Seleccionados");
		createAsignateClassroomAssignmentListeners();
		
		asignateClassroomAssignmentWithDesiredPeriodAndRequiredResources = new JButton("Asignar Pedido en el Aula, Periodo y Recursos Seleccionados");
		createAsignateClassroomAssignmentWithDesiredPeriodAndRequiredResourcesListeners();
		
		cancelButton = new JButton("Cancelar");
		createCancelButtonListeners();
		
		asignateRequestInAClassroom.setEnabled(false);
		asignateClassroomAssignment.setEnabled(false);
		asignateClassroomAssignmentWithDesiredPeriodAndRequiredResources.setEnabled(false);
	}
	
	private void createCancelButtonListeners() {
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(new JFrame(),	"¿Desea cancelar la asignacion?", "Cancelar Asignacion", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
					dispose();				
			}
		});
	}

	private void createAsignateClassroomAssignmentWithDesiredPeriodAndRequiredResourcesListeners() {
		asignateClassroomAssignmentWithDesiredPeriodAndRequiredResources.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(new JFrame(),	"¿Desea realizar la Asignacion elegida?", "Asignacion", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
					Map<Resource, Integer> resources = resourcesSelectionToMap(resourcesSelection);
					try {handler.asignateClassroomAssignmentWithDesiredPeriodAndRequiredResources(classroomRequestSelection, classroomSelection, periodSelection, resources);}
					catch (PeriodException e1) {}
					parentPanel.updateTables();
					dispose();
				}
			}
		});
	}

	private Map<Resource, Integer> resourcesSelectionToMap(List<Pair<Resource, Integer>> resourcesSelection) {
		Map<Resource, Integer> resources = new HashMap<Resource, Integer>();
		for (Pair<Resource, Integer> pair : resourcesSelection){
			resources.put((Resource)pair.getFirst(), (Integer)pair.getSecond());
		}
		return resources;
	}

	private void createAsignateClassroomAssignmentListeners() {
		asignateClassroomAssignment.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(new JFrame(),	"¿Desea realizar la Asignacion elegida?", "Asignacion", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
					try {handler.asignateClassroomAssignment(classroomRequestSelection, classroomSelection, periodSelection);}
					catch (PeriodException e1) {}
					parentPanel.updateTables();
					dispose();
				}
			}
		});
	}

	private void createAsignateRequestInAClassroomListeners() {
		asignateRequestInAClassroom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(new JFrame(),	"¿Desea realizar la Asignacion elegida?", "Asignacion", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
					try {handler.asignateRequestInAClassroom(classroomRequestSelection, classroomSelection);}
					catch (PeriodException e1) {}
					parentPanel.updateTables();
					dispose();
				}
			}
		});
	}

	private void createAsignateRequestInMostSatisfactoryClassroomListeners() {
		asignateRequestInMostSatisfactoryClassroom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(new JFrame(),	"¿Desea realizar la Asignacion elegida?", "Asignacion", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
					try {handler.asignateRequestInMostSatisfactoryClassroom(classroomRequestSelection);}
					catch (PeriodException e1) {} 
					catch (ResourceException e1) {}
					parentPanel.updateTables();
					dispose();
				}
			}
		});
	}

	private void createSelectResourcesListeners() {
		selectResources.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new ResourcesSelectionWindow(AsignateRequestWindow.this); 
			}
		});
	}

	private void createSelectPeriodListeners() {
		selectPeriod.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new NewPeriodWindow(AsignateRequestWindow.this); 
			}
		});
	}

	private void createClassroomsTable() {		
		ReadOnlyTableModel<Classroom> tableModel = new ReadOnlyTableModel<Classroom>(getClassrooms());
		addClassroomColumns(tableModel);
		classroomsTable = new JTable(tableModel);
		classroomsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		classroomsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				AsignateRequestWindow.this.whenClassroomsTableSelectionChanged(e);
			}
		});
		classroomsScrollPane = new JScrollPane(classroomsTable);
		classroomsScrollPane.setPreferredSize(new Dimension(200, 200));
	}

	private void addClassroomColumns(ReadOnlyTableModel<Classroom> tableModel) {
		tableModel.addColumn("Aula", "name");
		tableModel.addColumn("Capacidad", "capacity");
	}

	@SuppressWarnings("unchecked")
	protected void whenClassroomsTableSelectionChanged(ListSelectionEvent e) {
		DefaultListSelectionModel source = (DefaultListSelectionModel)e.getSource(); 
		if (source.isSelectionEmpty()) {
			classroomSelection = null;
			asignateRequestInAClassroom.setEnabled(false);
			asignateClassroomAssignment.setEnabled(false);
			asignateClassroomAssignmentWithDesiredPeriodAndRequiredResources.setEnabled(false);
		}
		else {
			int index = source.getMinSelectionIndex();
			List<Classroom> model = ((ReadOnlyTableModel<Classroom>)classroomsTable.getModel()).getModel();
			classroomSelection = (Classroom) model.get(index);
			asignateRequestInAClassroom.setEnabled(true);
			if (periodSelection != null){ 
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
		ReadOnlyTableModel<Pair<Resource, Integer>> tableModel = new ReadOnlyTableModel<Pair<Resource, Integer>>(resourcesSelection);
		addResourcesColumns(tableModel);
		resourcesTable = new JTable(tableModel);
		resourcesScrollPane = new JScrollPane(resourcesTable);
		resourcesScrollPane.setPreferredSize(new Dimension(300, 200));
	}

	private void addResourcesColumns(ReadOnlyTableModel<Pair<Resource, Integer>> tableModel) {
		tableModel.addColumn("Recurso", "first", new ObjectToStringConverter() {
			@Override
			public String convert(Object obj) {
				return ((Resource)obj).getName();
			};
		});
		tableModel.addColumn("Cantidad", "second", new ObjectToStringConverter() {
			@Override
			public String convert(Object obj) {
				return ((Integer)obj).toString();
			};
		});
	}

	private List<Classroom> getClassrooms() {
		return InformationManager.getInstance().getClassrooms();
	}

	public void validateButtons() {
		if (classroomSelection != null){
			asignateRequestInAClassroom.setEnabled(true);
			if (periodSelection != null){ 
				asignateClassroomAssignment.setEnabled(true);
				asignateClassroomAssignmentWithDesiredPeriodAndRequiredResources.setEnabled(true);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void updateResourcesTable(){
		((ReadOnlyTableModel<Pair<Resource, Integer>>)resourcesTable.getModel()).setModel(resourcesSelection);
	}
	
}
