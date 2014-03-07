
import java.util.Calendar;
import java.util.Comparator;

/**
*Class for comparing Meetings wrt the date of the meeting. 
*/

public class MeetingComparatorImpl<M extends Meeting> implements MeetingComparator<M>
{
	/**
	*Comparison based on chronology of the two Meetings.
	*Note that if two meetings occur at exactly the same time then the result will be -1 
	*@params Meetings for comparison
	*@returns positive int if parameter Meetings are in chronological order
	*/
	public int compare(M meeting1, M meeting2)
	{
		MeetingImpl mImpl1 = (MeetingImpl) meeting1;
		MeetingImpl mImpl2 = (MeetingImpl) meeting2;

		if(mImpl1.meetingDate.before(mImpl2.meetingDate))
		{
			return 1;
		}else{
			return -1;
		}
	}

}