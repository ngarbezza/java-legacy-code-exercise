package ar.edu.unq.sasa.gui;

import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Panel para la ventana principal de la aplicación.
 */
public class MainPanel extends JPanel {

	private static final long serialVersionUID = -6536603456827458242L;

	private JLabel sasaLabel;

	public MainPanel() {
		createWidgets();
		addWidgets();
	}

	private void addWidgets() {
		JPanel sasaPanel = new JPanel();
		add(sasaPanel);
		sasaPanel.setLayout(new BoxLayout(sasaPanel, BoxLayout.Y_AXIS));
		sasaPanel.add(sasaLabel);
		sasaPanel.add(new JLabel("Sistema de Asignación Sobre Aulas"));
		sasaPanel.add(new JLabel("por"));
		sasaPanel.add(new JLabel("Diego Campos"));
		sasaPanel.add(new JLabel("Gastón Charkiewicz"));
		sasaPanel.add(new JLabel("Nahuel Garbezza"));
		sasaPanel.add(new JLabel("Cristian Suárez"));
	}

	private void createWidgets() {
		sasaLabel = new JLabel("S.A.S.A");
		sasaLabel.setFont(new Font("Verdana", Font.BOLD, 30));
	}

	@Override
	public String getName() {
		return "Principal";
	}
}
