package com.azizanhakim.midwife.Activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.azizanhakim.midwife.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Calendar;

public class Quiz extends AppCompatActivity {

    private TextView mScoreView;
    private TextView mQuestion;
    private TextView mNoSoal;
    private Button mButtonChoice1, mButtonChoice2, mButtonChoice3, mButtonChoice4;
    private int mScore, mSoal, mQuestionNumber, idHari, idSoal = 0;
    private String mAnswer;
    private Firebase mQuestionRef, mChoice1Ref, mChoice2Ref, mChoice3Ref, mChoice4Ref, mAnswerRef, simpanSkor;

    public Quiz() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz);

        mScoreView = (TextView)findViewById(R.id.score);
        mQuestion = (TextView)findViewById(R.id.question);
        mNoSoal = (TextView) findViewById(R.id.soal);

        mButtonChoice1 = (Button)findViewById(R.id.choice1);
        mButtonChoice2 = (Button)findViewById(R.id.choice2);
        mButtonChoice3 = (Button)findViewById(R.id.choice3);
        mButtonChoice4 = (Button)findViewById(R.id.choice4);

        Calendar kalender = Calendar.getInstance();
        int hari = kalender.get(Calendar.DAY_OF_WEEK);
        switch (hari){
            case Calendar.SUNDAY:
                idHari = 0;
                idSoal = 0;
                break;
            case Calendar.MONDAY:
                idHari = 1;
                idSoal = 1;
                break;
            case Calendar.TUESDAY:
                idHari = 2;
                idSoal = 2;
                break;
            case Calendar.WEDNESDAY:
                idHari = 3;
                idSoal = 3;
                break;
            case Calendar.THURSDAY:
                idHari = 4;
                idSoal = 4;
                break;
            case Calendar.FRIDAY:
                idHari = 5;
                idSoal = 5;
                break;
            case Calendar.SATURDAY:
                idHari = 6;
                idSoal = 6;
                break;
        }
        //inisiasi no soal
        mQuestionNumber = 1;

        updatePertanyaan();
        tambahSkor();

        //kembali ke Halaman Utama
        TextView keluar = findViewById(R.id.kembali);
        keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Simpan Skor ke Firebase
                simpanSkor = new Firebase("https://chatbot-kebidanan.firebaseio.com/skor");
                Firebase skor = simpanSkor.child("skor"+idHari);
                skor.setValue(mScore);

                //Tampilkan Total Skor
                Toast.makeText(Quiz.this, "Anda keluar sebelum quiz berakhir. Total skor: "+mScore, Toast.LENGTH_SHORT).show();

                Intent kembali = new Intent(Quiz.this, MainActivity.class);
                startActivity(kembali);
            }
        });
    }

    private void tambahSkor(){
        final MediaPlayer suaraBenarPlay = MediaPlayer.create(this, R.raw.suarabenar);
        final MediaPlayer suaraSalahPlay = MediaPlayer.create(this, R.raw.suarasalah);

        //Button 1
        mButtonChoice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mButtonChoice1.getText().equals(mAnswer)) {
                    mScore = mScore + 1;
                    updateSkor(mScore);
                    suaraBenarPlay.start();
                    updatePertanyaan();
                }else {
                    updatePertanyaan();
                    suaraSalahPlay.start();
                }
            }
        });

        //Button 2
        mButtonChoice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mButtonChoice2.getText().equals(mAnswer)) {
                    mScore = mScore + 1;
                    updateSkor(mScore);
                    suaraBenarPlay.start();
                    updatePertanyaan();
                }else {
                    updatePertanyaan();
                    suaraSalahPlay.start();
                }
            }
        });

        //Button 3
        mButtonChoice3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mButtonChoice3.getText().equals(mAnswer)) {
                    mScore = mScore + 1;
                    updateSkor(mScore);
                    suaraBenarPlay.start();
                    updatePertanyaan();
                }else {
                    updatePertanyaan();
                    suaraSalahPlay.start();
                }
            }
        });

        //Button 4
        mButtonChoice4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mButtonChoice4.getText().equals(mAnswer)) {
                    mScore = mScore + 1;
                    updateSkor(mScore);
                    suaraBenarPlay.start();
                    updatePertanyaan();
                }else {
                    updatePertanyaan();
                    suaraSalahPlay.start();
                }
            }
        });
    }

    private void updateSkor(int score) {
        mScoreView.setText(""+mScore);
    }

    public void updatePertanyaan() {
        //question
        mQuestionRef = new Firebase("https://chatbot-kebidanan.firebaseio.com/"+ idSoal +"/question");

        mQuestionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String question = dataSnapshot.getValue(String.class);
                mQuestion.setText(question);
                mNoSoal.setText(""+mSoal);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        //choice 1
        mChoice1Ref = new Firebase("https://chatbot-kebidanan.firebaseio.com/"+ idSoal +"/choice1");

        mChoice1Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String choice = dataSnapshot.getValue(String.class);
                mButtonChoice1.setText(choice);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        //choice 2
        mChoice2Ref = new Firebase("https://chatbot-kebidanan.firebaseio.com/"+ idSoal +"/choice2");

        mChoice2Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String choice = dataSnapshot.getValue(String.class);
                mButtonChoice2.setText(choice);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        //choice 3
        mChoice3Ref = new Firebase("https://chatbot-kebidanan.firebaseio.com/"+ idSoal +"/choice3");

        mChoice3Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String choice = dataSnapshot.getValue(String.class);
                mButtonChoice3.setText(choice);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        //choice 4
        mChoice4Ref = new Firebase("https://chatbot-kebidanan.firebaseio.com/"+ idSoal +"/choice4");

        mChoice4Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String choice = dataSnapshot.getValue(String.class);
                mButtonChoice4.setText(choice);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        //answer
        mAnswerRef = new Firebase("https://chatbot-kebidanan.firebaseio.com/"+ idSoal +"/answer");

        mAnswerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mAnswer = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        idSoal=idSoal+7;
        mQuestionNumber++;
        mSoal++;

        if(mQuestionNumber > 8){
            //Simpan Skor ke Firebase
            simpanSkor = new Firebase("https://chatbot-kebidanan.firebaseio.com/skor");
            Firebase skor = simpanSkor.child("skor"+idHari);
            skor.setValue(mScore);

            //Tampilkan Total Skor
            Toast.makeText(this, "Total skor quiz anda adalah: "+mScore, Toast.LENGTH_SHORT).show();

            //Kembali ke Halaman Utama
            finish();
            Intent kembali = new Intent(this, MainActivity.class);
            startActivity(kembali);
        }

    }

}
