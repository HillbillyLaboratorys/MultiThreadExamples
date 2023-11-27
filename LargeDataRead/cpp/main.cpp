#include "main.h"


int main() {

    std::vector<Customer> custList;

    std::string line;
    std::ifstream inFile;

	inFile.open("C:\\Projects\\OSHW6\\accounts.txt");

	if (!inFile) {
    	std::cerr << "Error opening input file." << std::endl;
    	return 1;
	}
    // track which line is being read
    bool readID = false;
    double temp = 0;
    //int totalLines = 0;

    // progress bar yet to be implemented 
    // while (std::getline(inFile, line)) {
    //     totalLines++;
    // }

    // does not behave as expected
    // Reset the file pointer to the beginning of the file
    // inFile.clear(); // Clear any error flags
    // inFile.seekg(0, std::ios::beg); // Move to the beginning of the file

    // int incNum = (totalLines / 2) / 25;

    std::cout << "Reading the file \"accounts.txt\" \n";

    int itCount = 0;
    // read data into vector
    while (std::getline(inFile, line)) {
        
        if (!readID) {
            custList.push_back(Customer());
            custList.back().setID(line);
            readID = true;
            itCount++;
            //std::cout << itCount << " ";
            // if (itCount >= incNum) {
            //     itCount = 0;
            //     progress(false);
            // }
        }
        else {
            try {
                // int cast for account balance
                temp = std::stod(line);

            } catch (const std::invalid_argument& e) {
             std::cerr << "Invalid argument: " << e.what() << std::endl;
            } catch (const std::out_of_range& e) {
            std::cerr << "Out of range: " << e.what() << std::endl;
            }
            custList.back().setBalance(temp);
            readID = false;
        }
    }

    inFile.close();

    std::cout << "Start Sequential Run\n";
    auto start = std::chrono::high_resolution_clock::now();
    int seqCount = sequentialCounting(custList);
    auto end = std::chrono::high_resolution_clock::now();
    auto duration = std::chrono::duration_cast<std::chrono::milliseconds>(end - start);

    std::cout << seqCount << " accounts with less than 1000, took " << duration.count() << " ms\n";


    ParallelRun accountCount(custList);

    std::cout << "Start Parallel Run\n";
    start = std::chrono::high_resolution_clock::now();
    accountCount.start(4);
    int parCount = accountCount.getTotal();
    end = std::chrono::high_resolution_clock::now();
    duration = std::chrono::duration_cast<std::chrono::milliseconds>(end - start);

    std::cout << parCount << " accounts with less than 1000, took " << duration.count() << " ms\n";

    return 0;
}

// void progress(bool _reset_) {
//     static int pos;
//     if (_reset_){
//         pos = 0;
//     }
//     std::cout << "[";
//     for (int i = 0; i < 25; i++) {
//         if (pos >= i) {
//             std::cout << "#";
//         }
//         else {
//             std::cout << " ";
//         }
//     }
//     std::cout << "]\n";
//     pos++;
// }

int sequentialCounting(std::vector<Customer> myList) {
		int count = 0;
		for (int i = 0; i < myList.size(); ++i) {
			if (myList[i].getBalance() < 1000) {
				++count;
            }
        }
		return count;
}

Customer::Customer(void){
    this->ID = '-';
    this->balance = -1;
}

Customer::Customer(std::string _ID_, double _balance_){
    this->ID = _ID_;
    this->balance = _balance_;
}

double Customer::getBalance(void) const {
    return balance;
}

void Customer::setBalance(double _nBal_) {
    this->balance = _nBal_;
}

std::string Customer::getID(void) const {
    return ID;
}

void Customer::setID(std::string _nID_) {
    this->ID = _nID_;
}

