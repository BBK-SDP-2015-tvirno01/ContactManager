
import java.util.Calendar

public class ContactImpl implements Contact
{
	public final int contactID;
	public final Sting contactName;
	private String contactNotes;

	public ContactImpl(String name, String notes, int ID)
	{
		this.contact ID = ID;
		this.contactName = name;
		this.addNotes(notes);
	}


	public int getId()
	{
		return this.contactID;
	}

	public String getName()
	{
		return contactName;
	}

	public String getNotes()
	{
		if(this.contactNotes==null)
		{
			return "";
		}else{
			return contactNotes;
		}
	}

	public void addNotes(String note)
	{
		Calendar rightNow = Calendar.getInstance();
		String eol = System.getProperty("line.seperator");
		String date&time = rightNow.toString();

		if(this.contactNotes==null || this.contactNotes.equals(""))
		{
			this.contactNotes = date&time + eol + note;	
		}else{
			this.contactNotes = this.contactNotes + eol + eol
						+ date&time + eol + note;

		}
	}

}