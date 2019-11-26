The program is a  multi-client file transfer protocol code.It establishes a connection between the server and the clients on the IPAddress and port_Number provided and asks for the credentials of the user to authenticate the user. If the IPAddress or Port_Number is not known it shows a statement unknown host. Various commands such as displaying all the files in the directory of the server using the dir command ,uploads a file from the client folder to the server using the upload<filename> command and retrieves a file from the server to the client using the get < filename> command can be selected one by one by the user.


Instructions for running project
1.Unzip the file and go to the "project1" directory.
2.Compile the two files java *.java
3.Run Server using command "java ftpserver"
4.Run Client1 using command "java ftpclient"
5.Run Client2 using command "java ftpclient"
6.Write the command ftpclient <IPAddress> <Port Number> where the IPAddress is localhost and Port Number is 5056.
7.  Enter the username 
8.  Enter the Password
9.  Enter the command from dir , get < filename> and upload < filename>.
10.  If you wish to continue enter y/Y and select one of the above commands again.
11.  Enter n/N to exit and close the connection.
   
Note - There are three clients used and the username and password for the three clients is as follows:
Username-Client1,Password-pass1
Username-Client2,Password-pass2
Username-Client3,Password-pass3

