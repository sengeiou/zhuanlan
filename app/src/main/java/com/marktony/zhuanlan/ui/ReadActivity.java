package com.marktony.zhuanlan.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.marktony.zhuanlan.R;

import org.json.JSONException;
import org.json.JSONObject;


public class ReadActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FloatingActionButton fab;
    private WebView wbMain;
    private ImageView ivHeader;
    private CollapsingToolbarLayout toolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        initViews();

        Intent intent = getIntent();

        String imgUrl = intent.getStringExtra("img_url");
        String title = intent.getStringExtra("title");
        String slug = intent.getStringExtra("slug");

        setCollapsingToolbarLayoutTitle(title);

        Glide.with(ReadActivity.this).load(imgUrl).centerCrop().into(ivHeader);

        wbMain.getSettings().setJavaScriptEnabled(true);
        //缩放,设置为不能缩放可以防止页面上出现放大和缩小的图标
        wbMain.getSettings().setBuiltInZoomControls(false);
        //缓存
        wbMain.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        //开启DOM storage API功能
        wbMain.getSettings().setDomStorageEnabled(true);
        //开启application Cache功能
        wbMain.getSettings().setAppCacheEnabled(false);
        //不调用第三方浏览器即可进行页面反应
        wbMain.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                wbMain.loadUrl(url);
                return true;
            }
        });

        JsonObjectRequest re  = new JsonObjectRequest(Request.Method.GET, "https://zhuanlan.zhihu.com/api/posts/" + slug, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    String content = jsonObject.getString("content");
                    String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/master.css\" type=\"text/css\">";
                    String html = "<!DOCTYPE html>\n"
                            + "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">\n"
                            + "<head>\n"
                            + "\t<meta charset=\"utf-8\" />\n</head>\n"
                            + css
                            + "\n<body>"
                            + content
                            + "</body>\n</html>";

                    wbMain.loadDataWithBaseURL(null,html,"text/html","utf-8",null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        Volley.newRequestQueue(getApplicationContext()).add(re);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void initViews() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        wbMain = (WebView) findViewById(R.id.wb_main);
        ivHeader = (ImageView) findViewById(R.id.iv_header);
    }

    // to change the title's font size of toolbar layout
    private void setCollapsingToolbarLayoutTitle(String title) {
        toolbarLayout.setTitle(title);
        toolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        toolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        toolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBarPlus1);
        toolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBarPlus1);
    }

}