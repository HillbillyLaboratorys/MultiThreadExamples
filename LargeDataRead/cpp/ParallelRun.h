#ifndef _parallel_
#define _parallel_

#include "main.h"
#include <vector>
#include <thread>
#include <mutex>

class Customer;

class ParallelRun {
    private:
        const std::vector<Customer>& customers;
        int total = 0;
        std::mutex dataMutex;
        void worker(int _from_, int _to_, int _num_);
    public:
        ParallelRun(const std::vector<Customer>& _customers_) : customers(_customers_) {};
        void start(int numThreads);
        int getTotal(void);
};


#endif