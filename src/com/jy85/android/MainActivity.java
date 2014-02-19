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
	private TextView totalCostView;
	private TextView costPerPerson;
	

	private int tipPercentage;
	private int tipAmount;
	private int billAmount;
	private int totalCost;
	private int people;
	private int costPP;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		people = 1;
		billAmount = 0;
		tipPercentage = 0;
		tipAmount = 0;
		totalCost = 0;
		costPP = 0;
		bar = (SeekBar) findViewById(R.id.seekBar1);
		bar.setOnSeekBarChangeListener(this);
		billValue = (EditText) findViewById(R.id.billValue);
		textPercentage = (TextView) findViewById(R.id.textViewPercentage);
		tipValue = (TextView) findViewById(R.id.textViewTipValue);
		displayPeople = (TextView) findViewById(R.id.peopleDisplay);
		totalCostView = (TextView) findViewById(R.id.totalCostV);
 		costPerPerson = (TextView) findViewById(R.id.costPerPerson);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle b) {
		b.putInt("percentage", Integer.valueOf(textPercentage.toString()));
		super.onSaveInstanceState(b);
	}
	
	protected void onRestoreInstanceState(Bundle b) {
		super.onRestoreInstanceState(b);
	}
	
	private void setAllViews() {
		//TODO set all TextView's, then call from all modifiers
		tipValue.setText("£" + tipAmount);
		displayPeople.setText(people+"");
		totalCostView.setText("£" + totalCost);
		costPerPerson.setText("£" + costPP);
	}
	
	private void calcTipAmount() {
		tipAmount = billAmount*tipPercentage/100;
	}
	
	private void calcTotalCost() {
		totalCost = billAmount + tipAmount;
	}
	
	private void calcCostPerPerson() {
		costPP = totalCost / people;
	}
	/**
	 * Calculates the tip value based on the bill cost entered and percentage
	 * selected.
	 */
	public void onClickBtn(View v) {
		switch (v.getId()){
		case R.id.bAdd:
			people ++;
			calcTotalCost();
			calcCostPerPerson();
			setAllViews();
			break;
		case R.id.bSub:
			if (people>=2){
			people --;
			calcTotalCost();
			calcCostPerPerson();
			setAllViews();
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
		calcTipAmount();
		calcTotalCost();
		calcCostPerPerson();
		setAllViews();
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		seekBar.setSecondaryProgress(seekBar.getProgress());
	}

}
