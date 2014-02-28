package com.jy85.android;

import java.text.NumberFormat;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.text.Spanned;

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
		setContentView(R.layout.activity_main);
		people = 1;
		billAmount = 0;
		tipPercentage = 10;
		tipAmount = 0;
		totalCost = 0;
		costPP = 0;
		rounderOn = false;
		
		bar = (SeekBar) findViewById(R.id.seekBar1);
		bar.setOnSeekBarChangeListener(this);
		billValue = (EditText) findViewById(R.id.billValue);
		billValue.addTextChangedListener(mTextEditorWatcher);
		textPercentage = (TextView) findViewById(R.id.textViewPercentage);
		tipValue = (TextView) findViewById(R.id.textViewTipValue);
		displayPeople = (TextView) findViewById(R.id.peopleDisplay);
		totalCostView = (TextView) findViewById(R.id.totalCostV);
		costPerPerson = (TextView) findViewById(R.id.costPerPerson);

		SharedPreferences settings = getSharedPreferences("Tipster", 0);
		tipPercentage = settings.getInt("percentage", 0);
		bar.setProgress((int)tipPercentage);
		textPercentage.setText("Percentage Tip " + tipPercentage + " %");

		//input filter obtained from gist.github.com/gaara87/3607765
		billValue.setFilters(new InputFilter[] {
				new DigitsKeyListener(Boolean.FALSE, Boolean.TRUE) {
					int beforeDecimal = 6, afterDecimal = 2;

					@Override
					public CharSequence filter(CharSequence source, int start, int end,
							Spanned dest, int dstart, int dend) {
						String temp = billValue.getText() + source.toString();

						if (temp.equals(".")) {
							return "0.";
						}
						else if (temp.toString().indexOf(".") == -1) {
							// no decimal point placed yet
							if (temp.length() > beforeDecimal) {
								return "";
							}
						} else {
							temp = temp.substring(temp.indexOf(".") + 1);
							if (temp.length() > afterDecimal) {
								return "";
							}
						}
						return super.filter(source, start, end, dest, dstart, dend);
					}
				}
		});
		
	}

	@Override
	protected void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);	
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

	}

	@Override
	protected void onPause() {
		super.onPause();
		SharedPreferences settings = getSharedPreferences("Tipster", 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("percentage", tipPercentage);
		editor.commit();
	}

	protected void onStop() {
		super.onStop();
		SharedPreferences settings = getSharedPreferences("Tister", 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putFloat("percentage", tipPercentage);
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
		calcCostPerPerson();
		double costPPRoundD = 0;
		if(costPP < 5000) {
			int pence = costPP%100;
			System.out.println("pence = " + pence); //variable checks
			if (pence < 50) {
				//round down
				if (((costPP - pence) * people) > billAmount){
					costPPRound = costPP - pence;
					costPPRoundD = (double)costPPRound/100;
				} else {
					costPPRound = costPP + (100 - pence);
					costPPRoundD = (double)costPPRound/100;
				}
			} else {
				//round up
				costPPRound = costPP + (100 - pence);
				costPPRoundD = (double)costPPRound/100;
			}
		}
		if (5000 <= costPP && costPP < 10000) {
			int pounds = costPP%500;
			System.out.println("pounds = " + pounds); //variable checks
			if (pounds < 250) {
				//round down
				if (((costPP - pounds) * people) > billAmount){
					costPPRound = costPP - pounds;
					costPPRoundD = (double)costPPRound/100;
					System.out.println("rounding 1");
				} else {
					costPPRound = costPP + (500 - pounds);
					costPPRoundD = (double)costPPRound/100;
					System.out.println("rounding 2");
				}
			} else {
				//round up
				costPPRound = costPP + (500 - pounds);
				costPPRoundD = (double)costPPRound/100;
				System.out.println("rounding 3");
			}
		}
		if ( costPP > 10000) {
			int pounds = costPP%1000;
			System.out.println("pounds = " + pounds); //variable checks
			if (pounds < 500) {
				//round down
				if (((costPP - pounds) * people) > billAmount){
					costPPRound = costPP - pounds;
					costPPRoundD = (double)costPPRound/100;
					System.out.println("rounding 1");
				} else {
					costPPRound = costPP + (1000 - pounds);
					costPPRoundD = (double)costPPRound/100;
					System.out.println("rounding 2");
				}
			} else {
				//round up
				costPPRound = costPP + (1000 - pounds);
				costPPRoundD = (double)costPPRound/100;
				System.out.println("rounding 3");
			}
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
			setAllViews();
			break;
		case R.id.bSub:
			if (people>=2){
				people --;
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
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		tipPercentage = progress;
		textPercentage.setText("Percentage Tip " + tipPercentage + "%");
		setAllViews();
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
