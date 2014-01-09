package edu.uaic.fii.wad.edec.listener;

import edu.uaic.fii.wad.edec.view.ObservableScrollView;

public interface ScrollViewListener {

    void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldX, int oldY);

}