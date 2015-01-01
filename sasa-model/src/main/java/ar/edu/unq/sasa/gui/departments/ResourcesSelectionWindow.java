package ar.edu.unq.sasa.gui.departments;

import static ar.edu.unq.sasa.gui.util.Dialogs.withConfirmation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import uic.layout.HorizontalLayout;
import uic.layout.VerticalLayout;
import ar.edu.unq.sasa.gui.util.Pair;
import ar.edu.unq.sasa.gui.util.ToStringConverter;
import ar.edu.unq.sasa.gui.util.combos.EasyComboBoxModel;
import ar.edu.unq.sasa.gui.util.combos.EasyComboBoxRenderer;
import ar.edu.unq.sasa.gui.util.tables.ReadOnlyTableModel;
import ar.edu.unq.sasa.model.departments.AssignmentsDepartment;
import ar.edu.unq.sasa.model.items.MobileResource;
import ar.edu.unq.sasa.model.items.Resource;

public class ResourcesSelectionWindow extends JFrame {

	private static final long serialVersionUID = -5739243783412143543L;

	private AsignateRequestWindow parentFrame;

	private List<Pair<Resource, Integer>> resourcesSelected = new ArrayList<Pair<Resource, Integer>>();
	private Pair<Resource, Integer> resourceSelection;
	private Resource resource;
	private Integer quantity;

	private JScrollPane resourcesScrollPane;
	private JTable resourcesTable;
	private JLabel obligatoryResourceLabel;
	private JLabel resourcesComboBoxLabel;
	private JComboBox<MobileResource> resourcesComboBox;
	private JLabel obligatoryQuantityLabel;
	private JLabel resourcesQuantityLabel;
	private JTextField resourcesQuantity;
	private JButton addResourceButton;
	private JButton removeResourceButton;
	private JButton viewDetailsButton;
	private JButton acceptButton;
	private JButton cancelButton;

	private AssignmentsDepartment department;

