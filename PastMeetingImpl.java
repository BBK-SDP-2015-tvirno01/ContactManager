
public class PastMeetingImpl extends MeetingImpl implements PastMeeting
{

	private String meetingNotes;

	public PastMeetingImpl(Calendar date,Set<Contact> participants, String notes, int ID)
	{
		String eol = System.getProperty("line.seperator");
		Calendar rightNow = Calendar.getInstance();
		String time&date = rightNow.toString();

		super(date, participants, ID);
		this.meetingNotes = time&date+eol+notes;
	}

	public String getNotes()
	{
		String eol = System.getProperty("line.seperator");

		if(this.meetingNotes==null)
		{
			return "";
		}else{
			return 	"Meeting ID: "+this.meetingID+eol+
				"Date: "+this.meetingDate.toString()+eol+
				"Notes: "+this.meetingNotes;
		}
	}

	public void addNotes(String text)
	{
		String eol = System.getProperty("line.seperator");
		Calendar rightNow = Calendar.getInstance();
		String time&date = rightNow.toString();
		
		if(this.meetingNotes==null)
		{
			this.meetingNotes = text;
		}else{
			this.meetingNotes = this.meetingNotes + eol + eol + time&date + eol + text;
		}
	}

}