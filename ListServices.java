import org.omg.CORBA.*;
public class ListServices{ 
	public static void main(String[] args){
		try { 
			ORB orb = ORB.init(args,null);
			String[] services = orb.list_initial_services();
			for (int i = 0; i < services.length; i++)
			System.out.println(services[i]);
		}
		catch(Exception e){
			System.out.println("Error" + e);
		}
	}
}
