package ar.edu.unq.sasa.gui.period;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTree;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;

import uic.widgets.calendar.UICDateEdit;
import ar.edu.unq.sasa.gui.util.PeriodHolder;
import ar.edu.unq.sasa.gui.util.WidgetUtilities;
import ar.edu.unq.sasa.model.time.And;
import ar.edu.unq.sasa.model.time.Minus;
import ar.edu.unq.sasa.model.time.Or;
import ar.edu.unq.sasa.model.time.Period;
import ar.edu.unq.sasa.model.time.SimplePeriod;
import ar.edu.unq.sasa.model.time.hour.HourInterval;
import ar.edu.unq.sasa.model.time.hour.Timestamp;
import ar.edu.unq.sasa.model.time.repetition.Daily;
import ar.edu.unq.sasa.model.time.repetition.EndingRepetition;
import ar.edu.unq.sasa.model.time.repetition.Monthly;
import ar.edu.unq.sasa.model.time.repetition.None;
import ar.edu.unq.sasa.model.time.repetition.Repetition;
import ar.edu.unq.sasa.model.time.repetition.Weekly;

public class NewPeriodWindow extends JFrame {

	private static final long serialVersionUID = 1588966684159956141L;

	// componentes del tipo de condición
	protected JRadioButton
		simpleRadioButton, compositeRadioButton, andRadioButton,
		orRadioButton, minusRadioButton;
	// componentes de la selección de fechas
	protected JLabel toDateLabel, fromDateLabel;
	protected UICDateEdit fromDate, toDate;
	// componentes de la selección de horas
	protected JComboBox<Integer>
		fromHoursCombo, toHoursCombo, toMinutesCombo, fromMinutesCombo;
	protected JLabel toHourLabel, fromHourLabel;
	// componentes de las repeticiones
	protected JRadioButton repetitionRadioButton, noneRadioButton,
		monthlyRadioButton, weeklyRadioButton, dailyRadioButton;
	// los botones
	protected JButton saveButton, cancelButton, acceptButton;
	// al árbol de Periods
	protected JTree periodsTree;
	// widgets para la duración de intervalos horarios
	protected JSpinner minutesInRange;
	protected JLabel minutesInRangeLabel;

	// el objeto al que le seteo el period
	protected PeriodHolder periodHolder;

	public NewPeriodWindow(final PeriodHolder pHolder) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				periodHolder = pHolder;

				createPeriodsTree();
				createChooseConditionWidgets();
				createDateWidgets();
				createRepetitionWidgets();
				createHourWidgets();
				createButtons();
				addAllWidgets();
				disableWidgets();

