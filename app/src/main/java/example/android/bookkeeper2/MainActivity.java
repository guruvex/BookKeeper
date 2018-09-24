package example.android.bookkeeper2;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.net.URI;

import example.android.bookkeeper2.data.BooksDBHelper;
import example.android.bookkeeper2.data.BooksContract.BookEntry;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    // make the DB object
    private BooksDBHelper mDBHelper;
    // make cursor to use for cursor loader
    private static final int BookLoader = 0;
    BookCursorAdaptor mCursorAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);

        ListView listView = findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_view);
        // if there's no data
        listView.setEmptyView(emptyView);

        mCursorAdaptor = new BookCursorAdaptor(this,null);
        listView.setAdapter(mCursorAdaptor);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BooksActivity.class);
                startActivity(intent);
            }
        });
        //ToDo: may not need this DB object
        //mDBHelper = new BooksDBHelper(this);

        // start loader
        getLoaderManager().initLoader(BookLoader,null,this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, BooksActivity.class);
                Uri bookSelected = ContentUris.withAppendedId(BookEntry.CONTENT_URI,id);
                intent.setData(bookSelected);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //displayDatabaseInfo();
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
                //displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllBooks();
                //displayDatabaseInfo();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void insertBookData() {

        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMNS_BOOK_TITLE, "xxx");
        values.put(BookEntry.COLUMNS_BOOK_AUTHOR, "XXX");
        values.put(BookEntry.COLUMNS_BOOK_IBSN, "1234");
        values.put(BookEntry.COLUMNS_BOOK_PHONE, "6026660000");
        values.put(BookEntry.COLUMNS_BOOK_PRICE, "12");
        //ToDo: add quantity for data here

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

        String [] projection = {
                BookEntry._ID,
                BookEntry.COLUMNS_BOOK_TITLE,
                BookEntry.COLUMNS_BOOK_AUTHOR };
        return new CursorLoader(this, BookEntry.CONTENT_URI,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdaptor.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdaptor.swapCursor(null);
    }
}
