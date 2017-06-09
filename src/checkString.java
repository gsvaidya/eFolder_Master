import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by gandhar on 6/7/2017.
 */

public class checkString {

    public static boolean searchUsingBufferedReader(String searchQuery, String filePath) throws IOException {
        searchQuery = searchQuery.trim();
        BufferedReader br = null;
        boolean response = false;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
            String line;

            while (true) {
                line = br.readLine();
                if (line != null) {
                    if (!line.equals("")) {
                        //System.out.println(line);
                        if (line.contains(searchQuery)) {
                            System.out.println("found a match");
                            response = true;
                            return true;

                        } else {
                            System.out.println("not found");

                            response = false;
                            //return false;

                        }
                    } else {
                        System.out.println("Blank line!!");
                    }
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*finally
        {
            try
            {
                if (br != null)
                    br.close();
            }
            catch (Exception e)
            {
                System.err.println("Exception while closing bufferedreader " + e.toString());
            }
        }*/
        return response;
    }

//    public static void main(String[] args){
//        String path = "C:\\IdeaProjects\\src\\compile3.txt";
//        String match = "killed";
//        try {
//            System.out.println(checkString.searchUsingBufferedReader(match,path));
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}