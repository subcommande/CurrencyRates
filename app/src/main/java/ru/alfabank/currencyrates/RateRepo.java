package ru.alfabank.currencyrates;

import java.text.SimpleDateFormat;
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
    public Single<RateResponse> load(boolean refresh) {
        if (refresh) cache = null;
        if (cache == null) {
            Map<String, String> requestBody = new HashMap<>();

            requestBody.put("operationId", "Currency:GetCurrencyRates");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            date.setDate(date.getDate() - 7);
            String currentDate = sdf.format(date) + "T00:00:00.000+0400";
            requestBody.put("dateFrom", String.format("%s", currentDate));

            cache = api.loadRates(requestBody)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .cache();
        }
        return cache;
    }
}
