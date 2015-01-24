package ar.edu.unq.sasa.gui.period;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import ar.edu.unq.sasa.model.time.Period;

public class PeriodDetailWindow extends JFrame {

	private static final long serialVersionUID = 265531544901039040L;

	public PeriodDetailWindow(final Period period) {
		SwingUtilities.invokeLater(() -> {
            JTextArea area = new JTextArea();
            area.setFont(new Font("Verdana", Font.PLAIN, 12));
            area.setOpaque(true);
            area.setBackground(new Color(0xD8E3D8));
            area.setEditable(false);
            area.setText(period.toString());
            add(new JScrollPane(area));

            setTitle("Detalles de Período");
            setSize(430, 200);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setVisible(true);
        });
	}
}
