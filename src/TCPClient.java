import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gandhar on 6/7/2017.
 */

class TCPClient {
    private static int num_documents;
    private static String matchString;

    public static void main(String argv[]) throws Exception {
        List<String> filepaths = new ArrayList<String>(num_documents);
        String sentence;

        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter number of documents to be searched");
        num_documents = Integer.parseInt(inFromUser.readLine());
        //catch number format exception if any

        System.out.println("Enter the String to be searched");
        matchString = inFromUser.readLine();

        while (matchString == null || matchString == "") {
            System.out.println("Enter the String properly");
            matchString = inFromUser.readLine();
        }

        for (int i = 0; i < num_documents; i++) {
            System.out.println("Enter filepath for document" + (i + 1));
            String path = inFromUser.readLine().trim();
            if (path == "" || path == null) {
                System.out.println("Enter the path correctly");
                path = inFromUser.readLine().trim();
            }
            filepaths.add(i, path);
        }

        sentence = QueryGenerator.makeQuery(num_documents, matchString, filepaths);
        Socket clientSocket = new Socket("localhost", 60010);
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        Date start = new Date();

        outToServer.writeBytes(sentence + '\n');

        outToServer.flush();

        clientSocket.shutdownOutput();
        String line = null;

        line = inFromServer.readLine();
        System.out.println("Got this from server " + line);
        //Calculating time
        Date end = new Date();
        long difference = end.getTime() - start.getTime();
        System.out.println("Query execution took" + difference/1000.0 + "s");
        outToServer.close();
        clientSocket.close();
    }
}
