package ar.edu.unq.sasa.gui.util;

public final class LabelHelpers {

    private LabelHelpers() {}

    public static String friendlyBoolean(Boolean booleanResult) {
        return booleanResult ? "Sí" : "No";
    }
}
