package example.android.bookkeeper2.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import example.android.bookkeeper2.R;
import example.android.bookkeeper2.data.BooksDBHelper;
import example.android.bookkeeper2.data.BooksContract.BookEntry;

/**
 * Created by james on 9/11/2018.
 * content provider for books app.
 */

public class BookProvider extends ContentProvider{

    /**
     * Initialize the provider and the database helper object.
     */
    // make the DB object
    private BooksDBHelper mDBHelper;
    // URI matcher code and setup matcher
    private static final int BOOKS = 1;
    private static final int BOOKS_ID = 2;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.
        sUriMatcher.addURI(BookEntry.CONTENT_AUTHORITY,BookEntry.PATH_BOOKS,BOOKS);
        sUriMatcher.addURI(BookEntry.CONTENT_AUTHORITY,BookEntry.PATH_BOOKS + "/#",BOOKS_ID);
    }

    @Override
    public boolean onCreate() {
        mDBHelper = new BooksDBHelper(getContext());
        return true;
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase queryDataBase = mDBHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                cursor = queryDataBase.query(BookEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case BOOKS_ID:
                selection = BookEntry._ID+"=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = queryDataBase.query(BookEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        return cursor;
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return insertBook(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Insert a pet into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertBook(Uri uri, ContentValues values) {
        //check incoming data
        // check title
        String checkString = values.getAsString(BookEntry.COLUMNS_BOOK_TITLE);
        Log.v("string ",checkString);
        if (checkString == null) {
            throw new IllegalArgumentException("add title");
        }
        //check ibsn
        Integer checkValue = values.getAsInteger(BookEntry.COLUMNS_BOOK_IBSN);
        Log.v("val ",checkValue.toString());
        if (checkValue == null) {
            throw new IllegalArgumentException("add ibsn");
        }
        //check price
        checkString = values.getAsString(BookEntry.COLUMNS_BOOK_PRICE);
        Log.v("string ",checkString);
        if (checkString == null) {
            throw new IllegalArgumentException("add price");
        }
        //check phone number
        checkValue = values.getAsInteger(BookEntry.COLUMNS_BOOK_PHONE);
        Log.v("val ",checkValue.toString());
        if (checkValue == null) {
            throw new IllegalArgumentException("add phone number");
        }
        // get DB object
        SQLiteDatabase database = mDBHelper.getWritableDatabase();
        // insert new table
        long id = database.insert(BookEntry.TABLE_NAME, null, values);
        // see if it worked or not.
        if (id == -1) {
            Toast.makeText(getContext(), "not added", Toast.LENGTH_SHORT).show();
            return null;
        }
        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri, id);
    }

    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        return 0;
    }

    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    /**
     * Returns the MIME type of data for the content URI.
     */
    @Override
    public String getType(Uri uri) {
        return null;
    }
}
