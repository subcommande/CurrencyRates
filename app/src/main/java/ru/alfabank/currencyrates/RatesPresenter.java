package ru.alfabank.currencyrates;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import ru.alfabank.currencyrates.models.RateResponse;

public class RatesPresenter implements RatesContract.Presenter {

    private final RatesContract.Repo repo;
    private RatesContract.View view;

    public RatesPresenter(RatesContract.Repo repo) {
        this.repo = repo;
    }

    @Override
    public void attachView(RatesContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void load(String date) {


        repo.load(date)
                .subscribe(new SingleObserver<RateResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (view != null)
                            view.showLoading();
                    }

                    @Override
                    public void onSuccess(RateResponse response) {
                        if (view != null) view.showData(response.getCurrencies());
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (view != null) view.showError();
                    }
                });

    }
}
