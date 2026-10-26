package com.example.readyfit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    private ImageView imgUnit;

    private TextView txtTargetUnit;
    private TextView txtAge;
    private TextView txtWeight;
    private TextView txtRunTime;

    private TextView txtWorkoutTitle;
    private TextView txtWorkoutDescription;
    private TextView txtWorkoutDuration;

    private Button btnStartWorkout;
    private Button btnSettings;

    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        auth = FirebaseAuth.getInstance();


        // חיבור ל-XML
        imgUnit = findViewById(R.id.imgUnit);

        txtTargetUnit = findViewById(R.id.txtTargetUnit);
        txtAge = findViewById(R.id.txtAge);
        txtWeight = findViewById(R.id.txtWeight);
        txtRunTime = findViewById(R.id.txtRunTime);

        txtWorkoutTitle = findViewById(R.id.txtWorkoutTitle);
        txtWorkoutDescription = findViewById(R.id.txtWorkoutDescription);
        txtWorkoutDuration = findViewById(R.id.txtWorkoutDuration);

        btnStartWorkout = findViewById(R.id.btnStartWorkout);
        btnSettings = findViewById(R.id.btnSettings);


        // טעינת הנתונים
        loadUserData();

        // יצירת האימון והתמונה
        createDailyWorkout();


        // ============================
        // כפתור התחלת אימון
        // ============================

        btnStartWorkout.setOnClickListener(v -> {

            Toast.makeText(
                    HomeActivity.this,
                    "בקרוב מתחילים את האימון!",
                    Toast.LENGTH_SHORT
            ).show();

        });


        // ============================
        // כפתור הגדרות
        // ============================

        btnSettings.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            HomeActivity.this,
                            SettingsActivity.class
                    );

            startActivity(intent);

        });
    }


    // ==========================================
    // כשחוזרים מההגדרות
    // ==========================================

    @Override
    protected void onResume() {
        super.onResume();

        if (auth != null) {

            loadUserData();
            createDailyWorkout();

        }
    }


    // ==========================================
    // קבלת UID
    // ==========================================

    private String getUserId() {

        FirebaseUser user =
                auth.getCurrentUser();

        if (user == null) {
            return null;
        }

        return user.getUid();
    }


    // ==========================================
    // טעינת נתוני המשתמש
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
                        "--"
                );


        String weight =
                preferences.getString(
                        uid + "_weight",
                        "--"
                );


        String runTime =
                preferences.getString(
                        uid + "_runTime",
                        "--"
                );


        String targetUnit =
                preferences.getString(
                        uid + "_targetUnit",
                        "לא נבחר"
                );


        txtAge.setText(
                "גיל\n" + age
        );


        txtWeight.setText(
                "משקל\n" + weight + " ק״ג"
        );


        txtRunTime.setText(
                "2 ק״מ\n" + runTime
        );


        txtTargetUnit.setText(
                targetUnit
        );
    }


    // ==========================================
    // תמונה + אימון לפי יחידת היעד
    // ==========================================

    private void createDailyWorkout() {

        String uid = getUserId();

        if (uid == null) {
            return;
        }


        SharedPreferences preferences =
                getSharedPreferences(
                        "ReadyFitData",
                        MODE_PRIVATE
                );


        String targetUnit =
                preferences.getString(
                        uid + "_targetUnit",
                        ""
                );


        // ======================================
        // שייטת 13
        // ======================================

        if (targetUnit.equals("שייטת 13")) {

            imgUnit.setImageResource(
                    R.drawable.shayetet
            );


            txtWorkoutTitle.setText(
                    "אימון סיבולת ימית"
            );


            txtWorkoutDescription.setText(
                    "ריצה • זחילות • כוח • סיבולת"
            );


            txtWorkoutDuration.setText(
                    "משך משוער: 50 דקות"
            );

        }


        // ======================================
        // סיירת מטכ"ל
        // ======================================

        else if (targetUnit.equals("סיירת מטכ״ל")) {

            imgUnit.setImageResource(
                    R.drawable.matkal
            );


            txtWorkoutTitle.setText(
                    "אימון סיבולת קרבית"
            );


            txtWorkoutDescription.setText(
                    "ריצה • ספרינטים • זחילות"
            );


            txtWorkoutDuration.setText(
                    "משך משוער: 45 דקות"
            );

        }


        // ======================================
        // שלדג
        // ======================================

        else if (targetUnit.equals("שלדג")) {

            imgUnit.setImageResource(
                    R.drawable.shaldag
            );


            txtWorkoutTitle.setText(
                    "אימון כוח ומהירות"
            );


            txtWorkoutDescription.setText(
                    "ספרינטים • כוח • ריצה"
            );


            txtWorkoutDuration.setText(
                    "משך משוער: 45 דקות"
            );

        }


        // ======================================
        // חטיבת הקומנדו
        // ======================================

        else if (targetUnit.equals("חטיבת הקומנדו")) {

            imgUnit.setImageResource(
                    R.drawable.commando
            );


            txtWorkoutTitle.setText(
                    "אימון כושר קרבי"
            );


            txtWorkoutDescription.setText(
                    "ריצה • כוח • זריזות • זחילות"
            );


            txtWorkoutDuration.setText(
                    "משך משוער: 50 דקות"
            );

        }


        // ======================================
        // אין יחידה
        // ======================================

        else {

            txtWorkoutTitle.setText(
                    "אימון כללי"
            );


            txtWorkoutDescription.setText(
                    "ריצה • כוח • סיבולת"
            );


            txtWorkoutDuration.setText(
                    "משך משוער: 40 דקות"
            );

        }
    }
}