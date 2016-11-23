package com.calix.tools.widget;

import javax.swing.*;
import java.awt.*;

/**
 * Created by calix on 16-11-11
 *
 */
class TipPanel extends JScrollPane {

    private static TipPanel tipContent;
    private static JTextArea tipPanel;

    private TipPanel(JTextArea tip) {
        super(tip);
        tipPanel = tip;
        tip.setTabSize(4);
        tip.setMargin(new Insets(2,2,2,2));
    }

    static TipPanel getTipPanel() {
        if (tipContent == null) {
            tipContent = new TipPanel(new JTextArea());
        }
        return tipContent;
    }
}
