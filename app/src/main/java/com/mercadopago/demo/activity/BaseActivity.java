package com.mercadopago.demo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mercadopago.demo.R;

public abstract class BaseActivity extends AppCompatActivity {

    private ImageView imageViewError;
    private ContentLoadingProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        setUpActionBar();

        imageViewError = findViewById(R.id.baseview_imageview_error);
        progressBar = findViewById(R.id.baseview_progressbar_loading);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    abstract int getLayoutId();

    private void setUpActionBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
    }

    protected void setActionBarTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle(title);
    }

    protected void setSubheaderTitle(String title) {
        TextView activityAction = findViewById(R.id.textview_action);
        activityAction.setText(title);
    }

    protected void showErrorIcon(boolean show) {
        if (imageViewError == null)
            return;

        imageViewError.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    protected void showProgressBar(boolean show) {
        if (progressBar == null)
            return;

        if (show)
            progressBar.show();
        else
            progressBar.hide();
    }

}
