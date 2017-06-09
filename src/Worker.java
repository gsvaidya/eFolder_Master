import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gandhar on 6/7/2017.
 */

public class Worker implements Runnable {
    Socket csocket;
    private static volatile int workernum;

    Worker(Socket csocket) {
        //constructor for the Worker
        this.csocket = csocket;
    }

    public static void main(String args[]) throws Exception {
        //buffered reader input port
        String filePath = "C:\\IdeaProjects\\eFolder_Test\\xyz.txt";
        List<Integer> portNumberList = new ArrayList<>();
        BufferedReader b_read = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
        for(int i = 0;i<5;i++){
            portNumberList.add(i,Integer.parseInt(b_read.readLine()));
        }
        int port = portNumberList.get(workernum);
        ServerSocket ssock = new ServerSocket(port);
        System.out.println("Worker "+(workernum+1)+" Listening on " + port);
        workernum++;

        while (true)
        {
            //waiting for BootStrap to start and forward parsed request from Client
            Socket sock = ssock.accept();
            System.out.println("Connected");
            new Thread(new Worker(sock)).start();
        }
    }

    public void run() {
        try {
            DataOutputStream outToBS = new DataOutputStream(csocket.getOutputStream());
            BufferedReader is = new BufferedReader(new InputStreamReader(csocket.getInputStream()));
            String queryFromClient = is.readLine();
            System.out.println("Received " + queryFromClient + " in Worker");

            String[] params = queryFromClient.split(" ");
            int numOfDocuments = Integer.parseInt(params[2]);
            Boolean matchStringResult = checkString.searchUsingBufferedReader(params[1], params[0]);
            outToBS.writeBytes(matchStringResult.toString() + " for " + params[0]);

            outToBS.close();
            csocket.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}