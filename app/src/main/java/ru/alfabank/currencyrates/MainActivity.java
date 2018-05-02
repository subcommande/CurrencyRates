package ru.alfabank.currencyrates;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.List;

import javax.inject.Inject;

import ru.alfabank.currencyrates.models.Currencies;

public class MainActivity
        extends AppCompatActivity
        implements RatesContract.View {

    private RateAdapter rateAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View progressBar;
    private View errorTextView;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    @Inject
    RatesContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.component.injects(this);
        setContentView(R.layout.activity_main);

        init();

        setSupportActionBar(toolbar);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                load();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        rateAdapter = new RateAdapter();
        recyclerView.setAdapter(rateAdapter);

        presenter.attachView(this);
        load();
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        swipeRefreshLayout = findViewById(R.id.swipeToRefreshLayout);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        errorTextView = findViewById(R.id.errorTextView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    private void load() {
        presenter.load();
    }

    @Override
    public void showLoading() {
        errorTextView.setVisibility(View.GONE);
    }

    @Override
    public void showError() {
        turnOffSwipeRefresh();
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage(getString(R.string.alert_message));

        alertBuilder.setPositiveButton(R.string.alert_pos_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_SETTINGS));
            }
        });

        alertBuilder.create().show();

    }

    @Override
    public void showData(List<Currencies> currencies) {
        turnOffSwipeRefresh();
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        errorTextView.setVisibility(View.GONE);
        rateAdapter.setItems(currencies);
    }

    private void turnOffSwipeRefresh() {
        if (swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);
    }
}
