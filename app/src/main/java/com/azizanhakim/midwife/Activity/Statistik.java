package com.azizanhakim.midwife.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.azizanhakim.midwife.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;

public class Statistik extends AppCompatActivity{
    private Firebase mSkor1, mSkor2, mSkor3, mSkor4, mSkor5, mSkor6, mSkor7;
    private String Skor1, Skor2, Skor3, Skor4, Skor5, Skor6, Skor7, hr1, hr2, hr3, hr4, hr5, hr6, hr7;
    private int n1, n2, n3, n4, n5, n6, n7, h0, h1, h2, h3, h4, h5, h6, h7;

    public Statistik(){
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistik);
        updateNilai();

        //kembali ke Halaman Utama
        ImageView keluar = findViewById(R.id.kembali);
        keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent kembali = new Intent(Statistik.this, MainActivity.class);
                startActivity(kembali);
            }
        });
    }

    public void tampilChart(){
        Calendar kalender = Calendar.getInstance();
        int hari = kalender.get(Calendar.DAY_OF_WEEK);
        switch (hari){
            case Calendar.SUNDAY:
                h1 = 0;
                h1=n1; h2=n2; h3=n3; h4=n4; h5=n5; h6=n6; h7=n7;
                hr1 = "Minggu"; hr2 = "Senin"; hr3 = "Selasa"; hr4 = "Rabu"; hr5 = "Kamis"; hr6 = "Jumat"; hr7 = "Sabtu";
                break;
            case Calendar.MONDAY:
                h1 = 0;
                h1=n2; h2=n3; h3=n4; h4=n5; h5=n6; h6=n7; h7=n1;
                hr7 = "Minggu"; hr1 = "Senin"; hr2 = "Selasa"; hr3 = "Rabu"; hr4 = "Kamis"; hr5 = "Jumat"; hr6 = "Sabtu";
                break;
            case Calendar.TUESDAY:
                h1 = 0;
                h1=n3; h2=n4; h3=n5; h4=n6; h5=n7; h6=n1; h7=n2;
                hr6 = "Minggu"; hr7 = "Senin"; hr1 = "Selasa"; hr2 = "Rabu"; hr3 = "Kamis"; hr4 = "Jumat"; hr5 = "Sabtu";
                break;
            case Calendar.WEDNESDAY:
                h1 = 0;
                h1=n4; h2=n5; h3=n6; h4=n7; h5=n1; h6=n2;h7=n3;
                hr5 = "Minggu"; hr6 = "Senin"; hr7 = "Selasa"; hr1 = "Rabu"; hr2 = "Kamis"; hr3 = "Jumat"; hr4 = "Sabtu";
                break;
            case Calendar.THURSDAY:
                h1 = 0;
                h1=n5; h2=n6; h3=n7; h4=n1; h5=n2; h6=n3; h7=n4;
                hr4 = "Minggu"; hr5 = "Senin"; hr6 = "Selasa"; hr7 = "Rabu"; hr1 = "Kamis"; hr2 = "Jumat"; hr3 = "Sabtu";
                break;
            case Calendar.FRIDAY:
                h1 = 0;
                h1=n6; h2=n7; h3=n1; h4=n2; h5=n3; h6=n4; h7=n5;
                hr3 = "Minggu"; hr4 = "Senin"; hr5 = "Selasa"; hr6 = "Rabu"; hr7 = "Kamis"; hr1 = "Jumat"; hr2 = "Sabtu";
                break;
            case Calendar.SATURDAY:
                h1 = 0;
                h1=n7; h2=n1; h3=n2; h4=n3; h5=n4; h6=n5; h7=n6;
                hr2 = "Minggu"; hr3 = "Senin"; hr4 = "Selasa"; hr5 = "Rabu"; hr6 = "Kamis"; hr7 = "Jumat"; hr1 = "Sabtu";
                break;
        }
        BarChart barChart = findViewById(R.id.barchart);
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(h2, h0));
        entries.add(new BarEntry(h3, h0+1));
        entries.add(new BarEntry(h4, h0+2));
        entries.add(new BarEntry(h5, h0+3));
        entries.add(new BarEntry(h6, h0+4));
        entries.add(new BarEntry(h7, h0+5));
        entries.add(new BarEntry(h1, h0+6));

        BarDataSet bardataset = new BarDataSet(entries, "Cells");

        ArrayList<String> labels = new ArrayList<String>();
        labels.add(hr2);
        labels.add(hr3);
        labels.add(hr4);
        labels.add(hr5);
        labels.add(hr6);
        labels.add(hr7);
        labels.add(hr1);

        BarData data = new BarData(labels, bardataset);
        barChart.setData(data); // set the data and list of lables into chart

        barChart.setDescription("Data Nilai 1 Minggu Terakhir.");  // set the description

        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);

        barChart.animateY(5000);

    }

    public void tampungNilai1 (String s1){
        n1 = Integer.parseInt(s1);
        tampilChart();
    }
    public void tampungNilai2 (String s1){
        n2 = Integer.parseInt(s1);
        tampilChart();
    }
    public void tampungNilai3 (String s1){
        n3 = Integer.parseInt(s1);
        tampilChart();
    }
    public void tampungNilai4 (String s1){
        n4 = Integer.parseInt(s1);
        tampilChart();
    }
    public void tampungNilai5 (String s1){
        n5 = Integer.parseInt(s1);
        tampilChart();
    }
    public void tampungNilai6 (String s1){
        n6 = Integer.parseInt(s1);
        tampilChart();
    }
    public void tampungNilai7 (String s1){
        n7 = Integer.parseInt(s1);
        tampilChart();
    }

    public void updateNilai(){
        //hari minggu
        mSkor1 = new Firebase("https://chatbot-kebidanan.firebaseio.com/skor/skor0");
        mSkor1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Skor1 = dataSnapshot.getValue(String.class);
                tampungNilai1(Skor1);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }

        });

        //hari senin
        mSkor2 = new Firebase("https://chatbot-kebidanan.firebaseio.com/skor/skor1");
        mSkor2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Skor2 = dataSnapshot.getValue(String.class);
                tampungNilai2(Skor2);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }

        });

        //hari selasa
        mSkor3 = new Firebase("https://chatbot-kebidanan.firebaseio.com/skor/skor2");
        mSkor3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Skor3 = dataSnapshot.getValue(String.class);
                tampungNilai3(Skor3);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }

        });

        //hari rabu
        mSkor4 = new Firebase("https://chatbot-kebidanan.firebaseio.com/skor/skor3");
        mSkor4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Skor4 = dataSnapshot.getValue(String.class);
                tampungNilai4(Skor4);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }

        });

        //hari kamis
        mSkor5 = new Firebase("https://chatbot-kebidanan.firebaseio.com/skor/skor4");
        mSkor5.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Skor5 = dataSnapshot.getValue(String.class);
                tampungNilai5(Skor5);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }

        });

        //hari jumat
        mSkor6 = new Firebase("https://chatbot-kebidanan.firebaseio.com/skor/skor5");
        mSkor6.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Skor6 = dataSnapshot.getValue(String.class);
                tampungNilai6(Skor6);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }

        });

        //hari sabtu
        mSkor7 = new Firebase("https://chatbot-kebidanan.firebaseio.com/skor/skor6");
        mSkor7.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Skor7 = dataSnapshot.getValue(String.class);
                tampungNilai7(Skor7);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }

        });
    }
}
