package com.example.readyfit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity2 extends AppCompatActivity {

    private EditText editAge;
    private EditText editWeight;
    private EditText editHeight;
    private EditText editRunTime;

    private Button btnContinue;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main2);

        editAge = findViewById(R.id.editAge);
        editWeight = findViewById(R.id.editWeight);
        editHeight = findViewById(R.id.editHeight);
        editRunTime = findViewById(R.id.editRunTime);

        btnContinue = findViewById(R.id.btnContinue);

        // Firebase Authentication
        auth = FirebaseAuth.getInstance();

        // אם כבר יש למשתמש נתונים, מציג אותם
        loadUserData();

        btnContinue.setOnClickListener(v -> {
            saveDataAndContinue();
        });
    }


    // ==========================================
    // קבלת UID של המשתמש המחובר
    // ==========================================

    private String getUserId() {

        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            return null;
        }

        return user.getUid();
    }


    // ==========================================
    // טעינת נתונים קיימים
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


        editAge.setText(age);
        editWeight.setText(weight);
        editHeight.setText(height);
        editRunTime.setText(runTime);
    }


    // ==========================================
    // שמירת הנתונים
    // ==========================================

    private void saveDataAndContinue() {

        String uid = getUserId();

        if (uid == null) {

            Toast.makeText(
                    MainActivity2.this,
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


        if (age.isEmpty()
                || weight.isEmpty()
                || height.isEmpty()
                || runTime.isEmpty()) {

            Toast.makeText(
                    MainActivity2.this,
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


        // שומר לפי UID של המשתמש
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


        editor.apply();


        Toast.makeText(
                MainActivity2.this,
                "הנתונים נשמרו",
                Toast.LENGTH_SHORT
        ).show();


        // מעבר לבחירת יחידה
        Intent intent =
                new Intent(
                        MainActivity2.this,
                        TargetUnitActivity.class
                );

        startActivity(intent);
    }
}