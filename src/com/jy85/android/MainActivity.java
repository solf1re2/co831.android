package com.jy85.android;

import java.text.NumberFormat;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements OnSeekBarChangeListener {

	private SeekBar bar;
	private TextView textPercentage, tipValue;
	private EditText billValue;
	private TextView displayPeople;
	private TextView totalCostView;
	private TextView costPerPerson;
	

	private double tipPercentage;
	private double tipAmount;
	private double billAmount;
	private double totalCost;
	private int people;
	private double costPP;
	private double costPPRound;
	private boolean rounderOn;
	

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
		rounderOn = false;
		bar = (SeekBar) findViewById(R.id.seekBar1);
		bar.setOnSeekBarChangeListener(this);
		billValue = (EditText) findViewById(R.id.billValue);
		billValue.addTextChangedListener(mTextEditorWatcher);
		
		//billValue.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
		textPercentage = (TextView) findViewById(R.id.textViewPercentage);
		tipValue = (TextView) findViewById(R.id.textViewTipValue);
		displayPeople = (TextView) findViewById(R.id.peopleDisplay);
		totalCostView = (TextView) findViewById(R.id.totalCostV);
 		costPerPerson = (TextView) findViewById(R.id.costPerPerson);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle b) {
		super.onSaveInstanceState(b);	
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle b) {
		super.onRestoreInstanceState(b);	
	}
	
	@Override
	protected void onPause() 
	{
	  super.onPause();

	  // Store values between instances here
	  SharedPreferences preferences = getPreferences(MODE_PRIVATE);
	  SharedPreferences.Editor editor = preferences.edit();  // Put the values from the UI
	  
	  // Commit to storage
	  editor.commit();
	}
	
	   private final TextWatcher  mTextEditorWatcher = new TextWatcher() {

		   public void beforeTextChanged(CharSequence s, int start, int count, int after)
		   {
			   // When No Password Entered
			   
		   }

		   public void onTextChanged(CharSequence s, int start, int before, int count)
		   {

		   }

		   public void afterTextChanged(Editable s)
		   {
			   billAmount = Double.valueOf(billValue.getText().toString());
			   calcTipAmount();
				calcTotalCost();
				calcCostPerPerson();
				setAllViews();
		   }
	   };	
	
	private void setAllViews() {
		//TODO set all TextView's, then call from all modifiers
		tipValue.setText("Tip: £" + dFormatter(tipAmount));
		displayPeople.setText(people+"");
		totalCostView.setText("Total Cost: £" + dFormatter(totalCost));
		if (rounderOn){
			costPerPerson.setText("Cost Each: £" + dFormatter(costPPRound));
		}else {
			costPerPerson.setText("Cost Each: £" + dFormatter(costPP));
		}
	}
	
	private void calcTipAmount() {
		tipAmount = billAmount*tipPercentage/100;
	}
	
	private void calcTotalCost() {
		totalCost = billAmount + tipAmount;
	}
	
	private void calcCostPerPerson() {
		costPP = totalCost / people;
		double pence = costPP%1;
		if (pence < 0.50) {
			//round down
			if (((costPP - pence) * people) > billAmount){
				costPPRound = costPP - pence;
			} else {
				costPPRound = costPP + (1 - pence);
			}
		} else {
			//round up
			costPPRound = costPP + (1 - pence);
		}
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
	
	public void onToggleClicked(View view) {
	    // Is the toggle on?
	    rounderOn = ((ToggleButton) view).isChecked();
	    setAllViews();
	}
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
		
		if (billAmount!=0) {
			billAmount = Double.valueOf(billValue.getText().toString());
			calcTipAmount();
			calcTotalCost();
			calcCostPerPerson();
			setAllViews();
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		seekBar.setSecondaryProgress(seekBar.getProgress());
	}
	
	private String dFormatter(double number) {
		NumberFormat format = NumberFormat.getNumberInstance();
		format.setMinimumFractionDigits(2);
		format.setMaximumFractionDigits(2);
		String output = format.format(number);
		return output;
	}
}
