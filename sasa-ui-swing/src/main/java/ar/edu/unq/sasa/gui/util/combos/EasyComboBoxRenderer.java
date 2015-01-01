package ar.edu.unq.sasa.gui.util.combos;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

/**
 * Renderer para ComboBox que da la posibilidad de especificar la forma en que
 * un Object se va a mostrar (por defecto usando toString()).
 */
public class EasyComboBoxRenderer<T> extends BasicComboBoxRenderer {

	private static final long serialVersionUID = 5535038578103596283L;

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {

		// redefinido de vuelta casi todo, porque está mal programado
		// (no diseñado para redefinir) en BasicComboBoxRenderer

		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}

		setFont(list.getFont());

		if (value instanceof Icon)
			setIcon((Icon) value);
		else
			setText((value == null) ? "" : this.getDisplayName((T) value));
		return this;
	}

	protected String getDisplayName(T value) {
		return value.toString();
	}
}
