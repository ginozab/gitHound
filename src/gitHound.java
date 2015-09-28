import java.util.*;
import java.io.*;

public class gitHound {

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";

	private static int untrackedF = 0;
	private static int clean = 0; 
	private static int ahead = 0;
	private static int modified = 0; 
	private static int staged = 0;

	public static void main(String[] args) {
		// if no arguments are inputed show usage instructions
		if (args.length == 0) {
			System.out.println("Proper usage: \n");
			System.out.println("Args 1: <Path to directory> (Relative to current directory)");
			System.out.println("Args 2: <quick> or <full>");
			System.out.println("quick -> quick summary stats of all git repos in directory");
			System.out.println("full -> summary status of all git repos in directory");
		}
		// if they want full make quickSummary false and run expand method
		else if (args.length == 2 && args[1].equals("full")){
			String dir = args[0];
			File cDir = new File(dir); // get current directory
			instructions();
			boolean quickSummary = false;
			findGitDirs(cDir, quickSummary);
			ExpandStatus.expand();
		}
		// if they want quick make quicksummary1 true and just print quick summary stats
		else if (args.length == 2 && args[1].equals("quick")) {
			String dir = args[0];
			File cDir = new File(dir); // get current directory
			boolean quickSummary1 = true;
			findGitDirs(cDir, quickSummary1);
			printQuickSum();
		}
		// anything else show usage instructions
		else {
			System.out.println("Proper usage: \n");
			System.out.println("Args 1: <Path to directory> (Relative to current directory)");
			System.out.println("Args 2: <quick> or <full>");
			System.out.println("quick -> quick summary stats of all git repos in directory");
			System.out.println("full -> summary status of all git repos in directory");
		}

	} // main

/************************************************************************
* findGitDirs method
*
* This method finds all of the folders that are git repositories
* ***********************************************************************/

	public static void findGitDirs(File cd, boolean quickSummary) {

		try {

			File[] files = cd.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					
					// This is the key if the path contains .git
					if (file.getCanonicalPath().contains(".git")) {
						// call gitInfo method
						gitInfo(file, quickSummary);
						
					} // if contains .git

					// if no git folder found continue searching 
					// with recursive call
					else {
						findGitDirs(file, quickSummary);
					} // else if doesn't contain .git

				} // big if file.isDirectory

				else {
				// don't do anything for just files since we are focusing on 
				// git directories
				} // else if not file.isDirectory

			} // for loop

		} // try

		catch (IOException e) {
			e.printStackTrace();
		} // catch

	} // findGitDirs method

/************************************************************************
* gitStatus method
*
* This method runs the git status command in the directory that the 
* method findGitDirs found to be a git repository
* It takes in a file and prints out the status of each git repo
* ***********************************************************************/

	public static void gitInfo(File file, boolean quickSummary) {

		try {
			// create the processbuilder to run git status command
			// first arg is where binary is located second is command to be run
			ProcessBuilder p = new ProcessBuilder("/usr/bin/git","status");
			String path = file.getCanonicalPath();
			//System.out.println(ANSI_WHITE + fetchDir(path) + ANSI_RESET);
			
			// get the path without .git at end using fetchDir
			// then change working directory to it with p.directory command
			String newPath1 = fetchDir(path);
			p.directory(new File(newPath1));

			Process process = p.start();
			int errCode = process.waitFor();
			// find out if there was an error
			//System.out.println("Any errors? " + (errCode == 0 ? "No" : "Yes"));

			// stringbuilder to take in the output from git status
			StringBuilder sb1 = new StringBuilder();

			// bufferreader to take in the output from git status
			BufferedReader reader = 
				new BufferedReader(new InputStreamReader(process.getInputStream()));

			// while loop to build string of output
			String line = "";			
				while ((line = reader.readLine())!= null) {
						    
				//System.out.println(line + "\n");
				sb1.append(line + "\n");
				}

			String status = sb1.toString();

			// call parseStatus to determine the status of git repo
			int[] st = parseStatus(status);

			if (quickSummary == false) {
				printAll(newPath1, st);
			}

			else {

			}

		} // try block

			catch (Exception e) {
				e.printStackTrace();
			}

	} // gitStatus

/************************************************************************
* fetchDir method
*
* This method is needed because my recursive method to find git directories
* is keyed off by the .git folder so this method takes the working directory
* as a string and cuts off the .git at the end returning the new 
* working directory as a string
* ***********************************************************************/

	public static String fetchDir(String path) {
		
		String[] sep = path.split("/");

		StringBuilder sb = new StringBuilder();
		String newPath = "";
		for (int i = 0; i < sep.length -1; i++) {

			sb.append(newPath + sep[i] + "/");

		} // for

		return sb.toString();

	} // fetchDir method

