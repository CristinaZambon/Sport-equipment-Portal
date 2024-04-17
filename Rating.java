package sports;

public class Rating {
	
	String comment;
	int star;
	 String userName;
	
	public Rating( String user, int star, String comment) {
		this.comment = comment;
		this.star = star;
		this.userName = user;
	}

	@Override
	public String toString() {
		return star + " : " + comment ;
	}
	
	
	public boolean formatStar() {
		
		if (star < 0)
			return false;
		if (star >=6)
			return false;
		
		return true;
	}

	public String getComment() {
		return comment;
	}

	public int getStar() {
		return star;
	}

	public String getUserName() {
		return userName;
	}
	

}
