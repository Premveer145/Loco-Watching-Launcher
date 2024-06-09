package com.teamsky.loco;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.DataOutputStream;
import java.io.IOException;

public class LocoApplications extends AppCompatActivity
{

    private Switch LocoAutoOpen;
	private EditText currentLocoIndex;
	private ImageButton indexBackwardBtn;
	private ImageButton indexForwardBtn;
    private String[] packageNameArr;
    private int currentButtonIndex;
    private Handler handler;
    private ImageButton[] buttons;
    private String packageName;
    private Intent intentOpenLoco = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loco_applications);

        LocoAutoOpen = findViewById(R.id.LocoAutoOpen);
		currentLocoIndex = findViewById(R.id.currentLocoIndex);
		indexBackwardBtn = (ImageButton) findViewById(R.id.indexBackwardBtn);
		indexForwardBtn = (ImageButton) findViewById(R.id.indexForwardBtn);

		currentLocoIndex.setEnabled(false);
		currentLocoIndex.setTextColor(Color.WHITE);
		currentLocoIndex.setAlpha(1f);
		currentLocoIndex.setText(String.valueOf(currentButtonIndex));
        handler = new Handler();
        currentButtonIndex = 0;
        SharedPreferences SPSettings = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        if (SPSettings.contains("noLocoAppsValue"))
		{
            int noOfLocoApps = Integer.parseInt(SPSettings.getString("noLocoAppsValue", ""));
            buttons = new ImageButton[noOfLocoApps + 1];
        }
		else
		{
            buttons = new ImageButton[45];
        }

        for (int i = 0; i < buttons.length; i++)
		{
            String buttonId = "LocoProfile" + i;
            int resourceId = getResources().getIdentifier(buttonId, "id", getPackageName());
            buttons[i] = (ImageButton) findViewById(resourceId);
        }


        LocoAutoOpen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
				{
					if (isChecked)
					{
						clickButtonsWithInterval();
					}
					else
					{
						handler.removeCallbacksAndMessages(null);
					}
				}
			});

		indexBackwardBtn.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					if (currentButtonIndex > 0)
					{
						currentButtonIndex--;
						currentLocoIndex.setText(String.valueOf(currentButtonIndex));
					}
				}
			});

		indexForwardBtn.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					if (currentButtonIndex < buttons.length - 1)
					{
						currentButtonIndex++;
						currentLocoIndex.setText(String.valueOf(currentButtonIndex));
					}
				}
			});

        packageNameArr = new String[46];
        String name = "com.showtimea";
	    char lastChar = 'p';
	    char secLastChar = 'o';
        for (int i = 0, j = 0; i <= 45; i++, j++)
		{
            final int currentIndex = i;
            int buttonId = getResources().getIdentifier("LocoProfile" + i, "id", getPackageName());
            final ImageButton LocoProfile = findViewById(buttonId);
            j = j % 26;
            if (j == 0)
			{
				secLastChar++;
			}
			if (j < 11)
			{
				packageNameArr[i] = name + secLastChar + lastChar;
				lastChar++;
				if (j == 10)
				{
					lastChar = 'p';
				}
			}
			else if (j < 26)
			{
				lastChar--;
				packageNameArr[i] = name + secLastChar + lastChar;
				if (j == 25)
				{
					lastChar = 'p';
				}
			}
            LocoProfile.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v)
					{
                        packageName = packageNameArr[currentIndex];
						LocoOpenMethod(packageName);
						LocoProfile.setAlpha(0.6f);
                    }
				});

			LocoProfile.setOnLongClickListener(new View.OnLongClickListener() {
					@Override
					public boolean onLongClick(View p1)
					{
                        packageName = packageNameArr[currentIndex];
                        try
						{
                            String forceStopCommand = String.format("am force-stop %s", packageName);
                            forceStopLocoApp(forceStopCommand);
                        }
						catch (IOException e)
						{
                            e.printStackTrace();
                        }
						LocoProfile.setAlpha(1.0f);
						return true;
					}
				});
        }



    }

    private void clickButtonsWithInterval()
	{
        if (currentButtonIndex < buttons.length)
		{
            buttons[currentButtonIndex].performClick();
        }
		else
		{
            LocoAutoOpen.setChecked(false);
            currentButtonIndex = 0;
			currentLocoIndex.setText(String.valueOf(currentButtonIndex));
        }
    }

    private void LocoOpenMethod(String packageName)
	{
        if (LocoAutoOpen.isChecked())
		{
            LocoAutoOpenfn(packageName);
        }
		else
		{
            LocoOpen(packageName);
        }
    }

    private void LocoOpen(String packageName)
	{
        intentOpenLoco = getPackageManager().getLaunchIntentForPackage(packageName);
        if (intentOpenLoco != null)
		{
            try
			{
                startActivity(intentOpenLoco);
            }
			catch (ActivityNotFoundException e)
			{
                Toast.makeText(this, "Target app is not installed.", Toast.LENGTH_SHORT).show();
            }
        }
		else
		{
            Toast.makeText(this, "Target app is not installed.", Toast.LENGTH_SHORT).show();
            LocoAutoOpen.setChecked(false);
        }
    }

    private void LocoAutoOpenfn(final String packageName)
	{
        if (LocoAutoOpen.isChecked())
		{
            intentOpenLoco = getPackageManager().getLaunchIntentForPackage(packageName);
            if (intentOpenLoco != null)
			{
                try
				{
                    startActivity(intentOpenLoco);
                }
				catch (ActivityNotFoundException e)
				{
                    Toast.makeText(this, "Target app is not installed.", Toast.LENGTH_SHORT).show();
                }
            }
			else
			{
                Toast.makeText(this, "Target app is not installed.", Toast.LENGTH_SHORT).show();
            }
            waitAndCallMethod(packageName, 6000);
            waitAndRunMethod(7000);
        }
    }

    private void forceStopLocoApp(String command) throws IOException
	{
        try
		{
            ProcessBuilder processBuilder = new ProcessBuilder("su");
            Process suProcess = processBuilder.start();
            DataOutputStream os = new DataOutputStream(suProcess.getOutputStream());
            os.writeBytes(command + "\n");
            os.flush();
            os.writeBytes("exit\n");
            os.flush();
            suProcess.waitFor();
        }
		catch (IOException | InterruptedException e)
		{
            e.printStackTrace();
        }
    }

    private void waitAndCallMethod(String PackageName, long delayMillis)
	{
        handler.postDelayed(new Runnable() {
				@Override
				public void run()
				{
					buttons[currentButtonIndex].performLongClick();
				}
			}, delayMillis);
    }

    private void waitAndRunMethod(long delayMillis)
	{
        handler.postDelayed(new Runnable() {
				@Override
				public void run()
				{
					currentButtonIndex++;
					currentLocoIndex.setText(String.valueOf(currentButtonIndex));
					clickButtonsWithInterval();
				}
			}, delayMillis);
    }

    private void hideButtons()
	{
        SharedPreferences SPSettings = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        if (SPSettings.contains("noLocoAppsValue"))
		{
            int loopLimit = Integer.parseInt(SPSettings.getString("noLocoAppsValue", ""));

            for (int i = 1; i <= loopLimit; i++)
			{
                int DisableId = getResources().getIdentifier("LocoProfile" + i, "id", getPackageName());
                ImageButton Disable = (ImageButton) findViewById(DisableId);
                Disable.setVisibility(View.VISIBLE);
            }

            for (int i = loopLimit + 1; i <= 45; i++)
			{
                int DisableId = getResources().getIdentifier("LocoProfile" + i, "id", getPackageName());
                ImageButton Disable = (ImageButton) findViewById(DisableId);
                Disable.setVisibility(View.INVISIBLE);
            }

        }
    }

    @Override
    public void onResume()
	{
        super.onResume();

        hideButtons();

    }

}

