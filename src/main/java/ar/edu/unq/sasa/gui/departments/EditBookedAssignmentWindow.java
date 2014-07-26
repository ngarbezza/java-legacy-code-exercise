package ar.edu.unq.sasa.gui.departments;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import ar.edu.unq.sasa.model.assignments.BookedAssignment;
import ar.edu.unq.sasa.model.departments.AssignmentsDepartment;

/**
 * Ventana para editar una Reserva ({@link BookedAssignment})
 */
public class EditBookedAssignmentWindow extends JFrame{

	private static final long serialVersionUID = 2343559759639534499L;
	private AssignmentsDepartment department;
	private BookedAssignment bookedAssignmentSelection;

	private String newCause;

	private JLabel newCauseLabel;
	private JTextField newCauseTextField;
	private JButton acceptButton;
	private JButton cancelButton;

	public EditBookedAssignmentWindow(AssignmentsDepartment assignmentsDepartment, BookedAssignment selection) {
		department = assignmentsDepartment;
		bookedAssignmentSelection = selection;

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				createComponents();
				organizeComponents();

				setResizable(false);
				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				setTitle("Edicion de una Reserva");
				setSize(300, 120);
				setLocationRelativeTo(null);
				setVisible(true);
			}
		});
	}

	private void createComponents() {
		newCauseLabel = new JLabel("Nueva Causa");

		newCauseTextField = new JTextField(20);
		newCauseTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				String cause = ((JTextField)e.getSource()).getText();
				if (! cause.equals("")){
					newCause = cause;
					acceptButton.setEnabled(true);
				} else
					acceptButton.setEnabled(false);
			}
		});

		acceptButton = new JButton("Aceptar");
		acceptButton.setEnabled(false);
		createAcceptButtonListener();
		cancelButton = new JButton("Cancelar");
		createCancelButtonListener();
	}

	private void createAcceptButtonListener() {
		acceptButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				department.modifyBookedAssignmentCause(bookedAssignmentSelection, newCause);
				dispose();
			}
		});
	}

	private void createCancelButtonListener() {
		cancelButton.addActionListener(new ActionListener() {
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
		topPanel.setLayout(new FlowLayout());
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new FlowLayout());

		mainPanel.add(topPanel);
		mainPanel.add(bottomPanel);
		topPanel.add(newCauseLabel);
		topPanel.add(newCauseTextField);

		bottomPanel.add(acceptButton);
		bottomPanel.add(cancelButton);
	}


}
