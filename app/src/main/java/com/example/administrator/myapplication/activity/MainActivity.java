package com.example.administrator.myapplication.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.adapter.MainRecycleViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<String> mList;
    private RecyclerView recyclerView;
    private MainRecycleViewAdapter recycleViewAdapter;
    String[] strings=new String[]{
            "通讯",
            "狗狗"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        recyclerView=findViewById(R.id.rv_main);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recycleViewAdapter=new MainRecycleViewAdapter(mList);
        recyclerView.setAdapter(recycleViewAdapter);
    }

    private void initData() {
        mList=new ArrayList<>();
        for (int i=0;i<strings.length;i++){
            mList.add(strings[i]);
        }
    }
}
