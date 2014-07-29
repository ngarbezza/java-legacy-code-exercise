package ar.edu.unq.sasa.gui.departments;

import java.awt.BorderLayout;
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
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import uic.layout.HorizontalLayout;
import uic.layout.VerticalLayout;
import ar.edu.unq.sasa.gui.period.PeriodDetailWindow;
import ar.edu.unq.sasa.gui.util.ObjectToStringConverter;
import ar.edu.unq.sasa.gui.util.Pair;
import ar.edu.unq.sasa.gui.util.tables.ReadOnlyTableModel;
import ar.edu.unq.sasa.model.assignments.ClassroomAssignment;
import ar.edu.unq.sasa.model.items.Resource;
import ar.edu.unq.sasa.model.time.Period;

public class ShowSatisfactionWindow extends JFrame {

	private static final long serialVersionUID = -3664046632029358042L;

	private ClassroomAssignment assignment;

	private Period selectedPeriod;

	private JScrollPane missingResourcesScrollPane;
	private JScrollPane periodSuperpositionsScrollPane;
	private JTable missingResourcesTable;
	private JTable periodSuperpositionsTable;
	private JButton showPeriodButton;
	private JLabel asignatedClassroomTitleLabel;
	private JLabel asignatesClassroomLabel;
	private JLabel capacityDifferenceTitleLabel;
	private JLabel capacityDifferenceLabel;
	private JLabel satisfiedRequestTitleLabel;
	private JLabel satisfiedRequestLabel;
	private JButton closeButton;


