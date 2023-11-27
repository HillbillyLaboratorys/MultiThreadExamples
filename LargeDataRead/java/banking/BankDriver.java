package banking;

import java.io.File;
import threadWrapper.*;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class BankDriver {
 
	public static void main(String[] args) throws FileNotFoundException {
		 /*// feel free to create a different file with smaller/larger number of records
		 * // create a file with random records createAccountsFile(4000000);
		 * System.out.println("File Was Created!");
		 */
	    
	    	createAccountsFile(400000);
	   
		ArrayList<Customer> custList = new ArrayList<Customer>();
		// reading the info from the file and storing in the arrayList
		long currentRec=0;
		System.out.println("Reading the file \"accounts.txt\" ");
		Scanner inFile = new Scanner(new File("accounts.txt")); 
		while (inFile.hasNext()) { // while 2
			String currID = inFile.nextLine();
			double currBalance = inFile.nextDouble();
			if (inFile.hasNextLine())
				inFile.nextLine(); // dummy reading
			custList.add(new Customer(currID, currBalance));
			if (++currentRec % 30000==0)
				System.out.print(">>");
			if (currentRec % 1000000==0)
				System.out.println( );
			
		} // end of while 2

		// Counting the balances that are less than 1000$
		int lowBalances = 0;
		// --Sequential-Run--------------------------------------------------------------
		long startTime = System.currentTimeMillis();
		lowBalances = sequentialCounting(custList); // sequential run
		long estimatedTime = System.currentTimeMillis() - startTime;
		System.out.println("Number of accounts with less than 1000$ is: " + lowBalances);
		System.out.println("Sequential run took in miliseconds: " + estimatedTime);
		// --End of Sequential-Run--------------------------------------------------------
		// *******************************************************************************
		// --Parallel Run-----------------------------------------------------------------
		final int threadCount = 6;
		
		// create list of workers so they can be accessed after the threads finish
		ArrayList<Worker> workers = new ArrayList<Worker>();
		// create list of threads 
		ArrayList<Thread> threads = new ArrayList<Thread>();
		
		// determine section size for each thread
		int chunk = custList.size() / threadCount;
		
		long begin = System.currentTimeMillis();
		
		for (int i = 0; i < threadCount; i++) {
		    workers.add(new Worker(custList, 1000));
		}
		// init each thread
		for (int i = 0; i < threadCount - 1; i++) {
		    		    
		    threads.add(new Thread(new Extended(workers.get(i),i*chunk, (i+1)*chunk)));
		}
		// last thread takes left over from unequal chunk size
		threads.add(new Thread(new Extended(workers.get(threadCount - 1),(threadCount - 1)*chunk, custList.size() - 1)));
		
		for (int i = 0; i < threadCount; i++) {
		    threads.get(i).start();
		}
		
		for (int i = 0; i < threadCount; i++) {
		    try {
			threads.get(i).join();
		    } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		}
		
		int totalLow = 0;
		// collect all results
		for (int i = 0; i < threadCount; i++) {
		    totalLow += workers.get(i).getCount();
		}
		
		long totalTime = System.currentTimeMillis() - begin;
		
		System.out.println("Number of accounts with less than 1000$ is: " + totalLow);
		System.out.println("Parallel run took in miliseconds: " + totalTime);
		
		// --End-of-Parallel-Run----------------------------------------------------------

	}// end of main

	static int sequentialCounting(ArrayList<Customer> myList) {
		int count = 0;
		for (int i = 0; i < myList.size(); ++i)
			if (myList.get(i).getBalance() < 1000)
				++count;
		return count;
	};

	static void createAccountsFile(int count) throws FileNotFoundException {
		File accountsFile = new File("accounts.txt");
		PrintStream outFile = new PrintStream(accountsFile);
		Random rand = new Random();
		// needed for making sure that there is no duplication in the IDs
		// ArrayList<String> tmpIDs = new ArrayList<String>();

		// generating random records IDs from XXXX0 to XXXX300000000 (non-repeating) and
		// balances 0 to 1000000
		for (int i = 0; i < count; ++i) { 
			int tempId;
			tempId = rand.nextInt(300000001);
			// try to generate a random ID with 3 random letters and a number from 0 to
			// 300000000
			String currentID = "" + ((char) ('A' + rand.nextInt(25))) + ((char) ('A' + rand.nextInt(25)))
					+ ((char) ('A' + rand.nextInt(25))) + tempId + ((char) ('A' + rand.nextInt(25)))
					+ ((char) ('A' + rand.nextInt(25))) + ((char) ('A' + rand.nextInt(25)));
			// if (tmpIDs.contains(currentID) == false) {
			// tmpIDs.add(currentID); // insert to the array list in location i
			outFile.println(currentID); // send to file
			// }
			outFile.println(rand.nextInt(1000001) / 100.0); // send to file
		}
		outFile.close();
	}

}