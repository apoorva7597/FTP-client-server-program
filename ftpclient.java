// Java implementation for a client 
// Save file as Client.java 

import java.io.*;
import java.net.*;
import java.util.Scanner;

// Client class 
public class ftpclient {
    public static void main(String[] args) throws IOException {
        try {
            while (true) {
                System.out.println("Type the following command: `ftpclient <IPAddress> <PortNumber>`");
                Scanner scn = new Scanner(System.in);
                String arg = scn.nextLine();
                String[] array = arg.split(" ");
                // getting localhost ip
                if (array.length != 3 || !array[0].equals("ftpclient")) {
                    System.out.println("Invalid arguments");
                    System.out.println("Type as following: ftpclient <IPAddress> <PortNumber>");
                    continue;
                } else {
                    InetAddress ip = InetAddress.getByName(array[1]);
                    int port_number = Integer.parseInt(array[2]);
                    // establish the connection with server port 5056
                    Socket s = new Socket(ip, port_number);
                    // obtaining input and out streams
                    DataInputStream dis = new DataInputStream(s.getInputStream());
                    DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                    // the following loop performs the exchange of
                    // information between client and client handler
                    while (true) {
                        String received = dis.readUTF();
                        // System.out.println(recieved);
                        String tosend;
                        if (received.equals("CLOSE")) {
                            System.out.println("Closing this connection : " + s);
                            dos.writeUTF("Exit");
                            System.out.println("Connection closed");
                            System.out.println("Bye! Go Gators! \uD83D\uDC0A");
                            s.close();
                            break;
                        } else if (received.equals("POK")) {
                            System.out.println("Auth successful \uD83D\uDE0E");
                            System.out.println("Type from the following choices:");
                            System.out.println("(1)dir \t (2)get <filename> (3)upload <filename>");
                            tosend = scn.nextLine();
                            dos.writeUTF(tosend);
                        } else if (received.equals("CONTINUE")) {
                            // System.out.println("Auth successful!");
                            System.out.println("Type from the following choices:");
                            System.out.println("(1)dir \t (2)get <filename> (3)upload <filename>");
                            tosend = scn.nextLine();
                            dos.writeUTF(tosend);

                        } else if (received.equals("FAILED")) {
                            System.out.println("BAD Credentials \uD83D\uDE21 \nTry again...");
                        }

                        else if (received.equals("FAIL-CM")) {
                            System.out.println("Invalid command!");
                            System.out.println("Type from the following choices:");
                            System.out.println("(1)dir \t (2)get <filename> (3)upload <filename>");
                            tosend = scn.nextLine();
                            dos.writeUTF(tosend);
                        }

                        else if (received.equals("EU")) {
                            System.out.println("Enter username:");
                            tosend = scn.nextLine();
                            if (tosend.equals("Exit")) {
                                System.out.println("Closing this connection : " + s);
                                dos.writeUTF("Exit");
                                System.out.println("Connection closed");
                                s.close();
                                break;
                            }
                            dos.writeUTF(tosend);
                        }

                        else if (received.equals("OK")) {
                            System.out.println("Enter password");
                            tosend = scn.nextLine();
                            dos.writeUTF(tosend);
                        }

                        else if (received.equals("GRANT")) {
                            String directoryName = dis.readUTF();
                            System.out.println("List files:-");
                            try {
                                File directory = new File(directoryName);
                                String[] fileList = directory.list();
                                for (String fileName : fileList) {
                                    System.out.println(fileName);
                                }
                                System.out.println("---End of Directory---");
                                System.out.println("Do you want to still continue?(y/N)");
                                tosend = scn.nextLine();
                                dos.writeUTF(tosend);
                            } catch (Exception e) {
                                System.exit(-1);
                            }
                        }

                        else if (received.equals("INVALID-F")) {
                            System.out.println("Operation failed. File doesn't exists \uD83D\uDE22");
                            System.out.println("Do you want to still continue?(y/N)");
                            tosend = scn.nextLine();
                            dos.writeUTF(tosend);
                        }

                        else {
                            System.out.println(received);
                            System.out.println("Do you want to still continue?(y/N)");
                            tosend = scn.nextLine();
                            dos.writeUTF(tosend);
                        }

                    }
                    // closing resources
                    scn.close();
                    dis.close();
                    dos.close();
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Unknown Host \uD83D\uDE35" + "\nPlease re-run the command: `java Client`");
        }
    }
}
