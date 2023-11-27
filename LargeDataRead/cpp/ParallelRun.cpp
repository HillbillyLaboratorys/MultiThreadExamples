#include "ParallelRun.h"

int ParallelRun::getTotal(void) {
    return this->total;
}

void ParallelRun::worker(int _from_, int _to_, int _num_) {
    int tempcount = 0;
    for (int i = _from_; i < _to_; ++i) {
        if (customers[i].getBalance() < 1000) {
           tempcount++;
        }
    }
    std::lock_guard<std::mutex> lock(this->dataMutex);
    total += tempcount;
}

void ParallelRun::start(int numThreads) {
    // trhread tracking
    std::vector<std::thread> threads;

    // thread data size
    int chunk = static_cast<int>(customers.size()) / numThreads;
    
    // init threads 
    for (int i = 0; i < numThreads - 1; i++) {
        threads.push_back(std::thread(&ParallelRun::worker, this, i * chunk, (i + 1) * chunk, i));
    }
    // last thread picks up excess from unequal chunk size
    threads.push_back(std::thread(&ParallelRun::worker, this, (numThreads - 1) * chunk, customers.size() - 1, numThreads - 1));

    for (auto& thread : threads) {
        thread.join();
    }
}

