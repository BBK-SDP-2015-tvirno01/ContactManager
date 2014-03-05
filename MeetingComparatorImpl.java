
import java.util.Calendar;
import java.util.Comparator;

public class MeetingComparatorImpl<M extends Meeting> implements MeetingComparator<M>
{
	
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