package gis.iwacu_new.rit.edu.main.gps.map;

import android.app.Dialog;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import gis.iwacu_new.rit.edu.main.R;

/**
 * Created by bindernews on 5/6/2015.
 */
public class MapMagnificationDialog extends Dialog {

    private BigPlanet planet;

    public MapMagnificationDialog(BigPlanet planet) {
        super(planet);
        this.planet = planet;
        initialize();
    }

    private void initialize() {
        setCanceledOnTouchOutside(true);
        setCancelable(true);
        setTitle(R.string.MAP_MAGNIFICATION_MENU);

        final LinearLayout mainPanel = new LinearLayout(getContext());
        mainPanel.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT));
        mainPanel.setOrientation(LinearLayout.VERTICAL);

        RadioGroup levelsRadioGroup = new RadioGroup(getContext());

        LinearLayout.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                RadioGroup.LayoutParams.WRAP_CONTENT,
                RadioGroup.LayoutParams.WRAP_CONTENT);

        final CharSequence[] arrayMapMagnification = getContext().getResources().getTextArray(R.array.MapMagnification);
        for (int i = arrayMapMagnification.length-1; i >= 0; i--) {
            RadioButton btn = new RadioButton(getContext());
            btn.setText(arrayMapMagnification[i].toString());
            btn.setId(i);
            levelsRadioGroup.addView(btn, 0, layoutParams);
        }

        int mapMagnificationIndex = Preferences.getMapMagnificationIndex();
        levelsRadioGroup.check(mapMagnificationIndex);

        levelsRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int mapMagnificationIndex = checkedId;
                Preferences.putMapMagnificationIndex(mapMagnificationIndex);
                planet.setMapMagnification(Float.parseFloat(arrayMapMagnification[mapMagnificationIndex].toString()));
                dismiss();
            }
        });

        mainPanel.addView(levelsRadioGroup);
        setContentView(mainPanel);
    }
}
