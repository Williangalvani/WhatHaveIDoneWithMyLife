
public class LamportTimestamp {

	private int time = 0;
	
	public LamportTimestamp(int start)
	{
		time = start;
	}
	
	public void update(int timestamp)
	{
		time = Math.max(timestamp, time);
	}
	public int getNextTimestamp()
	{
		return ++time;
	}
	public int getTimestamp()
	{
		return time;
	}
}
