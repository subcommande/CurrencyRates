package ru.alfabank.currencyrates;

import org.junit.Test;
import static org.assertj.core.api.Assertions.*;
public class FormatUtilsTest {

    @Test
    public void formatAmount() {
        assertThat(FormatUtils.formatAmount("10.33")).isEqualTo("10.33");
        //assertThat(FormatUtils.formatAmount("10")).containsOnlyDigits();
        assertThat(FormatUtils.formatAmount("100.13")).isEqualTo("100.13");
        assertThat(FormatUtils.formatAmount("2000.00")).isEqualTo("2 000");
        assertThat(FormatUtils.formatAmount("40000.00")).isEqualTo("40 000");
    }
}