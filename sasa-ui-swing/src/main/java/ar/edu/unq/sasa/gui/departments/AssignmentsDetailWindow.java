package ar.edu.unq.sasa.gui.departments;

import ar.edu.unq.sasa.gui.util.ToStringConverter;
import ar.edu.unq.sasa.gui.util.tables.ReadOnlyTableModel;
import ar.edu.unq.sasa.model.requests.Request;
import ar.edu.unq.sasa.model.assignments.AssignmentByRequest;
import ar.edu.unq.sasa.model.items.AssignableItem;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.util.List;

public abstract class AssignmentsDetailWindow<A extends AssignableItem, B extends AssignmentByRequest> extends JFrame {

    private static final long serialVersionUID = 4835555392263678407L;

    protected A assignableItem;
    protected JTable assignmentsTable;
    protected JTextArea periodDetail;

    public AssignmentsDetailWindow(final A anAssignableItem) {
        SwingUtilities.invokeLater(() -> {
            assignableItem = anAssignableItem;
            createWidgets();
            addWidgets();

            setTitle("Detalle de asignaciones");
            setSize(new Dimension(getWindowWidth(), getWindowHeight()));
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setResizable(false);
            setLocationRelativeTo(null);
            setVisible(true);
        });
    }

    protected int getWindowWidth() {
        return 500;
    }

    protected int getWindowHeight() {
        return 500;
    }

    protected void createWidgets() {
        ReadOnlyTableModel<B> model = new ReadOnlyTableModel<>(getAssignments());
        model.addColumn("Profesor", "request", new ToStringConverter<Request>() {
            @Override
            public String convert(Request aRequest) {
                return aRequest.getProfessor().getName();
            }
        });
        model.addColumn("Materia", "request", new ToStringConverter<Request>() {
            @Override
            public String convert(Request aRequest) {
                return aRequest.getSubject().getName();
            }
        });
        assignmentsTable = new JTable(model);
        assignmentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        assignmentsTable.getSelectionModel().addListSelectionListener(
                AssignmentsDetailWindow.this::whenAssignmentsTableSelectionChanged);
        periodDetail = new JTextArea();
        periodDetail.setPreferredSize(new Dimension(480, 100));
        periodDetail.setEditable(false);
        createOtherWidgets();
    }

    protected abstract void whenAssignmentsTableSelectionChanged(ListSelectionEvent anEvent);

    protected abstract void createOtherWidgets();

    protected abstract void addWidgets();

    protected abstract List<B> getAssignments();
}
