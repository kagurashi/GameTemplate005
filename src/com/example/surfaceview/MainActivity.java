package com.example.surfaceview;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

public class MainActivity extends Activity 
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		final TextView textView = (TextView) findViewById(R.id.mainTextView1);
        Button buttonCircle = (Button) findViewById(R.id.buttonCircle);
		
		
		buttonCircle.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					textView.setText("サークル");
					Intent selectIntent = new Intent();
					selectIntent.setClassName("com.example.surfaceview", "com.example.surfaceview.Circle");
					startActivity(selectIntent);
				}
			});

		buttonCircle.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View p1)
				{
					// TODO: Implement this method
					textView.setText("サークル");
					
					
					
					return true;
				}
			});
			
			
			
		Button buttonTobineko = (Button) findViewById(R.id.buttonTobineko);

		buttonTobineko.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					textView.setText("飛び猫");
					Intent selectIntent = new Intent();
					selectIntent.setClassName("com.example.surfaceview", "com.example.surfaceview.TitleActivity");
					startActivity(selectIntent);
				}
			});

		buttonTobineko.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View p1)
				{
					// TODO: Implement this method
					textView.setText("飛び猫");
					


					return true;
				}
			}
		);
			
		
			
			
		Button buttonWorldMap = (Button) findViewById(R.id.buttonWorldMap);
			
			
		buttonWorldMap.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					textView.setText("WorldMap");
					Intent selectIntent = new Intent();
					selectIntent.setClassName("com.example.surfaceview", "com.example.surfaceview.Moving");
					startActivity(selectIntent);
				}
			}
		);

		buttonWorldMap.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View p1)
				{
					// TODO: Implement this method
					textView.setText("WorldMap");



					return true;
				}
			}
		);
			
			
    }
	
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate main_menu.xml 
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		Intent selectIntent = new Intent();
		switch (item.getItemId())
		{
			case R.id.mainMenuTop:
				selectIntent.setClassName("com.example.surfaceview", "com.example.surfaceview.MainActivity");
				startActivity(selectIntent);
				return true;
			case R.id.mainMenuGame:
				selectIntent.setClassName("com.example.surfaceview", "com.example.surfaceview.Circle");
				startActivity(selectIntent);
				return true;
			case R.id.mainMenuAbout:
				Toast.makeText(this, "This is my app!!!", Toast.LENGTH_SHORT).show();
				return true;
			case R.id.mainMenuExit:
				finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
