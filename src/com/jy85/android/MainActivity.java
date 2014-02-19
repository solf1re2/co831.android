package com.jy85.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MainActivity extends Activity implements OnSeekBarChangeListener {

	private SeekBar bar;
	private TextView textPercentage, tipValue;
	private EditText billValue;
	private TextView displayPeople;

	private int tipPercentage = 0;
	private int tipAmount;
	private int billAmount = 0;
	private int people;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		people = 0;
		bar = (SeekBar) findViewById(R.id.seekBar1);
		bar.setOnSeekBarChangeListener(this);
		billValue = (EditText) findViewById(R.id.billValue);
		textPercentage = (TextView) findViewById(R.id.textViewPercentage);
		tipValue = (TextView) findViewById(R.id.textViewTipValue);
		displayPeople = (TextView) findViewById(R.id.peopleDisplay);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle b) {
		b.putInt("percentage", Integer.valueOf(textPercentage.toString()));
		super.onSaveInstanceState(b);
	}
	
	protected void onRestoreInstanceState(Bundle b) {
		super.onRestoreInstanceState(b);
	}
	
	/**
	 * Calculates the tip value based on the bill cost entered and percentage
	 * selected.
	 */
	public void onClickBtn(View v) {
		switch (v.getId()){
		case R.id.bAdd:
			
			people ++;
			displayPeople.setText(people+"");
			break;
		case R.id.bSub:
			if (people>2){
			people --;
			displayPeople.setText(people+"");
			} 
			break;
		}
	}//TODO on change of people/percentage/billValue; update the costPerPerson.

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		textPercentage.setText(progress + "%");
		tipPercentage = progress;
		billAmount = Integer.valueOf(billValue.getText().toString());
		tipAmount = billAmount*tipPercentage/100;
		tipValue.setText("£" + tipAmount);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		seekBar.setSecondaryProgress(seekBar.getProgress());
	}

}
