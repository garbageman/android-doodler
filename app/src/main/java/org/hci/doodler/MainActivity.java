package org.hci.doodler;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SeekBar toolTipSize = null;
    private SeekBar opacity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Set the tool tip size seek bar */

        toolTipSize = (SeekBar) findViewById(R.id.toolTipSize);

        toolTipSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChanged = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                /* Set the new value of the toolTipSize */
                DoodleView doodleView = (DoodleView) findViewById(R.id.doodleView);
                doodleView.setToolTipSize(progressChanged);
            }
        });

        /* Create the opacity seekbar */

        opacity = (SeekBar) findViewById(R.id.opacity);

        opacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChanged = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                /* Set the new value of the toolTipSize */
                DoodleView doodleView = (DoodleView) findViewById(R.id.doodleView);
                doodleView.setToolTipOpacity(progressChanged);
            }
        });

        LinearLayout toolboxGrid = (LinearLayout) findViewById(R.id.buttonLayout);

        ButtonClickHandler buttonClickHandler = new ButtonClickHandler();

        for (int i = 0; i < toolboxGrid.getChildCount(); i++) {
            View v = toolboxGrid.getChildAt(i);
            if (v instanceof Button) {
                v.setOnClickListener(buttonClickHandler);
            }
        }

        /* Add the clear screen button to the click listener */
        Button clearScreen = (Button) findViewById(R.id.clearButton);

        clearScreen.setOnClickListener(buttonClickHandler);

        /* Set TextView Values */
        TextView widthLabel = (TextView) findViewById(R.id.toolTipSizeLabel);
        widthLabel.setText("Tooltip Size: Small -> Large");

        TextView opacityLabel = (TextView) findViewById(R.id.opacityLabel);
        opacityLabel.setText("Tooltip Opacity: Clear -> Solid");
    }

    private class ButtonClickHandler implements View.OnClickListener {

        /**
         *  This will handle the buttons being clicked to change the color of the app or clear the screen
         * @param v
         */
        @Override
        public void onClick(View v) {
            DoodleView doodleView = (DoodleView) findViewById(R.id.doodleView);
            doodleView.updateColor(((Button) v).getText().toString());
        }
    }
}
