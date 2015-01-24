package ar.edu.unq.sasa.gui.util;

import javax.swing.*;

public final class WidgetUtilities {

	private WidgetUtilities() { }

	public static void toggleAll(boolean enable, JComponent...components) {
		for (JComponent comp : components)
			comp.setEnabled(enable);
	}

	public static void disableAll(JComponent...components) {
		toggleAll(false, components);
	}

	public static void enableAll(JComponent...components) {
		toggleAll(true, components);
	}
}
