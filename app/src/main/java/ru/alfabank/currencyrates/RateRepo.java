package ru.alfabank.currencyrates;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.alfabank.currencyrates.models.RateResponse;

public class RateRepo implements RatesContract.Repo {

    private final Api api;
    private Single<RateResponse> cache;

    public RateRepo(Api api) {
        this.api = api;
    }

    @Override
    public Single<RateResponse> load(String date) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //Date date = new Date();
        //date.setDate(date.getDate() - 7);
        Date current = new Date();
        Date weekAgo;
        try {
            current = sdf.parse(date);
        } catch (Exception e) {
            //
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(current);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 7);
        weekAgo = calendar.getTime();

        String dateWeekAgo = sdf.format(weekAgo) + "T00:00:00.000+0400";
        String dateCurrent = date + "T23:59:59.000+0400";

        Map<String, String> requestBody = new HashMap<>();

        requestBody.put("operationId", "Currency:GetCurrencyRates");
        requestBody.put("dateTo", dateCurrent);
        requestBody.put("dateFrom", dateWeekAgo);

        cache = api.loadRates(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache();

        return cache;
    }
}
