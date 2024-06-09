package com.teamsky.loco;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.DataOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity
{

    private Button BrightnessBtn;
	private Switch LocoAutoOpen;
	private EditText currentLocoIndex;
	private ImageButton indexBackwardBtn;
	private ImageButton indexForwardBtn;
    private ImageButton LocoProfileID;
    private ImageButton LocoLoginBtn;
    private ImageButton LocoAppBtn;
    private ImageButton AppManagerBtn;
    private ImageButton SettingsBtn;

    private Intent intent = new Intent();
    private SharedPreferences sharedPreferences;
    private String[] packageNameArr;
    private String packageName;
    private String extrasValue;
    private String data;
    private String setMethod;
	private int currentButtonIndex;
    private ImageButton[] buttons;
	private String isDone;
	private int openAttempts;

    @Override
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BrightnessBtn = findViewById(R.id.BrightnessBtn);
		LocoAutoOpen = findViewById(R.id.LocoAutoOpen);
		currentLocoIndex = findViewById(R.id.currentLocoIndex);
		indexBackwardBtn = (ImageButton) findViewById(R.id.indexBackwardBtn);
		indexForwardBtn = (ImageButton) findViewById(R.id.indexForwardBtn);
        LocoProfileID = (ImageButton) findViewById(R.id.LocoProfileID);
        LocoLoginBtn = (ImageButton) findViewById(R.id.LocoLoginBtn);
        LocoAppBtn = (ImageButton) findViewById(R.id.LocoAppBtn);
        AppManagerBtn = (ImageButton) findViewById(R.id.AppManagerBtn);
        SettingsBtn = (ImageButton) findViewById(R.id.SettingsBtn);
        sharedPreferences = getSharedPreferences("Loco_ID", Activity.MODE_PRIVATE);

        disableHomeButton();

		openAttempts = 0;
		currentButtonIndex = 0;
		currentLocoIndex.setEnabled(false);
		currentLocoIndex.setTextColor(Color.WHITE);
		currentLocoIndex.setAlpha(1f);
		currentLocoIndex.setText(String.valueOf(currentButtonIndex));




		LocoAutoOpen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
				{
					if (isChecked)
					{
						clickButtons();
						Intent intent = new Intent("com.teamsky.autowatching.SwitchState");
						intent.putExtra("SwitchState", "true");
						MainActivity.this.sendBroadcast(intent);
					}
					else
					{
						Intent intent = new Intent("com.teamsky.autowatching.SwitchState");
						intent.putExtra("SwitchState", "flase");
						MainActivity.this.sendBroadcast(intent);
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
						SuperUserMode(packageName, extrasValue, data);
						LocoProfile.setAlpha(0.6f);
                    }
				});

			LocoProfile.setOnLongClickListener(new View.OnLongClickListener() {
					@Override
					public boolean onLongClick(View p1)
					{
                        packageName = packageNameArr[currentIndex];
						LocoForceStop(packageName);
						LocoProfile.setAlpha(1.0f);
						return true;
					}
				});
        }

        BrightnessBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View p1)
				{
					String pkg = "mobi.omegacentauri.red_pro";
					launchApplications(pkg);
				}
			});

        LocoProfileID.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View p1)
				{
					Intent intentProfileID = new Intent();
					intentProfileID.setClass(getApplicationContext(), LocoProfileID.class);
					startActivity(intentProfileID);
				}
			});

        LocoLoginBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View p1)
				{
					Intent intentLocoLogin = new Intent();
					intentLocoLogin.setClass(getApplicationContext(), LocoLogin.class);
					startActivity(intentLocoLogin);
				}


			});

        LocoAppBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View p1)
				{
					Intent intentLocoApplications = new Intent();
					intentLocoApplications.setClass(getApplicationContext(), LocoApplications.class);
					startActivity(intentLocoApplications);
				}


			});

        AppManagerBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View p1)
				{
					Intent intentAppManager = new Intent();
					intentAppManager.setClass(getApplicationContext(), AppManager.class);
					startActivity(intentAppManager);
				}


			});

        SettingsBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View p1)
				{
					Intent intentSettings = new Intent();
					intentSettings.setClass(getApplicationContext(), AppSettings.class);
					startActivity(intentSettings);
				}


			});

    }

	@Override
	protected void onStart()
	{
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

		IntentFilter filter = new IntentFilter("com.teamsky.loco.TaskDone");
		registerReceiver(receiver, filter);
		if (LocoAutoOpen.isChecked())
		{
			Intent intent = new Intent("com.teamsky.autowatching.SwitchState");
			intent.putExtra("SwitchState", "true");
			MainActivity.this.sendBroadcast(intent);
		}

		extrasValue = sharedPreferences.getString("extrasValue", "");
        data = sharedPreferences.getString("data", "");
        hideButtons();
        changeMethod();
		super.onStart();
	}


    @Override
    public void onResume()
	{
        super.onResume();
		if (LocoAutoOpen.isChecked())
		{
			if (isDone.equals("true"))
			{
				currentButtonIndex++;
				openAttempts = 0;
			}
			else
			{
				openAttempts++;
			}
			currentLocoIndex.setText(String.valueOf(currentButtonIndex));
			if (openAttempts < 3)
			{
				new Handler().postDelayed(new Runnable(){

						@Override
						public void run()
						{
							if (LocoAutoOpen.isChecked())
							{
								clickButtons();
							}
						}
					}, 3000);
			}
			else
			{
				openAttempts = 0;
				LocoAutoOpen.setChecked(false);
			}

		}
    }

    private void changeMethod(String packageName, String extrasValue, String data)
	{
        if (setMethod == "RootMode")
		{
            SuperUserMode(packageName, extrasValue, data);
        }
		else
		{
            IntentMode(packageName, extrasValue, data);
        }

    }

    private static void SuperUserMode(String packageName, String extrasValue, String data)
	{
        String shellCommand = String.format("am start -n %s/com.pocketaces.ivory.view.activities.ProfileActivity -d '%s' --ez isFromClips false --es profile_useruid %s", packageName, data, extrasValue);

        try
		{
            ProcessBuilder processBuilder = new ProcessBuilder("su");
            Process suProcess = processBuilder.start();
            DataOutputStream os = new DataOutputStream(suProcess.getOutputStream());
            os.writeBytes(shellCommand + "\n");
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

    private void IntentMode(String packageName, String extrasValue, String data)
	{
        String component = String.format("%s/com.pocketaces.ivory.view.activities.ProfileActivity", packageName);
        boolean isFromEdit = false;
        String userClip = null;
        String launchInfo = null;
        boolean isFromClips = false;
        String isFollowing = null;

        Intent intent = new Intent();
        intent.setClassName(component.split("/")[0], component.split("/")[1]);
        intent.setData(Uri.parse(data));
        intent.putExtra("profile_useruid", extrasValue);
        intent.putExtra("isFromEdit", isFromEdit);
        intent.putExtra("userClip", userClip);
        intent.putExtra("launchInfo", launchInfo);
        intent.putExtra("isFromClips", isFromClips);
        intent.putExtra("isFollowing", isFollowing);
        if (intent != null)
		{
            try
			{
                startActivity(intent);
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
    }

    private void LocoForceStop(String packageName)
	{
        String forceStopCommand = String.format("am force-stop %s", packageName);
        try
		{
            ProcessBuilder processBuilder = new ProcessBuilder("su");
            Process suProcess = processBuilder.start();
            DataOutputStream os = new DataOutputStream(suProcess.getOutputStream());
            os.writeBytes(forceStopCommand + "\n");
            os.flush();
            os.writeBytes("exit\n");
            os.flush();
            Toast.makeText(MainActivity.this, "Stopped Successfully", Toast.LENGTH_SHORT).show();
            suProcess.waitFor();
        }
		catch (IOException | InterruptedException e)
		{
            e.printStackTrace();
        }
    }

    private void hideButtons()
	{
        SharedPreferences SPSettings = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        if (!SPSettings.contains("noLocoAppsValue"))
		{
            SPSettings.edit().putString("noLocoAppsValue", "45").commit();
        }
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

    private void changeMethod()
	{
        SharedPreferences SPSettings = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        if (SPSettings.contains("isRootMode"))
		{

            if (SPSettings.getBoolean("isRootMode", false))
			{
                setMethod = "RootMode";
            }
			else
			{
                setMethod = "IntentMode";
            }

        }
    }

    private void launchApplications(String pkg)
	{
        try
		{
            Intent launchIntent = getPackageManager().getLaunchIntentForPackage(pkg);
            if (launchIntent != null)
			{
                startActivity(launchIntent);
            }
			else
			{
                Toast.makeText(getApplicationContext(), "Application Not Installed", Toast.LENGTH_SHORT).show();
            }
        }
		catch (Exception e)
		{
            e.printStackTrace();
        }
    }

	@Override
	protected void onStop()
	{
		Intent intent = new Intent("com.teamsky.autowatching.SwitchState");
		intent.putExtra("SwitchState", "false");
		MainActivity.this.sendBroadcast(intent);
		super.onStop();
	}



    @Override
    public void onBackPressed()
	{
    }

    private void disableHomeButton()
	{
        View decorView = getWindow().getDecorView();
        decorView.setOnKeyListener(new View.OnKeyListener() {
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event)
				{
					if (keyCode == KeyEvent.KEYCODE_HOME)
					{
						return true;
					}
					return false;
				}
			});
    }

	private void clickButtons()
	{
		if (currentButtonIndex < buttons.length)
		{
            buttons[currentButtonIndex].performClick();
			isDone = "false";
        }
		else
		{
            LocoAutoOpen.setChecked(false);
            currentButtonIndex = 0;
			currentLocoIndex.setText(String.valueOf(currentButtonIndex));
        }
	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent)
		{
			isDone = intent.getStringExtra("TaskDone");
		}
	};

	@Override
	protected void onDestroy()
	{
		unregisterReceiver(receiver);
		super.onDestroy();
	}
}

