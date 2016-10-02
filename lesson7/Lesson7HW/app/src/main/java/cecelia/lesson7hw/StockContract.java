package cecelia.lesson7hw;

import android.provider.BaseColumns;

/**
 * Created by Cecelia on 9/19/16.
 */
public class StockContract {

    private StockContract() {
    }

    public static class StockEntry implements BaseColumns {
        public static final String TABLE_NAME = "stock";
        public static final String COLUMN_NAME_TICKER = "stock_ticker";
        public static final String COLUMN_NAME_PRICE = "stock_price";
    }
}
