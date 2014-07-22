package ar.edu.unq.sasa.gui.handlers;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ar.edu.unq.sasa.gui.util.ObjectToStringConverter;
import ar.edu.unq.sasa.gui.util.combos.EasyComboBoxModel;
import ar.edu.unq.sasa.gui.util.combos.EasyComboBoxRenderer;
import ar.edu.unq.sasa.gui.util.tables.ReadOnlyTableModel;
import ar.edu.unq.sasa.model.academic.ClassroomRequest;
import ar.edu.unq.sasa.model.academic.Professor;
import ar.edu.unq.sasa.model.academic.Request;
import ar.edu.unq.sasa.model.academic.Subject;
import ar.edu.unq.sasa.model.assignments.Assignment;
import ar.edu.unq.sasa.model.assignments.ClassroomAssignment;
import ar.edu.unq.sasa.model.handlers.Asignator;
import ar.edu.unq.sasa.model.handlers.Handler;
import ar.edu.unq.sasa.model.items.Classroom;
import ar.edu.unq.sasa.util.Subscriber;

/**
 * Crea un panel para manejar asignaciones de pedidos.
 */
public class AssignmentsPanel extends JPanel implements Subscriber {

	private static final long serialVersionUID = -39425443658019906L;
	protected Asignator handler = Asignator.getInstance();
	protected ClassroomRequest requestSelection = null;
	protected ClassroomAssignment assignmentSelection = null;

	protected JTable requestsTable;
	protected JTable assignmentsTable;
	protected JScrollPane requestsScrollPane;
	protected JScrollPane assignmentsScrollPane;
	protected JButton deleteButton;
	protected JButton modifyButton;
	protected JButton asignateButton;
	protected JButton viewDetailsButton;
	protected JLabel searchLabel;
	protected JComboBox<Professor> professorsComboBox;

	public AssignmentsPanel(){
		registerAsSubscriber();
		createSearchComponents();
		createRequestsTable();
		createAssignmentsTable();
		createButtons();
		organizeComponents();
	}

	public Handler getHandler(){
		return handler;
	}

	protected int getWindowWidth() {
		return 500;
	}

	protected int getWindowHeight() {
		return 500;
	}

	protected String getWindowTitle() {
		return "ABM Asignaciones";
	}

	@Override
	public String getName() {
		return "Asignaciones";
	}

	public ClassroomRequest getRequestSelection(){
		return requestSelection;
	}

	private List<ClassroomRequest> getRequests() {
		List<ClassroomRequest> requests = new ArrayList<ClassroomRequest>();
		for (Request request : getHandler().getInformationManager().getRequests())
			if (request.isClassroomRequest())
				if (! request.isAsignated())
					if (professorsComboBox.getModel().getSelectedItem() == null)
						requests.add((ClassroomRequest)request);
					else if (professorsComboBox.getModel().getSelectedItem().equals(request.getProfessor()))
						requests.add((ClassroomRequest)request);
		return requests;
	}

	protected List<ClassroomAssignment> getAssignments() {
		List<ClassroomAssignment> assignments = new ArrayList<ClassroomAssignment>();
		for (Assignment assignment : getHandler().getInformationManager().getAssignments())
			if (assignment.isClassroomAssignment())
				if (professorsComboBox.getModel().getSelectedItem() == null ||
						professorsComboBox.getModel().getSelectedItem().equals(assignment.getRequest().getProfessor()))
					assignments.add((ClassroomAssignment)assignment);
		return assignments;
	}

	@SuppressWarnings("unchecked")
	private void createSearchComponents() {
		searchLabel = new JLabel("Busqueda por Profesor");
		professorsComboBox = (JComboBox<Professor>) makeProfessorsComboBox();
	}

	@SuppressWarnings({ "unchecked", "serial" })
	private Component makeProfessorsComboBox() {
		EasyComboBoxModel<Professor> comboModel = new EasyComboBoxModel<Professor>(
				handler.getInformationManager().getProfessors());
		JComboBox<Professor> combo = new JComboBox<Professor>(comboModel);
		combo.setRenderer(new EasyComboBoxRenderer<Professor>() {
			@Override
			public String getDisplayName(Professor professor) {
				return professor.getName();
			}
		});
		combo.addActionListener(new ActionListener() {
		    @Override
			public void actionPerformed(ActionEvent e) {
		    	updateTables();
		    }
		});

		combo.setPreferredSize(new Dimension(120, 20));
		return combo;
	}

	private void createButtons() {
		asignateButton = new JButton("Asignar Pedido");
		createAsignateButtonListeners();
		viewDetailsButton = new JButton("Detalle del Pedido Asignado");
		createViewDetailsButtonListeners();
		modifyButton = new JButton("Modificar Asignacion");
		createModifyButtonListeners();
		deleteButton = new JButton("Eliminar Asignacion");
		createDeleteButtonListeners();
		viewDetailsButton.setEnabled(false);
		asignateButton.setEnabled(false);
		deleteButton.setEnabled(false);
		modifyButton.setEnabled(false);
	}

