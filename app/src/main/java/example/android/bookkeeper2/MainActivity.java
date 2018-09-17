package example.android.bookkeeper2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import example.android.bookkeeper2.data.BooksDBHelper;
import example.android.bookkeeper2.data.BooksContract.BookEntry;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    // make the DB object
    private BooksDBHelper mDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BooksActivity.class);
                startActivity(intent);
            }
        });
        mDBHelper = new BooksDBHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */
    private void displayDatabaseInfo() {
        // Create and/or open a database to read from it
        //SQLiteDatabase booksDB = mDBHelper.getReadableDatabase();
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMNS_BOOK_TITLE,
                BookEntry.COLUMNS_BOOK_AUTHOR,
                BookEntry.COLUMNS_BOOK_AUTHOR_COUNTRY,
                BookEntry.COLUMNS_BOOK_IBSN,
                BookEntry.COLUMNS_BOOK_PHONE,
                BookEntry.COLUMNS_BOOK_PRICE

        };

        Cursor cursor = getContentResolver().query(BookEntry.CONTENT_URI,projection,null,null,null);
        TextView displayView = findViewById(R.id.text_view_books);

        try {
            // Create a header in the Text View that looks like this:
            // _id - title - author - county - price - ibsn
            // In the while loop below, iterate through the rows of the cursor and display
            // the information from each column in this order.
            displayView.setText(getString(R.string.table_contains) + cursor.getCount() + getString(R.string.table_contains_2));
            displayView.append(BookEntry._ID + " - " +
                    BookEntry.COLUMNS_BOOK_TITLE + " - " +
                    BookEntry.COLUMNS_BOOK_AUTHOR + " - " +
                    BookEntry.COLUMNS_BOOK_AUTHOR_COUNTRY + " - " +
                    BookEntry.COLUMNS_BOOK_PRICE + " - " +
                    BookEntry.COLUMNS_BOOK_PHONE + " - " +
                    BookEntry.COLUMNS_BOOK_IBSN + "\n");

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(BookEntry._ID);
            int titleColumnIndex = cursor.getColumnIndex(BookEntry.COLUMNS_BOOK_TITLE);
            int authorColumnIndex = cursor.getColumnIndex(BookEntry.COLUMNS_BOOK_AUTHOR);
            int countyColumnIndex = cursor.getColumnIndex(BookEntry.COLUMNS_BOOK_AUTHOR_COUNTRY);
            int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMNS_BOOK_PRICE);
            int phoneColumnIndex = cursor.getColumnIndex(BookEntry.COLUMNS_BOOK_PHONE);
            int ibsnColumnIndex = cursor.getColumnIndex(BookEntry.COLUMNS_BOOK_IBSN);
            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentTitle = cursor.getString(titleColumnIndex);
                String currentAuthor = cursor.getString(authorColumnIndex);
                String currentAuthorCounty = cursor.getString(countyColumnIndex);
                String currentPrice = cursor.getString(priceColumnIndex);
                String currentISBN = cursor.getString(ibsnColumnIndex);
                String currentPhone = cursor.getString(phoneColumnIndex);
                // Display the values from each column of the current row in the cursor in the TextView
                displayView.append(("\n" + currentID + " - " +
                        currentTitle + " - " +
                        currentAuthor + " - " +
                        currentAuthorCounty + " - " +
                        currentPrice + " - " +
                        currentPhone + " - " +
                        currentISBN

                ));
            }
        } finally {
            cursor.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.save:
                insertBookData();
                displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllBooks();
                displayDatabaseInfo();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void insertBookData() {

        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMNS_BOOK_TITLE, "xxx");
        values.put(BookEntry.COLUMNS_BOOK_AUTHOR, "XXX");
        values.put(BookEntry.COLUMNS_BOOK_AUTHOR_COUNTRY, "UK");
        values.put(BookEntry.COLUMNS_BOOK_IBSN, "1234");
        values.put(BookEntry.COLUMNS_BOOK_PHONE, "6026660000");
        values.put(BookEntry.COLUMNS_BOOK_PRICE, "12");

        Uri newUri = getContentResolver().insert(BookEntry.CONTENT_URI, values);

        if (newUri == null) {
            Toast.makeText(this, getString(R.string.added_no), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.added_good), Toast.LENGTH_SHORT).show();
        }
    }
    private void deleteAllBooks() {
        int rowsDeleted = getContentResolver().delete(BookEntry.CONTENT_URI, null, null);
        Log.v("MainActivity", rowsDeleted + " rows deleted from pet database");
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
