package com.madproject.imagesearch;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText searchInput;
    private Button searchButton, loadMoreButton;
    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    private List<ImageItem> imageList;

    private String query = "";
    private int page = 1;
    private static final String ACCESS_KEY = "OgSwjgU0WyYVGXz3-UeVm-3Cz6zECSnNRTZfnrvqKgg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchInput = findViewById(R.id.search_input);
        searchButton = findViewById(R.id.search_button);
        loadMoreButton = findViewById(R.id.load_more_button);
        recyclerView = findViewById(R.id.recycler_view);

        imageList = new ArrayList<>();
        imageAdapter = new ImageAdapter(this, imageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(imageAdapter);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        searchButton.setOnClickListener(v -> {
            query = searchInput.getText().toString().trim();
            if (!query.isEmpty()) {
                page = 1;
                imageList.clear();
                fetchImages(requestQueue);
            } else {
                Toast.makeText(this, "Enter a search term", Toast.LENGTH_SHORT).show();
            }
        });

        loadMoreButton.setOnClickListener(v -> fetchImages(requestQueue));
    }

    private void fetchImages(RequestQueue requestQueue) {
        String url = "https://api.unsplash.com/search/photos?page=" + page + "&query=" + query + "&client_id=" + ACCESS_KEY;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray results = response.getJSONArray("results");
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject result = results.getJSONObject(i);
                            String imageUrl = result.getJSONObject("urls").getString("small");
                            String description = result.getString("alt_description");
                            imageList.add(new ImageItem(imageUrl, description));
                        }
                        imageAdapter.notifyDataSetChanged();
                        page++;
                        loadMoreButton.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Error fetching images", Toast.LENGTH_SHORT).show());

        requestQueue.add(jsonObjectRequest);
    }
}
