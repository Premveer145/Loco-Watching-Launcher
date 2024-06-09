package com.teamsky.loco;



import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.DataOutputStream;
import java.io.IOException;

public class AppManager extends AppCompatActivity
{

    private Switch PlayStoreSwitch;
    private Switch PlayServiceSwitch;
    private Switch KeyboardSwitch;
    private Switch PlayPartnerSwitch;
    private Switch BluetoothSwitch;
    private Switch CameraSwitch;
    private Switch GallerySwitch;
    private Switch MessageSwitch;
    private Switch SubscribeSwitch;
    private Switch ViaSwitch;
    private Switch LauncherSwitch;
    private Switch MagiskSwitch;
    private Boolean switchState;
    private Button MagiskBtn;
    private Button FilesBtn;
    private Button LauncherBtn;
    private Button DisableAllBtn;
    private SharedPreferences SPAppManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_manager);

        PlayStoreSwitch = findViewById(R.id.PlayStoreSwitch);
        PlayServiceSwitch = findViewById(R.id.PlayServiceSwitch);
        KeyboardSwitch = findViewById(R.id.KeyboardSwitch);
        PlayPartnerSwitch = findViewById(R.id.PlayPartnerSwitch);
        BluetoothSwitch = findViewById(R.id.BluetoothSwitch);
        CameraSwitch = findViewById(R.id.CameraSwitch);
        GallerySwitch = findViewById(R.id.GallerySwitch);
        MessageSwitch = findViewById(R.id.MessageSwitch);
        SubscribeSwitch = findViewById(R.id.SubscribeSwitch);
        ViaSwitch = findViewById(R.id.ViaSwitch);
        LauncherSwitch = findViewById(R.id.LauncherSwitch);
        MagiskSwitch = findViewById(R.id.MagiskSwitch);
        FilesBtn = findViewById(R.id.FilesBtn);
        LauncherBtn = findViewById(R.id.LauncherBtn);
        MagiskBtn = findViewById(R.id.MagiskBtn);
        DisableAllBtn = findViewById(R.id.DisableAllBtn);

        SPAppManager = getSharedPreferences("Settings", Activity.MODE_PRIVATE);

        LoadState();

        PlayStoreSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
				{
					String pkg = "com.android.vending";
					if (isChecked)
					{
						EnableApps(pkg);
					}
					else
					{
						DisableApps(pkg);
					}
				}
			}
        );

        PlayServiceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
				{
					String pkg = "com.google.android.gms";
					if (isChecked)
					{
						EnableApps(pkg);
					}
					else
					{
						DisableApps(pkg);
					}
				}
			}
        );

        KeyboardSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
				{
					String pkg = "com.google.android.inputmethod.latin";
					if (isChecked)
					{
						EnableApps(pkg);
					}
					else
					{
						DisableApps(pkg);
					}
				}
			}
        );

        PlayPartnerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
				{
					String pkg = "com.google.android.partnersetup";
					if (isChecked)
					{
						EnableApps(pkg);
					}
					else
					{
						DisableApps(pkg);
					}
				}
			}
        );

        BluetoothSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
				{
					String pkg = "com.android.bluetooth";
					if (isChecked)
					{
						EnableApps(pkg);
					}
					else
					{
						DisableApps(pkg);
					}
				}
			}
        );

        CameraSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
				{
					String pkg = "com.android.camera2";
					if (isChecked)
					{
						EnableApps(pkg);
					}
					else
					{
						DisableApps(pkg);
					}
				}
			}
        );

        GallerySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
				{
					String pkg = "com.android.gallery3d";
					if (isChecked)
					{
						EnableApps(pkg);
					}
					else
					{
						DisableApps(pkg);
					}
				}
			}
        );

        MessageSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
				{
					String pkg = "com.android.messaging";
					if (isChecked)
					{
						EnableApps(pkg);
					}
					else
					{
						DisableApps(pkg);
					}
				}
			}
        );

        SubscribeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
				{
					String pkg = "com.vphonegaga.web";
					if (isChecked)
					{
						EnableApps(pkg);
					}
					else
					{
						DisableApps(pkg);
					}
				}
			}
        );

        switchState = SPAppManager.getBoolean("isLauncherEnabled", true);
        LauncherSwitch.setChecked(switchState);
        LauncherSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
				{
					String pkg = "com.android.launcher3";
					if (isChecked)
					{
						EnableApps(pkg);
					}
					else
					{
						DisableApps(pkg);
					}
					switchState = LauncherSwitch.isChecked();
					SPAppManager.edit().putBoolean("isLauncherEnabled", switchState).commit();
				}
			}
        );

        MagiskSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
				{
					String pkg = "com.topjohnwu.magisk";
					if (isChecked)
					{
						EnableApps(pkg);
					}
					else
					{
						DisableApps(pkg);
					}
				}
			}
        );

        ViaSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
				{
					String pkg = "mark.via";
					if (isChecked)
					{
						EnableApps(pkg);
					}
					else
					{
						DisableApps(pkg);
					}
				}
			}
        );

        FilesBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view)
				{
					String pkg = "com.android.documentsui";
					launchApplications(pkg);
				}
			});

        LauncherBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view)
				{
					startActivity(new Intent(Settings.ACTION_HOME_SETTINGS));
				}
			});

        MagiskBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view)
				{
					String pkg = "com.topjohnwu.magisk";
					launchApplications(pkg);
				}
			});

        DisableAllBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view)
				{
					DisableAll();
					LoadState();
					Toast.makeText(AppManager.this, "Applications Disabled Successfully", Toast.LENGTH_SHORT).show();
				}
			});

    }

    private void DisableApps(String pkg)
	{
        String shellCommand = String.format("pm disable %s", pkg);
        RunCommand(shellCommand);
    }

    private void EnableApps(String pkg)
	{
        String shellCommand = String.format("pm enable %s", pkg);
        RunCommand(shellCommand);
    }

    private static void DisableAll()
	{
        String pkg;
        String shellCommand;
        pkg = "com.android.vending";
        shellCommand = String.format("pm disable %s", pkg);
        RunCommand(shellCommand);
        pkg = "com.google.android.gms";
        shellCommand = String.format("pm disable %s", pkg);
        RunCommand(shellCommand);
        pkg = "com.google.android.inputmethod.latin";
        shellCommand = String.format("pm disable %s", pkg);
        RunCommand(shellCommand);
        pkg = "com.google.android.partnersetup";
        shellCommand = String.format("pm disable %s", pkg);
        RunCommand(shellCommand);
        pkg = "com.android.bluetooth";
        shellCommand = String.format("pm disable %s", pkg);
        RunCommand(shellCommand);
        pkg = "com.android.camera2";
        shellCommand = String.format("pm disable %s", pkg);
        RunCommand(shellCommand);
        pkg = "com.android.gallery3d";
        shellCommand = String.format("pm disable %s", pkg);
        RunCommand(shellCommand);
        pkg = "com.android.messaging";
        shellCommand = String.format("pm disable %s", pkg);
        RunCommand(shellCommand);
        pkg = "com.vphonegaga.web";
        shellCommand = String.format("pm disable %s", pkg);
        RunCommand(shellCommand);
        pkg = "mark.via";
        shellCommand = String.format("pm disable %s", pkg);
        RunCommand(shellCommand);
        pkg = "com.topjohnwu.magisk";
        shellCommand = String.format("pm disable %s", pkg);
        RunCommand(shellCommand);
    }

    private static void RunCommand(String shellCommand)
	{
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
                Toast.makeText(getApplicationContext(), "Failed to launch Application", Toast.LENGTH_SHORT).show();
            }
        }
		catch (Exception e)
		{
            e.printStackTrace();
        }
    }

    private void LoadState()
	{
        switchState = isAppEnabled("com.android.vending");
        PlayStoreSwitch.setChecked(switchState);
        switchState = isAppEnabled("com.google.android.gms");
        PlayServiceSwitch.setChecked(switchState);
        switchState = isAppEnabled("com.google.android.inputmethod.latin");
        KeyboardSwitch.setChecked(switchState);
        switchState = isAppEnabled("com.google.android.partnersetup");
        PlayPartnerSwitch.setChecked(switchState);
        switchState = isAppEnabled("com.android.bluetooth");
        BluetoothSwitch.setChecked(switchState);
        switchState = isAppEnabled("com.android.camera2");
        CameraSwitch.setChecked(switchState);
        switchState = isAppEnabled("com.android.gallery3d");
        GallerySwitch.setChecked(switchState);
        switchState = isAppEnabled("com.android.messaging");
        MessageSwitch.setChecked(switchState);
        switchState = isAppEnabled("com.vphonegaga.web");
        SubscribeSwitch.setChecked(switchState);
        switchState = isAppEnabled("mark.via");
        ViaSwitch.setChecked(switchState);
        switchState = isAppEnabled("com.topjohnwu.magisk");
        MagiskSwitch.setChecked(switchState);
    }

    private boolean isAppEnabled(String packageName)
	{
        PackageManager packageManager = getApplicationContext().getPackageManager();
        try
		{
            ApplicationInfo appInfo = packageManager.getApplicationInfo(packageName, 0);
            return appInfo.enabled;
        }
		catch (PackageManager.NameNotFoundException e)
		{
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onResume()
	{
        super.onResume();
        LoadState();
    }
}

