package ar.edu.unq.sasa.gui.handlers;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Clase que sirve de base en la construcción de ventanas de edición
 * para los elementos que manejan los handlers.
 * 
 * @author Nahuel Garbezza
 *
 */
public abstract class AbstractEditWindow<A> extends JFrame {

	protected A item;
	protected JButton acceptButton, cancelButton;
	
	public AbstractEditWindow() {
		this(null);
	}
	
	public AbstractEditWindow(final A anItem) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				item = anItem;
				createWidgetsTopPanel();
				createWidgetsBottomPanel();
				organizeWidgets();
				doValidations();
				// propiedades de la ventana
				setResizable(mustBeResizable());
				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				setTitle(getWindowTitle());
				setSize(getWindowWidth(), getWindowHeight());
				setAnotherConfigurations();
				setLocationRelativeTo(null);
				setVisible(true);			
			}
		});
	}

	protected void organizeWidgets() {
		JPanel mainPanel = new JPanel();
		getContentPane().add(mainPanel);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		JPanel topPanel = new JPanel();
		JPanel bottomPanel = new JPanel();
		organizeTopPanelWidgets(topPanel);
		bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		bottomPanel.add(acceptButton);
		bottomPanel.add(cancelButton);
		mainPanel.add(topPanel);
		mainPanel.add(bottomPanel);
	}

	// puede redefinirse !
	protected boolean mustBeResizable() {
		return true;
	}
	
	protected boolean inEditMode() {
		return item != null;
	}

	protected void createWidgetsBottomPanel() {
		acceptButton = new JButton("Aceptar");
		acceptButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (inEditMode())
					doAcceptActionInEditMode();
				else
					doAcceptInAddingMode();
				dispose();
			}
		});
		cancelButton = new JButton("Cancelar");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();				
			}
		});
	}
	
	protected void doValidations() {}
	// configuraciones especiales de algunos widgets
	protected void setAnotherConfigurations() {}

	protected abstract String getWindowTitle();
	protected abstract int getWindowWidth();
	protected abstract int getWindowHeight();
	
	// widgets particulares a cada ventana de edición
	protected abstract void createWidgetsTopPanel();
	
	protected abstract void organizeTopPanelWidgets(JPanel topPanel);

	// qué hacer cuando se apreta el botón de "Aceptar"
	protected abstract void doAcceptInAddingMode();
	protected abstract void doAcceptActionInEditMode();
}
