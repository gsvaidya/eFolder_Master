import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by gandhar on 6/7/2017.
 */

public class BootStrapServer {

    private static volatile boolean flag = false; // to make sender initialization wait untill client's request is received
    private static volatile String value = null; // value is used to store the query from client and then do processing on it as required.
    private static volatile String responseFromWorker = ""; // This variable is used to deal with the final response from the Workers
    private static volatile int num_docs = 0; // number of documents extracted from the query which comes from the client.
    private static volatile int counter = 0; // used to make sure we get all the results from the worker before sending it back to the client
    private static String fullfile = ""; // contains the entire String of file paths/paths of respective documents.
    private static String searchQuery = ""; // this variable is used to store the match string which is

    private static volatile int globalFileIndex = 0; // used to iterate over the array of documents to send to the Worker

    public static void main(String[] args) throws IOException {

        startServer();

    }

    //client part of the BootStrap
    public static void startSender() {
        (new Thread() {
            @Override
            public void run() {
                try {
                    String sendToWorker = "";
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    System.out.println("Please provide the same port numbers which were given for the workers to connect to them");
                    int port_num = Integer.parseInt(br.readLine());

                    Socket s = new Socket("localhost", port_num);
                    BufferedWriter out = new BufferedWriter(
                            new OutputStreamWriter(s.getOutputStream()));
                    System.out.println("Sending from BS to Workers for computation");

                    // separating filepaths from a set of paths
                    String[] arr = fullfile.split(",");

                    sendToWorker = arr[globalFileIndex] + " " + searchQuery + " " + num_docs; // distribution of paths and search string
                    globalFileIndex++;

                    out.write(sendToWorker);
                    out.newLine();
                    out.flush();

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(s.getInputStream()));
                    String line = null;
                    while ((line = in.readLine()) != null) {
                        responseFromWorker = responseFromWorker + line;
                        System.out.println("response from worker " + line);
                        counter++;
                    }
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //server part of the BootStrap
    public static void startServer() {
        (new Thread() {
            @Override
            public void run() {
                BufferedWriter out = null;
                ServerSocket ss = null;
                while (true) {
                    try {
                        if (ss == null)
                            ss = new ServerSocket(60010);
                        System.out.println("Server side ON - waiting for client to connect.....");
                        Socket s = ss.accept();

                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(s.getInputStream()));

                        String line = null;
                        while ((line = in.readLine()) != null) {
                            System.out.println(line + " reached Server side of BS");
                            value = line;
                            String[] inFromClient = value.split(":");
                            num_docs = Integer.parseInt(inFromClient[0]);
                            searchQuery = inFromClient[1];
                            fullfile = inFromClient[2];

                            for (int i = 0; i < num_docs; i++) {
                                startSender();
                            }
                        }
                        flag = true;
                        line = null;
                        value = null;

                        long startTime = System.currentTimeMillis(); //fetch starting time
                        while (counter < num_docs && (System.currentTimeMillis() - startTime) < 5000) {

                        }

                        if (counter < num_docs) {
                            System.out.println("Only some records could be fetched from the Server. Sending the records which were fetched.");
                        }

                        out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                        out.write(responseFromWorker);

                        counter = 0;
                        responseFromWorker = "";
                        globalFileIndex = 0;

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (out != null) {
                        try {
                            out.flush();
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }
}