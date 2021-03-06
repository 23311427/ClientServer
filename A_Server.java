// Simple server: reads a file & sends it to a client
// Creates a new thread for each connection request
/* while (true)
			{
				System.out.println();
				if (connections != nPlayers)
				{
					System.out.println("server: waiting for connection");
					socket = listen.accept();
				}
				sockets.add(socket);
				connections++;
				// it was using everythign on one socket. We might want an array of sockets
				System.out.println("server: creating thread for socket");
				
				if (connections == nPlayers)
				{
					for (int i = 0; i < sockets.size(); i++)
					{
						new_thread = new A_ServerThread(sockets.get(i), portList, machineNames, clientPort, 11445, nPlayers, ids);
						new_thread.start();
						portList.add(clientPort);
						clientPort++;
					}
					System.out.println("Connected all players");
				}
			} */
import java.io.*;
import java.net.*;
import java.util.*;

public class A_Server {

	// server port number that all clients will connect to initially
	
	public static void main(String args[]) 
	{
		ServerSocket listen;
		Socket socket = null;
		int clientPort = 13474;
		A_ServerThread new_thread;
		if (args.length < 1)
		{
			System.out.println("Needs 1 argument");
			System.out.println("java Server <number of players>");
			System.exit(1);
		}
		int nPlayers = Integer.parseInt(args[0]);
		if (nPlayers < 2)
		{
			System.out.println("This is a board game, get another player to make 2.");
			System.exit(1);
		}
		int connections = 0;
		
		final int basePort = 13573;
		ArrayList<Integer> portList = new ArrayList<Integer>();
		ArrayList<Socket> sockets = new ArrayList<Socket>();
		ArrayList<String> machineNames = new ArrayList<String>();
		ArrayList<Integer> ids = new ArrayList<Integer>();
		try 
		{
			listen = new ServerSocket(basePort);

			while (true)
			{
				System.out.println();
				if (connections != nPlayers)
				{
					System.out.println("server: waiting for connection");
					socket = listen.accept();
					new_thread = new A_ServerThread(connections, socket, portList, machineNames, clientPort, 11445, nPlayers, ids);
					new_thread.start();
					portList.add(clientPort);
					clientPort++;
				}
				sockets.add(socket);
				connections++;
				// it was using everythign on one socket. We might want an array of sockets
				System.out.println("server: creating thread for socket");
				
			}
		}
		catch (Exception e)
		{  // report any exceptions
			System.err.println(e);
			e.printStackTrace();
		}
	} // main
} // FileReaderServer

