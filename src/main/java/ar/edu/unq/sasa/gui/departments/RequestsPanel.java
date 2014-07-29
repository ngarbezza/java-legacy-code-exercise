package ar.edu.unq.sasa.gui.departments;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;

import ar.edu.unq.sasa.gui.util.ObjectToStringConverter;
import ar.edu.unq.sasa.gui.util.combos.EasyComboBoxModel;
import ar.edu.unq.sasa.gui.util.combos.EasyComboBoxRenderer;
import ar.edu.unq.sasa.gui.util.tables.ReadOnlyTableModel;
import ar.edu.unq.sasa.model.academic.ClassroomRequest;
import ar.edu.unq.sasa.model.academic.Professor;
import ar.edu.unq.sasa.model.academic.Request;
import ar.edu.unq.sasa.model.academic.Subject;
import ar.edu.unq.sasa.model.departments.RequestsDepartment;

/**
 * Panel de consulta sobre {@link Request}s (Pedidos).
 */
public class RequestsPanel extends AbstractDepartmentPanel<Request> {

	private static final long serialVersionUID = -6791066586292230725L;

	private RequestsDepartment department;

	protected JButton requestDetail;

	public RequestsPanel(RequestsDepartment requestsDepartment) {
		department = requestsDepartment;
		initialize();
	}

	@Override
	public String getName() {
		return "Pedidos";
	}

	@Override
	protected void addColumns(ReadOnlyTableModel<Request> tableModel) {
		tableModel.addColumn("Profesor", "professor", new ObjectToStringConverter() {
			@Override
			public String convert(Object obj) {
				return ((Professor)obj).getName();
			};
		});
		tableModel.addColumn("Materia", "subject", new ObjectToStringConverter() {
			@Override
			public String convert(Object obj) {
				return ((Subject)obj).getName();
			};
		});
		tableModel.addColumn("Asignado", "asignated", new ObjectToStringConverter() {
			@Override
			public String convert(Object obj) {
				return ((Boolean) obj)? "Sí" : "No";
			}
		});
	}

	@Override
	protected void createAddButtonListeners() {
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new NewRequestWindow(department);
			}
		});
	}

	@Override
	protected void createDeleteButtonListeners() {
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(new JFrame(),
						"¿Desea eliminar el pedido seleccionado?", "Eliminar",
				        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
				      department.deleteRequest(selection);
			}
		});
	}

	@Override
	protected void createModifyButtonListeners() {
		modifyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Abrir ventana de Modificar Pedido
			}
		});
	}

	@Override
	protected List<Request> getListModel() {
		return department.getRequests();
	}

	@Override
	protected String getSearchLabelText() {
		return "Búsqueda rápida por profesor";
	}

	@SuppressWarnings({ "serial", "unchecked" })
	@Override
	protected Component makeSearchField() {
		EasyComboBoxModel<Professor> comboModel = new EasyComboBoxModel<Professor>(getProfessors());
		final JComboBox<Professor> combo = new JComboBox<Professor>(comboModel);
		combo.setRenderer(new EasyComboBoxRenderer<Professor>() {
			@Override
			public String getDisplayName(Professor professor) {
				return professor.getName();
			}
		});
		combo.setPreferredSize(new Dimension(120, 20));
		combo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List<Request> search = department.searchByProfessor((Professor) combo.getSelectedItem());
				((ReadOnlyTableModel<Request>) table.getModel()).setModel(search);
			}
		});
		return combo;
	}

	private List<Professor> getProfessors() {
		return department.getProfessorsDepartment().getProfessors();
	}

	@Override
	protected void addOtherWidgetsToBottomPanel(JPanel bottomPanel) {
		requestDetail = new JButton("Detalle de Pedido");
		requestDetail.setEnabled(false);
		requestDetail.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new RequestViewWindow(department.getAssignmentsDepartment(), (ClassroomRequest) selection);
			}
		});
		bottomPanel.add(requestDetail);
	}

	@Override
	protected void whenTableSelectionChanged(ListSelectionEvent e) {
		super.whenTableSelectionChanged(e);
		requestDetail.setEnabled(selection != null);
	}

	@Override
	protected void registerAsSubscriber() {
		department.getPublisher().addSubscriber("requestsChanged", this);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void update(String aspect, Object value) {
		((ReadOnlyTableModel<Request>)table.getModel())
			.setModel(getListModel());
	}
}
