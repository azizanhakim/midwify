package com.azizanhakim.midwife.Activity;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.azizanhakim.midwife.R;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    private Dialog myDialog;
    private FirebaseAuth mAuth;
    private long backPressedTime;
    private Toast backToast;
    private TextView txtclose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        // pilih layout untuk kelas ini
        setContentView(R.layout.activity_main);

        //cek apakah user sudah login
        if (mAuth.getCurrentUser() == null || mAuth.getCurrentUser().isEmailVerified() == false) {
            finish();
            startActivity(new Intent(this, Login.class));
        }
        //Set Listener untuk masing-masing menu
        findViewById(R.id.menu1).setOnClickListener(this);
        findViewById(R.id.menu2).setOnClickListener(this);
        findViewById(R.id.menu3).setOnClickListener(this);
        /*findViewById(R.id.pengguna).setOnClickListener(this);*/
        findViewById(R.id.keluar).setOnClickListener(this);
        myDialog = new Dialog(this);
    };

    //pesan konfirmasi keluar
    @Override
    public void onBackPressed(){
        if ( backPressedTime + 2000 > System.currentTimeMillis()){
            backToast.cancel();
            super.onBackPressed();
            return;
        }else{
            backToast = Toast.makeText(getBaseContext(),"Tekan kembali sekali lagi untuk keluar",Toast.LENGTH_SHORT);
            backToast.show();
        }

        backPressedTime = System.currentTimeMillis();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //pindah ke menu Midwify
            case R.id.menu1:
                startActivity(new Intent(this, Midwify.class));
                break;

            //pindah ke menu Quiz
            case R.id.menu2:
                startActivity(new Intent(this, Quiz.class));
                break;

            //pindah ke menu Statistik
            case R.id.menu3:
                startActivity(new Intent(this, Statistik.class));
                break;

            //pindah ke menu Pengguna
            /*case R.id.pengguna:
                startActivity(new Intent(this, ProfileActivity.class));
                break;*/

            //logout
            case R.id.keluar:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(MainActivity.this, Login.class));
        }
    }

    //menampilkan pop up menu panduan penggunaan
    public void showPopup(View v){
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.daftar_penggunaan);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.midwifychat:

                myDialog.setContentView(R.layout.popup_chat);
                txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
                txtclose.setText("X");
                txtclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
                    }
                });
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.show();
            return true;

            case R.id.midwifyvoice:
                myDialog.setContentView(R.layout.popup_voice);
                txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
                txtclose.setText("X");
                txtclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
                    }
                });
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.show();
                return true;

            case R.id.midwifyquiz:
                myDialog.setContentView(R.layout.popup_quiz);
                txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
                txtclose.setText("X");
                txtclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
                    }
                });
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.show();
                return true;

            default:
                return false;
        }
    }
}