	private void createViewDetailsButtonListeners() {
		viewDetailsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new RequestViewWindow(assignmentSelection.getRequest());
			}
		});
	}

	private void createAsignateButtonListeners() {
		asignateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new AsignateRequestWindow(AssignmentsPanel.this);
			}
		});
	}

	private void createModifyButtonListeners() {
		modifyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new EditAssignmentWindow(assignmentSelection);
			}
		});
	}

	private void createDeleteButtonListeners() {
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(new JFrame(),	"¿Desea eliminar la asignacion seleccionada?", "Eliminar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
					((Asignator) getHandler()).deleteAssignment(assignmentSelection);
					updateTables();
				}
			}
		});
	}

	private void createAssignmentsTable() {
		ReadOnlyTableModel<ClassroomAssignment> tableModel = new ReadOnlyTableModel<ClassroomAssignment>(getAssignments());
		addAssignmentColumns(tableModel);
		assignmentsTable = new JTable(tableModel);
		assignmentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		assignmentsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				AssignmentsPanel.this.whenAssignmentsTableSelectionChanged(e);
			}
		});
		assignmentsScrollPane = new JScrollPane(assignmentsTable);
	}

	@SuppressWarnings("unchecked")
	private void whenAssignmentsTableSelectionChanged(ListSelectionEvent e) {
		DefaultListSelectionModel source = (DefaultListSelectionModel)e.getSource();
		if (source.isSelectionEmpty()) {
			assignmentSelection = null;
			viewDetailsButton.setEnabled(false);
			deleteButton.setEnabled(false);
			modifyButton.setEnabled(false);
		}
		else {
			int index = source.getMinSelectionIndex();
			List<ClassroomAssignment> model = ((ReadOnlyTableModel<ClassroomAssignment>)assignmentsTable.getModel()).getModel();
			assignmentSelection = model.get(index);
			viewDetailsButton.setEnabled(true);
			deleteButton.setEnabled(true);
			modifyButton.setEnabled(true);
		}
	}

	private void addAssignmentColumns(ReadOnlyTableModel<ClassroomAssignment> tableModel) {
		tableModel.addColumn("Profesor", "request", new ObjectToStringConverter() {
			@Override
			public String convert(Object obj) {
				return ((Request)obj).getProfessor().getName();
			};
		});
		tableModel.addColumn("Materia", "request", new ObjectToStringConverter() {
			@Override
			public String convert(Object obj) {
				return ((Request)obj).getSubject().getName();
			};
		});
		tableModel.addColumn("Aula", "assignableItem", new ObjectToStringConverter() {
			@Override
			public String convert(Object obj) {
				return ((Classroom)obj).getName();
			};
		});
	}

	private void createRequestsTable() {
		ReadOnlyTableModel<ClassroomRequest> tableModel = new ReadOnlyTableModel<ClassroomRequest>(getRequests());
		addRequestsColumns(tableModel);
		requestsTable = new JTable(tableModel);
		requestsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		requestsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				AssignmentsPanel.this.whenRequestsTableSelectionChanged(e);
			}
		});
		requestsScrollPane = new JScrollPane(requestsTable);
	}

	@SuppressWarnings("unchecked")
	protected void whenRequestsTableSelectionChanged(ListSelectionEvent e) {
		DefaultListSelectionModel source = (DefaultListSelectionModel)e.getSource();
		if (source.isSelectionEmpty()) {
			requestSelection = null;
			asignateButton.setEnabled(false);
		}
		else {
			int index = source.getMinSelectionIndex();
			List<ClassroomRequest> model = ((ReadOnlyTableModel<ClassroomRequest>)requestsTable.getModel()).getModel();
			requestSelection = model.get(index);
			asignateButton.setEnabled(true);
		}
	}

	private void addRequestsColumns(ReadOnlyTableModel<ClassroomRequest> tableModel) {
		tableModel.addColumn("Profesor", "professor", new ObjectToStringConverter() {
			@Override
			public String convert(Object obj) {
				return ((Professor)obj).getName();
			};
		});
		tableModel.addColumn("Materia", "subject", new ObjectToStringConverter() {
			@Override
			public String convert(Object obj) {
				return ((Subject)obj).getName();
			};
		});
	}

	private void registerAsSubscriber() {
		getHandler().getPublisher().addSubscriber("requestsChanged", this);
		getHandler().getPublisher().addSubscriber("assignmentsChanged", this);
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

		JPanel asignateButtonPanel = new JPanel();
		asignateButtonPanel.setLayout(new FlowLayout());
		asignateButtonPanel.add(asignateButton);

		JPanel modifyButtonPanel = new JPanel();
		modifyButtonPanel.setLayout(new FlowLayout());
		modifyButtonPanel.add(modifyButton);

		JPanel deleteButtonPanel = new JPanel();
		deleteButtonPanel.setLayout(new FlowLayout());
		deleteButtonPanel.add(deleteButton);

		leftPanel.add(requestsScrollPane);
		leftPanel.add(asignateButtonPanel);

		rightPanel.add(assignmentsScrollPane);
		rightPanel.add(viewDetailsButtonPanel);
		rightPanel.add(modifyButtonPanel);
		rightPanel.add(deleteButtonPanel);
	}

	@SuppressWarnings("unchecked")
	public void updateTables() {
		((ReadOnlyTableModel<ClassroomAssignment>)assignmentsTable.getModel())
			.setModel(getAssignments());
		((ReadOnlyTableModel<ClassroomRequest>)requestsTable.getModel())
			.setModel(getRequests());
	}

	@Override
	public void update(String aspect, Object value) {
		updateTables();
	}

}
