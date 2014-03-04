
public class Flusher<T extends ContactManagerImpl> implements Runnable
{
	private T requestor;

	public Flusher(T requestor)
	{
		this.requestor = requestor;
	}

	public void run()
	{
		requestor.flush();
	}
}