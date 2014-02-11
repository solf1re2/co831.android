
package com.jy85.android;



import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MainActivity extends Activity implements OnSeekBarChangeListener{

	private SeekBar bar;
	private TextView textPercentage;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bar = (SeekBar)findViewById(R.id.seekBar1);
        bar.setOnSeekBarChangeListener(this);
        
        textPercentage = (TextView)findViewById(R.id.textViewPercentage);
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public void onProgressChanged(SeekBar seekBar, int percentage, boolean fromUser) {
    	textPercentage.setText(percentage + "%");
    }
    
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    	
    }
    
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    	seekBar.setSecondaryProgress(seekBar.getProgress());
    }
    
}
