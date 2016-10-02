package cecelia.lesson7hw;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;

import butterknife.BindView;
import butterknife.ButterKnife;


import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.Console;
import java.util.ArrayList;

public class MainActivityFragment extends Fragment {

    @BindView(R.id.stock_list)
    ListView stockList;
    @BindView(R.id.add_stock_button)
    FloatingActionButton addButton;
    @BindView(R.id.refresh_stock_button)
    FloatingActionButton refreshButton;

    StockHelper stockHelper;
    StockItemAdapter adapter;

    SQLiteDatabase writeDb;
    SQLiteDatabase readDb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        this.stockHelper = new StockHelper(getActivity());
        this.writeDb = stockHelper.getWritableDatabase();
        this.readDb = stockHelper.getReadableDatabase();

        this.adapter = createStockItemAdapter();

        createListView(view);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ArrayList<StockItem> stocks = new ArrayList<StockItem>();
                for (int i = 0; i < stockList.getAdapter().getCount(); i ++) {
                    StockItem item = (StockItem) stockList.getAdapter().getItem(i);
                    int position = adapter.getPosition(item);
                    adapter.remove(item);
                    requestForPrice(item.ticker, position);
                }
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createStockItemAlertDialog();
            }
        });
        return view;
    }

    private void createListView(View view) {
        stockList.setAdapter(this.adapter);
    }

    private StockItemAdapter createStockItemAdapter() {
        ArrayList<StockItem> stocks = readItemsFromDatabase();
        return new StockItemAdapter(getActivity(), stocks, this.stockHelper);
    }

    private ArrayList<StockItem> readItemsFromDatabase() {
        SQLiteDatabase readDb = this.stockHelper.getReadableDatabase();
        ArrayList<StockItem> allItems = new ArrayList<StockItem>();
        Cursor toDoDbCursor = readDb.query(StockContract.StockEntry.TABLE_NAME, StockHelper.itemProjection, null, null, null, null, null, null);
        Boolean hasItems = toDoDbCursor.moveToFirst();
        Boolean atLastElement = false;

        while (!atLastElement && hasItems) {
            addDbToDoItemtoList(allItems, toDoDbCursor);
            atLastElement = toDoDbCursor.isLast();
            toDoDbCursor.moveToNext();
        }

        return allItems;
    }

    private void addDbToDoItemtoList(ArrayList<StockItem> allItems, Cursor toDoDbCursor) {
        String itemTicker = toDoDbCursor.getString(
                toDoDbCursor.getColumnIndexOrThrow(StockContract.StockEntry.COLUMN_NAME_TICKER)
        );
        String itemPrice =  toDoDbCursor.getString(
                toDoDbCursor.getColumnIndexOrThrow(StockContract.StockEntry.COLUMN_NAME_PRICE)
        );
        StockItem newStock = new StockItem(itemTicker, itemPrice);
        allItems.add(newStock);
//        requestForPrice(newStock);
    }

    public void createStockItemAlertDialog() {

        AlertDialog.Builder builder = createBuilder();
        View layout = setBuilderCustomLayout(builder);

        EditText stockInput = (EditText) layout.findViewById(R.id.add_stock_input);
        setDialogPositiveNegativeButtons(builder, stockInput);

        AlertDialog alert = builder.create();
        alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        alert.show();
    }

    private void setDialogPositiveNegativeButtons(AlertDialog.Builder builder, final EditText stockInput) {
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String newStockTicker = stockInput.getText().toString();
                createStockItem(newStockTicker);

            }
        });
    }

    private View setBuilderCustomLayout(AlertDialog.Builder builder) {
        LayoutInflater inflater = LayoutInflater.from(this.getActivity());
        View layout = inflater.inflate(R.layout.add_stock_dialog, null);
        builder.setView(layout);
        return layout;
    }

    private AlertDialog.Builder createBuilder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setCancelable(true);
        return builder;
    }

    public void createStockItem(String ticker) {
        requestForPrice(ticker, -1);
    }

    public void requestForPrice(final String ticker, final int position) {
        String url = createUrlforTicker(ticker);
//        final StockItemAdapter adapter = this.adapter;
//        final StockItemAdapter adapter = this.adapter;
//        final TextView priceView = (TextView) getViewByPosition(position).findViewById(R.id.stock_price);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        handleResponse(response, position, ticker);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }

                });

        MySingleton.getInstance(this.getActivity()).addToRequestQueue(jsonObjectRequest);
    }

    private void handleResponse(JSONObject response, int position, String ticker) {
        try {
            String priceFromResponse = response.getString("l");
            if (position < 0 ) {
                createNewStockItem(ticker, priceFromResponse);
            } else {
                editPriceInDatabase(position, priceFromResponse);
                this.adapter.insert(new StockItem(ticker, priceFromResponse), position);
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void createNewStockItem(String ticker, String price) {
            StockItem newStock = new StockItem(ticker, price);
            this.adapter.add(newStock);
            createItemInDatabase(newStock);
    }

    public String createUrlforTicker(String ticker) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("finance.google.com")
                .appendPath("finance")
                .appendPath("info")
                .appendQueryParameter("client", "iq")
                .appendQueryParameter("q", ticker);
        return builder.build().toString();
    }

    private void createItemInDatabase(StockItem stockItem) {
        ContentValues values = new ContentValues();
        values.put(StockContract.StockEntry.COLUMN_NAME_TICKER, stockItem.ticker);
        values.put(StockContract.StockEntry.COLUMN_NAME_PRICE, stockItem.price);
        this.writeDb.insert(StockContract.StockEntry.TABLE_NAME, null, values);
    }

    private void editPriceInDatabase(int position, String newPrice) {
        ContentValues values = new ContentValues();
        values.put(StockContract.StockEntry.COLUMN_NAME_PRICE, newPrice);

        String selection = StockHelper.ID_SELECTION_QUERY;
        String[] selectionArgs = {getItemDbId(position)};

        this.readDb.update(StockContract.StockEntry.TABLE_NAME, values, selection, selectionArgs);
    }


    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    //taken from http://stackoverflow.com/questions/24811536/android-listview-get-item-view-by-position
    public View getViewByPosition(int pos) {
        final int firstListItemPosition = stockList.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + stockList.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return stockList.getAdapter().getView(pos, null, stockList);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return stockList.getChildAt(childIndex);
        }
    }

    private String getItemDbId(int position) {
        Cursor toDoDbCursor = readDb.query(StockContract.StockEntry.TABLE_NAME, StockHelper.itemProjection, null, null, null, null, null, null);
        toDoDbCursor.moveToPosition(position);
        String id = toDoDbCursor.getString(
                toDoDbCursor.getColumnIndexOrThrow(StockContract.StockEntry._ID)
        );
        return id;
    }
}
