package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Document doc;
    private Thread secThread;
    private Runnable runnable;
    private ListView listView;
    private CustomArrayAdapter adapter;
    private List<ListItemClass> arrayList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();





    }
    protected void init () {
        ListView listView = findViewById(R.id.listView);
        arrayList = new ArrayList<>();
        adapter = new CustomArrayAdapter(this, R.layout.list_1, arrayList, getLayoutInflater());
        listView.setAdapter(adapter);
        Runnable runnable = this::getWeb;
        Thread secThread = new Thread(new Runnable() {
            @Override
            public void run() {
                getWeb();
            }
        });
        secThread.start();



    }




    private void getWeb () {

        try {
            Document doc = Jsoup.connect("https://news.sportbox.ru/Vidy_sporta/Futbol/Evropejskie_chempionaty/Germaniya/stats").get();
            Elements tables = doc.getElementsByTag("tbody");
            Element our_table = tables.get(0);
            Elements elements_from_table = our_table.children();
            Element bavaria = elements_from_table.get(0);
            Elements bavaria_elements = bavaria.children();
            Log.d("MyLog", "Tbody size : " + our_table.children().get(0).text());
            for (int i = 0; i < 16; i++) {
                ListItemClass items = new ListItemClass();
                items.setData_1(our_table.children().get(i).child(0).text());
                items.setData_2(our_table.children().get(i).child(1).text());
                items.setData_3(our_table.children().get(i).child(2).text().substring(0, 7));
                items.setData_4(our_table.children().get(i).child(3).text());
                arrayList.add(items);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });



        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
