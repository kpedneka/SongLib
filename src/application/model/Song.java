package application.model;

/**
 * 
 * @author Kunal Pednekar (ksp101)
 * @author Rachana Thanawala (rt468)
 *
 */
public class Song {
	
	private String title;
	private String artist;
	private String album;
	private String year;

	public Song(String t, String ar, String al, String y)
	{
		title=t;
		artist=ar;
		album=al;
		year=y;
		
	}
	
	public String getTitle()
	{
		return title;
	}
	public String getArtist()
	{
		return artist;
	}
	public String getAlbum()
	{
		return album;
	}
	public String getYear()
	{
		return year;
	}
	public void setTitle(String x)
	{
		title = x;
	}
	public void setArtist(String x)
	{
		artist= x;
	}
	public void setAlbum(String x)
	{
		album = x;
	}
	public void setYear(String x)
	{
		year=x;
	}
}
