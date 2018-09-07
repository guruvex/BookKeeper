package example.android.bookkeeper2.data;

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
    }
}