/************************************************************************
* parseStatus method
*
* This method parses the output of git status command
* parseStatus returns an array of 0s and 1s depending on the status
* of the git folder being looked at
* ***********************************************************************/

	 public static int[] parseStatus(String stat) {
	 	int[] s = {0,0,0,0,0,0};
		// if there are untracked files
		if (stat.contains("Untracked files:")) {
			untrackedF++;
			s[0] = 1;
		}

		// if there are files added and not committed
		if (stat.contains("Changes to be committed")) {
			staged++;
			s[1] = 1;
		}

		// if the folder is up to date
		if (stat.contains("Your branch is up-to-date with 'origin/master'") && stat.contains("nothing to commit")) {
			clean++;
			s[2] = 1;
		}

		// if the folder is ahead of branch
		if (stat.contains("Your branch is ahead of 'origin/master'")) {
			ahead++;
			s[3] = 1;
		}

		// if the folder has modified files not commited
		if (stat.contains("Changes not staged for commit") && stat.contains("modified:")) {
			modified++;
			s[4] = 1;
		}

		// other output?
		else {
			s[5] = 1;
		}

		return s;
 
	 } // parseStatus method

/************************************************************************
* printAll method
*
* This method takes in the path as a string and prints out the 
* the path in a color depending on the git status output given by the second
* argument which is an int array of 0s and 1s. 
* It will also print out color coded plus signs next to the directory 
* to quickly tell the user what exactly is the status of the repo
* ***********************************************************************/

	public static void printAll(String path, int[] stats) {
		// prints directory in red if untracked or uncommited files
		if (stats[0] == 1 || stats[1] == 1 || stats[4] == 1) {
			System.out.print(ANSI_RED + path + ANSI_RESET + "\nProblems with above repo: ");
		}
		// prints directory in cyan if up to date but ahead of branch
		else if (stats[3] == 1) {
			System.out.print(ANSI_CYAN + path + ANSI_RESET + "\nProblems with above repo: ");
		}
		// prints out green if up to date
		else {
			System.out.print(ANSI_GREEN + path + ANSI_RESET + "\nNo problems with above repo.");
		}

// This set of if statements prints out the plus signs that indicate the exact status
// of each git repo under review

		// prints out red "+" if there are untracked files
		if (stats[0] == 1) {
			System.out.print(ANSI_RED + " +" + ANSI_RESET);
		}

		// prints out yellow + if there are new files not committed
		if (stats[1] == 1) {
			System.out.print(ANSI_YELLOW + " +" + ANSI_RESET);
		}

		// prints out green + if everything is up to date
		// if (stats[2] == 1) {
		// 	System.out.print(ANSI_GREEN + " +" + ANSI_RESET);
		// }

		// prints out cyan + if ahead of branch
		if (stats[3] == 1) {
			System.out.print(ANSI_CYAN + " +" + ANSI_RESET);
		}

		// prints out white + if there are modified files
		if (stats[4] == 1) {
			System.out.print(ANSI_WHITE + " +" + ANSI_RESET);
		}

		else {}

		System.out.print("\n");
	} // printAll method

/************************************************************************
* Instructions method
*
* This method will print out the color scheme directory so the user knows 
* what each color means. 
* ***********************************************************************/

	public static void instructions() {

		System.out.println("\nThank you for choosing gitHound!");
		System.out.println("Here are the color code instructions:\n");
		System.out.println("Directory color scheme:");
		System.out.println(ANSI_GREEN + "Green" + ANSI_RESET + " -> clean repo");
		System.out.println(ANSI_RED + "Red" + ANSI_RESET + " -> repo not up to date");
		System.out.println(ANSI_CYAN + "Cyan" + ANSI_RESET + " -> repo ahead of origin\n");
		System.out.println("Problem '+' Color scheme:");
		System.out.println(ANSI_RED + "Red + " + ANSI_RESET + " -> Untracked files");
		System.out.println(ANSI_YELLOW + "Yellow + " + ANSI_RESET + " -> New files not commited");
		System.out.println(ANSI_CYAN + "Cyan + " + ANSI_RESET + " -> repo ahead of origin");
		System.out.println(ANSI_WHITE + "White + " + ANSI_RESET + " -> Modified files not commited\n");

		System.out.println("Here are the git repos we found in your specified directory: \n");

	} // instructions method

/************************************************************************
* printQuickSum method
*
* This method prints out the quick summary statistics 
* ***********************************************************************/

	public static void printQuickSum() {

		System.out.println("\nThank you for choosing gitHound!");
		System.out.println("Here is a quick summary of all the git repos in your specified directory: \n");
		System.out.println("***********************************************************************\n");
		System.out.print("Number of clean git repositories: ");
		System.out.println(clean);
		System.out.print("Number of git repositories ahead of origin: ");
		System.out.println(ahead);
		System.out.print("Number of git repositories with untracked files: ");
		System.out.println(untrackedF);
		System.out.print("Number of git repositories with modified files: ");
		System.out.println(modified);
		System.out.print("Number of git repositories with staged but not committed files: ");
		System.out.println(staged);
		System.out.println("***********************************************************************\n");
	}

} // class




