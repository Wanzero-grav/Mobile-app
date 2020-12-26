package space.sadnov.alenov.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Database;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import space.sadnov.alenov.com.models.registerDTP;



public class MapActivity extends AppCompatActivity  {

    FirebaseAuth auth;
    Button btnregisterDTP, btndellDTP;
    FirebaseDatabase db;
    DatabaseReference newdtp; //ссылка на базу
    RelativeLayout newroot; //записали родительский элемент

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        btnregisterDTP = (Button)findViewById(R.id.btnregisterDTP);
        btndellDTP = (Button)findViewById(R.id.btndellDTP);

        auth = FirebaseAuth.getInstance(); //запускаем авторизацию в базе данных
        db = FirebaseDatabase.getInstance();
        newdtp = db.getReference("newdtp");

        newroot = findViewById(R.id.map_element);

        btnregisterDTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //при нажатии на btnregisterDTP обрабатывает код который здесь написан
                showRegisterDTP();
            }
        });

    }


    private void showRegisterDTP() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Зарегистрировать ДТП");
        dialog.setMessage("Введите все данные для регистрации");

        LayoutInflater inflater = LayoutInflater.from(this); //создали нужный нам объект
        View activityRegister = inflater.inflate(R.layout.activity_register,null); //получение нужного нам шаблона
        dialog.setView(activityRegister); //устанавливаем шаблон для всплывающего окна

        final MaterialEditText vidDTP = activityRegister.findViewById(R.id.vidDTPField);
        final MaterialEditText date = activityRegister.findViewById(R.id.namelField);
        final MaterialEditText time = activityRegister.findViewById(R.id.timeField);
        final MaterialEditText location = activityRegister.findViewById(R.id.locationField);
        final MaterialEditText pavement = activityRegister.findViewById(R.id.pavementField);
        final MaterialEditText lighting = activityRegister.findViewById(R.id.lightingField);
        final MaterialEditText inform = activityRegister.findViewById(R.id.informField);


        dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });
        dialog.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if (TextUtils.isEmpty(vidDTP.getText().toString())) {
                    Snackbar.make(newroot, "Введите вид ДТП", Snackbar.LENGTH_SHORT).show(); //если пользователь ничего вводит выводится эта ошибка
                    return;
                }
                if (TextUtils.isEmpty(date.getText().toString())) {
                    Snackbar.make(newroot, "Введите дату", Snackbar.LENGTH_SHORT).show(); //если пользователь ничего вводит выводится эта ошибка
                    return;
                }
                if (TextUtils.isEmpty(time.getText().toString())) {
                    Snackbar.make(newroot, "Введите время", Snackbar.LENGTH_SHORT).show(); //если пользователь ничего вводит выводится эта ошибка
                    return;
                }
                if (TextUtils.isEmpty(location.getText().toString())) {
                    Snackbar.make(newroot, "Введите местоположение", Snackbar.LENGTH_SHORT).show(); //если пользователь ничего вводит выводится эта ошибка
                    return;
                }
                if (TextUtils.isEmpty(pavement.getText().toString())) {
                    Snackbar.make(newroot, "Введите дорожное покрытие", Snackbar.LENGTH_SHORT).show(); //если пользователь ничего вводит выводится эта ошибка
                    return;
                }
                if (TextUtils.isEmpty(lighting.getText().toString())) {
                    Snackbar.make(newroot, "Введите освещение", Snackbar.LENGTH_SHORT).show(); //если пользователь ничего вводит выводится эта ошибка
                    return;
                }
                if (TextUtils.isEmpty(inform.getText().toString())) {
                    Snackbar.make(newroot, "Введите краткую информацию", Snackbar.LENGTH_SHORT).show(); //если пользователь ничего вводит выводится эта ошибка
                    return;
                }
                //Регистрация ДТП
                auth.createUserWithEmailAndPassword(vidDTP.getText().toString(), date.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                registerDTP newDTP = new registerDTP();
                                newDTP.setVidDTP(vidDTP.getText().toString());
                                newDTP.setDate(date.getText().toString());
                                newDTP.setTime(time.getText().toString());
                                newDTP.setLocation(location.getText().toString());
                                newDTP.setPavement(pavement.getText().toString());
                                newDTP.setLighting(lighting.getText().toString());
                                newDTP.setInform(inform.getText().toString());


                                newdtp.child(FirebaseAuth.getInstance().getCurrentUser().getUid()) //добавление в таблицу пользователей нового пользователя
                                        .setValue(newDTP)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Snackbar.make(newroot, "ДТП добавлено!", Snackbar.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(newroot, "Ошибка" + e.getMessage(), Snackbar.LENGTH_SHORT).show(); //если уже существует пользователь с такой почтой

                    }
                });
            }
        });
        dialog.show();

    }

    private void btndellDTP(View view)
    {
    }

}




