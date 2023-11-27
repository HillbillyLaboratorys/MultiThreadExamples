package threadWrapper;

import java.util.ArrayList;
import banking.Customer;


public class Worker {
    
	private ArrayList<Customer> myList;
	private int key;
	private int count = 0;

	public Worker(ArrayList<Customer> _inList_, int _key_) {
		myList = _inList_;
		key = _key_;
	}
	
	public int getCount() {
	    return count;
	}
	
	public void linearSearch(int from, int to) throws InterruptedException {
	    
		for (int i = from; i < to ; ++i) {
			
			if (myList.get(i).getBalance() < key) {
			    count++;
			}
		}
	}
}

