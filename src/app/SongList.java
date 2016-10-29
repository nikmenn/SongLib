/*
 * Nikhil Menon and Renard Tumbokon 
 */

package app;

import java.io.*;
import app.Song;

public class SongList
{
	public Song front;
	
	public SongList(){ //constructor 
		front = null;
		populateListFrFile();
	}
	
	/*
	private void populateListFrFile(){
		add("song1", "artist1A", "album1", "year1");
		add("song2", "artist2", "album2", "");
		add("song1", "artist1C", "", "");
		add("song3", "artist3", "", "");
		add("song1", "artist1B", "", "");
		
		
	}
	*/
	
	public void populateListFrFile(){
		String fileName = "save.txt";
		String line = null; // String holds 1 line (at a time)
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while((line = bufferedReader.readLine()) != null){
				//update linked list
				String songTitle, songArtist, songAlbum, songYear;
				
				// song info should be in groups of 4 lines
				songTitle = line;
				line = bufferedReader.readLine(); songArtist = line; 
				line = bufferedReader.readLine(); songAlbum = line;
				line = bufferedReader.readLine(); songYear = line; 
				
				add(songTitle, songArtist, songAlbum, songYear);
			}
			bufferedReader.close(); 
		} catch(FileNotFoundException ex){
			System.out.println("Error: can't find file: " + fileName);
		} catch(IOException ex){
			System.out.println("Error: can't read file: " + fileName);
		}
	}
	
	private void updateListToFile(){
		BufferedWriter writer = null;
		try {
			File file = new File("save.txt");
			writer = new BufferedWriter(new FileWriter(file));
			
			//write stuff here
			Song ptr = front;
			while (ptr != null){
				// song info write in groups of 4 lines into double space for next song
				writer.write(ptr.title + "\n");
				writer.write(ptr.artist + "\n");
				writer.write(ptr.album + "\n");
				writer.write(ptr.year + "\n");

				ptr = ptr.next;				
			}
		} catch (Exception e){
			e.printStackTrace();
		} 
		try { 
			writer.close();
		} catch (Exception e){
		}
	}
	
	
	/*
	private void updateListToFile(){
		return;
	}
	*/
	
	public boolean isEmpty(){
		return (front == null);
	}
	
	public boolean add(String title, String artist, String album, String year){
		Song newSong = new Song(title, artist, album, year);
		return insertSort(newSong);
	}
	
	// called from add
	private boolean insertSort(Song song){
		// first song
		if (front == null){
			front = song; 
			updateListToFile(); // update save.txt
			return true;
		}
		Song ptr = front;
		Song prev = null;
		
		// song is new front
		if (front.title.compareTo(song.title) > 0){
			song.next = front;
			front = song;
			updateListToFile(); // update save.txt
			return true;
		}
		
		// move ptr until lexicographically less than song (or equal (or null)
		while (ptr != null && ptr.title.compareTo(song.title) <= 0){
			if (ptr.title.equals(song.title)){ //if same titles
				if (ptr.artist.equals(song.artist)){
					// same exact song title + artist exists already
					return false;					
				}
				//compare artists to determine order
				if (ptr.artist.compareTo(song.artist) > 0){
					break;
				}
			}
			prev = ptr; 
			ptr = ptr.next;
		}
		
		// song is last
		if (ptr == null){
			prev.next = song;
			song.next = null;
			updateListToFile(); // update save.txt
			return true;
		}
		
		// song in middle
		prev.next = song;
		song.next = ptr; 
		updateListToFile(); // update save.txt
		return true;
	}
	
	public Song findSong(String title, String artist){
		//traverse list until found matching song and return matching
		Song ptr = front;
		while (ptr != null){
			if (ptr.title == title && ptr.artist == artist){
				return ptr; // found song
			}
			ptr = ptr.next;
		}
		// not found song
		System.out.println("Error: Song title=" + title + ", artist=" + artist + " not found");
		return null; 
	}
	
	public Song findSong(int index){
		//traverse list until found matching song and return matching
		Song ptr = front;
		for (int i = 0; i < index; i++){
			ptr = ptr.next; // go to index of linked list
		}
		if (ptr != null){
			return ptr; // found song
		}
		// not found song
		System.out.println("Error: Song index=" + index + " not found");
		return null;		
	}
	
	public int findIndex(String title, String artist){
		int foundIndex = 0;
		Song ptr = front;
		while(ptr != null){
			if (ptr.title == title && ptr.artist == artist){
				return foundIndex;
			}
			ptr = ptr.next;
			foundIndex++;
		}
		return -1; // ret -1 if not found
	}
	
	public boolean edit(String oldTitle, String oldArtist, String newTitle, String newArtist, String newAlbum, String newYear){
		// traverse list until found matching song, delete song and make new song to sort 
		boolean isExist = delete(oldTitle, oldArtist); // false if title + artist not found
		if (isExist){
			isExist = add(newTitle, newArtist, newAlbum, newYear); // false if new title + artist exists
		}
		return isExist;
	}
	
	public boolean delete(String title, String artist){
		//if null
		if (front == null){
			front = null;
			return false;
		}	
		
		Song ptr = front;
		Song prev = null;
		
		//if delete front
		if (ptr.title == title && ptr.artist == artist){
			front = front.next; 
			return true;
		}
		
		while (ptr != null){
			if (ptr.title.equals(title) && ptr.artist.equals(artist)){
				// found matching title and artist
				prev.next = ptr.next;
				updateListToFile(); // update save.txt
				return true;
			}
			prev = ptr; 
			ptr = ptr.next;
		}
		return false; // if not found matching title and artist
	}
	
	public void printList(){
		Song ptr = front;
		System.out.println("");
		while (ptr != null){
			System.out.print("[" + ptr.title + "][" + ptr.artist + "][" + ptr.album + "][" + ptr.year + "] -> ");
			ptr = ptr.next;
		}
		System.out.println("");
	}
}