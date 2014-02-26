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
	

	private int tipPercentage;
	private int tipAmount;
	private int billAmount;
	private int totalCost;
	private int people;
	private int costPP;
	private int costPPRound;
	private boolean rounderOn;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences preferences = getPreferences(MODE_PRIVATE);
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
//		if(preferences.contains("percentage")) {
//			bar.setProgress(preferences.getInt("percentage",0));
//		}
		billValue = (EditText) findViewById(R.id.billValue);
		billValue.addTextChangedListener(mTextEditorWatcher);
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
	  editor.putInt("percentage", tipPercentage);
	  // Commit to storage
	  editor.commit();
	}
	
	   private final TextWatcher  mTextEditorWatcher = new TextWatcher() {

		   public void beforeTextChanged(CharSequence s, int start, int count, int after)
		   {
		   }

		   public void onTextChanged(CharSequence s, int start, int before, int count)
		   {
		   }

		   public void afterTextChanged(Editable s)
		   {
			   double bill = Double.valueOf(billValue.getText().toString());
			   billAmount = (int) (100*bill);
//			   calcTipAmount();
//			   calcTotalCost();
//			   calcCostPerPerson();
//			   calcRoundCostPP();
			   setAllViews();
		   }
	   };	
	
	private void setAllViews() {
		tipValue.setText("Tip: £" + dFormatter(calcTipAmount()));
		displayPeople.setText(people+"");
		totalCostView.setText("Total Cost: £" + dFormatter(calcTotalCost()));
		if (rounderOn){
			costPerPerson.setText("Cost Each: £" + dFormatter(calcRoundCostPP()));
		}else {
			costPerPerson.setText("Cost Each: £" + dFormatter(calcCostPerPerson()));
		}
	}
	
	private double calcTipAmount() {
		tipAmount = billAmount*tipPercentage/100;
		double tipAmountD = (double)tipAmount/100;
		System.out.println("tip = " +tipAmount);//variable checks
		return tipAmountD;
	}
	
	private double calcTotalCost() {
		totalCost = (billAmount + tipAmount);
		double totalCostD = (double)totalCost/100;
		System.out.println("total = " + totalCost);//variable checks
		return totalCostD;
	}
	
	private double calcCostPerPerson() {
		costPP = totalCost / people;
		double costPPD = (double)costPP/100;
		System.out.println("CostPP = " + costPP);//variable checks
		return costPPD;
	}
	
	//TODO switch statement- >100 round to 10; >500 round 50; <1000 round to 1000.
	private double calcRoundCostPP() {
		int pence = costPP%100;
		double costPPRoundD;
		System.out.println("pence = " + pence); //variable checks
		if (pence < 50) {
			//round down
			if (((costPP - pence) * people) > billAmount){
				costPPRound = costPP - pence;
				costPPRoundD = costPPRound/100;
			} else {
				costPPRound = costPP + (100 - pence);
				costPPRoundD = costPPRound/100;
			}
		} else {
			//round up
			costPPRound = costPP + (100 - pence);
			costPPRoundD = costPPRound/100;
		}
		return costPPRoundD;
	}
	
	/**
	 * Calculates the tip value based on the bill cost entered and percentage
	 * selected.
	 */
	public void onClickBtn(View v) {
		switch (v.getId()){
		case R.id.bAdd:
			people ++;
//			calcTotalCost();
//			calcCostPerPerson();
//			calcRoundCostPP();
			setAllViews();
			break;
		case R.id.bSub:
			if (people>=2){
			people --;
//			calcTotalCost();
//			calcCostPerPerson();
//			calcRoundCostPP();
			setAllViews();
			} 
			break;
		}
	}
	
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
		
//		if (billAmount!=0) {
//			billAmount = Integer.valueOf(billValue.getText().toString());
//			calcTipAmount();
//			calcTotalCost();
//			calcCostPerPerson();
//			calcRoundCostPP();
			setAllViews();
//		}
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
