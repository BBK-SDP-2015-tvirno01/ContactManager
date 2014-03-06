
import java.util.*;
import java.io.*;
import java.text.*;

public class ContactManagerImpl
{

	private int contactCount;
	private int meetingCount;
	private ArrayList<Contact> contactList;
	private ArrayList<Meeting> meetingList;

	public ContactManagerImpl()
	{
		/**
		*Instantiation of member fields
		*Initiate Shutdown Thread to run flusher, calls flush()
		*/

		this.readFile();

		Flusher hookWriter = new Flusher(this);
		Thread hook = new Thread(hookWriter);
		Runtime.getRuntime().addShutdownHook(hook);
	}

	private void readFile()
	{
		/**
		*Data stored in text file called contacts.txt in current file directory
		*/

		ObjectInputStream impt = null;
		try
		{
			impt = new ObjectInputStream(new FileInputStream(".contacts.txt"));
			this.contactCount = (int) impt.readObject();
			this.meetingCount = (int) impt.readObject();
			this.contactList = (ArrayList<Contact>) impt.readObject();
			this.meetingList = (ArrayList<Meeting>) impt.readObject();
		}catch(FileNotFoundException ex){
			this.contactCount = 0;
			this.meetingCount = 0;
			this.contactList = new ArrayList<Contact>();
			this.meetingList = new ArrayList<Meeting>();
		}catch(ClassNotFoundException ex){
			ex.printStackTrace();
		}catch(IOException ex){
			ex.printStackTrace();
		}finally{
			try
			{
				impt.close();
			}catch(IOException ex){
				ex.printStackTrace();
			}
		}
	}

	private boolean inPast(Calendar testDate)
	{
		Calendar rightNow = Calendar.getInstance();
		return testDate.before(rightNow);
	}

	private Meeting addNewMeeting(Calendar date, Set<Contact> contacts, String text)
	{
		/**
		*All meetings instantiated as PastMeetings to enable meetingNotes field
		*/

		this.meetingCount = this.meetingCount + 1;
		Meeting newMeeting = new PastMeetingImpl(date, contacts, text, this.meetingCount); 
		return newMeeting;
	}

	public int addFutureMeeting(Set<Contact> contacts, Calendar date)
	{
		try
		{
			if(inPast(date) || !contactList.containsAll(contacts))
			{
				throw new IllegalArgumentException();
			}

			Meeting newMeeting = addNewMeeting(date, contacts, "");
			this.meetingList.add(newMeeting);
			return newMeeting.getId();

		}catch(IllegalArgumentException ex){
			System.out.println("Invalid Future Meeting parameters");
			Integer nullResult = null;
			return nullResult;
		}
	}

	public PastMeeting getPastMeeting(int id)
	{
		PastMeeting result = null;
		PastMeetingImpl tempM = null;

		try
		{
			result = (PastMeeting) this.getMeeting(id);

			tempM = (PastMeetingImpl) result;			

			if(result.equals(null))
			{
				return null;
			}else{
				if(inPast(tempM.meetingDate))
				{
					return result;
				}else{
					throw new IllegalArgumentException();
				}
			}
		}catch(IllegalArgumentException ex){
			System.out.println("Selected Meeting occurs in the past");
			result = null;
			return result;
		}
	}

	public FutureMeeting getFutureMeeting(int id)
	{
		MeetingImpl tempM = null;

		try
		{
			FutureMeeting result = (FutureMeeting) this.getMeeting(id);
			if(result.equals(null))
			{
				return null;
			}else{
				if(inPast(tempM.meetingDate))
				{
					throw new IllegalArgumentException();
				}else{
					return result;
				}
			}
		}catch(IllegalArgumentException ex){
			System.out.println("Selected Meeting occurs in the past");
			FutureMeeting nullResult = null;
			return nullResult;
		}
	}

	public Meeting getMeeting(int id)
	{
		MeetingImpl tempM = null;

		for(Meeting m : meetingList)
		{
			tempM = (MeetingImpl) m;

			if(tempM.meetingID==id)
			{
				return m;
			}
		}

		return null;		
	}	

	public List<Meeting> getFutureMeetingList(Contact contact)
	{
		List<Meeting> result = new ArrayList<Meeting>();
		
		MeetingImpl tempM = null;

		try
		{
			if(!contactList.contains(contact))
			{
				throw new IllegalArgumentException();
			}else{
				for(Meeting m : meetingList)
				{
					tempM = (MeetingImpl) m;
					if(!inPast(tempM.meetingDate))
					{
						if(tempM.participants.contains(contact))
						{
							result.add(m);
						}
					}
				}
			}
			
			MeetingComparator<Meeting> cDate = new MeetingComparatorImpl<Meeting>();

			Collections.sort(result, cDate);

			return result;

		}catch(IllegalArgumentException ex){
			System.out.println("Contact is not a member of contact list");
			return result;
		}
	}

