package com.example.readyfit;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    // השדות במסך ההתחברות
    private EditText editEmail;
    private EditText editPassword;

    // הכפתורים במסך ההתחברות
    private Button btnLogin;
    private Button btnOpenRegister;

    // Firebase Authentication
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        // חיבור ל-Firebase Authentication
        auth = FirebaseAuth.getInstance();


        // חיבור לרכיבים של activity_main.xml
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);

        btnLogin = findViewById(R.id.btnLogin);
        btnOpenRegister = findViewById(R.id.btnOpenRegister);


        // =========================================
        // כפתור התחברות
        // =========================================

        btnLogin.setOnClickListener(v -> {

            loginUser();

        });


        // =========================================
        // כפתור "לחץ כאן להרשמה"
        // =========================================

        btnOpenRegister.setOnClickListener(v -> {

            openRegisterDialog();

        });
    }


    // =====================================================
    // התחברות למשתמש קיים
    // =====================================================

    private void loginUser() {

        String email =
                editEmail.getText().toString().trim();

        String password =
                editPassword.getText().toString();


        // בדיקה שהמשתמש מילא את שני השדות
        if (email.isEmpty() || password.isEmpty()) {

            Toast.makeText(
                    MainActivity.this,
                    "יש למלא אימייל וסיסמה",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        // התחברות דרך Firebase Authentication
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        Toast.makeText(
                                MainActivity.this,
                                "התחברת בהצלחה",
                                Toast.LENGTH_SHORT
                        ).show();


                        // מעבר למסך השני
                        openSecondScreen();

                    } else {

                        Toast.makeText(
                                MainActivity.this,
                                "האימייל או הסיסמה שגויים",
                                Toast.LENGTH_SHORT
                        ).show();
                    }

                });
    }


    // =====================================================
    // פתיחת ה-Custom Dialog של ההרשמה
    // =====================================================

    private void openRegisterDialog() {

        // יצירת Dialog חדש
        Dialog dialog = new Dialog(MainActivity.this);


        // חיבור ל-dialog_register.xml
        dialog.setContentView(R.layout.dialog_register);


        // =========================================
        // חיבור לרכיבים שנמצאים בתוך הדיאלוג
        // =========================================

        EditText registerEmail =
                dialog.findViewById(R.id.registerEmail);

        EditText registerPassword =
                dialog.findViewById(R.id.registerPassword);

        EditText registerConfirmPassword =
                dialog.findViewById(R.id.registerConfirmPassword);

        Button btnRegister =
                dialog.findViewById(R.id.btnRegister);

        Button btnCancel =
                dialog.findViewById(R.id.btnCancel);


        // =========================================
        // כפתור ביטול
        // =========================================

        btnCancel.setOnClickListener(v -> {

            dialog.dismiss();

        });


        // =========================================
        // כפתור הרשמה
        // =========================================

        btnRegister.setOnClickListener(v -> {

            String email =
                    registerEmail
                            .getText()
                            .toString()
                            .trim();


            String password =
                    registerPassword
                            .getText()
                            .toString();


            String confirmPassword =
                    registerConfirmPassword
                            .getText()
                            .toString();


            // בדיקה שכל השדות מלאים
            if (email.isEmpty()
                    || password.isEmpty()
                    || confirmPassword.isEmpty()) {

                Toast.makeText(
                        MainActivity.this,
                        "יש למלא את כל השדות",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }


            // בדיקה שהסיסמאות זהות
            if (!password.equals(confirmPassword)) {

                Toast.makeText(
                        MainActivity.this,
                        "הסיסמאות אינן זהות",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }


            // Firebase דורש לפחות 6 תווים בסיסמה
            if (password.length() < 6) {

                Toast.makeText(
                        MainActivity.this,
                        "הסיסמה חייבת להכיל לפחות 6 תווים",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }


            // =========================================
            // יצירת משתמש ב-Firebase Authentication
            // =========================================

            auth.createUserWithEmailAndPassword(
                            email,
                            password
                    )

                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {

                            Toast.makeText(
                                    MainActivity.this,
                                    "נרשמת בהצלחה",
                                    Toast.LENGTH_SHORT
                            ).show();


                            // סוגר את חלונית ההרשמה
                            dialog.dismiss();


                            // מעבר למסך השני
                            openSecondScreen();

                        } else {

                            // אם Firebase החזיר שגיאה
                            String error = "ההרשמה נכשלה";

                            if (task.getException() != null) {

                                error =
                                        task.getException().getMessage();

                            }


                            Toast.makeText(
                                    MainActivity.this,
                                    error,
                                    Toast.LENGTH_LONG
                            ).show();
                        }

                    });

        });


        // =========================================
        // הצגת ה-Dialog
        // =========================================

        dialog.show();


        // מגדיל את החלונית לרוחב המסך
        if (dialog.getWindow() != null) {

            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
    }


    // =====================================================
    // מעבר ל-MainActivity2
    // =====================================================

    private void openSecondScreen() {

        Intent intent =
                new Intent(
                        MainActivity.this,
                        MainActivity2.class
                );

        startActivity(intent);

        finish();
    }
}