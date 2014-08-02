package ar.edu.unq.sasa.gui.departments;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

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
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import uic.layout.VerticalLayout;
import ar.edu.unq.sasa.gui.period.NewPeriodWindow;
import ar.edu.unq.sasa.gui.util.PeriodHolder;
import ar.edu.unq.sasa.gui.util.tables.ReadOnlyTableModel;
import ar.edu.unq.sasa.model.assignments.ClassroomAssignment;
import ar.edu.unq.sasa.model.departments.AssignmentsDepartment;
import ar.edu.unq.sasa.model.items.Classroom;
import ar.edu.unq.sasa.model.time.Period;

public class EditAssignmentWindow extends JFrame implements PeriodHolder {

	private static final long serialVersionUID = 4976218465593759722L;

	private ClassroomAssignment assignment;

	private Classroom classroomSelected;
	private Period period;

	private JLabel searchLabel;
	private JTextField searchTextField;
	private JScrollPane classroomsTableScrollPane;
	private JTable classroomsTable;
	private JButton changePeriodButton;
	private JButton moveAssignmentOfClassroomButton;
	private JButton moveAssignmentOfPeriodButton;
	private JButton cancelButton;

	private AssignmentsDepartment department;

	public EditAssignmentWindow(AssignmentsDepartment assignmentsDepartment, ClassroomAssignment assignmentSelection) {
		department = assignmentsDepartment;
		assignment = assignmentSelection;
		period = assignmentSelection.getPeriod();

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				createSearchComponents();
				createClassroomsTable();
				createButtons();
				organizeComponents();

				setResizable(false);
				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				setTitle("Modificar Asignacion");
				setSize(350, 450);
				setLocationRelativeTo(null);
				setVisible(true);
			}
		});
	}

	@Override
	public Period getPeriod() {
		return period;
	}

	@Override
	public void setPeriod(Period p) {
		period = p;
		moveAssignmentOfPeriodButton.setEnabled(true);
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
				String text = ((JTextField) e.getSource()).getText();
				List<Classroom> res = department.getClassroomsDepartment().searchClassroomByName(text);
				((ReadOnlyTableModel<Classroom>) classroomsTable.getModel()).setModel(res);
			}
		});
	}

	private void createClassroomsTable() {
		ReadOnlyTableModel<Classroom> tableModel =
				new ReadOnlyTableModel<Classroom>(department.getClassroomsDepartment().searchClassroomByName(""));
		addClassroomsColumns(tableModel);
		classroomsTable = new JTable(tableModel);
		classroomsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		classroomsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				EditAssignmentWindow.this.whenClassroomsTableSelectionChanged(e);
			}
		});
		classroomsTableScrollPane = new JScrollPane(classroomsTable);
	}

	private void addClassroomsColumns(ReadOnlyTableModel<Classroom> tableModel) {
		tableModel.addColumn("Aula", "name");
		tableModel.addColumn("Capacidad", "capacity");
	}

	@SuppressWarnings("unchecked")
	protected void whenClassroomsTableSelectionChanged(ListSelectionEvent anEvent) {
		DefaultListSelectionModel source = (DefaultListSelectionModel) anEvent.getSource();
		if (source.isSelectionEmpty()) {
			classroomSelected = null;
			moveAssignmentOfClassroomButton.setEnabled(false);
			System.out.println("Entre!!");
		} else {
			int index = source.getMinSelectionIndex();
			List<Classroom> model = ((ReadOnlyTableModel<Classroom>) classroomsTable.getModel()).getModel();
			classroomSelected = model.get(index);
			moveAssignmentOfClassroomButton.setEnabled(!classroomSelected.equals(assignment.getAssignableItem()));
		}
	}

	private void createButtons() {
		changePeriodButton = new JButton("Cambiar Periodo");
		createChangePeriodButtonListeners();
		moveAssignmentOfClassroomButton = new JButton("Cambiar Asignacion al aula seleccionada");
		createMoveAssignmentOfClassroomButtonButtonListeners();
		moveAssignmentOfClassroomButton.setEnabled(false);
		moveAssignmentOfPeriodButton = new JButton("Cambiar Asignacion de Periodo");
		createMoveAssignmentOfPeriodButtonButtonListeners();
		moveAssignmentOfPeriodButton.setEnabled(false);
		cancelButton = new JButton("Cancelar");
		createCancelButtonListeners();
	}

	private void createChangePeriodButtonListeners() {
		changePeriodButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent anEvent) {
				new NewPeriodWindow(EditAssignmentWindow.this);
			}
		});
	}

	private void createMoveAssignmentOfClassroomButtonButtonListeners() {
		moveAssignmentOfClassroomButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent anEvent) {
				if (JOptionPane.showConfirmDialog(new JFrame(),	"¿Desea cambiar la asignación al aula elegida?",
						"Cambio de Aula", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					department.moveAssignmentOfClassroom(assignment, classroomSelected);
					dispose();
				}
			}
		});
	}

	private void createMoveAssignmentOfPeriodButtonButtonListeners() {
		moveAssignmentOfPeriodButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent anEvent) {
				if (JOptionPane.showConfirmDialog(new JFrame(),	"¿Desea cambiar el periodo de la asignación?",
						"Cambio de Periodo", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					department.moveAssignmentOfPeriod(assignment, period);
					dispose();
				}
			}
		});
	}

	private void createCancelButtonListeners() {
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent anEvent) {
				if (JOptionPane.showConfirmDialog(new JFrame(),	"¿Desea cancelar los cambios?",
						"Cancelar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
					dispose();
			}
		});
	}

	private void organizeComponents() {
		JPanel mainPanel = new JPanel();
		getContentPane().add(mainPanel);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		JPanel classroomsTablePanel = new JPanel();
		classroomsTablePanel.setLayout(new BoxLayout(classroomsTablePanel, BoxLayout.Y_AXIS));
		classroomsTablePanel.setBorder(BorderFactory.createTitledBorder("Seleccione un Aula"));
		JPanel periodSelectionPanel = new JPanel();
		JPanel moveButtonsPanel = new JPanel();
		JPanel cancelPanel = new JPanel();
		cancelPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new FlowLayout());
		searchPanel.add(searchLabel);
		searchPanel.add(searchTextField);

		classroomsTablePanel.add(searchPanel);
		classroomsTablePanel.add(classroomsTableScrollPane);

		mainPanel.add(classroomsTablePanel);
		mainPanel.add(periodSelectionPanel);
		mainPanel.add(moveButtonsPanel);
		mainPanel.add(cancelPanel);

		periodSelectionPanel.setLayout(new FlowLayout());
		periodSelectionPanel.add(changePeriodButton);

		moveButtonsPanel.setLayout(new VerticalLayout());
		moveButtonsPanel.add(moveAssignmentOfClassroomButton);
		moveButtonsPanel.add(moveAssignmentOfPeriodButton);

		cancelPanel.add(cancelButton);
	}
}