	public List<Meeting> getFutureMeetingList(Calendar date)
	{
		List<Meeting> result = new ArrayList<Meeting>();

		SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");

		MeetingImpl tempM = null;
		
		for(Meeting m : meetingList)
		{
			tempM = (MeetingImpl) m;
			if(f.format(date).equals(f.format(tempM.meetingDate)))
			{
				result.add(m);
			}
		}

			MeetingComparator<Meeting> cDate = new MeetingComparatorImpl<Meeting>();

			Collections.sort(result, cDate);

			return result;
		
	}	

	public List<PastMeeting> getPastMeetingList(Contact contact)
	{
		List<PastMeeting> result = new ArrayList<PastMeeting>();

		PastMeetingImpl tempM = null;
		PastMeeting tempPM = null;
		
		try
		{
			if(!contactList.contains(contact))
			{
				throw new IllegalArgumentException();
			}else{
				for(Meeting m : meetingList)
				{
					tempM = (PastMeetingImpl) m;
					if(inPast(tempM.meetingDate))
					{
						if(tempM.participants.contains(contact))
						{
							tempPM = (PastMeeting) tempM;
							result.add(tempPM);
						}
					}
				}
			}
			
			MeetingComparator<PastMeeting> cDate = new MeetingComparatorImpl<PastMeeting>();

			Collections.sort(result, cDate);

			return result;

		}catch(IllegalArgumentException ex){
			System.out.println("Contact is not a member of contact list");
			return result;
		}
	}

	public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text)
	{
		try
		{
			if(contacts.equals(null) || date.equals(null) || text.equals(null))
			{
				throw new NullPointerException();
			}

			if(contacts.isEmpty() || !contactList.containsAll(contacts))
			{
				throw new IllegalArgumentException();
			}

			this.meetingCount = this.meetingCount + 1;
			Meeting newMeeting = new PastMeetingImpl(date, contacts, text, this.meetingCount);

			this.meetingList.add(newMeeting);

		}catch(NullPointerException ex){
			System.out.println("One of the arguments is null");
		}catch(IllegalArgumentException ex){
			System.out.println("Invalid set of contacts");
		}
	}	

	public void addMeetingNotes(int id, String text)
	{
		try
		{
			if(text.equals(null))
			{
				throw new NullPointerException();
			}
			
			PastMeetingImpl pMeeting = (PastMeetingImpl) getMeeting(id); //downcast Meeting as PastMeetingImpl
	
			if(pMeeting.equals(null))
			{
				throw new IllegalArgumentException();
			}
	
			if(!inPast(pMeeting.meetingDate))
			{
				throw new IllegalStateException();
			}


			pMeeting.addNotes(text);

		}catch(NullPointerException ex){
			System.out.println("Meeting notes are null");
		}catch(IllegalArgumentException ex){
			System.out.println("Meeting ID not recognised");
		}catch(IllegalStateException ex){
			System.out.println("Meeting occurs in the future");
		}
	}

	public void addNewContact(String name, String notes)
	{
		try
		{
			if(name.equals(null) || notes.equals(null))
			{
				throw new NullPointerException();
			}
			
			this.contactCount = this.contactCount + 1;
			Contact newContact = new ContactImpl(name,notes,this.contactCount);
			this.contactList.add(newContact);

		}catch(NullPointerException ex){
			System.out.println("An input parameter is null");
		}
	}
	
	public Set<Contact> getContacts(int... ids)
	{
		Set<Contact> result = new HashSet<Contact>();
		ContactImpl tempC = null;
		try
		{
			List<Integer> control = new ArrayList<Integer>();
			/**
			*control Array used to confirm that all ids have been found
			*ids are removed as they are found in contactList
			*/

			for(int i : ids)
			{
				control.add(i);
			}

			for(Contact c : this.contactList)
			{
				tempC = (ContactImpl) c;

				if(control.contains(tempC.contactID))
				{
					result.add(c);
					control.remove(tempC.contactID);
				}
			}

			if(!control.isEmpty())
			{
				throw new IllegalArgumentException();
			}

			return result;

		}catch(IllegalArgumentException ex){
			System.out.println("Not all IDs exist in contact list");
			result = null;
			return result;
		}
	}

	public Set<Contact> getContact(String name)
	{
		Set<Contact> result = new HashSet<Contact>();
		ContactImpl tempC = null;
		try
		{
			if(name.equals(null))
			{
				throw new NullPointerException();
			}

			for(Contact c : this.contactList)
			{
				tempC = (ContactImpl) c;
				if(tempC.contactName.contains(name))
				{
					result.add(c);
				}
			}

			return result;

		}catch(NullPointerException ex){
			System.out.println("Search criteria is null");
			result = null;
			return result;
		}
	}

	public void flush()
	{
		FileOutputStream saveFile = null;
		try
		{
			saveFile = new FileOutputStream(".contacts.txt");
			ObjectOutputStream expt = new ObjectOutputStream(saveFile);
			expt.writeObject(this.contactCount);
			expt.writeObject(this.meetingCount);
			expt.writeObject(this.contactList);
			expt.writeObject(this.meetingList);
		}catch(IOException ex){
			ex.printStackTrace();
		}finally{
			try
			{
				saveFile.close();
			}catch(IOException ex){
				ex.printStackTrace();
			}
		}
	}

}