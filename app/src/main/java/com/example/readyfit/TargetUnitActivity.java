package com.example.readyfit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class TargetUnitActivity extends AppCompatActivity {

    private TextView cardShayetet;
    private TextView cardMatkal;
    private TextView cardShaldag;
    private TextView cardCommando;

    private TextView txtSelectedUnit;

    private Button btnContinueHome;

    private String selectedUnit = "";

    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_target_unit);


        // Firebase
        auth = FirebaseAuth.getInstance();


        // חיבור ל-XML
        cardShayetet =
                findViewById(R.id.cardShayetet);

        cardMatkal =
                findViewById(R.id.cardMatkal);

        cardShaldag =
                findViewById(R.id.cardShaldag);

        cardCommando =
                findViewById(R.id.cardCommando);

        txtSelectedUnit =
                findViewById(R.id.txtSelectedUnit);

        btnContinueHome =
                findViewById(R.id.btnContinueHome);


        // טוען יחידה קודמת אם קיימת
        loadSelectedUnit();


        // ==========================
        // שייטת 13
        // ==========================

        cardShayetet.setOnClickListener(v -> {

            selectUnit(
                    "שייטת 13",
                    cardShayetet
            );

        });


        // ==========================
        // סיירת מטכ"ל
        // ==========================

        cardMatkal.setOnClickListener(v -> {

            selectUnit(
                    "סיירת מטכ״ל",
                    cardMatkal
            );

        });


        // ==========================
        // שלדג
        // ==========================

        cardShaldag.setOnClickListener(v -> {

            selectUnit(
                    "שלדג",
                    cardShaldag
            );

        });


        // ==========================
        // חטיבת הקומנדו
        // ==========================

        cardCommando.setOnClickListener(v -> {

            selectUnit(
                    "חטיבת הקומנדו",
                    cardCommando
            );

        });


        // ==========================
        // כפתור המשך
        // ==========================

        btnContinueHome.setOnClickListener(v -> {

            // חייב לבחור יחידה
            if (selectedUnit.isEmpty()) {

                Toast.makeText(
                        TargetUnitActivity.this,
                        "בחר יחידה לפני שממשיכים",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }


            // מעבר למסך הבית
            Intent intent =
                    new Intent(
                            TargetUnitActivity.this,
                            HomeActivity.class
                    );

            startActivity(intent);

            finish();
        });
    }


    // ==========================================
    // UID של המשתמש
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
    // בחירת יחידה
    // ==========================================

    private void selectUnit(
            String unit,
            TextView selectedCard
    ) {

        selectedUnit = unit;


        // מחזיר את כולם לצבע הרגיל
        cardShayetet.setBackgroundColor(
                getColor(android.R.color.darker_gray)
        );

        cardMatkal.setBackgroundColor(
                getColor(android.R.color.darker_gray)
        );

        cardShaldag.setBackgroundColor(
                getColor(android.R.color.darker_gray)
        );

        cardCommando.setBackgroundColor(
                getColor(android.R.color.darker_gray)
        );


        // צובע את היחידה שנבחרה בירוק
        selectedCard.setBackgroundColor(
                getColor(R.color.olive_green)
        );


        // מציג מה נבחר
        txtSelectedUnit.setText(
                "היעד שלך: " + unit
        );


        // שמירה
        saveSelectedUnit();


        Toast.makeText(
                TargetUnitActivity.this,
                unit + " נבחרה",
                Toast.LENGTH_SHORT
        ).show();
    }


    // ==========================================
    // שמירת היחידה
    // ==========================================

    private void saveSelectedUnit() {

        String uid = getUserId();

        if (uid == null) {
            return;
        }


        SharedPreferences preferences =
                getSharedPreferences(
                        "ReadyFitData",
                        MODE_PRIVATE
                );


        SharedPreferences.Editor editor =
                preferences.edit();


        editor.putString(
                uid + "_targetUnit",
                selectedUnit
        );


        editor.apply();
    }


    // ==========================================
    // טעינת יחידה שנבחרה בעבר
    // ==========================================

    private void loadSelectedUnit() {

        String uid = getUserId();

        if (uid == null) {
            return;
        }


        SharedPreferences preferences =
                getSharedPreferences(
                        "ReadyFitData",
                        MODE_PRIVATE
                );


        selectedUnit =
                preferences.getString(
                        uid + "_targetUnit",
                        ""
                );


        if (selectedUnit.isEmpty()) {

            txtSelectedUnit.setText(
                    "בחר אחת מהיחידות"
            );

            return;
        }


        txtSelectedUnit.setText(
                "היעד שלך: " + selectedUnit
        );


        // מסמן את היחידה שנבחרה בעבר
        if (selectedUnit.equals("שייטת 13")) {

            cardShayetet.setBackgroundColor(
                    getColor(R.color.olive_green)
            );

        } else if (selectedUnit.equals("סיירת מטכ״ל")) {

            cardMatkal.setBackgroundColor(
                    getColor(R.color.olive_green)
            );

        } else if (selectedUnit.equals("שלדג")) {

            cardShaldag.setBackgroundColor(
                    getColor(R.color.olive_green)
            );

        } else if (selectedUnit.equals("חטיבת הקומנדו")) {

            cardCommando.setBackgroundColor(
                    getColor(R.color.olive_green)
            );
        }
    }
}