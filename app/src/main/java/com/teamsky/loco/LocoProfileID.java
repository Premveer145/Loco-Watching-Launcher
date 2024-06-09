package com.teamsky.loco;


import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LocoProfileID extends AppCompatActivity
{

    private Switch ModifyProfiles;
    private EditText[] editTexts;
    private int totalEditTextFields = 25;
    private SharedPreferences sharedPreferences;
    private SharedPreferences sharedPreferences2;
    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loco_profile_id);

        sharedPreferences = getSharedPreferences("Loco_Profiles", Activity.MODE_PRIVATE);
        sharedPreferences2 = getSharedPreferences("Loco_ID", Activity.MODE_PRIVATE);

        editTexts = new EditText[totalEditTextFields];
        for (int i = 1; i <= totalEditTextFields; i++)
        {
            int editTextId = getResources().getIdentifier("UserID" + i, "id", getPackageName());
            editTexts[i - 1] = findViewById(editTextId);
        }

        ModifyProfiles = findViewById(R.id.ModifyProfiles);

        editTextDisable();
        for (int i = 1; i <= totalEditTextFields; i++)
        {
            String userNameKey = "UserName" + i;
            String userIDKey = "UserID" + i;

            final EditText userNameEditText = findViewById(getResources().getIdentifier(userNameKey, "id", getPackageName()));
            final EditText userIDEditText = findViewById(getResources().getIdentifier(userIDKey, "id", getPackageName()));

            // Retrieve data from Firebase
            databaseRef.child(userNameKey).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot)
                {
                    String userName = snapshot.getValue(String.class);
                    userNameEditText.setText(userName);
                }

                @Override
                public void onCancelled(DatabaseError error)
                {
                    // Handle any errors
                }
            });

            databaseRef.child(userIDKey).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot)
                {
                    String userID = snapshot.getValue(String.class);
                    userIDEditText.setText(userID);
                }

                @Override
                public void onCancelled(DatabaseError error)
                {
                    // Handle any errors
                }
            });
        }

        ModifyProfiles.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    editTextEnable();
                }
                else
                {
                    saveDataToFirebase();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    for (int i = 1; i <= totalEditTextFields; i++)
                    {
                        int editTextId = getResources().getIdentifier("UserName" + i, "id", getPackageName());
                        EditText editText = findViewById(editTextId);
                        String text = editText.getText().toString();
                        editor.putString("UserName" + i, text);
                        editTextId = getResources().getIdentifier("UserID" + i, "id", getPackageName());
                        editText = findViewById(editTextId);
                        text = editText.getText().toString();
                        editor.putString("UserID" + i, text);
                    }
                    editor.apply();
                    editTextDisable();
                }
            }
        });

        for (int i = 1; i <= totalEditTextFields; i++)
        {
            int buttonId = getResources().getIdentifier("ApplyID" + i, "id", getPackageName());
            Button applyButton = findViewById(buttonId);
            final int index = i;
            applyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    String extrasValue = editTexts[index - 1].getText().toString();
                    String data = String.format("https://dl.loco.gg?type=4&sub_type=1&streamer_uid=%s", extrasValue);
                    sharedPreferences2.edit().putString("extrasValue", extrasValue).apply();
                    sharedPreferences2.edit().putString("data", data).apply();
                }
            });
        }

    }

    private void saveDataToFirebase()
    {
        for (int i = 1; i <= totalEditTextFields; i++)
        {
            String userNameKey = "UserName" + i;
            String userIDKey = "UserID" + i;

            EditText userNameEditText = findViewById(getResources().getIdentifier(userNameKey, "id", getPackageName()));
            EditText userIDEditText = findViewById(getResources().getIdentifier(userIDKey, "id", getPackageName()));

            String userName = userNameEditText.getText().toString();
            String userID = userIDEditText.getText().toString();

            // Save data to Firebase
            databaseRef.child(userNameKey).setValue(userName);
            databaseRef.child(userIDKey).setValue(userID);
        }
    }

    private void editTextEnable()
    {
        for (int i = 1; i <= totalEditTextFields; i++)
        {

            int UserNamesID = getResources().getIdentifier("UserName" + i, "id", getPackageName());
            EditText UserNamesEditText = findViewById(UserNamesID);
            UserNamesEditText.setEnabled(true);
            UserNamesEditText.setTextColor(Color.BLACK);
            UserNamesEditText.setAlpha(1f);

            int UserIDs = getResources().getIdentifier("UserID" + i, "id", getPackageName());
            EditText UserIDsEditText = findViewById(UserIDs);
            UserIDsEditText.setEnabled(true);
            UserIDsEditText.setTextColor(Color.BLACK);
            UserIDsEditText.setAlpha(1f);

            int ApplyIDs = getResources().getIdentifier("ApplyID" + i, "id", getPackageName());
            Button ApplyIDsButton = findViewById(ApplyIDs);
            ApplyIDsButton.setEnabled(false);

        }
    }

    private void editTextDisable()
    {
        for (int i = 1; i <= totalEditTextFields; i++)
        {

            int UserNamesID = getResources().getIdentifier("UserName" + i, "id", getPackageName());
            EditText UserNamesEditText = findViewById(UserNamesID);
            UserNamesEditText.setEnabled(false);
            UserNamesEditText.setTextColor(Color.BLACK);
            UserNamesEditText.setAlpha(1f);

            int UserIDs = getResources().getIdentifier("UserID" + i, "id", getPackageName());
            EditText UserIDsEditText = findViewById(UserIDs);
            UserIDsEditText.setEnabled(false);
            UserIDsEditText.setTextColor(Color.BLACK);
            UserIDsEditText.setAlpha(1f);

            int ApplyIDs = getResources().getIdentifier("ApplyID" + i, "id", getPackageName());
            Button ApplyIDsButton = findViewById(ApplyIDs);
            ApplyIDsButton.setEnabled(true);

        }
    }
}