				setSize(450, 480);
				setLocationRelativeTo(null);
				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				setVisible(true);
			}
		});
	}

	protected void disableWidgets() {
		WidgetUtilities.enableOrDisableWidgets(false, fromDateLabel, fromDate,
				toDateLabel, toDate, fromHourLabel, fromHoursCombo, toHourLabel,
				toHoursCombo, fromMinutesCombo, toMinutesCombo, simpleRadioButton,
				compositeRadioButton, orRadioButton, andRadioButton,
				minusRadioButton, repetitionRadioButton, noneRadioButton,
				dailyRadioButton, weeklyRadioButton, monthlyRadioButton,
				minutesInRangeLabel, minutesInRange, saveButton);
	}

	protected void createDateWidgets() {
		fromDateLabel = new JLabel("Desde");
		toDateLabel = new JLabel("Hasta");
		fromDate = new UICDateEdit();
		toDate = new UICDateEdit();
	}

	protected void createHourWidgets() {
		fromHourLabel = new JLabel("Inicio");
		toHourLabel = new JLabel("Fin");
		fromHoursCombo = new JComboBox<Integer>();
		toHoursCombo = new JComboBox<Integer>();
		for (int i = 0; i < 24; i++) {
			fromHoursCombo.addItem(i);
			toHoursCombo.addItem(i);
		}
		fromMinutesCombo = new JComboBox<Integer>();
		toMinutesCombo = new JComboBox<Integer>();
		fromMinutesCombo.addItem(0);
		fromMinutesCombo.addItem(30);
		toMinutesCombo.addItem(0);
		toMinutesCombo.addItem(30);
		minutesInRangeLabel = new JLabel("Duración del intervalo (horas)");
		minutesInRange = new JSpinner(new SpinnerNumberModel(new Float(0.5),
				new Float(0.5), new Float(24), new Float(0.5)));
	}

	protected void createRepetitionWidgets() {
		ButtonGroup withOrWithoutRepetition = new ButtonGroup();
		noneRadioButton = new JRadioButton("Sin repetición");
		repetitionRadioButton = new JRadioButton("Con repetición");
		withOrWithoutRepetition.add(noneRadioButton);
		withOrWithoutRepetition.add(repetitionRadioButton);
		ButtonGroup repetitionTypes = new ButtonGroup();
		dailyRadioButton = new JRadioButton("Diaria");
		weeklyRadioButton = new JRadioButton("Semanal");
		monthlyRadioButton = new JRadioButton("Mensual");
		repetitionTypes.add(dailyRadioButton);
		repetitionTypes.add(weeklyRadioButton);
		repetitionTypes.add(monthlyRadioButton);
		createRepetitionListeners();
	}

	private void createRepetitionListeners() {
		noneRadioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean visibility = ((JRadioButton) e.getSource()).isSelected();
				WidgetUtilities.enableOrDisableWidgets(!visibility,
						dailyRadioButton, weeklyRadioButton, monthlyRadioButton,
						toDateLabel, toDate);
			}
		});
		repetitionRadioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean visibility = ((JRadioButton) e.getSource()).isSelected();
				WidgetUtilities.enableOrDisableWidgets(visibility,
						dailyRadioButton, weeklyRadioButton, monthlyRadioButton,
						toDateLabel, toDate);
				weeklyRadioButton.setSelected(true); // por defecto
			}
		});
	}

	protected void createChooseConditionWidgets() {
		ButtonGroup simpleCompositeGroup = new ButtonGroup();
		simpleRadioButton = new JRadioButton("Simple", false);
		compositeRadioButton = new JRadioButton("Compuesta", false);
		simpleCompositeGroup.add(simpleRadioButton);
		simpleCompositeGroup.add(compositeRadioButton);
		ButtonGroup compositeButtonsGroup = new ButtonGroup();
		orRadioButton = new JRadioButton("O", false);
		andRadioButton = new JRadioButton("Y", false);
		minusRadioButton = new JRadioButton("Menos", false);
		compositeButtonsGroup.add(orRadioButton);
		compositeButtonsGroup.add(andRadioButton);
		compositeButtonsGroup.add(minusRadioButton);
		createConditionListeners();
	}

	private void createConditionListeners() {
		simpleRadioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean visibility = ((JRadioButton) e.getSource()).isSelected();
				WidgetUtilities.enableOrDisableWidgets(!visibility,
						orRadioButton, andRadioButton, minusRadioButton);
				WidgetUtilities.enableOrDisableWidgets(visibility,
						fromDateLabel, fromDate, toHourLabel,
						toHoursCombo, toMinutesCombo, fromHourLabel, fromHoursCombo,
						fromMinutesCombo, noneRadioButton, repetitionRadioButton,
						minutesInRangeLabel, minutesInRange);
				if (repetitionRadioButton.isSelected())
					WidgetUtilities.enableOrDisableWidgets(true, toDateLabel, toDate,
							dailyRadioButton, weeklyRadioButton, monthlyRadioButton);
			}
		});
		compositeRadioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean visibility = ((JRadioButton) e.getSource()).isSelected();
				WidgetUtilities.enableOrDisableWidgets(visibility,
						orRadioButton, andRadioButton, minusRadioButton);
				WidgetUtilities.enableOrDisableWidgets(!visibility,
						fromDateLabel, fromDate, toDateLabel, toDate, toHourLabel,
						toHoursCombo, toMinutesCombo, fromHourLabel, fromHoursCombo,
						fromMinutesCombo, noneRadioButton, repetitionRadioButton,
						dailyRadioButton, weeklyRadioButton, monthlyRadioButton,
						minutesInRangeLabel, minutesInRange);
				orRadioButton.setSelected(true);
			}
		});
	}

	@SuppressWarnings("serial")
	protected void createPeriodsTree() {
		Period period = periodHolder.getPeriod();
		PeriodTreeNode rootPeriod = null;
		if (period == null)
			rootPeriod = new SimplePeriodTreeNode();
		else
			rootPeriod = makeTreeFromPeriod(period);
			periodsTree = new JTree(rootPeriod) {
			@Override
			public String convertValueToText(Object value, boolean selected,
					boolean expanded, boolean leaf, int row, boolean hasFocus) {
				return ((PeriodTreeNode) value).getDisplayText();
			}
		};
		periodsTree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				whenSelectedPeriodChanged(e);
			}
		});
	}

	// NOTA : podría haberlo resuelto con double dispatching para evitar
	// preguntar por la clase, pero si lo hacía de esa forma, estaba metiendo
	// lógica de interfaz en el modelo. Y me pareció mucho peor la segunda.
	protected PeriodTreeNode makeTreeFromPeriod(Period period) {
		if (period instanceof SimplePeriod)
			return makeTreeFromPeriod((SimplePeriod) period);
		else if (period instanceof And)
			return new AndPeriodTreeNode(
					makeTreeFromPeriod(((And) period).getLeftPeriod()),
					makeTreeFromPeriod(((And) period).getRightPeriod()));
		else if (period instanceof Or)
			return new OrPeriodTreeNode(
					makeTreeFromPeriod(((Or) period).getLeftPeriod()),
					makeTreeFromPeriod(((Or) period).getRightPeriod()));
		else if (period instanceof Minus)
			return new MinusPeriodTreeNode(
					makeTreeFromPeriod(((Minus) period).getLeftPeriod()),
					makeTreeFromPeriod(((Minus) period).getRightPeriod()));
		else
			return null;
	}

	// Asume que el Period está construido con LogicalHourFulfiller simple, o
	// sea HourInterval, no Or. Esto es por limitación del diseño de la interfaz.
	protected SimplePeriodTreeNode makeTreeFromPeriod(SimplePeriod period) {
		SimplePeriodTreeNode newSP = new SimplePeriodTreeNode();
		newSP.setRepetition(period.getRepetition());
		newSP.setStartDate(period.getStart());
		newSP.setMinutesInRange(((HourInterval) period.getHourFulfiller()).getMinutesInRange());
		newSP.setStartHour(((HourInterval) period.getHourFulfiller()).getStart());
		newSP.setEndHour(((HourInterval) period.getHourFulfiller()).getEnd());
		return newSP;
	}

	protected void whenSelectedPeriodChanged(TreeSelectionEvent e) {
		PeriodTreeNode periodNode = (PeriodTreeNode) periodsTree.getLastSelectedPathComponent();
		if (periodNode == null)
			disableWidgets();
		else if (periodNode.isCompositePeriodNode()) {
			WidgetUtilities.enableOrDisableWidgets(false,
					toDateLabel, toDate, fromDateLabel, fromDate, fromHourLabel,
					fromHoursCombo, toHourLabel, toHoursCombo, toMinutesCombo,
					fromMinutesCombo, noneRadioButton, repetitionRadioButton,
					dailyRadioButton, weeklyRadioButton, monthlyRadioButton,
					minutesInRangeLabel, minutesInRange);
			WidgetUtilities.enableOrDisableWidgets(true,
					simpleRadioButton, compositeRadioButton,
					orRadioButton, andRadioButton, minusRadioButton, saveButton);
			simpleRadioButton.setSelected(false);
			compositeRadioButton.setSelected(true);
			((CompositePeriodTreeNode) periodNode).selectOrDeselect(
					orRadioButton, andRadioButton, minusRadioButton);
		} else {
			WidgetUtilities.enableOrDisableWidgets(true,
					toDateLabel, toDate, fromDateLabel, fromDate, fromHourLabel,
					fromHoursCombo, toHourLabel, toHoursCombo, toMinutesCombo,
					fromMinutesCombo, noneRadioButton, repetitionRadioButton,
					dailyRadioButton, weeklyRadioButton, monthlyRadioButton,
					simpleRadioButton, compositeRadioButton,
					minutesInRangeLabel, minutesInRange, saveButton);
			WidgetUtilities.enableOrDisableWidgets(false,
					orRadioButton, andRadioButton, minusRadioButton);
			simpleRadioButton.setSelected(true);
			compositeRadioButton.setSelected(false);
			SimplePeriodTreeNode pNode = (SimplePeriodTreeNode) periodNode;
			Repetition rep = pNode.getRepetition();
			// De nuevo, pregunto por la clase ya que si hago double
			// dispatching, meto lógica de interfaz gráfica en el modelo.
			if (rep instanceof None) {
				WidgetUtilities.enableOrDisableWidgets(false, toDateLabel, toDate,
						dailyRadioButton, weeklyRadioButton, monthlyRadioButton);
				noneRadioButton.setSelected(true);
				repetitionRadioButton.setSelected(false);

			} else {
				noneRadioButton.setSelected(false);
				repetitionRadioButton.setSelected(true);
				dailyRadioButton.setSelected(rep instanceof Daily);
				weeklyRadioButton.setSelected(rep instanceof Weekly);
				monthlyRadioButton.setSelected(rep instanceof Monthly);
				toDate.setSelectedCalendar(((EndingRepetition) rep).getEnd());
			}
			fromDate.setSelectedCalendar(pNode.getStartDate());
			fromHoursCombo.setSelectedItem(pNode.getStartHour().getHour());
			toHoursCombo.setSelectedItem(pNode.getEndHour().getHour());
			fromMinutesCombo.setSelectedItem(pNode.getStartHour().getMinutes());
			toMinutesCombo.setSelectedItem(pNode.getEndHour().getMinutes());
			minutesInRange.setValue((float) pNode.getMinutesInRange() / 60);
		}
	}

	protected void createButtons() {
		saveButton = new JButton("Guardar");
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveChanges();
			}
		});
		acceptButton = new JButton("Aceptar");
		acceptButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ((PeriodTreeNode) periodsTree.getLastSelectedPathComponent() != null)
					saveChanges();
				Period period = ((PeriodTreeNode)
				periodsTree.getModel().getRoot()).makePeriod();
				if (period != null) {
					periodHolder.setPeriod(period);
					dispose();
				}
			}
		});
		cancelButton = new JButton("Cancelar");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(new JFrame(),
						"¿Desea salir de la ventana y perder los cambios?", "Salir",
				        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
				      dispose();
			}
		});
	}

	protected void saveChanges() {
		PeriodTreeNode selectedNode = (PeriodTreeNode) periodsTree.getLastSelectedPathComponent();
		if(selectedNode.matchPeriodType(simpleRadioButton.isSelected(), orRadioButton.isSelected(),
				andRadioButton.isSelected(), minusRadioButton.isSelected()))
			selectedNode.updateChanges(this);
		else {
			PeriodTreeNode newNode = null;
			if (simpleRadioButton.isSelected())
				newNode = new SimplePeriodTreeNode();
			else if (orRadioButton.isSelected())
				newNode = new OrPeriodTreeNode(new SimplePeriodTreeNode(), new SimplePeriodTreeNode());
			else if (andRadioButton.isSelected())
				newNode = new AndPeriodTreeNode(new SimplePeriodTreeNode(), new SimplePeriodTreeNode());
			else if (minusRadioButton.isSelected())
				newNode = new MinusPeriodTreeNode(new SimplePeriodTreeNode(), new SimplePeriodTreeNode());
			if (selectedNode.getParent() == null)
				((DefaultTreeModel) periodsTree.getModel()).setRoot(newNode);
			else {
				PeriodTreeNode parent = (PeriodTreeNode) selectedNode.getParent();
				int index = parent.getIndex(selectedNode);
				parent.remove(selectedNode);
				parent.insert(newNode, index);
			}
			newNode.updateChanges(this);
		}
		DefaultTreeModel model = ((DefaultTreeModel) periodsTree.getModel());
		model.reload();
		periodsTree.clearSelection();
	}

	protected void addAllWidgets() {
		// estructura principal
		JPanel main = new JPanel();
		getContentPane().add(main);
		main.setLayout(new BoxLayout(main, BoxLayout.X_AXIS));
		JPanel leftPanel = new JPanel(), rightPanel = new JPanel();
		main.add(leftPanel);
		main.add(rightPanel);
		// 1 - panel izquierdo
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		JScrollPane treeScrollPane = new JScrollPane(periodsTree);
		JPanel chooseConditionPanel = new JPanel();
		leftPanel.add(treeScrollPane);
		leftPanel.add(chooseConditionPanel);
		// 1.b - panel de elección de tipo de condición
		chooseConditionPanel.setBorder(BorderFactory.createTitledBorder("Tipo de condición"));
		chooseConditionPanel.setLayout(new BoxLayout(chooseConditionPanel, BoxLayout.Y_AXIS));
		JPanel condAuxPanel = new JPanel();
		chooseConditionPanel.add(condAuxPanel);
		condAuxPanel.setLayout(new FlowLayout());
		JPanel condAuxPanel2 = new JPanel();
		condAuxPanel.add(condAuxPanel2);
		condAuxPanel2.setLayout(new BoxLayout(condAuxPanel2, BoxLayout.Y_AXIS));
		condAuxPanel2.add(simpleRadioButton);
		condAuxPanel2.add(compositeRadioButton);
		condAuxPanel2.add(orRadioButton);
		condAuxPanel2.add(andRadioButton);
		condAuxPanel2.add(minusRadioButton);
		// 2 - panel derecho
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		JPanel datesPanel = new JPanel(), repetitionsPanel = new JPanel();
		JPanel hoursPanel = new JPanel(), buttonsPanel = new JPanel();
		rightPanel.add(datesPanel);
		rightPanel.add(repetitionsPanel);
		rightPanel.add(hoursPanel);
		rightPanel.add(buttonsPanel);
		// 2.a - panel de fechas
		datesPanel.setBorder(BorderFactory.createTitledBorder("Días"));
		datesPanel.setLayout(new BoxLayout(datesPanel, BoxLayout.Y_AXIS));
		JPanel fromDatePanel = new JPanel(), toDatePanel = new JPanel();
		datesPanel.add(fromDatePanel);
		datesPanel.add(toDatePanel);
		// 2.a.1 - panel de fecha inicio
		fromDatePanel.setLayout(new FlowLayout());
		fromDatePanel.add(fromDateLabel);
		fromDatePanel.add(fromDate);
		// 2.a.1 - panel de fecha fin
		toDatePanel.setLayout(new FlowLayout());
		toDatePanel.add(toDateLabel);
		toDatePanel.add(toDate);
		// 2.b - panel de repeticiones
		repetitionsPanel.setBorder(BorderFactory.createTitledBorder("Tipo de repetición"));
		repetitionsPanel.setLayout(new BoxLayout(repetitionsPanel, BoxLayout.Y_AXIS));
		JPanel repAuxPanel = new JPanel();
		repetitionsPanel.add(repAuxPanel);
		repAuxPanel.setLayout(new FlowLayout());
		JPanel repAuxPanel2 = new JPanel();
		repAuxPanel2.setLayout(new BoxLayout(repAuxPanel2, BoxLayout.Y_AXIS));
		repAuxPanel.add(repAuxPanel2);
		repAuxPanel2.add(noneRadioButton);
		repAuxPanel2.add(repetitionRadioButton);
		repAuxPanel2.add(dailyRadioButton);
		repAuxPanel2.add(weeklyRadioButton);
		repAuxPanel2.add(monthlyRadioButton);
		// 2.c - panel de horas
		hoursPanel.setLayout(new BoxLayout(hoursPanel, BoxLayout.Y_AXIS));
		JPanel startHourPanel = new JPanel(), endHourPanel = new JPanel();
		JPanel minutesInRangePanel = new JPanel();
		hoursPanel.setBorder(BorderFactory.createTitledBorder("Horas"));
		hoursPanel.add(startHourPanel);
		hoursPanel.add(endHourPanel);
		hoursPanel.add(minutesInRangePanel);
		// 2.b.1 - panel hora inicio
		startHourPanel.setLayout(new FlowLayout());
		startHourPanel.add(fromHourLabel);
		startHourPanel.add(fromHoursCombo);
		startHourPanel.add(fromMinutesCombo);
		// 2.b.2 - panel hora fin
		endHourPanel.setLayout(new FlowLayout());
		endHourPanel.add(toHourLabel);
		endHourPanel.add(toHoursCombo);
		endHourPanel.add(toMinutesCombo);
		// 2.b.3 - panel de duración del intervalo
		minutesInRangePanel.setLayout(new FlowLayout());
		minutesInRangePanel.add(minutesInRangeLabel);
		minutesInRangePanel.add(minutesInRange);
		// 3 - panel inferior
		buttonsPanel.setLayout(new FlowLayout());
		buttonsPanel.add(saveButton);
		buttonsPanel.add(acceptButton);
		buttonsPanel.add(cancelButton);
	}

	public void updateChangesFromSimple(SimplePeriodTreeNode sptn) {
		sptn.setStartHour(new Timestamp((Integer) fromHoursCombo.getSelectedItem(),
				(Integer) fromMinutesCombo.getSelectedItem()));
		sptn.setEndHour(new Timestamp((Integer) toHoursCombo.getSelectedItem(),
				(Integer) toMinutesCombo.getSelectedItem()));
		sptn.setMinutesInRange(Math.round((Float) minutesInRange.getValue() * 60));
		sptn.setStartDate(fromDate.getSelectedCalendar());
		if (noneRadioButton.isSelected())
			sptn.setRepetition(new None());
		else {
			Calendar end = toDate.getSelectedCalendar();
			if (dailyRadioButton.isSelected())
				sptn.setRepetition(new Daily(end));
			else if (weeklyRadioButton.isSelected())
				sptn.setRepetition(new Weekly(end));
			else if (monthlyRadioButton.isSelected())
				sptn.setRepetition(new Monthly(end));
		}
	}
}