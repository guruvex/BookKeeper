package example.android.bookkeeper2.data;

/**
 * Created by james on 9/17/2018.
 */

public class DataChecks {
    public boolean CheckData (String title, String ibsn, String phone ) {
        boolean isGood = true;
        // ToDo: this dose not seem to work.

        if (title == null || title == "") {
            isGood = false;
            //throw new IllegalArgumentException("add title");
        }
        //check ibsn
        if (ibsn == null || ibsn == "") {
            //throw new IllegalArgumentException("add ibsn");
            isGood = false;
            //return null;
        }
        //check phone number
        if (phone == null || phone == "") {
            //   throw new IllegalArgumentException("add phone number");
            isGood = false;
            //return null;
        }

        return isGood;
    }
}
