package example.android.bookkeeper2.data;

/**
 * Created by james on 9/17/2018.
 */

public class DataChecks {
    public boolean CheckData (String title, String ibsn, String phone ) {
        boolean isGood = true;
        if (title == null || title == "") {
            isGood = false;
        }
        //check ibsn
        if (ibsn == null || ibsn == "") {
            isGood = false;
        }
        //check phone number
        if (phone == null || phone == "") {
            isGood = false;
        }
        return isGood;
    }
}
