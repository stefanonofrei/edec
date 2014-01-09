package edu.uaic.fii.wad.edec.listener;

import android.view.View;
import android.widget.TextView;

public class RuleOnClickListener implements View.OnClickListener {

    public int ruleIndex;
    public TextView ruleView;

    public RuleOnClickListener(int index, TextView ruleView) {
        this.ruleIndex = index;
        this.ruleView = ruleView;
    }
    @Override
    public void onClick(View view) {

    }
}
