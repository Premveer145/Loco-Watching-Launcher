package com.teamsky.loco;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.DataOutputStream;
import java.io.IOException;

public class LocoLogin extends AppCompatActivity
{

    private ImageButton LocoProfile;
    private String[] packageNameArr;
    private String packageName;
    private String mainLocoApp = "com.showtimeapp";

    @Override
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loco_login);

        LocoProfile = (ImageButton) findViewById(R.id.LocoProfile);

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

            if (i == 0)
			{
            	continue;
            }

            LocoProfile.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v)
					{
                        packageName = packageNameArr[currentIndex];
						LocoLoginMethod(mainLocoApp, packageName);
						LocoProfile.setAlpha(0.6f);
                    }
				});
        }


        LocoProfile.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View p1)
				{
					packageName = "com.showtimeapp";
					Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);
					startActivity(launchIntent);

				}
			});

    }

    private void LocoLoginMethod(String mainLocoApp, String packageName)
	{
        try
		{
            // Copy shared_prefs folder to the destination
            String copyCommand = String.format("cp -R /data/user/0/%s/shared_prefs /data/user/0/%s/", mainLocoApp, packageName);
            executeCommand(copyCommand);

            // Clear the main application's data
            String clearCommand = String.format("pm clear %s", mainLocoApp);
            executeCommand(clearCommand);

            //Force stop target application
            String forceStopCommand = String.format("am force-stop %s", packageName);
            executeCommand(forceStopCommand);

        }
		catch (IOException e)
		{
            e.printStackTrace();
        }
    }

    private void executeCommand(String command) throws IOException
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
        Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_SHORT).show();
        packageName = "com.showtimeapp";
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);
        startActivity(launchIntent);
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

