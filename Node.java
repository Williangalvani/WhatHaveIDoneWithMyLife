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
	
	static int[] voters;
	static int replies;
	static states state;
	static Boolean voted = false;
	static int nodeId;
	
	static Queue requestsQueue = new LinkedList();

	private static void request(int voter)
	{
		client.request(voter);
	}
	
	private static void release(int voter)
	{
		client.release(voter);
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
		voters[1] = ((nodeId+1) % 3)+1;
		replies = 0;
		state = states.RELEASED;
		
		
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
		
			for (int voter : voters)
			{
				release(voter);
			}
			
		}
				
	}

	public static void onRequest(int asker, int timestamp) {
		if ((state != states.HELD) && !voted)
		{
			if ((state==states.RELEASED) || (state==states.WANTED && getTimestamp() > timestamp))
			{
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
		
	}

	public static void onRelease(int sender) {
		if(requestsQueue.size() != 0)
		{
			reply((int)requestsQueue.remove());
		}
		
	}
	
	public static void reply(int node)
	{
		client.reply(nodeId);
		
	}
}
