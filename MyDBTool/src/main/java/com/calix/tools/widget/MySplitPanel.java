package com.calix.tools.widget;

import javax.swing.*;
import java.awt.*;

/**
 * Created by calix on 16-11-11
 *
 */
class MySplitPanel extends JSplitPane {

    MySplitPanel(int style, Component component1, Component component2) {
        super(style, component1, component2);
        setDividerSize(5);
        setBorder(BorderFactory.createLineBorder(null, 0));
    }
}
