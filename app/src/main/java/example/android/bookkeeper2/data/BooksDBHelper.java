package example.android.bookkeeper2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import example.android.bookkeeper2.data.BooksContract.BookEntry;

/**
 * Created by james on 9/5/2018.
 */

public class BooksDBHelper extends SQLiteOpenHelper {

    /**
     * Name of the database file
     */
    private static final String DATABASE_NAME = "bookstore.db";

    /**
     * Database version if database table change increment version
     */
    private static final int DATABASE_VERSION = 1;

    public BooksDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDB) {
        // Create a String that will build the books table
        String SQL_CREATE_BOOKS_TABLE = "CREATE TABLE " + BookEntry.TABLE_NAME + " ("
                + BookEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BookEntry.COLUMNS_BOOK_TITLE + " TEXT, "
                + BookEntry.COLUMNS_BOOK_AUTHOR + " TEXT, "
                + BookEntry.COLUMNS_BOOK_IBSN + " TEXT, "
                + BookEntry.COLUMNS_BOOK_PHONE + " TEXT, "
                + BookEntry.COLUMNS_BOOK_AUTHOR_COUNTRY + " TEXT, "
                + BookEntry.COLUMNS_BOOK_PRICE + " TEXT); ";
        // make the table
        sqLiteDB.execSQL(SQL_CREATE_BOOKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // no on upgrade code yet
    }
}