	public ShowSatisfactionWindow(ClassroomAssignment anAssignment) {
		assignment = anAssignment;

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
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
			}
		});
	}

	private void createMissingResourcesTable() {
		ReadOnlyTableModel<Pair<Resource, Integer>> tableModel =
				new ReadOnlyTableModel<Pair<Resource, Integer>>(getMissingResources());
		addResourceColumns(tableModel);
		missingResourcesTable = new JTable(tableModel);
		missingResourcesScrollPane = new JScrollPane(missingResourcesTable);
	}

	private void addResourceColumns(ReadOnlyTableModel<Pair<Resource, Integer>> tableModel) {
		tableModel.addColumn("Recurso", "first", new ObjectToStringConverter() {
			@Override
			public String convert(Object obj) {
				return ((Resource) obj).getName();
			};
		});
		tableModel.addColumn("Cantidad", "second", new ObjectToStringConverter() {
			@Override
			public String convert(Object obj) {
				return ((Integer) obj).toString();
			};
		});
	}

	private List<Pair<Resource, Integer>> getMissingResources() {
		Map<Resource, Integer> missingResourcesMap = assignment.getSatisfaction().getResources();
		return resourcesMapToList(missingResourcesMap);
	}

	private List<Pair<Resource, Integer>> resourcesMapToList(Map<Resource, Integer> resourcesMap) {
		List<Pair<Resource, Integer>> resourcesList = new ArrayList<Pair<Resource, Integer>>();
		for (Entry<Resource, Integer> entry : resourcesMap.entrySet())
			resourcesList.add(new Pair<Resource, Integer>(entry.getKey(), entry.getValue()));
		return resourcesList;
	}

	private void createPeriodSuperpositionsTable() {
		ReadOnlyTableModel<Pair<Period, Float>> tableModel =
				new ReadOnlyTableModel<Pair<Period, Float>>(getPeriodSuperpositionsResources());
		addPeriodSuperpositionsColumns(tableModel);
		periodSuperpositionsTable = new JTable(tableModel);
		periodSuperpositionsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		periodSuperpositionsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				ShowSatisfactionWindow.this.whenPeriodTableSelectionChanged(e);
			}
		});

		periodSuperpositionsScrollPane = new JScrollPane(periodSuperpositionsTable);

	}

	@SuppressWarnings("unchecked")
	protected void whenPeriodTableSelectionChanged(ListSelectionEvent e) {
		DefaultListSelectionModel source = (DefaultListSelectionModel) e.getSource();
		if (source.isSelectionEmpty()) {
			selectedPeriod = null;
			showPeriodButton.setEnabled(false);
		} else {
			int index = source.getMinSelectionIndex();
			List<Pair<Period, Float>> model =
					((ReadOnlyTableModel<Pair<Period, Float>>)periodSuperpositionsTable.getModel()).getModel();
			selectedPeriod = (Period) model.get(index).getFirst();
			showPeriodButton.setEnabled(true);
		}
	}

	private List<Pair<Period, Float>> getPeriodSuperpositionsResources() {
		Map<Period, Float> periodSuperpositionsMap = assignment.getSatisfaction().getTimeDifference();
		return periodSuperpositionsMapToList(periodSuperpositionsMap);
	}

	private List<Pair<Period, Float>> periodSuperpositionsMapToList(Map<Period, Float> periodSuperpositionsMap) {
		List<Pair<Period, Float>> periodSuperpositionsList = new ArrayList<Pair<Period, Float>>();
		for (Entry<Period, Float> entry : periodSuperpositionsMap.entrySet())
			periodSuperpositionsList.add(new Pair<Period, Float>(entry.getKey(), entry.getValue()));
		return periodSuperpositionsList;
	}

	private void addPeriodSuperpositionsColumns(ReadOnlyTableModel<Pair<Period, Float>> tableModel) {
		tableModel.addColumn("Tipo", "first", new ObjectToStringConverter() {
			@Override
			public String convert(Object obj) {
				return assignment.getAssignableItem().getAssignments().get(obj).isBookedAssignment()
						? "(Reserva)"
						: "(Asignacion por Pedido)";
			};
		});
		tableModel.addColumn("Cantidad de Horas", "second", new ObjectToStringConverter() {
			@Override
			public String convert(Object obj) {
				return "Superpuesta " + ((Float) obj).toString() + " hs";
			};
		});
	}

	private void createLabels() {
		asignatedClassroomTitleLabel = new JLabel("               Aula Asignada:  ");
		Font profTFont = asignatedClassroomTitleLabel.getFont();
		asignatedClassroomTitleLabel.setFont(new Font(profTFont.getFontName(), profTFont.getStyle(), 14));

		asignatesClassroomLabel = new JLabel(" " + assignment.getAssignableItem().getName());
		Font profFont = asignatesClassroomLabel.getFont();
		asignatesClassroomLabel.setFont(new Font(profFont.getFontName(), profFont.getStyle(), 14));

		capacityDifferenceTitleLabel = new JLabel("Diferencia de capacidad:  ");
		Font subTFont = capacityDifferenceTitleLabel.getFont();
		capacityDifferenceTitleLabel.setFont(new Font(subTFont.getFontName(), subTFont.getStyle(), 14));

		String capacityDifferenceShower;
		int capacityDifference = assignment.getSatisfaction().getCapacityDifference();
		if (capacityDifference > 0)
			capacityDifferenceShower = "Sobran " + Integer.valueOf(capacityDifference) + " lugares";
		else if (capacityDifference == 0)
			capacityDifferenceShower = "No sobran ni faltan asientos";
		else
			capacityDifferenceShower = "Faltan " + Integer.valueOf(capacityDifference * -1) + " lugares";
		capacityDifferenceLabel = new JLabel(" " + capacityDifferenceShower);
		Font subFont = capacityDifferenceLabel.getFont();
		capacityDifferenceLabel.setFont(new Font(subFont.getFontName(), subFont.getStyle(), 14));

		satisfiedRequestTitleLabel = new JLabel("         Pedido Satisfecho:  ");
		Font capTFont = satisfiedRequestTitleLabel.getFont();
		satisfiedRequestTitleLabel.setFont(new Font(capTFont.getFontName(), capTFont.getStyle(), 14));

		String satisfied;
		if (assignment.getSatisfaction().isSatisfied())
			satisfied = "Si";
		else
			satisfied = "No";
		satisfiedRequestLabel = new JLabel(" " + satisfied);
		Font capFont = satisfiedRequestLabel.getFont();
		satisfiedRequestLabel.setFont(new Font(capFont.getFontName(), capFont.getStyle(), 14));
	}

	private void createButtons() {
		closeButton = new JButton("Cerrar");
		createCloseButtonListeners();
		showPeriodButton = new JButton("Detalle de Periodo");
		createShowPeriodButtonListeners();
		showPeriodButton.setEnabled(false);
	}

	private void createCloseButtonListeners() {
		closeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}

	private void createShowPeriodButtonListeners() {
		showPeriodButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new PeriodDetailWindow(selectedPeriod);
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
		missingResourcesScrollPane.setBorder(BorderFactory.createTitledBorder("Recursos que faltaron Asignar"));
		topPanel.add(missingResourcesScrollPane);

		JPanel topRightPanel = new JPanel();
		topRightPanel.setLayout(new VerticalLayout());
		topRightPanel.setAlignmentX(CENTER_ALIGNMENT);
		topRightPanel.setBorder(BorderFactory.createTitledBorder("Horas de los Periodos Superpuestos"));
		topRightPanel.add(periodSuperpositionsScrollPane);
		topRightPanel.add(showPeriodButton);

		topPanel.add(topRightPanel);

		mainPanel.add(topPanel);

		JPanel leftMiddlePanel = new JPanel();
		leftMiddlePanel.setLayout(new GridLayout(4, 1));
		leftMiddlePanel.add(asignatedClassroomTitleLabel);
		leftMiddlePanel.add(capacityDifferenceTitleLabel);
		leftMiddlePanel.add(satisfiedRequestTitleLabel);
		leftMiddlePanel.add(new JLabel(" "));

		JPanel rightMiddlePanel = new JPanel();
		rightMiddlePanel.setLayout(new GridLayout(4, 1));
		rightMiddlePanel.add(asignatesClassroomLabel);
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
