package ar.edu.unq.sasa.gui.departments;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;

import ar.edu.unq.sasa.gui.util.tables.ReadOnlyTableModel;
import ar.edu.unq.sasa.model.departments.ResourcesDepartment;
import ar.edu.unq.sasa.model.exceptions.departments.ResourceException;
import ar.edu.unq.sasa.model.items.MobileResource;

/**
 * ABM de recursos móviles. Permite también búsqueda por nombre.
 */
public class MobileResourcesPanel extends AbstractDepartmentPanel<MobileResource> {

	private static final long serialVersionUID = -484671956803489488L;

	protected JButton assignmentsDetailButton;

	private ResourcesDepartment department;

	public MobileResourcesPanel(ResourcesDepartment resourcesDepartment) {
		department = resourcesDepartment;
		initialize();
	}

	@Override
	public String getName() {
		return "Recursos";
	}

	@Override
	protected void addColumns(ReadOnlyTableModel<MobileResource> tableModel) {
		tableModel.addColumn("ID", "id");
		tableModel.addColumn("Nombre", "name");
	}

	@Override
	protected void createAddButtonListeners() {
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new EditMobileResourceWindow(department);
			}
		});
	}

	@Override
	protected void createDeleteButtonListeners() {
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(new JFrame(),
						"¿Desea eliminar el recurso seleccionado?", "Eliminar",
				        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
					try {
						department.deleteResource(selection.getName());
					} catch (ResourceException e1) {
						JOptionPane.showMessageDialog(MobileResourcesPanel.this,
								"No se pudo borrar el recurso",
								"Advertencia", JOptionPane.WARNING_MESSAGE);
					}
			}
		});
	}

	@Override
	protected void createModifyButtonListeners() {
		modifyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new EditMobileResourceWindow(department, selection);
			}
		});
	}

	@Override
	protected List<MobileResource> getListModel() {
		return department.getMobileResources();
	}

	@Override
	protected String getSearchLabelText() {
		return "Búsqueda por nombre";
	}

	@Override
	protected Component makeSearchField() {
		JTextField input = new JTextField(10);
		input.addKeyListener(new KeyAdapter() {
			@Override
			@SuppressWarnings("unchecked")
			public void keyReleased(KeyEvent e) {
				String text = ((JTextField)e.getSource()).getText();
				List<MobileResource> res = department.searchResources(text);
				((ReadOnlyTableModel<MobileResource>)table.getModel()).setModel(res);
			}
		});
		return input;
	}

	@Override
	protected void whenTableSelectionChanged(ListSelectionEvent e) {
		super.whenTableSelectionChanged(e);
		assignmentsDetailButton.setEnabled(selection != null);
	}

	@Override
	protected void addOtherWidgetsToBottomPanel(JPanel bottomPanel) {
		assignmentsDetailButton = new JButton("Ver asignaciones");
		assignmentsDetailButton.setEnabled(false);
		assignmentsDetailButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new MobileResourceAssignmentsDetailWindow(selection);
			}
		});
		bottomPanel.add(assignmentsDetailButton);
	}

	@Override
	protected void registerAsSubscriber() {
		department.getPublisher().addSubscriber("mobileResources", this);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void update(String aspect, Object value) {
		((ReadOnlyTableModel<MobileResource>)table.getModel())
		.setModel((List<MobileResource>)value);
	}
}