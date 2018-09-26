package example.android.bookkeeper2;

import android.content.Context;
        import android.database.Cursor;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
import android.widget.Button;
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
        TextView titleTextView =  view.findViewById(R.id.title);
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
        TextView quantityTextView =  view.findViewById(R.id.text_quanitiy);
        int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMNS_BOOK_QUANTITY);
        quantityTextView.setText(cursor.getString(quantityColumnIndex));

        TextView soldButton = view.findViewById(R.id.sold_button);

        soldButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // this works. needs to some work to change quantity.
                Toast.makeText(context, "you clicked me"+title, Toast.LENGTH_SHORT).show();
            }
        });
        // Find the columns of book attributes that we're interested in
        // Read the book attributes from the Cursor for the current pet
        // Update the TextViews with the attributes for the current book
        //ToDo: view container is for background color.
        //ToDo:  Suggestion You could implement here, in the adapter, the Sale button in the list of items functionality.
    }
}
