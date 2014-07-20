package ar.edu.unq.sasa.gui.handlers;

import java.awt.Color;
import java.awt.Dimension;
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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import uic.layout.HorizontalLayout;
import uic.layout.VerticalLayout;
import ar.edu.unq.sasa.gui.period.NewPeriodWindow;
import ar.edu.unq.sasa.gui.util.PeriodHolder;
import ar.edu.unq.sasa.gui.util.tables.ReadOnlyTableModel;
import ar.edu.unq.sasa.model.assignments.BookedAssignment;
import ar.edu.unq.sasa.model.exceptions.time.PeriodException;
import ar.edu.unq.sasa.model.handlers.Asignator;
import ar.edu.unq.sasa.model.handlers.ClassroomHandler;
import ar.edu.unq.sasa.model.items.Classroom;
import ar.edu.unq.sasa.model.time.Period;

/**
 * Ventana para crear Reservas ({@link BookedAssignment})
 * 
 * @author Cristian Suarez
 *
 */
public class CreateBookedAssignmentWindow extends JFrame implements PeriodHolder {
	private Asignator handler = Asignator.getInstance();
	
	private Classroom classroomSelected;
	private Period period;
	private String cause;
	
	private JLabel searchTextLabel;
	private JTextField searchTextField;
	private JScrollPane classroomsTableScrollPane;
	private JTable classroomsTable;
	private JLabel obligatoryPeriodLabel;
	private JButton newPeriodButton;
	private JLabel obligatoryCauseLabel;
	private JLabel causeLabel;
	private JTextField causeTextField;
	private JScrollPane periodDetailScrollPane;
	private JTextArea periodDetailTextArea;
	private JButton acceptButton;
	private JButton cancelButton;
	
	public CreateBookedAssignmentWindow() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				createClassroomsTable();
				createOtherComponents();
				organizeComponents();
				
				setResizable(true);
				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				setTitle("Creación de una Reserva");
				setSize(475, 375);
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
	public void setPeriod(Period newPeriod) {
		period = newPeriod;
		if (cause == null || classroomSelected == null){
			acceptButton.setEnabled(false);
		}
		else if (cause.equals("")) {
			acceptButton.setEnabled(false);
		}
		else {
			acceptButton.setEnabled(true);
		}
		periodDetailTextArea.setText(period.toString());
	}

	public Asignator getHandler(){
		return handler;
	}
	
