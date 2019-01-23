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
					textView.setText("Clicked");
				}
			});

		buttonCircle.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View p1)
				{
					// TODO: Implement this method
					textView.setText("Long Clicked");
					Intent selectIntent = new Intent();
					selectIntent.setClassName("com.example.surfaceview", "com.example.surfaceview.Circle");
					startActivity(selectIntent);
					
					
					return true;
				}
			});
			
			
			
		Button buttonTobineko = (Button) findViewById(R.id.buttonTobineko);

		buttonTobineko.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					textView.setText("Clicked");
				}
			});

		buttonTobineko.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View p1)
				{
					// TODO: Implement this method
					textView.setText("Long Clicked");
					Intent selectIntent = new Intent();
					selectIntent.setClassName("com.example.surfaceview", "com.example.surfaceview.TitleActivity");
					startActivity(selectIntent);


					return true;
				}
			});
			
			
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
		switch (item.getItemId())
		{
			case R.id.mainMenuTop:
				Intent selectIntent = new Intent();
				selectIntent.setClassName("com.example.surfaceview", "com.example.surfaceview.MainActivity");
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
