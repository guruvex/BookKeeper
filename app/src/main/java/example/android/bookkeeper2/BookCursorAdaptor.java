package example.android.bookkeeper2;

import android.content.Context;
        import android.database.Cursor;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.CursorAdapter;
        import android.widget.TextView;

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
    public void bindView(View view, Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView titleTextView =  view.findViewById(R.id.title);
        TextView authorTextView =  view.findViewById(R.id.author);
        // Find the columns of book attributes that we're interested in
        int titleColumnIndex = cursor.getColumnIndex(BookEntry.COLUMNS_BOOK_TITLE);
        int authorColumnIndex = cursor.getColumnIndex(BookEntry.COLUMNS_BOOK_AUTHOR);
        // Read the book attributes from the Cursor for the current pet
        String titleName = cursor.getString(titleColumnIndex);
        String author = cursor.getString(authorColumnIndex);
        // Update the TextViews with the attributes for the current book
        titleTextView.setText(titleName);
        authorTextView.setText(author);
    }
}
