package com.android.Earthdroid;


import android.app.Activity;
import android.os.Bundle; 
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class EarthdroidActivity extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        View earthquakeButton = findViewById(R.id.earthquake_button);
        earthquakeButton.setOnClickListener(this);
        View aboutButton = findViewById(R.id.about_button);
        aboutButton.setOnClickListener(this);
    }
   
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
    	case R.id.earthquake_button:
    		Intent i = new Intent(this, Earthquake.class);
    		startActivity(i);
    		break;
	} 
	}
}