import java.util.List;

/**
 * Created by gandhar on 6/7/2017.
 */
public class QueryGenerator {

    public static String makeQuery(int num_docs, String match, List<String> paths) {
        String result = "";
        String make = "";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < num_docs; i++) {
            if (i == (num_docs - 1)) {
                System.out.println(i + "i");
                System.out.println("here");
                sb.append(paths.get(i));
            } else {
                sb.append(paths.get(i) + ",");
            }
            result = String.valueOf(num_docs) + ":" + match + ":" + sb.toString();
        }
        System.out.println("result");
        return result;
    }
}