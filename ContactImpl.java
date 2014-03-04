
public class ContactImpl implements Contact
{
	public final int contactID;
	public final String contactName;
	private String contactNotes;

	public ContactImpl(String name, String notes, int ID)
	{
		this.contactID = ID;
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
		String eol = System.getProperty("line.seperator");

		if(this.contactNotes==null || this.contactNotes.equals(""))
		{
			this.contactNotes = note;	
		}else{
			this.contactNotes = this.contactNotes + eol + note;

		}
	}

}