package ar.edu.unq.sasa.gui.util;

import javax.swing.JComponent;

public final class WidgetUtilities {

	private WidgetUtilities() { }

	// TODO change to enableAll / disableAll / toggleAll
	public static void enableOrDisableWidgets(boolean enable, JComponent...components) {
		for (JComponent comp : components)
			comp.setEnabled(enable);
	}
}
