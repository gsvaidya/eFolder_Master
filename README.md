# eFolder_Master
The architecture of this project relies on 3 main components - a. BootStrap ( combo of client and server ) b. Worker c. TCPClient.

In order to run the code , initialize Workers as required. ( I have tried with 5 ). 
You will have to provide port numbers for the workers to start on.

Then start the BootStrap.java followed by the Client.
Depending on the number of paths or documents that need to be searched you will need to provide port numbers for the startSender() thread in BootStrap.java
These port numbers are used to connect the senders to the Workers ( so make sure you do not mention any other port numbers, besides the ones you mentioned for workers.)

Having done that you should see your results in the CLient console !! ( Result is in a boolean format, true for value found and false for not found )
