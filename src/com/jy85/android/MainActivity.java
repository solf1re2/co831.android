package com.jy85.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MainActivity extends Activity implements OnSeekBarChangeListener {

	private SeekBar bar;
	private TextView textPercentage, tipValue;
	private EditText billValue;
	private Button calcButton;

	private int tipPercentage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		bar = (SeekBar) findViewById(R.id.seekBar1);
		bar.setOnSeekBarChangeListener(this);
		billValue = (EditText) findViewById(R.id.billValue);
		textPercentage = (TextView) findViewById(R.id.textViewPercentage);
		tipValue = (TextView) findViewById(R.id.textViewTipValue);
		calcButton = (Button) findViewById(R.id.calculateButton);
		calcButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				tipValue.setText(calcTip());
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int percentage,
			boolean fromUser) {
		textPercentage.setText(percentage + "%");
		tipPercentage = percentage;
	}

	/**
	 * Calculates the tip value based on the bill cost entered and percentage
	 * selected.
	 */
	private int calcTip() {
		int bill = Integer.valueOf(billValue.getText().toString());
		return tipPercentage / 100 * bill;
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		seekBar.setSecondaryProgress(seekBar.getProgress());
	}

}
