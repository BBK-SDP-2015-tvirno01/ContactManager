
/**
*Class to run the flush() method of a requestor object
*/

public class Flusher<T extends ContactManagerImpl> implements Runnable
{
	/**
	*Denotes requesting generic type
	*/
	private T requestor;

	/**
	*Creates a new instance of flusher for the requesting object type
	*@param requestor Object requesting flush method to be called
	*/
	public Flusher(T requestor)
	{
		this.requestor = requestor;
	}

	/**
	*Calls the requesting objects flush method
	*/
	public void run()
	{
		requestor.flush();
	}
}