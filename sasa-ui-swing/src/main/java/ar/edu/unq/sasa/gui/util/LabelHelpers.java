package ar.edu.unq.sasa.gui.util;

import javax.swing.*;
import java.awt.*;

public final class LabelHelpers {

    private LabelHelpers() {}

    public static String friendlyBoolean(Boolean booleanResult) {
        return booleanResult ? "Sí" : "No";
    }

    public static JLabel requiredRedStar() {
        JLabel label = new JLabel("*");
        label.setForeground(Color.RED);
        return label;
    }
}