	private void createClassroomsTable() {
		ReadOnlyTableModel<Classroom> tableModel = new ReadOnlyTableModel<Classroom>(ClassroomHandler.getInstance().searchClassroomByName(""));
		addClassroomsColumns(tableModel);
		classroomsTable = new JTable(tableModel);
		classroomsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		classroomsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				CreateBookedAssignmentWindow.this.whenClassroomsTableSelectionChanged(e);
			}
		});
		classroomsTableScrollPane = new JScrollPane(classroomsTable);
	}

	private void addClassroomsColumns(ReadOnlyTableModel<Classroom> tableModel) {
		tableModel.addColumn("Aula", "name");
		tableModel.addColumn("Capacidad", "capacity");
	}

	@SuppressWarnings("unchecked")
	protected void whenClassroomsTableSelectionChanged(ListSelectionEvent e) {
		DefaultListSelectionModel source = (DefaultListSelectionModel)e.getSource(); 
		if (source.isSelectionEmpty()) {
			classroomSelected = null;
			acceptButton.setEnabled(false);
		}
		else {
			int index = source.getMinSelectionIndex();
			List<Classroom> model = ((ReadOnlyTableModel<Classroom>)classroomsTable.getModel()).getModel();
			classroomSelected = model.get(index);
			if (period == null || cause.equals("") || cause == null){
				acceptButton.setEnabled(false);
			}
			else {
				acceptButton.setEnabled(true);
			}
		}
	}

	private void createOtherComponents() {
		obligatoryPeriodLabel = new JLabel("*");
		obligatoryPeriodLabel.setForeground(Color.RED);
		obligatoryCauseLabel = new JLabel("*");
		obligatoryCauseLabel.setForeground(Color.RED);
		searchTextLabel = new JLabel("Busqueda por nombre");
		searchTextField = new JTextField(10);
		createSearchTextFieldListeners();
		newPeriodButton = new JButton("Elegir Periodo");
		createNewPeriodButtonListeners();
		causeLabel = new JLabel(" Causa ");
		causeTextField = makeCauseTextField();
		acceptButton = new JButton("Aceptar");
		acceptButton.setEnabled(false);
		createAcceptButtonListeners();
		cancelButton = new JButton("Cancelar");
		createCancelButtonListeners();
		periodDetailTextArea = new JTextArea("Todavía no se eligió ningun Periodo");
		periodDetailScrollPane = new JScrollPane(periodDetailTextArea);
		periodDetailScrollPane.setPreferredSize(new Dimension(455, 50));
	}
	

	private void createNewPeriodButtonListeners() {
		newPeriodButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new NewPeriodWindow(CreateBookedAssignmentWindow.this);
 			}
		});
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

	private JTextField makeCauseTextField() {
		JTextField field = new JTextField(20);
		field.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				cause = ((JTextField)e.getSource()).getText();
				if (cause.equals("") || period == null || classroomSelected == null){
					acceptButton.setEnabled(false);
				}
				else {
					acceptButton.setEnabled(true);
				}
			}
		});
		return field;
	}

	private void createAcceptButtonListeners() {
		acceptButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(new JFrame(),	"¿Desea crear la Reseva con esos datos?", "Aceptar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
					try {getHandler().asignateBookedAssignment(classroomSelected, cause, period);}
					catch (PeriodException e1) {}
					dispose();
				}
			}
		});
	}

	private void createCancelButtonListeners() {
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(new JFrame(),	"¿Desea cancelar la creacion de la Reserva?", "Cancelar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
					dispose();
				}
			}
		});
	}

	private void organizeComponents() {
		JPanel mainPanel = new JPanel();
		getContentPane().add(mainPanel);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		JPanel topMain = new JPanel();
		topMain.setLayout(new HorizontalLayout());
		JPanel bottomMain = new JPanel();
		bottomMain.setLayout(new FlowLayout());
		
		JPanel leftTopPanel = new JPanel();
		leftTopPanel.setLayout(new VerticalLayout());
		leftTopPanel.setBorder(BorderFactory.createTitledBorder("Seleccione un Aula"));
		JPanel rightTopPanel = new JPanel();
		rightTopPanel.setBorder(BorderFactory.createTitledBorder("Informacion de la Reserva"));
		rightTopPanel.setLayout(new VerticalLayout());

		JPanel acceptAndCancelPanel = new JPanel();
		acceptAndCancelPanel.setLayout(new FlowLayout());

		acceptAndCancelPanel.add(acceptButton);
		acceptAndCancelPanel.add(cancelButton);

		JPanel periodSelectionPanel = new JPanel();
		periodSelectionPanel.setLayout(new FlowLayout());
		JPanel causeSelectionPanel = new JPanel();
		causeSelectionPanel.setLayout(new BoxLayout(causeSelectionPanel, BoxLayout.X_AXIS));
		
		periodSelectionPanel.add(obligatoryPeriodLabel);
		periodSelectionPanel.add(newPeriodButton);
		
		causeSelectionPanel.add(obligatoryCauseLabel);
		causeSelectionPanel.add(causeLabel);
		causeSelectionPanel.add(causeTextField);
		
		JPanel classroomsSearchPanel = new JPanel();
		classroomsSearchPanel.setLayout(new FlowLayout());
		
		classroomsSearchPanel.add(searchTextLabel);
		classroomsSearchPanel.add(searchTextField);
		
		JPanel periodDetailPanel = new JPanel();
		periodDetailPanel.setLayout(new FlowLayout());
		periodDetailPanel.setBorder(BorderFactory.createTitledBorder("Detalle del Periodo elegido"));
		periodDetailPanel.add(periodDetailScrollPane);
		
		mainPanel.add(topMain);
		mainPanel.add(periodDetailPanel);
		mainPanel.add(bottomMain);
		
		topMain.add(leftTopPanel);
		topMain.add(rightTopPanel);
		bottomMain.add(acceptAndCancelPanel);
		
		leftTopPanel.add(classroomsSearchPanel);
		leftTopPanel.add(classroomsTableScrollPane);
		
		rightTopPanel.add(new JPanel().add(new JLabel(" ")));
		rightTopPanel.add(new JPanel().add(new JLabel(" ")));
		rightTopPanel.add(periodSelectionPanel);
		rightTopPanel.add(new JPanel().add(new JLabel(" ")));
		rightTopPanel.add(new JPanel().add(new JLabel(" ")));
		rightTopPanel.add(causeSelectionPanel);
	}
	
}
