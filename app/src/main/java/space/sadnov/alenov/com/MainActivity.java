package space.sadnov.alenov.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import space.sadnov.alenov.com.models.User;

public class MainActivity extends AppCompatActivity {

    Button btnSignIn, btnRegister;
    FirebaseAuth auth;  //для авторизации
    FirebaseDatabase db; //для подключения к бд
    DatabaseReference users; //для работы с таблицами

    RelativeLayout root; //записали родительский элемент

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignIn = findViewById(R.id.btnSignIn);
        btnRegister = findViewById(R.id.btnRegister);

        root = findViewById(R.id.root_element);

        auth = FirebaseAuth.getInstance(); //запускаем авторизацию в базе данных
        db = FirebaseDatabase.getInstance(); //подлючечаемся к базе данных
        users = db.getReference("Users");

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegisterWindow();
            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignInWindow();
            }
        });
    }

    private void showSignInWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Войти");
        dialog.setMessage("Введите данные для ввода");

        LayoutInflater inflater = LayoutInflater.from(this); //создали нужный нам объект
        View sing_in_window = inflater.inflate(R.layout.sign_in_window, null); //получение нужного нам шаблона
        dialog.setView(sing_in_window); //устанавливаем шаблон для всплывающего окна

        final MaterialEditText email = sing_in_window.findViewById(R.id.emailField);
        final MaterialEditText pass = sing_in_window.findViewById(R.id.passField);

        dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });
        dialog.setPositiveButton("Войти", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if(TextUtils.isEmpty(email.getText().toString())) {
                    Snackbar.make(root, "Введите вашу почту", Snackbar.LENGTH_SHORT).show(); //если пользователь ничего вводит выводится эта ошибка
                    return;
                }

                if(pass.getText().toString().length() < 5) {
                    Snackbar.make(root, "Введите пароль, который более 5 символов", Snackbar.LENGTH_SHORT).show(); //если пользователь ничего вводит выводится эта ошибка
                    return;
                }
                auth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        startActivity(new Intent(MainActivity.this, MapActivity.class)); //вывоз стартовой страницы
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(root, "Ошибка авторизации" + e.getMessage(), Snackbar.LENGTH_SHORT).show(); //неуспешная авторизация пользоателя

                    }
                });

            }
        });

        dialog.show();

    }

    private void showRegisterWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Зарегистрироваться");
        dialog.setMessage("Введите все данные для регистрации");

        LayoutInflater inflater = LayoutInflater.from(this); //создали нужный нам объект
        View register_window = inflater.inflate(R.layout.register_window, null); //получение нужного нам шаблона
        dialog.setView(register_window); //устанавливаем шаблон для всплывающего окна

        final MaterialEditText email = register_window.findViewById(R.id.emailField);
        final MaterialEditText pass = register_window.findViewById(R.id.passField);
        final MaterialEditText name = register_window.findViewById(R.id.namelField);
        final MaterialEditText phone = register_window.findViewById(R.id.phoneField);

        dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });
        dialog.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
               if(TextUtils.isEmpty(email.getText().toString())) {
                   Snackbar.make(root, "Введите вашу почту", Snackbar.LENGTH_SHORT).show(); //если пользователь ничего вводит выводится эта ошибка
                   return;
               }

                if(TextUtils.isEmpty(name.getText().toString())) {
                    Snackbar.make(root, "Введите ваше имя", Snackbar.LENGTH_SHORT).show(); //если пользователь ничего вводит выводится эта ошибка
                    return;
                }

                if(TextUtils.isEmpty(phone.getText().toString())) {
                    Snackbar.make(root, "Введите ваш телефон", Snackbar.LENGTH_SHORT).show(); //если пользователь ничего вводит выводится эта ошибка
                    return;
                }

                if(pass.getText().toString().length() < 5) {
                    Snackbar.make(root, "Введите пароль, который более 5 символов", Snackbar.LENGTH_SHORT).show(); //если пользователь ничего вводит выводится эта ошибка
                    return;
                }

                //регистрация пользователя
                auth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                User user = new User();
                                user.setEmail(email.getText().toString());
                                user.setName(name.getText().toString());
                                user.setPass(pass.getText().toString());
                                user.setPhone(phone.getText().toString());

                                users.child(FirebaseAuth.getInstance().getCurrentUser().getUid()) //добавление в таблицу пользователей нового пользователя
                                        .setValue(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Snackbar.make(root, "Пользователь добавлен!", Snackbar.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(root, "Ошибка регистрации" + e.getMessage(), Snackbar.LENGTH_SHORT).show(); //если уже существует пользователь с такой почтой

                    }
                });
            }
        });

        dialog.show();



    }
}