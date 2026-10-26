package com.example.readyfit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingsActivity extends AppCompatActivity {

    private EditText editAge;
    private EditText editWeight;
    private EditText editHeight;
    private EditText editRunTime;

    private Spinner spinnerTargetUnit;

    private Button btnSave;
    private Button btnBack;
    private Button btnLogout;

    private FirebaseAuth auth;

    private final String[] units = {
            "שייטת 13",
            "סיירת מטכ״ל",
            "שלדג",
            "חטיבת הקומנדו"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        auth = FirebaseAuth.getInstance();

        // חיבור ל-XML
        editAge = findViewById(R.id.editSettingsAge);
        editWeight = findViewById(R.id.editSettingsWeight);
        editHeight = findViewById(R.id.editSettingsHeight);
        editRunTime = findViewById(R.id.editSettingsRunTime);

        spinnerTargetUnit = findViewById(R.id.spinnerTargetUnit);

        btnSave = findViewById(R.id.btnSaveSettings);
        btnBack = findViewById(R.id.btnBackHome);
        btnLogout = findViewById(R.id.btnLogout);

        // יצירת רשימת היחידות
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        units
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerTargetUnit.setAdapter(adapter);

        // טעינת הנתונים הקיימים
        loadUserData();

        // שמירת שינויים
        btnSave.setOnClickListener(v -> {
            saveChanges();
        });

        // חזרה למסך הבית
        btnBack.setOnClickListener(v -> {
            finish();
        });

        // התנתקות
        btnLogout.setOnClickListener(v -> {

            auth.signOut();

            Intent intent =
                    new Intent(
                            SettingsActivity.this,
                            MainActivity.class
                    );

            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
            );

            startActivity(intent);
        });
    }

    // ==========================================
    // קבלת UID של המשתמש
    // ==========================================

    private String getUserId() {

        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            return null;
        }

        return user.getUid();
    }

    // ==========================================
    // טעינת הנתונים של המשתמש
    // ==========================================

    private void loadUserData() {

        String uid = getUserId();

        if (uid == null) {
            return;
        }

        SharedPreferences preferences =
                getSharedPreferences(
                        "ReadyFitData",
                        MODE_PRIVATE
                );

        String age =
                preferences.getString(
                        uid + "_age",
                        ""
                );

        String weight =
                preferences.getString(
                        uid + "_weight",
                        ""
                );

        String height =
                preferences.getString(
                        uid + "_height",
                        ""
                );

        String runTime =
                preferences.getString(
                        uid + "_runTime",
                        ""
                );

        String targetUnit =
                preferences.getString(
                        uid + "_targetUnit",
                        ""
                );

        // מציג את הנתונים
        editAge.setText(age);
        editWeight.setText(weight);
        editHeight.setText(height);
        editRunTime.setText(runTime);

        // מציג את יחידת היעד הנוכחית
        for (int i = 0; i < units.length; i++) {

            if (units[i].equals(targetUnit)) {

                spinnerTargetUnit.setSelection(i);
                break;
            }
        }
    }

    // ==========================================
    // שמירת שינויים
    // ==========================================

    private void saveChanges() {

        String uid = getUserId();

        if (uid == null) {

            Toast.makeText(
                    this,
                    "לא נמצא משתמש מחובר",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        String age =
                editAge.getText().toString().trim();

        String weight =
                editWeight.getText().toString().trim();

        String height =
                editHeight.getText().toString().trim();

        String runTime =
                editRunTime.getText().toString().trim();

        String targetUnit =
                spinnerTargetUnit
                        .getSelectedItem()
                        .toString();

        // בדיקה שכל הנתונים מולאו
        if (age.isEmpty()
                || weight.isEmpty()
                || height.isEmpty()
                || runTime.isEmpty()) {

            Toast.makeText(
                    this,
                    "יש למלא את כל הנתונים",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        SharedPreferences preferences =
                getSharedPreferences(
                        "ReadyFitData",
                        MODE_PRIVATE
                );

        SharedPreferences.Editor editor =
                preferences.edit();

        // שמירה לפי המשתמש המחובר
        editor.putString(
                uid + "_age",
                age
        );

        editor.putString(
                uid + "_weight",
                weight
        );

        editor.putString(
                uid + "_height",
                height
        );

        editor.putString(
                uid + "_runTime",
                runTime
        );

        editor.putString(
                uid + "_targetUnit",
                targetUnit
        );

        editor.apply();

        Toast.makeText(
                this,
                "השינויים נשמרו בהצלחה",
                Toast.LENGTH_SHORT
        ).show();

        // חזרה למסך הבית
        finish();
    }
}