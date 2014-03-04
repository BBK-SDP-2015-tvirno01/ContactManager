
import java.util.Calendar;
import java.util.Set;

public class MeetingImpl implements Meeting
{
	public final int meetingID;
	public final Calendar meetingDate;
	public final Set<Contact> participants;

	public MeetingImpl(Calendar date,Set<Contact> participants, int ID)

	{
		this.meetingID = ID;
		this.meetingDate = date;
		this.participants = participants;
	}

	public int getId()
	{
		return this.meetingID;
	}

	public Calendar getDate()
	{
		return this.meetingDate;
	}

	public Set<Contact> getContacts()
	{
		return participants;
	}
}