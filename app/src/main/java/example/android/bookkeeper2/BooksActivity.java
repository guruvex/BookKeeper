package example.android.bookkeeper2;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import example.android.bookkeeper2.data.BooksDBHelper;
import example.android.bookkeeper2.data.BooksContract.BookEntry;

public class BooksActivity extends AppCompatActivity {

    private BooksDBHelper mDBHelper;

    /** EditText field to enter the author */
    private EditText mAuthorEditText;

    /** EditText field to enter the books title */
    private EditText mTitleEditText;

    /** EditText field to enter the ibsn */
    private EditText mIbsnEditText;

    /** EditText field to enter the country */
    private Spinner mCountrySpinner;
    private String mCountry;

    /** EditText field to enter price */
    private EditText mPriceEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        // Find all relevant views that we will need to read user input from
        mAuthorEditText = (EditText) findViewById(R.id.edit_book_title);
        mTitleEditText = (EditText) findViewById(R.id.edit_author);
        mIbsnEditText = (EditText) findViewById(R.id.edit_ibsn);
        mCountrySpinner = (Spinner) findViewById(R.id.spinner_country);
        mPriceEditText = findViewById(R.id.edit_price);

        setupSpinner();
    }

    /**
     * Setup the dropdown spinner for the country
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.country_array, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mCountrySpinner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mCountrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.england))) {
                        mCountry = getString(R.string.england);
                    } else if (selection.equals(getString(R.string.france))) {
                        mCountry = getString(R.string.france);
                    } else if (selection.equals(getString(R.string.usa))) {
                        mCountry = getString(R.string.usa);
                    } else if (selection.equals(getString(R.string.germany))) {
                        mCountry = getString(R.string.germany);
                    } else {
                        mCountry = getString(R.string.country_unknown);
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Unknown
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.books_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                Toast.makeText(this, "i have triggered", Toast.LENGTH_SHORT).show();
                insertData();
                finish();
                // Do nothing for now
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void insertData() {
        String bookTitle = mTitleEditText.getText().toString().trim();
        String bookAuthor = mAuthorEditText.getText().toString().trim();
        String bookPrice =  mPriceEditText.getText().toString().trim();

        String bIBSN = mIbsnEditText.getText().toString().trim();
        // turn bIBSN to an integer
        int bookIBSN = Integer.parseInt(bIBSN);

        // add book
        SQLiteDatabase addNewBookDB = mDBHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMNS_BOOK_TITLE,bookTitle);
        values.put(BookEntry.COLUMNS_BOOK_AUTHOR,bookAuthor);
        values.put(BookEntry.COLUMNS_BOOK_AUTHOR_COUNTRY,mCountry);
        values.put(BookEntry.COLUMNS_BOOK_IBSN,bookIBSN);
        values.put(BookEntry.COLUMNS_BOOK_PRICE,bookPrice);

        long newRowID = addNewBookDB.insert(BookEntry.TABLE_NAME,null,values);

        if (newRowID > -1) {
            Toast.makeText(this, getString(R.string.added_good), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.added_no), Toast.LENGTH_SHORT).show();
        }
    }
}
