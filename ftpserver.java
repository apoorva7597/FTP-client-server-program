// Java implementation of Server side 
// It contains two classes : Server and ClientHandler 
// Save file as Server.java 

import java.io.*;
import java.text.*;
import java.util.*;
import java.net.*;

// Server class 
public class ftpserver {
    public static void main(String[] args) throws IOException {
        // server is listening on port 5056
        ServerSocket ss = new ServerSocket(5056);

        // running infinite loop for getting
        // client request
        while (true) {
            Socket s = null;

            try {
                // socket object to receive incoming client requests
                s = ss.accept();

                System.out.println("A new client is connected : " + s);

                // obtaining input and out streams
                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                System.out.println("Assigning new thread for this client");

                // create a new thread object
                Thread t = new ClientHandler(s, dis, dos);

                // Invoking the start() method
                t.start();

            } catch (Exception e) {
                s.close();
                e.printStackTrace();
            }
        }
    }
}

// ClientHandler class
class ClientHandler extends Thread {
    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;

    // Constructor
    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos) {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
    }

    public void makeDirectory(String directory) {
        File file = new File(directory);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    public void getFile(String filepath) throws IOException {
        BufferedInputStream bis;
        byte[] buffer = new byte[1024];
        String fileName = filepath.split("/")[1];
        try {
            File fileToGet = new File("Server/" + fileName);
            bis = new BufferedInputStream(new FileInputStream(fileToGet));
            makeDirectory(filepath.split("/")[0]);
            File newFile = new File(filepath);
            OutputStream os = new FileOutputStream(newFile);
            BufferedOutputStream bos = new BufferedOutputStream(os);
            int readCount;
            while ((readCount = bis.read(buffer)) > 0) {
                bos.write(buffer, 0, readCount);
            }
            this.dos.writeUTF("File: " + "`" + fileName + "`" + " successfully received. \uD83D\uDE0E");
            bis.close();
            bos.close();
        } catch (Exception err) {
            // System.out.println("Invalid file");
            this.dos.writeUTF("INVALID-F");
        }
    }

    public void uploadFile(String filepath) throws IOException {
        BufferedInputStream bis;
        String fileName = filepath.split("/")[1];
        byte[] buffer = new byte[1024];
        try {
            File fileToUpload = new File(filepath);
            bis = new BufferedInputStream(new FileInputStream(fileToUpload));
            File newFile = new File("Server/" + fileName);
            OutputStream os = new FileOutputStream(newFile);
            BufferedOutputStream bos = new BufferedOutputStream(os);
            int readCount;
            while ((readCount = bis.read(buffer)) > 0) {
                bos.write(buffer, 0, readCount);
            }
            dos.writeUTF("File: " + "`" + fileName + "`" + " successfully uploaded. \uD83D\uDE0E");
            bis.close();
            bos.close();
            // updateLog(filepath.split("/")[1]);
        } catch (Exception err) {
            // System.out.println("Invalid file");
            this.dos.writeUTF("INVALID-F");
        }
    }

    public void myFxn(String user) {
        try {
            dos.writeUTF("POK");
            while (true) {
                String code = dis.readUTF();
                String[] arr = code.split("\\s");

                if (arr.length == 0) {
                    dos.writeUTF("FAIL-CM");
                    continue;
                }
                String command = arr[0];
                if (arr.length == 2) {
                    String filename = arr[1];

                    // file get logic
                    if (command.equals("get")) {
                        filename = arr[1];
                        getFile(user + "/" + filename);
                        if (dis.readUTF().equalsIgnoreCase("y")) {
                            dos.writeUTF("CONTINUE");
                            continue;
                        } else {
                            dos.writeUTF("CLOSE");
                            break;
                        }
                    }

                    // file upload logic
                    else if (command.equals("upload")) {
                        filename = arr[1];
                        uploadFile(user + "/" + filename);
                        if (dis.readUTF().equalsIgnoreCase("y")) {
                            dos.writeUTF("CONTINUE");
                            continue;
                        } else {
                            dos.writeUTF("CLOSE");
                            break;
                        }
                    }

                    else {
                        dos.writeUTF("FAIL-CM");
                    }
                }

                else if (arr.length == 1 && command.equals("dir")) {
                    // file list logic
                    dos.writeUTF("GRANT");
                    dos.writeUTF("Server");
                    if (dis.readUTF().equalsIgnoreCase("y")) {
                        dos.writeUTF("CONTINUE");
                        continue;
                    } else {
                        dos.writeUTF("CLOSE");
                        break;
                    }
                } else {
                    dos.writeUTF("FAIL-CM");
                }
            }

        } catch (Exception e) {
            System.exit(-1);
        }
    }

    @Override
    public void run() {
        String received;
        while (true) {
            try {

                // Ask user what he wants
                dos.writeUTF("EU");

                // receive the answer from client
                received = dis.readUTF();

                if (received.equals("Exit")) {
                    System.out.println("Client " + this.s + " sends exit...");
                    System.out.println("Closing this connection.");
                    this.s.close();
                    System.out.println("Connection closed");
                    break;
                }

                // creating Date object
                // Date date = new Date();

                // write on output stream based on the
                // answer from the client
                switch (received) {

                case "Client1":
                    dos.writeUTF("OK");
                    if (dis.readUTF().equals("pass1")) {
                        myFxn(received);
                    } else
                        dos.writeUTF("FAILED");
                    break;

                case "Client2":
                    dos.writeUTF("OK");
                    if (dis.readUTF().equals("pass2"))
                        myFxn(received);
                    else
                        dos.writeUTF("FAILED");
                    break;

                case "Client3":
                    dos.writeUTF("OK");
                    if (dis.readUTF().equals("pass3"))
                        myFxn(received);
                    else
                        dos.writeUTF("FAILED");
                    break;

                default:
                    dos.writeUTF("FAILED");
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            // closing resources
            this.dis.close();
            this.dos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
