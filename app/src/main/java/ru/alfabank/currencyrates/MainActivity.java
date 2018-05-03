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
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
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
    private EditText dayText;
    private EditText monthText;
    private EditText yearText;
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
                if (checkEditTexts())
                    load();
                else {
                    createDateToast();
                    turnOffSwipeRefresh();
                }
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        rateAdapter = new RateAdapter();
        recyclerView.setAdapter(rateAdapter);

        presenter.attachView(this);
        load();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkEditTexts())
            load();
        else
            createDateToast();
    }

    private void init() {
        dayText = findViewById(R.id.day);
        monthText = findViewById(R.id.month);
        yearText = findViewById(R.id.year);
        toolbar = findViewById(R.id.toolbar);
        swipeRefreshLayout = findViewById(R.id.swipeToRefreshLayout);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        errorTextView = findViewById(R.id.errorTextView);

        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateFromTexts = sdf.format(now);
        dayText.setText(dateFromTexts.split("-")[2]);
        monthText.setText(dateFromTexts.split("-")[1]);
        yearText.setText(dateFromTexts.split("-")[0]);
    }

    private boolean checkEditTexts() {
        if (dayText.getText().toString().equals("") || dayText.getText().toString().contains(",")) {
            return false;
        }
        if (monthText.getText().toString().equals("") || monthText.getText().toString().contains(",")) {
            return false;
        }
        if (yearText.getText().toString().equals("") || yearText.getText().toString().contains(",")) {
            return false;
        }

        if (Integer.parseInt(dayText.getText().toString()) > 31 || Integer.parseInt(dayText.getText().toString()) == 0) {
            return false;
        }
        if (Integer.parseInt(monthText.getText().toString()) > 12 || Integer.parseInt(monthText.getText().toString()) == 0 ) {
            return false;
        }
        if (Integer.parseInt(yearText.getText().toString()) > new Date().getYear() + 1900 || Integer.parseInt(yearText.getText().toString()) == 0) {
            return false;
        }

        if (Integer.parseInt(yearText.getText().toString()) < new Date().getYear() + 1900 - 10){
            return false;
        }
        if (Integer.parseInt(yearText.getText().toString()) == (new Date().getYear() + 1900)) {

            if (Integer.parseInt(monthText.getText().toString()) > new Date().getMonth() + 1) {
                return false;
            }
            if (Integer.parseInt(monthText.getText().toString()) == new Date().getMonth() + 1){
                if (Integer.parseInt(dayText.getText().toString()) > new Date().getDate()) {
                    return false;
                }
            }
        }
        return true;
    }

    private void createDateToast() {
        Toast.makeText(this, "Wrong date.", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    private void load() {
        StringBuilder sbDate = new StringBuilder();
        sbDate.append(yearText.getText()).append('-');
        sbDate.append(monthText.getText()).append('-');
        sbDate.append(dayText.getText());

        presenter.load(sbDate.toString());
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
                finish();
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
