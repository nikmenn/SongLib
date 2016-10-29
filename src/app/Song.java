/*
 * Nikhil Menon and Renard Tumbokon 
 */

package app;

public class Song
{
	public String title;
	public String artist;
	public String album;
	public String year;
	public Song next; 
	
	public Song(String title, String artist, String album, String year){ //constructor
		// required 
		this.title = title;
		this.artist = artist;
		
		// optional.. set to "" or 0 if none entered
		this.album = album;
		this.year = year;
		
		// for linked list
		this.next = null;
	}
}