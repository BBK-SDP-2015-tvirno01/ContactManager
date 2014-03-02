
import java.util.Calendar;

public class MeetingList extends Set
{
	//Implement meeting list as linked list with meetings closest to current date at start

	private Meeting meeting;
	private MeetingList nextMeeting;

	public MeetingList(Meeting meeting)
	{
		this.meeting = meeting;
		this.nextMeeting = null;
	}

	public void addPastMeeting(Meeting newMeeting)
	{
		MeetingList newNode = new MeetingList(newMeeting);
		Pointer<MeetingList> P = new Pointer<MeetingList>(this);

		while(!P.point.nextMeeting.equals(null))
		{
			if(newNode.meeting.meetingDate.after(P.point.nextMeeting.meeting.meetingDate))
			{
				break;
			}

			P.point = P.point.nextMeeting;
		}

			newNode.nextMeeting = this.nextMeeting;
			P.point.nextMeeting = newNode;
	}

	public void addFutureMeeting(Meeting newMeeting)
	{
		MeetingList newNode = new MeetingList(newMeeting);
		Pointer<MeetingList> P = new Pointer<MeetingList>(this);

		while(!P.point.nextMeeting.equals(null))
		{
			if(newNode.meeting.meetingDate.before(P.point.nextMeeting.meeting.meetingDate))
			{
				break;
			}

			P.point = P.point.nextMeeting;
		}

			newNode.nextMeeting = this.nextMeeting;
			P.point.nextMeeting = newNode;
	}

	public Meeting getMeeting(int ID)
	{
		Pointer<MeetingList> P = new Pointer<MeetingList>(this);

		while(!P.point.nextMeeting.equals(null))
		{
			if(ID==newNode.meeting.meetingID)
			{
				return P.point.meeting
			}

			P.point = P.point.nextMeeting;
		}

		return null;
	}

	public MeetingList contactSchedule(Contact ctc)
	{
		Calendar currentDate = Calendar.getInstance();
		List searchContact = new ContactList(ctc);
		Meeting criteria = new MeetingImpl(currentDate,searchContact);
		List result = new MeetingList(criteria);
		Pointer<MeetingList> P = new Pointer<MeetingList>(this);

		while(!P.point.nextMeeting.equals(null))
		{
			if(P.point.meeting.participants.search(ctc))
			{
				Meeting returnMeeting = new Meeting(P.point.meeting.meetingDate,P.point.meeting.participants);
				result.addFutureMeeting(returnMeeting);
			}

			P.point = P.point.nextMeeting;
		}
		return result.nextMeeting;
	}

	public MeetingList dateSchedule(Calendar date)
	{
		SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
		List dummy = new ContactList("0");
		Meeting criteria = new MeetingImpl(date,dummy);
		List result = new MeetingList(criteria);
		Pointer<MeetingList> P = new Pointer<MeetingList>(this);

		while(!P.point.nextMeeting.equals(null))
		{
			if(f.format(P.point.meeting.meetingDate).equals(f.format(date))
			{
				result.addFutureMeeting(P.point);
			}

			P.point = P.point.nextMeeting;
		}
		return result.nextMeeting;
	}





}