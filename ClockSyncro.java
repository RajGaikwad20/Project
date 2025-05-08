Server.java


  
import java.io.*; import java.net.*;
import java.text.SimpleDateFormat; import java.util.*;

public class Server {
private static final int PORT = 5000;
private static List<ClientHandler> clients = new ArrayList<>();
private static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm"); public static void main(String[] args) {
try (ServerSocket serverSocket = new ServerSocket(PORT)) { System.out.println("Master Clock Server is running..."); while (clients.size() < 3) {
Socket clientSocket = serverSocket.accept(); ClientHandler client = new ClientHandler(clientSocket); clients.add(client);
System.out.println("Client connected: " + clientSocket.getInetAddress());
}
List<Date> clientTimes = new ArrayList<>(); List<Long> timeOffsets = new ArrayList<>();
Date masterTime = timeFormat.parse("03:10"); // Set master clock time
(3:10)
System.out.println("\nMaster Clock Time: " +
timeFormat.format(masterTime));
// Collect client times and compute offsets
for (ClientHandler client : clients) {
Date clientTime = client.receiveTime(); clientTimes.add(clientTime);
long offset = (clientTime.getTime() - masterTime.getTime()) / 60000;
// Offset in minutes
timeOffsets.add(offset);
}
// Print old times and offsets System.out.println("\nClient Old Times and Offsets:"); for (int i = 0; i < clientTimes.size(); i++) {
System.out.println("Client " + (i + 1) + " Time: " + timeFormat.format(clientTimes.get(i)) +
" | Offset: " + timeOffsets.get(i) + " min");
}
// Calculate new synchronized time
long sumOffsets = 0;
for (long offset : timeOffsets) { sumOffsets += offset;
                                 60000));

}
long averageOffset = sumOffsets / timeOffsets.size(); System.out.println("\nSum of Offsets: " + sumOffsets + " min"); System.out.println("Average Offset: " + averageOffset + " min");
Date newMasterTime = new Date(masterTime.getTime() + (averageOffset *
System.out.println("New Synchronized Time: " +

timeFormat.format(newMasterTime));
// Send adjustments to clients
for (int i = 0; i < clients.size(); i++) {
long adjustment = (newMasterTime.getTime() - clientTimes.get(i).getTime()) / 60000;
clients.get(i).sendAdjustment(adjustment); System.out.println("Client " + (i + 1) + " Adjustment: " +
adjustment + " min");
}
} catch (IOException | java.text.ParseException e) { e.printStackTrace();
}
}


Client.java

  
