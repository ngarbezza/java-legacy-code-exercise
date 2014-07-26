package ar.edu.unq.sasa.gui;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import ar.edu.unq.sasa.gui.departments.AssignmentsPanel;
import ar.edu.unq.sasa.gui.departments.BookedAssignmentsPanel;
import ar.edu.unq.sasa.gui.departments.ClassroomsPanel;
import ar.edu.unq.sasa.gui.departments.MobileResourcesPanel;
import ar.edu.unq.sasa.gui.departments.ProfessorsPanel;
import ar.edu.unq.sasa.gui.departments.RequestsPanel;
import ar.edu.unq.sasa.model.academic.University;

/**
 * Panel con todos los componentes de la ventana principal.
 */
public class MainWindow extends JFrame {

	private static final long serialVersionUID = -1524127390983723901L;

	public MainWindow(final University university) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {

				setTitle("Sistema de Asignación Sobre Aulas");
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				setSize(800, 600);
				setLocationRelativeTo(null);
				setVisible(true);

				addTabs(university);
			}
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
		mainTabsPanel.add(new BookedAssignmentsPanel(university.getAssignmentsDepartment()));
		add(mainTabsPanel);
	}
}
