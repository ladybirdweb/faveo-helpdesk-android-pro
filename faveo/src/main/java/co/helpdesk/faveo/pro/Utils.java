package co.helpdesk.faveo.pro;


import java.util.Collections;
import java.util.LinkedHashSet;

/**
 * Created by narendra on 18/10/16.
 */

public class Utils {

    //Removing duplicates from Collection objects
    public static String[] removeDuplicates(String[] arr) {
        //arr[0] = "--";
        LinkedHashSet<String> lhs = new LinkedHashSet<>();
        lhs.add("--");
        Collections.addAll(lhs, arr);

        String[] strArr = new String[lhs.size()];

        lhs.toArray(strArr);

        return strArr;
    }

}