	public ResourcesSelectionWindow(AssignmentsDepartment assignmentsDepartment, AsignateRequestWindow aParentFrame) {
		department = assignmentsDepartment;
		parentFrame = aParentFrame;
		resourcesSelected.addAll(aParentFrame.getResourcesSelection());

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				createResourceCreationFields();
				createResourcesTable();
				createButtons();
				organizeWidgets();

				setResizable(true);
				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				setTitle("Elegir Recursos");
				setSize(525, 250);
				setLocationRelativeTo(null);
				setVisible(true);
			}
		});
	}

	private void organizeWidgets() {
		JPanel mainPanel = new JPanel();
		getContentPane().add(mainPanel);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		JPanel topMain = new JPanel();
		topMain.setLayout(new HorizontalLayout());
		JPanel bottomMain = new JPanel();
		bottomMain.setLayout(new FlowLayout());

		JPanel leftTopPanel = new JPanel();
		leftTopPanel.setLayout(new VerticalLayout());
		JPanel rightTopPanel = new JPanel();
		rightTopPanel.setBorder(BorderFactory.createTitledBorder("Adición de recursos"));
		rightTopPanel.setLayout(new VerticalLayout());

		JPanel addAndRemoveButtonsPanel = new JPanel();
		addAndRemoveButtonsPanel.setLayout(new FlowLayout());
		JPanel acceptAndCancelPanel = new JPanel();
		acceptAndCancelPanel.setLayout(new FlowLayout());

		addAndRemoveButtonsPanel.add(addResourceButton);
		addAndRemoveButtonsPanel.add(removeResourceButton);

		acceptAndCancelPanel.add(acceptButton);
		acceptAndCancelPanel.add(cancelButton);

		JPanel resourceSelectionPanel = new JPanel();
		resourceSelectionPanel.setLayout(new BoxLayout(resourceSelectionPanel, BoxLayout.X_AXIS));
		JPanel quantitySelectionPanel = new JPanel();
		quantitySelectionPanel.setLayout(new FlowLayout());

		resourceSelectionPanel.add(obligatoryResourceLabel);
		resourceSelectionPanel.add(resourcesComboBoxLabel);
		resourceSelectionPanel.add(resourcesComboBox);

		quantitySelectionPanel.add(obligatoryQuantityLabel);
		quantitySelectionPanel.add(resourcesQuantityLabel);
		quantitySelectionPanel.add(resourcesQuantity);

		mainPanel.add(topMain);
		mainPanel.add(bottomMain);

		topMain.add(leftTopPanel);
		topMain.add(rightTopPanel);
		bottomMain.add(acceptAndCancelPanel);

		resourcesScrollPane.setBorder(BorderFactory.createTitledBorder("Recursos Deseados"));
		leftTopPanel.add(resourcesScrollPane);

		JPanel viewDetailsPanel = new JPanel();
		viewDetailsPanel.setLayout(new FlowLayout());
		viewDetailsPanel.add(viewDetailsButton);

		rightTopPanel.add(resourceSelectionPanel);
		rightTopPanel.add(new JPanel().add(new JLabel(" ")));
		rightTopPanel.add(quantitySelectionPanel);
		rightTopPanel.add(new JPanel().add(new JLabel(" ")));
		rightTopPanel.add(addAndRemoveButtonsPanel);
		rightTopPanel.add(viewDetailsPanel);
	}

	private void createResourceCreationFields() {
		obligatoryResourceLabel = new JLabel("* ");
		obligatoryResourceLabel.setForeground(Color.RED);
		resourcesComboBoxLabel = new JLabel("Seleccione un Recurso ");
		resourcesQuantityLabel = new JLabel("Indique la Cantidad");
		obligatoryQuantityLabel = new JLabel("*");
		obligatoryQuantityLabel.setForeground(Color.RED);
		resourcesComboBox = makeResourcesComboBox();
		resourcesQuantity = makeQuantitySearchField();
	}

	private JTextField makeQuantitySearchField() {
		JTextField field = new JTextField(10);
		field.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent anEvent) {
				String number = ((JTextField) anEvent.getSource()).getText();
				if (!number.equals("")) {
					if (quantity != null) {
						addResourceButton.setEnabled(false);
						quantity = null;
					}
					quantity = Integer.parseInt(number);
					if (resource != null)
						addResourceButton.setEnabled(true);
				} else {
					addResourceButton.setEnabled(false);
					quantity = null;
				}
			}
		});
		return field;
	}

	@SuppressWarnings({ "unchecked", "serial" })
	private JComboBox<MobileResource> makeResourcesComboBox() {
		EasyComboBoxModel<MobileResource> comboModel = new EasyComboBoxModel<MobileResource>(getResources());
		JComboBox<MobileResource> combo = new JComboBox<MobileResource>(comboModel);
		combo.setRenderer(new EasyComboBoxRenderer<MobileResource>() {
			// TODO there are a lot of these inner classes, we can build an EasyComboBoxRenderer
			// who asks for getName to its inner objects
			@Override
			public String getDisplayName(MobileResource mobileResource) {
				return mobileResource.getName();
			}
		});
		combo.addActionListener(anEvent -> {
	    	// TODO refactor me!
	    	if (!(((Resource) ((JComboBox<Resource>) anEvent.getSource()).getSelectedItem()) == null)) {
    			if (!isInResourcesList((((Resource) ((JComboBox<Resource>) anEvent.getSource()).getSelectedItem())))) {
	    			resource = (Resource) ((JComboBox<Resource>) anEvent.getSource()).getSelectedItem();
	    			if (quantity != null)
						addResourceButton.setEnabled(true);
    			} else {
    				addResourceButton.setEnabled(false);
    				resource = null;
    			}
	    	} else {
    			addResourceButton.setEnabled(false);
    			resource = null;
    		}
		});

		combo.setPreferredSize(new Dimension(120, 20));
		return combo;
	}

	private List<MobileResource> getResources() {
		List<MobileResource> resources = new ArrayList<MobileResource>();
		for (MobileResource mobileResource : department.getResourcesDepartment().getMobileResources())
			if (!resources.contains(mobileResource))
				resources.add(mobileResource);
		return resources;
	}

	private boolean isInResourcesList(Resource aResource) {
		for (Pair<Resource, Integer> pair : resourcesSelected)
			if (((Resource) pair.getFirst()).getName().equals(aResource.getName()))
				return true;
		return false;
	}

	private void createButtons() {
		addResourceButton = new JButton("Agregar Recurso");
		createAddResourceButtonListeners();
		removeResourceButton = new JButton("Eliminar recurso");
		createRemoveResourceButtonListeners();
		acceptButton = new JButton("Aceptar");
		createAcceptButtonListeners();
		cancelButton = new JButton("Cancelar");
		createCancelButtonListeners();
		viewDetailsButton = new JButton("Detalle del Pedido");
		createViewDetailsButtonListeners();
		removeResourceButton.setEnabled(false);
		addResourceButton.setEnabled(false);
	}

	private void createViewDetailsButtonListeners() {
		viewDetailsButton.addActionListener(anEvent -> {
			new RequestViewWindow(department, parentFrame.getParentPanel().getRequestSelection());
		});
	}

	private void createAddResourceButtonListeners() {
		addResourceButton.addActionListener(anEvent -> {
			resourcesSelected.add(new Pair<Resource, Integer>(resource, quantity));
			resource = null;
			quantity = null;
			resourcesComboBox.setSelectedItem(null);
			resourcesComboBox.updateUI();
			resourcesQuantity.setText("");
			addResourceButton.setEnabled(false);
			updateTable();
		});
	}

	@SuppressWarnings("unchecked")
	private void updateTable() {
		((ReadOnlyTableModel<Pair<Resource, Integer>>) resourcesTable.getModel()).setModel(resourcesSelected);
	}

	private void createRemoveResourceButtonListeners() {
		removeResourceButton.addActionListener(anEvent -> {
			withConfirmation("Eliminar", "¿Desea eliminar el recurso seleccionado?", () -> {
				resourcesSelected.remove(resourceSelection);
				resourcesComboBox.setSelectedItem(null);
				resourcesComboBox.updateUI();
				resourcesQuantity.setText("");
				addResourceButton.setEnabled(false);
				updateTable();
			});
		});
	}

	private void createAcceptButtonListeners() {
		acceptButton.addActionListener(anEvent -> {
			withConfirmation("Aceptar", "¿Desea guardar los cambios?", () -> {
				parentFrame.setResourcesSelection(resourcesSelected);
				parentFrame.validateButtons();
				parentFrame.updateResourcesTable();
				dispose();
			});
		});
	}

	private void createCancelButtonListeners() {
		cancelButton.addActionListener(anEvent -> {
			withConfirmation("Cancelar", "¿Desea cancelar los cambios?", () -> { dispose(); });
		});
	}

	private void createResourcesTable() {
		ReadOnlyTableModel<Pair<Resource, Integer>> tableModel =
				new ReadOnlyTableModel<Pair<Resource, Integer>>(resourcesSelected);
		addResourcesColumns(tableModel);
		resourcesTable = new JTable(tableModel);
		resourcesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		resourcesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent anEvent) {
				ResourcesSelectionWindow.this.whenResourcesTableSelectionChanged(anEvent);
			}
		});
		resourcesScrollPane = new JScrollPane(resourcesTable);
	}

	@SuppressWarnings("unchecked")
	protected void whenResourcesTableSelectionChanged(ListSelectionEvent anEvent) {
		DefaultListSelectionModel source = (DefaultListSelectionModel) anEvent.getSource();
		if (source.isSelectionEmpty()) {
			resourceSelection = null;
			removeResourceButton.setEnabled(false);
		} else {
			int index = source.getMinSelectionIndex();
			List<Pair<Resource, Integer>> model =
					((ReadOnlyTableModel<Pair<Resource, Integer>>) resourcesTable.getModel()).getModel();
			resourceSelection = model.get(index);
			removeResourceButton.setEnabled(true);
		}
	}

	private void addResourcesColumns(ReadOnlyTableModel<Pair<Resource, Integer>> tableModel) {
		tableModel.addColumn("Recurso", "first", new ToStringConverter<Resource>() {
			@Override
			public String convert(Resource aResource) {
				return aResource.getName();
			};
		});
		tableModel.addColumn("Cantidad", "second", new ToStringConverter<Integer>() {
			@Override
			public String convert(Integer anAmount) {
				return anAmount.toString();
			};
		});
	}
}