
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.File;

public class Writer implements Runnable
{
	private ContactManager CM;

	public Writer(ContactManager requestor)
	{
		this.CM = requestor;
	}

	public void run()
	{
		CM.writeLists();
	}
}