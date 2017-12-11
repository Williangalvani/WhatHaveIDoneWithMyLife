import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Node {
	private enum states {
		RELEASED,
		WANTED,
		HELD
	}
	static NodeClient client;
	static NodeServer server;
	
	static int[] voters = {0, 0};
	static int replies;
	static states state;
	static Boolean voted = false;
	static int nodeId;
	
	static Queue requestsQueue = new LinkedList();
	
	static LamportTimestamp lamport = new LamportTimestamp(nodeId);

	private static void request(int voter)
	{
		int timestamp = lamport.getNextTimestamp();
		System.out.println(""+nodeId + ">>request>>" + voter + ":t" + timestamp);
		client.request(voter, timestamp);
	}
	
	private static void release(int voter)
	{
		int timestamp = lamport.getNextTimestamp();
		System.out.println(""+nodeId + ">>release>>" + voter + ":t" + timestamp);
		client.release(nodeId, voter, timestamp);
	}
	
	public static void main(String args[	]) {

		Scanner ler = new Scanner(System.in);
		nodeId = 1;
		if (args.length > 0) {
		    try {
		        nodeId = Integer.parseInt(args[0]);
		    } catch (NumberFormatException e) {
		        System.err.println("Argument" + args[0] + " must be an integer.");
		        System.exit(1);
		    }
		}
		
		
		server = new NodeServer(args, nodeId);
		Thread tserver = new Thread(server);
		tserver.start();
		
		client = new NodeClient(args, nodeId);
		Thread tclient = new Thread(client);
		tclient.start();
		

		
		voters[0] = nodeId; 
		voters[1] = ((nodeId) % 3)+1;
		replies = 0;
		state = states.RELEASED;
		
		while (!client.ready)
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("STARTING!");
		System.out.println("my peers are " + voters[0] + " and " + voters[1]);
		
		
		while(true)
		{
			for (int voter : voters)
			{
				request(voter);
			}
			state = states.WANTED;
			
			while (replies < voters.length)
			{
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			state = states.HELD;
			System.out.println("requisicao em exclusao mutua: ");
			ler.nextLine(); // bloqueio no scanner esperando por qualquer toque no teclado
			System.out.println("releasing da requisição "); 
			state = states.RELEASED;
			replies = 0;
			for (int voter : voters)
			{
				release(voter);
			}
			
		}
				
	}

	public static void onRequest(int asker, int timestamp) {
		System.out.println("got request from " + asker + " with timestamp " + timestamp);
		if ((state != states.HELD) && !voted)
		{
			if ((state==states.RELEASED) || (state==states.WANTED && lamport.getTimestamp() > timestamp))
			{
				lamport.update(timestamp);
				
				reply(asker);
				voted = true;
			}else
			{
				requestsQueue.add(asker);
			}
		}else
		{
			requestsQueue.add(asker);
		}
		lamport.update(timestamp);
		
	}

	public static void onRelease(int sender, int timestamp) {
		System.out.println(""+nodeId + "<<release<<" + sender + ":t" + timestamp);
		lamport.update(timestamp);
		if(requestsQueue.size() != 0)
		{
			reply((int)requestsQueue.remove());
			voted = true;
		}
		
	}
	
	public static void reply(int node)
	{
		int timestamp = lamport.getNextTimestamp();
		System.out.println(""+nodeId + ">>reply>>" + node + ":t" + timestamp);
		client.reply(nodeId,node, timestamp);
		
	}

	public static void onReply(int sender, int timestamp) {
		System.out.println(""+nodeId + "<<reply<<" + sender + ":t" + timestamp);
		replies++;
		
	}
}
