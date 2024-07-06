package com.kuba6000.mobsinfo.nei.scrollable;

import java.util.List;

import codechicken.nei.PositionedStack;

public interface IScrollableGUI {

    Scrollbar getScrollbar();

    List<PositionedStack> getAllItems(int recipe);
}
