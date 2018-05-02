package ru.alfabank.currencyrates;

import java.util.List;

import io.reactivex.Single;
import ru.alfabank.currencyrates.models.Currencies;
import ru.alfabank.currencyrates.models.RateResponse;

public interface RatesContract {

    interface View {

        void showLoading();

        void showError();

        void showData(List<Currencies> currencies);
    }

    interface Presenter {

        void attachView(View view);

        void detachView();

        void load(String date);
    }

    interface Repo {

        Single<RateResponse> load(String date);
    }
}
