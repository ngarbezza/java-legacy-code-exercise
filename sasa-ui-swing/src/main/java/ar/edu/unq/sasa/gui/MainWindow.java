package ar.edu.unq.sasa.gui;

import ar.edu.unq.sasa.gui.departments.*;
import ar.edu.unq.sasa.model.academic.University;

import javax.swing.*;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = -1524127390983723901L;

	public MainWindow(final University university) {
		SwingUtilities.invokeLater(() -> {

            setTitle("Sistema de Asignación Sobre Aulas");
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            setSize(800, 600);
            setLocationRelativeTo(null);
            setVisible(true);

            addTabs(university);
        });
	}

	protected void addTabs(University university) {
		JTabbedPane mainTabsPanel = new JTabbedPane();
		mainTabsPanel.setTabPlacement(JTabbedPane.LEFT);
		mainTabsPanel.add(new MainPanel());
		mainTabsPanel.add(new ProfessorsPanel(university.getProfessorsDepartment()));
		mainTabsPanel.add(new ClassroomsPanel(university.getClassroomsDepartment()));
		mainTabsPanel.add(new RequestsPanel(university.getRequestsDepartment()));
		mainTabsPanel.add(new MobileResourcesPanel(university.getResourcesDepartment()));
		mainTabsPanel.add(new AssignmentsPanel(university.getAssignmentsDepartment()));
		mainTabsPanel.add(new BookingsPanel(university.getAssignmentsDepartment()));
		add(mainTabsPanel);
	}
}
