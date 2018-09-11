package example.android.bookkeeper2.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by james on 9/5/2018.
 */

public final class BooksContract implements BaseColumns {

    private BooksContract () {}

    public static final class BookEntry implements BaseColumns {
        // database tables
        public final static String TABLE_NAME = "books";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMNS_BOOK_TITLE = "title";
        public final static String COLUMNS_BOOK_AUTHOR = "author";
        public final static String COLUMNS_BOOK_IBSN = "ibsn";
        public final static String COLUMNS_BOOK_AUTHOR_COUNTRY = "country";
        public final static String COLUMNS_BOOK_PRICE = "price";
        public final static String COLUMNS_BOOK_PHONE = "phone";
        // content authority
        public static final String CONTENT_AUTHORITY = "example.android.bookkeeper2";
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
        public static final String PATH_BOOKS = TABLE_NAME;
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BOOKS);
    }
}