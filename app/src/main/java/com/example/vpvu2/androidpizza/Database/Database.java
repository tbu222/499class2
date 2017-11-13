package com.example.vpvu2.androidpizza.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.vpvu2.androidpizza.Model.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thinh on 10/27/2017.
 */
//Database class for function
public class Database extends SQLiteAssetHelper {
    private static final String DB_name = "AndroidpizzaDB.db";
    private static final int DB_ver=1;
    public Database(Context context) {
        super(context, DB_name, null, DB_ver);
    }
    public List<Order>getCarts()
    {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] sqlSelect = {"ProductName","ProductID","Quantity","Price"};
        String sqlTable = "OrderInfo";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db,sqlSelect,null,null,null,null,null);
        final List<Order> result = new ArrayList<>();
        if(c.moveToFirst())
        {
            do{
                result.add(new Order(c.getString(c.getColumnIndex("ProductID")), c.getString(c.getColumnIndex("ProductName")), c.getString(c.getColumnIndex("Quantity")), c.getString(c.getColumnIndex("Price"))));


            }while(c.moveToNext());
        }
        return result;
    }

    public void addToCart(Order order)
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("Insert into OrderInfo(ProductID,ProductName,Quantity,Price) Values('%s','%s','%s','%s');",order.getProductID(),order.getProductName(),order.getQuantity(),order.getPrice());
        db.execSQL(query);

    }
    public void clearCart()
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("Delete from OrderInfo");
        db.execSQL(query);

    }
}
