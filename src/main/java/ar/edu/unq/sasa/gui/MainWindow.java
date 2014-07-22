package ar.edu.unq.sasa.gui;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import ar.edu.unq.sasa.gui.handlers.AssignmentsPanel;
import ar.edu.unq.sasa.gui.handlers.BookedAssignmentsPanel;
import ar.edu.unq.sasa.gui.handlers.ClassroomsPanel;
import ar.edu.unq.sasa.gui.handlers.MobileResourcesPanel;
import ar.edu.unq.sasa.gui.handlers.ProfessorsPanel;
import ar.edu.unq.sasa.gui.handlers.RequestsPanel;

/**
 * Panel con todos los componentes de la ventana principal.
 */
public class MainWindow extends JFrame {

	private static final long serialVersionUID = -1524127390983723901L;

	public MainWindow() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {

				setTitle("Sistema de Asignación Sobre Aulas");
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				setSize(800, 600);
				setLocationRelativeTo(null);
				setVisible(true);

				addTabs();
			}
		});
	}

	protected void addTabs() {
		JTabbedPane mainTabsPanel = new JTabbedPane();
		mainTabsPanel.setTabPlacement(JTabbedPane.LEFT);
		mainTabsPanel.add(new MainPanel());
		mainTabsPanel.add(new ProfessorsPanel());
		mainTabsPanel.add(new ClassroomsPanel());
		mainTabsPanel.add(new RequestsPanel());
		mainTabsPanel.add(new MobileResourcesPanel());
		mainTabsPanel.add(new AssignmentsPanel());
		mainTabsPanel.add(new BookedAssignmentsPanel());
		add(mainTabsPanel);
	}
}
