package ar.edu.unq.sasa.gui.departments;

import static ar.edu.unq.sasa.gui.util.Dialogs.withConfirmation;

import java.awt.Component;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;

import ar.edu.unq.sasa.gui.util.ToStringConverter;
import ar.edu.unq.sasa.gui.util.combos.EasyComboBoxModel;
import ar.edu.unq.sasa.gui.util.combos.EasyComboBoxRenderer;
import ar.edu.unq.sasa.gui.util.tables.ReadOnlyTableModel;
import ar.edu.unq.sasa.model.requests.ClassroomRequest;
import ar.edu.unq.sasa.model.academic.Professor;
import ar.edu.unq.sasa.model.requests.Request;
import ar.edu.unq.sasa.model.academic.Subject;
import ar.edu.unq.sasa.model.departments.RequestsDepartment;

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
		tableModel.addColumn("Profesor", "professor", new ToStringConverter<Professor>() {
			@Override
			public String convert(Professor aProfessor) {
				return aProfessor.getName();
			};
		});
		tableModel.addColumn("Materia", "subject", new ToStringConverter<Subject>() {
			@Override
			public String convert(Subject aSubject) {
				return aSubject.getName();
			};
		});
		tableModel.addColumn("Asignado", "assigned", new ToStringConverter<Boolean>() {
			@Override
			public String convert(Boolean aBoolean) {
				return aBoolean ? "Sí" : "No";
			}
		});
	}

	@Override
	protected void createAddButtonListeners() {
		addButton.addActionListener(anEvent -> new NewRequestWindow(department));
	}

	@Override
	protected void createDeleteButtonListeners() {
		deleteButton.addActionListener(anEvent ->
				withConfirmation("Eliminar", "¿Desea eliminar el pedido seleccionado?", () ->
						department.deleteRequest(selection)));
	}

	@Override
	protected void createModifyButtonListeners() {
		modifyButton.addActionListener(anEvent -> {
			// TODO Abrir ventana de Modificar Pedido
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

	@Override
	@SuppressWarnings({ "serial", "unchecked" })
	protected Component makeSearchField() {
		EasyComboBoxModel<Professor> comboModel = new EasyComboBoxModel<>(getProfessors());
		final JComboBox<Professor> combo = new JComboBox<>(comboModel);
		combo.setRenderer(new EasyComboBoxRenderer<Professor>() {
			// TODO implement with lambdas
			@Override
			public String getDisplayName(Professor professor) {
				return professor.getName();
			}
		});
		combo.setPreferredSize(new Dimension(120, 20));
		combo.addActionListener(anEvent -> {
			List<Request> search = department.searchByProfessor((Professor) combo.getSelectedItem());
			((ReadOnlyTableModel<Request>) table.getModel()).setModel(search);
		});
		return combo;
	}

	private List<Professor> getProfessors() {
		return department.getProfessorsDepartment().getProfessors();
	}

	@Override
	protected void addOtherWidgetsToBottomPanel(JPanel bottomPanel) {
		requestDetail = new JButton("Detalle de pedido");
		requestDetail.setEnabled(false);
		requestDetail.addActionListener(anEvent ->
				new RequestViewWindow(department.getAssignmentsDepartment(), (ClassroomRequest) selection));
		bottomPanel.add(requestDetail);
	}

	@Override
	protected void whenTableSelectionChanged(ListSelectionEvent anEvent) {
		super.whenTableSelectionChanged(anEvent);
		requestDetail.setEnabled(selection != null);
	}

	@Override
	protected void registerAsSubscriber() {
		department.getPublisher().addSubscriber("requestsChanged", this);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void update(String aspect, Object value) {
		((ReadOnlyTableModel<Request>) table.getModel()).setModel(getListModel());
	}
}
