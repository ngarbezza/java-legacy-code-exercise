package ar.edu.unq.sasa.gui.util;

import javax.swing.JComponent;

public class WidgetUtilities {
	
	public static void enableOrDisableWidgets(boolean enable, JComponent...components) {
		for (JComponent comp : components)
			comp.setEnabled(enable);
	}
}
