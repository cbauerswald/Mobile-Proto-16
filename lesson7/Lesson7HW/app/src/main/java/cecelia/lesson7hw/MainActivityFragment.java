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

    StockItemAdapter adapter;

    StockHelper stockHelper;
    SQLiteDatabase writeDb;
    SQLiteDatabase readDb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        initializeDatabaseAndHelpers();
        initializeStockItemAdapter();
        setOnClickListeners();

        return view;
    }

    //initialization methods

    public void initializeDatabaseAndHelpers() {
        this.stockHelper = new StockHelper(getActivity());
        this.writeDb = stockHelper.getWritableDatabase();
        this.readDb = stockHelper.getReadableDatabase();
    }

    private void initializeStockItemAdapter() {
        ArrayList<StockItem> stocks = readItemsFromDatabase();
        this.adapter = new StockItemAdapter(getActivity(), stocks, this.stockHelper);
        stockList.setAdapter(this.adapter);
    }

    public void setOnClickListeners() {
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshStocks();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createStockItemAlertDialog();
            }
        });

    }

    //main methods -- refreshing stocks or creating a new stock
    public void refreshStocks() {
        for (int i = 0; i < stockList.getAdapter().getCount(); i ++) {
            StockItem item = (StockItem) stockList.getAdapter().getItem(i);
            int position = this.adapter.getPosition(item);
            this.adapter.remove(item);
            requestForPrice(item.ticker, position);
        }
    }

    public void createStockItem(String ticker) {
        requestForPrice(ticker, -1);
    }

    //Add stock alert dialog and alert dialog helper methods

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

    //Sending and handling API request and helper methods

    public void requestForPrice(final String ticker, final int position) {
        String url = generateUrlforTicker(ticker);

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
                        error.printStackTrace();
                    }

                });

        MySingleton.getInstance(this.getActivity()).addToRequestQueue(jsonObjectRequest);
    }

    private void handleResponse(JSONObject response, int position, String ticker) {
        try {
            String priceFromResponse = response.getString("l");
            if (position < 0 ) {
                StockItem newStock = createNewListViewStockItem(ticker, priceFromResponse);
                createItemInDatabase(newStock);
            } else {
                reinsertEditedStockItemIntoListView(ticker, priceFromResponse, position);
                editPriceInDatabase(position, priceFromResponse);
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public String generateUrlforTicker(String ticker) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("finance.google.com")
                .appendPath("finance")
                .appendPath("info")
                .appendQueryParameter("client", "iq")
                .appendQueryParameter("q", ticker);
        return builder.build().toString();
    }

    private StockItem createNewListViewStockItem(String ticker, String price) {
            StockItem newStock = new StockItem(ticker, price);
            this.adapter.add(newStock);
            return newStock;
    }

    private void reinsertEditedStockItemIntoListView(String ticker, String price, int position) {
        this.adapter.insert(new StockItem(ticker, price), position);
    }

    //reading, adding, and editing database and helper methods

    private ArrayList<StockItem> readItemsFromDatabase() {

        ArrayList<StockItem> allItems = new ArrayList<StockItem>();
        Cursor toDoDbCursor = this.readDb.query(StockContract.StockEntry.TABLE_NAME, StockHelper.itemProjection, null, null, null, null, null, null);
        Boolean hasItems = toDoDbCursor.moveToFirst();
        Boolean atLastElement = false;

        while (!atLastElement && hasItems) {
            addDatabaseItemtoList(allItems, toDoDbCursor);
            atLastElement = toDoDbCursor.isLast();
            toDoDbCursor.moveToNext();
        }

        return allItems;
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

    private void addDatabaseItemtoList(ArrayList<StockItem> allItems, Cursor toDoDbCursor) {
        String itemTicker = toDoDbCursor.getString(
                toDoDbCursor.getColumnIndexOrThrow(StockContract.StockEntry.COLUMN_NAME_TICKER)
        );
        String itemPrice =  toDoDbCursor.getString(
                toDoDbCursor.getColumnIndexOrThrow(StockContract.StockEntry.COLUMN_NAME_PRICE)
        );
        StockItem newStock = new StockItem(itemTicker, itemPrice);
        allItems.add(newStock);
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
