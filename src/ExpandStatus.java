import java.util.*;
import java.io.*;

public class ExpandStatus {

	// int to detect if this is first repo expansion or not
	private static int count = 0;

	public static void expand(boolean anyRepos) {

		if (anyRepos == false) {
			System.out.println("No git repositories were found in the specified directory.\n");
			System.out.println("**************************************************************\n");
			System.out.println("Thanks for using gitHound!\n");
		}
		else {
			// prompt print out
			
			if (count == 0) {
				System.out.println("\n**************************************************************\n");
				System.out.println("Would you like to expand the status information of a repo above?");
			}
			else {
				System.out.println("**************************************************************\n");
				System.out.println("Would you like to expand the status information of another repo above?");
			}
			System.out.println("Enter: (yes/no)");

			// method to read in if user wants to expand a repo status or not
			try {
			    BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			    
			    // boolean to detect valid answer
			    boolean validAns = false;

			   	do {
			   		// take in user input
				    String s = bufferRead.readLine();
				    
				    // if yes go to showStatus method and change validAns to true
				    if (s.equals("yes") || s.equals("Yes") || s.equals("YES")) {
				    	showStatus();
				    	validAns = true;
				    }

				    // turn validAns to true
				    else if (s.equals("no") || s.equals("No") || s.equals("NO")) {
				    	System.out.println("\nThank you for using gitHound!");
				    	validAns = true;
				    }

				    // else ask for new input yes or no
				    else {
				    	System.out.println("Wrong input. Input (yes/no)");
				    }

				} while (validAns == false);

			} // try
			
			catch(IOException e) {
				e.printStackTrace();
			} // catch
		}

	} // expand method

/************************************************************************
* showStatus method
*
* This method gets the status information of the desired git repo
* ***********************************************************************/

	public static void showStatus() {
		System.out.println("Copy and paste a repo from above you would like to expand the status of:");
		count++;
		try {
		    BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		    String s = bufferRead.readLine();

		    ProcessBuilder p1 = new ProcessBuilder("/usr/bin/git","status");
			
			// then change working directory to s with p.directory command
			p1.directory(new File(s));

			Process process = p1.start();
			int errCode = process.waitFor();
			// find out if there was an error
			//System.out.println("Any errors? " + (errCode == 0 ? "No" : "Yes"));

			// stringbuilder to take in the output from git status
			StringBuilder sb2 = new StringBuilder();

			// bufferreader to take in the output from git status
			BufferedReader reader = 
				new BufferedReader(new InputStreamReader(process.getInputStream()));

			// while loop to build string of output
			String line = "";			
				while ((line = reader.readLine())!= null) {		    
					sb2.append(line + "\n");
				}

			String status = sb2.toString();
			// print out the status
			System.out.println("\n**************************************************************\n");
			System.out.println(status);

			// recursion to restart process
			expand(true);

		} // try
		
		catch(IOException e) {
			//e.printStackTrace();
			System.out.println("\n**************************************************************\n");
			System.out.println("Invalid working directory path! \nPlease try again\nIf you are wondering why you should expand a repository, its because its so much fun! :D\n");
			expand(true);

		} // catch
		catch(Exception e) {
			e.printStackTrace();
		} // catch

	} // show status method

} // expandstatus class