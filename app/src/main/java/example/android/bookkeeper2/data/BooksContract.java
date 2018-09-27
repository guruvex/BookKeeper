package example.android.bookkeeper2.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by james on 9/5/2018.
 * contracts for variables to values
 * for the book keeper 2 app.
 */

public final class BooksContract implements BaseColumns {

    private BooksContract () {}

    public static final class BookEntry implements BaseColumns {
        // database tables
        public final static String TABLE_NAME = "books";
        public final static String _ID = BaseColumns._ID;
        // representing the primary key field called _ID, so you don't need to include it in the Contract Class.
        public final static String COLUMNS_BOOK_TITLE = "title";
        public final static String COLUMNS_BOOK_AUTHOR = "author";
        public final static String COLUMNS_BOOK_IBSN = "ibsn";
        public final static String COLUMNS_BOOK_PHONE = "phone";
        public final static String COLUMNS_BOOK_PRICE = "price";
        public final static String COLUMNS_BOOK_QUANTITY = "quantity";
        public final static String COLUMNS_BOOK_CAN_SELL = "sell";
        // content authority
        public static final String CONTENT_AUTHORITY = "example.android.bookkeeper2";
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
        public static final String PATH_BOOKS = TABLE_NAME;
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BOOKS);
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;
        //Possible values for if the book can be sold or not.
        public static final int CAN_SELL_UNKNOWN = 0;
        public static final int CAN_SELL_YES = 1;
        public static final int CAN_SELL_NO = 2;

    }
}