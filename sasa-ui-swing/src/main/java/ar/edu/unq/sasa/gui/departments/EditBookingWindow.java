package ar.edu.unq.sasa.gui.departments;

import ar.edu.unq.sasa.model.assignments.Booking;
import ar.edu.unq.sasa.model.departments.AssignmentsDepartment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class EditBookingWindow extends JFrame {

    private static final long serialVersionUID = 2343559759639534499L;
    private AssignmentsDepartment department;
    private Booking bookingSelection;

    private String newCause;

    private JLabel newCauseLabel;
    private JTextField newCauseTextField;
    private JButton acceptButton;
    private JButton cancelButton;

    public EditBookingWindow(AssignmentsDepartment assignmentsDepartment, Booking selection) {
        department = assignmentsDepartment;
        bookingSelection = selection;

        SwingUtilities.invokeLater(() -> {
            createComponents();
            organizeComponents();

            setResizable(false);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setTitle("Editar Reserva");
            setSize(300, 120);
            setLocationRelativeTo(null);
            setVisible(true);
        });
    }

    private void createComponents() {
        newCauseLabel = new JLabel("Nueva Causa");

        newCauseTextField = new JTextField(20);
        newCauseTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent anEvent) {
                String cause = ((JTextField) anEvent.getSource()).getText();
                if (!cause.equals("")) {
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
        acceptButton.addActionListener(anEvent -> {
            department.modifyBookedAssignmentCause(bookingSelection, newCause);
            dispose();
        });
    }

    private void createCancelButtonListener() {
        cancelButton.addActionListener(anEvent -> dispose());
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
