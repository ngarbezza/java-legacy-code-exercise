package ar.edu.unq.sasa.gui.util;

import javax.swing.*;

public final class Dialogs {

	private Dialogs() { }

	public static void withConfirmation(String windowTitle, String message, Runnable aBlock) {
		if (JOptionPane.showConfirmDialog(new JFrame(),	message, windowTitle,
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
			aBlock.run();
	}
}
