package example.android.bookkeeper2;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.text.NumberFormat;

import example.android.bookkeeper2.data.BooksContract.BookEntry;

/**
 * Created by james on 9/18/2018.
 */

public class BookCursorAdaptor extends CursorAdapter {
    /**
     * Constructs a new { BookCursorAdapter}.
     */
    public BookCursorAdaptor(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the book data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current book can be set on the name TextView
     * in the list item layout.
     */
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        // find the cursor item title and the matching text view and place the data in the text view
        final TextView titleTextView =  view.findViewById(R.id.title);
        int titleColumnIndex = cursor.getColumnIndex(BookEntry.COLUMNS_BOOK_TITLE);
        titleTextView.setText(cursor.getString(titleColumnIndex));
        final String title = cursor.getString(titleColumnIndex);
        // find the cursor item author and the matching text view and place the data in the text view
        TextView authorTextView =  view.findViewById(R.id.author);
        int authorColumnIndex = cursor.getColumnIndex(BookEntry.COLUMNS_BOOK_AUTHOR);
        authorTextView.setText(cursor.getString(authorColumnIndex));
        // find the cursor item price and the matching text view and place the data in the text view
        // in dollars format
        TextView priceTextView =  view.findViewById(R.id.text_price);
        int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMNS_BOOK_PRICE);
        double price = Double.parseDouble(cursor.getString(priceColumnIndex));
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        priceTextView.setText(formatter.format(price));
        // find the cursor item quantity and the matching text view and place the data in the text view
        final TextView quantityTextView =  view.findViewById(R.id.text_quanitiy);
        int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMNS_BOOK_QUANTITY);
        quantityTextView.setText(cursor.getString(quantityColumnIndex));
        TextView soldButton = view.findViewById(R.id.sold_button);
        final TextView secretTextView =  view.findViewById(R.id.my_secret);
        int secretColumnIndex = cursor.getColumnIndex(BookEntry._ID);
        secretTextView.setText(cursor.getString(secretColumnIndex));
        //set click listener for this view
        soldButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // pull in quantity from textview
                int changeQuantity = Integer.parseInt(quantityTextView.getText().toString().trim());
                if (changeQuantity > 0) {
                    //adjust quantity
                    changeQuantity -= 1;
                    quantityTextView.setText(Integer.toString(changeQuantity));
                    // get secret id from view
                    long id_number = Integer.parseInt(secretTextView.getText().toString());
                    Uri bookSelected = ContentUris.withAppendedId(BookEntry.CONTENT_URI,id_number);
                    ContentValues values = new ContentValues();
                    values.put(BookEntry.COLUMNS_BOOK_QUANTITY,quantityTextView.getText().toString());
                    // update datebase
                    int rowsAffected = context.getContentResolver().update(bookSelected, values, null, null);
                    // Show a toast message depending on whether or not the update was successful.
                    if (rowsAffected == 0) {
                        // If no rows were affected, then there was an error with the update.
                        Toast.makeText(context, "error with sale accured", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "sold 1 copy", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "no copy to sell", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
