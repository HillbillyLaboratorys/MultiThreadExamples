#ifndef _MAIN_H
#define _MAIN_H

#include "ParallelRun.h"
#include <vector>
#include <string>
#include <chrono>
#include <iostream>
#include <fstream>

class Customer {

    private:
        std::string ID;
        double balance;

    public:

        Customer(void);
        Customer(std::string _ID_, double _balance_);
        double getBalance(void) const;
        void setBalance(double _nBal_);
        std::string getID(void) const;
        void setID(std::string _nID_);

};

// void progress(bool _reset_);

int sequentialCounting(std::vector<Customer> myList);

#endif // _MAIN_H
