package ar.edu.unq.sasa.gui.departments;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import uic.layout.HorizontalLayout;
import ar.edu.unq.sasa.gui.period.PeriodDetailWindow;
import ar.edu.unq.sasa.gui.util.ObjectToStringConverter;
import ar.edu.unq.sasa.gui.util.Pair;
import ar.edu.unq.sasa.gui.util.tables.ReadOnlyTableModel;
import ar.edu.unq.sasa.model.academic.ClassroomRequest;
import ar.edu.unq.sasa.model.assignments.Assignment;
import ar.edu.unq.sasa.model.assignments.ClassroomAssignment;
import ar.edu.unq.sasa.model.departments.AssignmentsDepartment;
import ar.edu.unq.sasa.model.items.Classroom;
import ar.edu.unq.sasa.model.items.Resource;
import ar.edu.unq.sasa.model.time.Period;

public class RequestViewWindow extends JFrame {

	private static final long serialVersionUID = -1212640840819470477L;

	private ClassroomRequest request;
	private ClassroomAssignment requestAssignment;
	private Classroom classroom;

	private JScrollPane requiredResourcesScrollPane;
	private JScrollPane optionalResourcesScrollPane;
	private JTable requiredResourcesTable;
	private JTable optionalResourcesTable;
	private JLabel professorTitleLabel;
	private JLabel professorLabel;
	private JLabel subjectTitleLabel;
	private JLabel subjectLabel;
	private JLabel capacityTitleLabel;
	private JLabel capacityLabel;
	private JButton showDesiredPeriodButton;
	private JButton showAsignatedPeriodButton;
	private JButton showSatisfactionButton;
	private JLabel satisfactionConditionLabel;
	private JButton closeButton;

	public RequestViewWindow(AssignmentsDepartment assignmentsDepartment, ClassroomRequest requestSelection) {
		request = requestSelection;
		if (requestSelection.isAsignated())
			// TODO move this logic out of here
			for (Assignment assignment : assignmentsDepartment.getAssignments())
				if (assignment.isClassroomAssignment())
					if (assignment.getRequest().equals(requestSelection)) {
						requestAssignment = (ClassroomAssignment) assignment;
						classroom = (Classroom) assignment.getAssignableItem();
						break;
					}
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				createRequiredResourcesTable();
				createOptionalResourcesTable();
				createLabels();
				createButtons();
				organizeComponents();

				setResizable(false);
				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				setTitle("Detalle del Pedido");
				setSize(530, 410);
				setLocationRelativeTo(null);
				setVisible(true);
			}
		});
	}

	private void createRequiredResourcesTable() {
		List<Pair<Resource, Integer>> requiredResources = resourcesMapToList(request.getRequiredResources());
		ReadOnlyTableModel<Pair<Resource, Integer>> tableModel = new ReadOnlyTableModel<Pair<Resource, Integer>>(requiredResources);
		addResourceColumns(tableModel);
		requiredResourcesTable = new JTable(tableModel);
		requiredResourcesScrollPane = new JScrollPane(requiredResourcesTable);
	}

	private void addResourceColumns(ReadOnlyTableModel<Pair<Resource, Integer>> tableModel) {
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

	private void createOptionalResourcesTable() {
		List<Pair<Resource, Integer>> optionalResources = resourcesMapToList(request.getOptionalResources());
		ReadOnlyTableModel<Pair<Resource, Integer>> tableModel = new ReadOnlyTableModel<Pair<Resource, Integer>>(optionalResources);
		addResourceColumns(tableModel);
		optionalResourcesTable = new JTable(tableModel);
		optionalResourcesScrollPane = new JScrollPane(optionalResourcesTable);
	}

	private List<Pair<Resource, Integer>> resourcesMapToList(Map<Resource, Integer> resourcesMap) {
		List<Pair<Resource, Integer>> resourcesList = new ArrayList<Pair<Resource, Integer>>();
		for (Entry<Resource, Integer> entry : resourcesMap.entrySet())
			resourcesList.add(new Pair<Resource, Integer>(entry.getKey(),entry.getValue()));
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

		satisfactionConditionLabel = new JLabel("(Solo si el pedido fue asignado)");
		satisfactionConditionLabel.setForeground(Color.GRAY);
		Font satisFont = satisfactionConditionLabel.getFont();
		satisfactionConditionLabel.setFont(new Font(satisFont.getFontName(), satisFont.getStyle(), 11));
	}


	private void createButtons() {
		showDesiredPeriodButton = new JButton("Ver Periodo pedido");
		createDesiredPeriodButtonListeners();
		showAsignatedPeriodButton = new JButton("Ver Periodo Asignado");
		createAsignatedPeriodButtonListeners();
		showSatisfactionButton = new JButton("   Ver Satisfacción   ");
		createSatisfactionButtonListeners();
		closeButton = new JButton("Cerrar");
		createCloseButtonListeners();
		if (! request.isAsignated()){
			showAsignatedPeriodButton.setEnabled(false);
			showSatisfactionButton.setEnabled(false);
		}
	}

	private void createDesiredPeriodButtonListeners() {
		showDesiredPeriodButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new PeriodDetailWindow(request.getDesiredHours());
			}
		});
	}

	private void createAsignatedPeriodButtonListeners() {
		showAsignatedPeriodButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (request.isAsignated())
					new PeriodDetailWindow(getAsignatedPeriod());
			}
		});
	}

	private Period getAsignatedPeriod() {
		Period asignatedPeriod = null;
		for (Entry<Period, Assignment> entry : classroom.getAssignments().entrySet())
			if (entry.getValue().isClassroomAssignment())
				if (entry.getValue().getRequest().equals(request)){
					asignatedPeriod = entry.getKey();
					break;
				}
		return asignatedPeriod;
	}

	private void createSatisfactionButtonListeners() {
		showSatisfactionButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new ShowSatisfactionWindow(requestAssignment);
			}
		});
	}

	private void createCloseButtonListeners() {
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}

	private void organizeComponents() {
		JPanel mainPanel = new JPanel();
		getContentPane().add(mainPanel);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new HorizontalLayout());
		topPanel.setAlignmentX(CENTER_ALIGNMENT);
		requiredResourcesScrollPane.setBorder(BorderFactory.createTitledBorder("Recursos Obligatorios"));
		topPanel.add(requiredResourcesScrollPane);
		optionalResourcesScrollPane.setBorder(BorderFactory.createTitledBorder("Recursos Opcionales"));
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

		JPanel asignatedRequestPanel = new JPanel();
		asignatedRequestPanel.setLayout(new FlowLayout());
		asignatedRequestPanel.add(showSatisfactionButton);
		asignatedRequestPanel.add(showAsignatedPeriodButton);

		JPanel asignatedRequestLabelPanel = new JPanel();
		asignatedRequestLabelPanel.setLayout(new FlowLayout());
		asignatedRequestLabelPanel.add(satisfactionConditionLabel);

		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
		bottomPanel.add(asignatedRequestPanel);
		bottomPanel.add(asignatedRequestLabelPanel);
		bottomPanel.add(closePanel);

		mainPanel.add(bottomPanel);
	}
}
