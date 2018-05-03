package ru.alfabank.currencyrates;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import ru.alfabank.currencyrates.models.Currencies;

public class RateAdapter extends RecyclerView.Adapter<RateAdapter.RateVH> {

    private final List<Currencies> items = new ArrayList<>();

    public RateAdapter() {
        super();
    }

    @NonNull
    @Override
    public RateVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new RateVH(inflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RateVH holder, int position) {
        Currencies rate = items.get(position);
        holder.bind(rate);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    void setItems(List<Currencies> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    class RateVH extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView description;
        private TextView sellRate;
        private TextView buyRate;
        private ImageView arrowSell;
        private ImageView arrowBuy;


        public RateVH(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_rate, parent, false));
            name = itemView.findViewById(R.id.rates_list_item_name);
            description = itemView.findViewById(R.id.rates_list_item_description);
            sellRate = itemView.findViewById(R.id.rates_list_item_sell_rate);
            buyRate = itemView.findViewById(R.id.rates_list_item_buy_rate);
            arrowSell = itemView.findViewById(R.id.arrow_sell);
            arrowBuy = itemView.findViewById(R.id.arrow_buy);
        }

        void bind(Currencies rate) {
            name.setText(rate.code);
            description.setText(rate.description);

            sellRate.setText(FormatUtils.formatAmount(getSellRateByDate(rate, 0)));
            buyRate.setText(FormatUtils.formatAmount(getBuyRateByDate(rate, 0)));

            checkWeekChanges(rate);

        }

        void checkWeekChanges(Currencies rate) {

            if (rate == null) {
                return;
            }

            double currentSellRate;
            double currentBuyRate;
            double previousSellRate;
            double previousBuyRate;

            try {
                currentSellRate = Double.parseDouble(getSellRateByDate(rate, 0));
                currentBuyRate = Double.parseDouble(getBuyRateByDate(rate, 0));
                previousSellRate = Double.parseDouble(getSellRateByDate(rate, rate.ratesByDate.size() - 1));
                previousBuyRate = Double.parseDouble(getBuyRateByDate(rate, rate.ratesByDate.size() - 1));
            } catch (Exception e) {
                arrowBuy.setImageDrawable(itemView.getResources().getDrawable(R.drawable.warning));
                arrowSell.setImageDrawable(itemView.getResources().getDrawable(R.drawable.warning));
                return;
            }

            boolean isSellRateFading = currentSellRate < previousSellRate;

            boolean isBuyRateFading = currentBuyRate < previousBuyRate;

            if (isSellRateFading)
                arrowSell.setImageDrawable(itemView.getResources().getDrawable(R.drawable.down_arrow));
            else
                arrowSell.setImageDrawable(itemView.getResources().getDrawable(R.drawable.up_arrow));

            if (isBuyRateFading)
                arrowBuy.setImageDrawable(itemView.getResources().getDrawable(R.drawable.down_arrow));
            else
                arrowBuy.setImageDrawable(itemView.getResources().getDrawable(R.drawable.up_arrow));

        }

        private String getSellRateByDate(Currencies rate, int date) {
            String toReturn = rate.ratesByDate.get(date).currencyRates.get(0).sellRate;

            return toReturn;
        }
        private String getBuyRateByDate(Currencies rate, int date) {
            String toReturn = rate.ratesByDate.get(date).currencyRates.get(0).buyRate;

            return toReturn;
        }
    }
}