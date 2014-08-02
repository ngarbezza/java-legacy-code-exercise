package ar.edu.unq.sasa.gui.departments;

import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ar.edu.unq.sasa.model.departments.ResourcesDepartment;
import ar.edu.unq.sasa.model.items.MobileResource;

/**
 * Ventana de edición de recursos móviles. También sirve para crear
 * nuevos recursos móviles.
 */
public class EditMobileResourceWindow extends AbstractEditWindow<MobileResource> {

	private static final long serialVersionUID = -7340989131088695181L;

	protected JTextField nameField;
	protected JLabel nameLabel;

	private ResourcesDepartment department;

	public EditMobileResourceWindow(ResourcesDepartment resourcesDepartment) {
		this(resourcesDepartment, null);
	}

	public EditMobileResourceWindow(ResourcesDepartment resourcesDepartment, MobileResource mr) {
		super(mr);
		department = resourcesDepartment;
	}

	@Override
	protected void createWidgetsTopPanel() {
		nameLabel = new JLabel("Nombre");
		nameField = new JTextField(12);
		nameField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				acceptButton.setEnabled((validateName()));
			}
		});
		if (inEditMode())
			nameField.setText(item.getName());
	}

	@Override
	protected void doAcceptActionInEditMode() {
		department.modifyResource(item, nameField.getText());
	}

	private boolean validateName() {
		return !(nameField.getText().equals(""));
	}

	@Override
	protected void doAcceptInAddingMode() {
		department.createMobileResource(nameField.getText());
	}

	@Override
	protected int getWindowHeight() {
		return 120;
	}

	@Override
	protected String getWindowTitle() {
		return inEditMode() ? "Editar Recurso móvil" : "Nuevo Recurso móvil";
	}

	@Override
	protected int getWindowWidth() {
		return 300;
	}

	@Override
	protected void organizeTopPanelWidgets(JPanel topPanel) {
		topPanel.setLayout(new FlowLayout());
		topPanel.add(nameLabel);
		topPanel.add(nameField);
	}
}
