package example.android.bookkeeper2;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import example.android.bookkeeper2.data.BooksDBHelper;
import example.android.bookkeeper2.data.BooksContract.BookEntry;

public class MainActivity extends AppCompatActivity {

    // make the DB object
    private BooksDBHelper mDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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
        SQLiteDatabase booksDB = mDBHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMNS_BOOK_TITLE,
                BookEntry.COLUMNS_BOOK_AUTHOR,
        };

        // Perform a query on the books table
        Cursor cursor = booksDB.query(
                BookEntry.TABLE_NAME,   // The table to query
                projection,            // The columns to return
                null,                  // The columns for the WHERE clause
                null,                  // The values for the WHERE clause
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // The sort order

        TextView displayView = (TextView) findViewById(R.id.text_view_books);

        try {
            // Create a header in the Text View that looks like this:
            // _id - title - author
            // In the while loop below, iterate through the rows of the cursor and display
            // the information from each column in this order.
            displayView.setText("The pets table contains " + cursor.getCount() + " pets.\n\n");
            displayView.append(BookEntry._ID + " - " +
                    BookEntry.COLUMNS_BOOK_TITLE + " - " +
                    BookEntry.COLUMNS_BOOK_AUTHOR + "\n");

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(BookEntry._ID);
            int titleColumnIndex = cursor.getColumnIndex(BookEntry.COLUMNS_BOOK_TITLE);
            int authorColumnIndex = cursor.getColumnIndex(BookEntry.COLUMNS_BOOK_AUTHOR);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentTitle = cursor.getString(titleColumnIndex);
                String currentAuthor = cursor.getString(authorColumnIndex);
                // Display the values from each column of the current row in the cursor in the TextView
                displayView.append(("\n" + currentID + " - " +
                        currentTitle + " - " +
                        currentAuthor + " - "

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
            case R.id.action_insert_dummy_data:
                insertBookData();
                displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void insertBookData(){
        SQLiteDatabase addToDB = mDBHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMNS_BOOK_TITLE,"book a");
        values.put(BookEntry.COLUMNS_BOOK_AUTHOR,"no one");
        values.put(BookEntry.COLUMNS_BOOK_AUTHOR_COUNTRY,"UK");
        values.put(BookEntry.COLUMNS_BOOK_IBSN,"1234");
        values.put(BookEntry.COLUMNS_BOOK_PRICE,"$12.00");

        long newRowID = addToDB.insert(BookEntry.TABLE_NAME,null,values);

        if (newRowID > -1) {
            Toast.makeText(this, "added row id "+newRowID, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "new row failed to create", Toast.LENGTH_SHORT).show();
        }
    }
}
