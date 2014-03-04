
import java.util.Calendar;
import java.util.Set;

public class PastMeetingImpl extends MeetingImpl implements PastMeeting
{

	private String meetingNotes;

	public PastMeetingImpl(Calendar date,Set<Contact> participants, String notes, int ID)
	{
		super(date, participants, ID);
		String eol = System.getProperty("line.seperator");
		this.meetingNotes = notes;
	}

	public String getNotes()
	{
		String eol = System.getProperty("line.seperator");

		if(this.meetingNotes==null)
		{
			return "";
		}else{
			return 	this.meetingNotes;
		}
	}

	public void addNotes(String text)
	{
		String eol = System.getProperty("line.seperator");
		
		if(this.meetingNotes==null)
		{
			this.meetingNotes = text;
		}else{
			this.meetingNotes = this.meetingNotes + eol + text;
		}
	}

}