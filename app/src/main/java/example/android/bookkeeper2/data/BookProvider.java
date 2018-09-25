package example.android.bookkeeper2.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.widget.Toast;

import example.android.bookkeeper2.data.BooksContract.BookEntry;

/**
 * Created by james on 9/11/2018.
 * content provider for books app.
 */

public class BookProvider extends ContentProvider {

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
        sUriMatcher.addURI(BookEntry.CONTENT_AUTHORITY, BookEntry.PATH_BOOKS, BOOKS);
        sUriMatcher.addURI(BookEntry.CONTENT_AUTHORITY, BookEntry.PATH_BOOKS + "/#", BOOKS_ID);
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
                cursor = queryDataBase.query(BookEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case BOOKS_ID:
                selection = BookEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = queryDataBase.query(BookEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
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
     * Insert a book into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertBook(Uri uri, ContentValues values) throws IllegalArgumentException {
        //check incoming data
        DataChecks dataChecks = new DataChecks();
        String title = values.getAsString(BookEntry.COLUMNS_BOOK_TITLE);
        String ibsn = values.getAsString(BookEntry.COLUMNS_BOOK_IBSN);
        String phone = values.getAsString(BookEntry.COLUMNS_BOOK_PHONE);
        if (dataChecks.CheckData(title,ibsn,phone)) {
            // get DB object
            SQLiteDatabase database = mDBHelper.getWritableDatabase();
            // insert new table
            long id = database.insert(BookEntry.TABLE_NAME, null, values);
            // see if it worked or not.
            if (id == -1) {
                Toast.makeText(getContext(), "not added", Toast.LENGTH_SHORT).show();
                return null;
            }
            // Notify all listeners that the data has changed
            getContext().getContentResolver().notifyChange(uri, null);
            // Once we know the ID of the new row in the table,
            // return the new URI with the ID appended to the end of it
            return ContentUris.withAppendedId(uri, id);
        } else {
            return null;
        }
    }

    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return updateBook(uri, contentValues, selection, selectionArgs);
            case BOOKS_ID:
                // For the BOOK_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = BookEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateBook(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }

    }

    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        // Get writeable database
        SQLiteDatabase database = mDBHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(BookEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case BOOKS_ID:
                // Delete a single row given by the ID in the URI
                selection = BookEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(BookEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows deleted
        return rowsDeleted;
    }

    /**
     * Returns the MIME type of data for the content URI.
     */
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return BookEntry.CONTENT_LIST_TYPE;
            case BOOKS_ID:
                return BookEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    private int updateBook(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // If the {@link PetEntry#COLUMN_PET_NAME} key is present,
        // check that the name value is not null.
        if (values.containsKey(BookEntry.COLUMNS_BOOK_TITLE)) {
            String name = values.getAsString(BookEntry.COLUMNS_BOOK_TITLE);
            if (name == null) {
                throw new IllegalArgumentException("Book requires a title");
            }
        }
        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDBHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(BookEntry.TABLE_NAME, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows updated
        return rowsUpdated;
    }
}
