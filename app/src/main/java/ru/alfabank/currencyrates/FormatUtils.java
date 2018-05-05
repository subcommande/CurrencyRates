package ru.alfabank.currencyrates;

import android.widget.EditText;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;

public class FormatUtils {
    private static DecimalFormat formatter;

    static {
        formatter = new DecimalFormat();
        formatter.setGroupingUsed(true);
        formatter.setGroupingSize(3);
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        symbols.setGroupingSeparator(' ');
        formatter.setDecimalFormatSymbols(symbols);
        formatter.setRoundingMode(RoundingMode.DOWN);
    }

    public static boolean checkEditTexts(EditText dayText, EditText monthText, EditText yearText) {
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

    public static String formatAmount(String amount) {
        if ((amount == null) || (amount == "")) {
            return "load_err";
        }

        if (!amount.contains(".00")){
            formatter.setMinimumFractionDigits(2);
            formatter.setMaximumFractionDigits(2);
        }
        else{
            formatter.setMaximumFractionDigits(0);
            formatter.setMinimumFractionDigits(0);
        }
        return formatter.format(new BigDecimal(amount));
    }
}
