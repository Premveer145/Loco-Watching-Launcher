package com.teamsky.loco;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AppSettings extends AppCompatActivity
{

    private Switch setMode;
    private Boolean switchState;
    private EditText noLocoApps;
	private Button AutoWatchingBtn;
	private Button AutoWatchingStatusBtn;
    private String noLocoAppsValue;
    private SharedPreferences SPSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setMode = findViewById(R.id.setMode);
        noLocoApps = findViewById(R.id.noLocoApps);
		AutoWatchingBtn = findViewById(R.id.AutoWatchingBtn);
		AutoWatchingStatusBtn = findViewById(R.id.AutoWatchingStatusBtn);

		AutoWatchingStatusCheck();

        SPSettings = getSharedPreferences("Settings", Activity.MODE_PRIVATE);

        switchState = SPSettings.getBoolean("isRootMode", false);
        setMode.setChecked(switchState);
        setMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
				{
					if (isChecked)
					{
						executeShellCommand("");
						SPSettings.edit().putBoolean("isRootMode", true).commit();
					}
					else
					{
						SPSettings.edit().putBoolean("isRootMode", false).commit();
					}
				}
			});

        noLocoAppsValue = SPSettings.getString("noLocoAppsValue", "");

        if (noLocoAppsValue != null)
		{
            noLocoApps.setText(noLocoAppsValue);
        }

        noLocoApps.setOnEditorActionListener(new TextView.OnEditorActionListener() {
				@Override
				public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent)
				{
					if (actionId == EditorInfo.IME_ACTION_DONE)
					{
						noLocoAppsValue = noLocoApps.getText().toString();
						if (Integer.parseInt(noLocoAppsValue) >= 0 && Integer.parseInt(noLocoAppsValue) <= 45)
						{
							SPSettings.edit().putString("noLocoAppsValue", noLocoAppsValue).commit();
							return true;
						}
						else
						{
							Toast.makeText(getApplicationContext(), "Enter Value between 0 to 45.", Toast.LENGTH_SHORT).show();
						}
					}
					return false;
				}
			});

		AutoWatchingBtn.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					executeShellCommand("am force-stop com.teamsky.autowatching");
					startActivity(new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS));
				}
			});

		AutoWatchingStatusBtn.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					if (isAppInstalled("com.teamsky.autowatching"))
					{
						Toast.makeText(AppSettings.this, "Auto Watching Already Installed", Toast.LENGTH_SHORT).show();
					}
					else
					{
						installApkWithSuperuserPermissions();
						AutoWatchingStatusCheck();
					}
				}
			});

    }

    private void AutoWatchingStatusCheck()
	{
		if (isAppInstalled("com.teamsky.autowatching"))
		{
			AutoWatchingStatusBtn.setText("Installed");
		}
		else
		{
			AutoWatchingStatusBtn.setText("Install");
		}
	}

	public boolean isAppInstalled(String packageName)
	{
		PackageManager pm = getPackageManager();
		try
		{
			pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
			return true;
		}
		catch (PackageManager.NameNotFoundException e)
		{
			return false;
		}
	}

    public static void executeShellCommand(String command)
	{
        try
		{
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream outputStream = new DataOutputStream(process.getOutputStream());
            outputStream.writeBytes(command + "\n");
            outputStream.flush();
            outputStream.writeBytes("exit\n");
            outputStream.flush();
            process.waitFor();
        }
		catch (IOException | InterruptedException e)
		{
            e.printStackTrace();
        }
    }

	private void installApkWithSuperuserPermissions()
	{
        try
		{
			// Copy APK from assets to device storage
			InputStream inputStream = getAssets().open("AutoWatching.apk");
			File apkFile = new File(getExternalFilesDir(null), "AutoWatching.apk");

			OutputStream outputStream = new FileOutputStream(apkFile);

			byte[] buffer = new byte[1024];
			int length;
			while ((length = inputStream.read(buffer)) > 0)
			{
				outputStream.write(buffer, 0, length);
			}

			outputStream.close();
			inputStream.close();

			// Install APK with superuser permissions
			Process process = Runtime.getRuntime().exec("su");
			OutputStream suOutputStream = process.getOutputStream();

			suOutputStream.write(("pm install -r " + apkFile.getAbsolutePath() + "\n").getBytes());
			suOutputStream.flush();
			suOutputStream.close();
			Toast.makeText(AppSettings.this, "Auto Watching Installed Successfully", Toast.LENGTH_SHORT).show();
			process.waitFor();
		}
		catch (IOException | InterruptedException e)
		{
			e.printStackTrace();
		}
    }

	@Override
	protected void onResume()
	{
		AutoWatchingStatusCheck();
		super.onResume();
	}

}

