package example.android.bookkeeper2.data;

import android.text.TextUtils;

/**
 * Created by james on 9/17/2018.
 */

public class DataChecks {
    public boolean CheckData (String title, String ibsn, String phone ) {
        boolean isGood = true;
        if (TextUtils.isEmpty(title)) {
            isGood = false;
        }
        //check ibsn
        if (TextUtils.isEmpty(ibsn)) {
            isGood = false;
        }
        //check phone number
        if (TextUtils.isEmpty(phone)) {
            isGood = false;
        }
        return isGood;
    }
}
