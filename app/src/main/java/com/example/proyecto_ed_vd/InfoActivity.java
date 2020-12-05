package com.example.proyecto_ed_vd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.proyecto_ed_vd.models.ModelInfo;
import com.example.proyecto_ed_vd.models.ModelUser;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class InfoActivity extends AppCompatActivity {
    
    ModelUser fuser;
    Button mostrarBtn;
    ImageView avatarIv, coverIv;
    TextView nameTv, emailTv, phoneTv;
    HashMapUsers hashUsers = new HashMapUsers(31);

    private BarChart barChart;
    private String[] parts = new String[]{"Posts", "Likes", "Comentarios"};
    private int[] nums = new int[3];
    private int[] colors = new int[]{Color.RED, Color.BLUE, Color.GREEN};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mostrarBtn = findViewById(R.id.mostrar);

        barChart = findViewById(R.id.barChart);

        avatarIv = findViewById(R.id.RavatarIv);
        coverIv = findViewById(R.id.RcoverIv);
        nameTv = findViewById(R.id.RnameTv);
        emailTv = findViewById(R.id.RemailTv);
        phoneTv = findViewById(R.id.RphoneTv);

        GetUsersNumPosts();

        fuser = (ModelUser)getIntent().getSerializableExtra("Usuario");

        mostrarBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try{
                    cargar();

                    nums[0] = hashUsers.Get(fuser)[0];
                    nums[1] = hashUsers.Get(fuser)[1];
                    nums[2] = hashUsers.Get(fuser)[2];

                    CreateCharts();

                } catch (Exception e) { }
            }
        });

    }

    private void GetUsersNumPosts() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(final DataSnapshot ds : dataSnapshot.getChildren()){
                    DatabaseReference df = FirebaseDatabase.getInstance().getReference("Posts");
                    df.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                            int contp = 0;
                            int contl = 0;
                            int contc = 0;
                            for(DataSnapshot dp : dataSnapshot1.getChildren()){
                                if(ds.child("uid").getValue().equals(dp.child("uid").getValue())){
                                    contp += 1;
                                    contl += Integer.parseInt(dp.child("pLikes").getValue().toString());
                                    contc += Integer.parseInt(dp.child("pComments").getValue().toString());
                                }
                            }
                            String name = ds.child("name").getValue().toString();       String email = ds.child("email").getValue().toString();
                            String phone = ds.child("phone").getValue().toString();     String tipo_usuario = ds.child("Usuario").getValue().toString();
                            String image = ds.child("image").getValue().toString();     String cover = ds.child("cover").getValue().toString();
                            String uid = ds.child("uid").getValue().toString();

                            ModelUser model = new ModelUser(name,email,phone,tipo_usuario,image,cover,uid);
                            hashUsers.Set(model, contp, contl, contc);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void cargar() {

        try {

            nameTv.setText(fuser.name);
            emailTv.setText(fuser.email);
            phoneTv.setText(fuser.phone);

            try {
                Picasso.get().load(fuser.image).into(avatarIv);
            }catch(Exception e){
                Picasso.get().load(R.drawable.ic_default_img_white).into(avatarIv);
            }
            try {
                Picasso.get().load(fuser.cover).into(coverIv);
            }catch(Exception e){
                Picasso.get().load(R.drawable.ic_default_img_white).into(coverIv);
            }
        } catch (Exception e) {

        }
    }

    private Chart getSameChart(Chart chart, String description, int textColor, int background, int animateY) {
        chart.getDescription().setText(description);
        chart.getDescription().setTextSize(15);
        chart.setBackgroundColor(background);
        chart.animateY(animateY);

        return chart;

    }

    private void legend(Chart chart) {
        Legend legend = chart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);

        ArrayList<LegendEntry> entries = new ArrayList<>();
        for (int i=0; i<parts.length; i++) {
            LegendEntry entry = new LegendEntry();
            entry.formColor = colors[i];
            entry.label = parts[i];
        }
        legend.setCustom(entries);

    }

    private ArrayList<BarEntry> getBarEntries() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i=0; i<nums.length; i++) {
            entries.add(new BarEntry(i, nums[i]));
        }
        return entries;
    }

    private void axisX(XAxis axis) {
        axis.setGranularityEnabled(true);
        axis.setPosition(XAxis.XAxisPosition.BOTTOM);
        axis.setValueFormatter(new IndexAxisValueFormatter(parts));
    }

    private void axisLeft(YAxis axis) {
        axis.setSpaceTop(30);
        axis.setAxisMinimum(0);
    }

    private void axisRight(YAxis axis) {
        axis.setEnabled(false);
    }

    public void CreateCharts() {
        barChart = (BarChart)getSameChart(barChart, "", Color.RED, Color.CYAN, 3000);
        barChart.setDrawGridBackground(true);
        barChart.setDrawBarShadow(true);

        barChart.setData(getBarData());
        barChart.invalidate();

        axisX(barChart.getXAxis());
        axisLeft(barChart.getAxisLeft());
        axisRight(barChart.getAxisRight());

    }

    private DataSet getData(DataSet dataSet) {
        dataSet.setColors(colors);
        dataSet.setValueTextSize(Color.WHITE);
        dataSet.setValueTextSize(10);

        return dataSet;
    }

    private BarData getBarData() {
        BarDataSet barDataSet = (BarDataSet)getData(new BarDataSet(getBarEntries(), ""));

        barDataSet.setBarShadowColor(Color.GRAY);
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.45f);
        return barData;
    }

}