package gis.iwacu_new.rit.edu.main.gps.map;

import android.app.Dialog;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import java.util.List;

public class RadioButtonDialog extends Dialog {

    private List<RadioButton> buttonList;
    private RadioGroup.OnCheckedChangeListener listener;
    private RadioGroup radioGroup;

    public RadioButtonDialog(Context context, List<RadioButton> buttons) {
        super(context);
        this.buttonList = buttons;
        this.listener = listener;
    }

    private void initialize() {
        setCanceledOnTouchOutside(true);
        setCancelable(true);

        final LinearLayout mainPanel = new LinearLayout(getContext());
        mainPanel.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT));
        mainPanel.setOrientation(LinearLayout.VERTICAL);

        ScrollView scrollPanel = new ScrollView(getContext());
        scrollPanel.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT));

        radioGroup = new RadioGroup(getContext());

        LinearLayout.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                RadioGroup.LayoutParams.WRAP_CONTENT,
                RadioGroup.LayoutParams.WRAP_CONTENT);

        int index = 0;
        for(RadioButton btn : buttonList) {
            radioGroup.addView(btn, index++, layoutParams);
        }

        mainPanel.addView(radioGroup);
        scrollPanel.addView(mainPanel);
        setContentView(scrollPanel);
    }

    public void setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener listener) {
        radioGroup.setOnCheckedChangeListener(listener);
    }

    public void setChecked(int index) {
        radioGroup.check(index);
    }
}
