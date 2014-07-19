package ar.edu.unq.sasa.gui.period;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import ar.edu.unq.sasa.model.time.Period;

/**
 * Ventana que muestra en forma de texto un período de tiempo.
 * 
 * @author Nahuel Garbezza
 *
 */
public class PeriodDetailWindow extends JFrame {
	
	public PeriodDetailWindow(final Period p) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JTextArea area = new JTextArea();
				area.setFont(new Font("Verdana", Font.PLAIN, 12));
				area.setOpaque(true);
				area.setBackground(new Color(0xD8E3D8));
				area.setEditable(false);
				area.setText(p.toString());
				add(new JScrollPane(area));
				
				setTitle("Detalles de Período");
				setSize(430, 200);
				setLocationRelativeTo(null);
				setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				setVisible(true);
			}
		});
	}
}
