#include <iostream>
#include <map>
#include <vector>

using namespace std;

int r;
vector<int> resource;
vector<int> available;
map<int, vector<int> > need;
map<int, vector<int> > allocation;
map<int, vector<int> > maxNeed;
int pid;
string func;
vector<int> request;

void new_process(int pid) {
    for (int i = 0; i < r; ++i) {
        maxNeed[pid].push_back(request[i]);
        need[pid].push_back(request[i]);
        allocation[pid].push_back(0);
    }
}

bool new_handle(int pid) {
    if (maxNeed.find(pid) != maxNeed.end()) {
        return false;
    }
    for (int i = 0; i < r; ++i) {
        if (resource[i] < request[i]) {
            return false;
        }
    }
    new_process(pid);
    return true;
}

bool isSafe(map<int, vector<int> > &tmpNeed,
            map<int, vector<int> > &tmpAllocation, vector<int> &tmpAvailable) {
    map<int, bool> finish;
    for (auto &iter : tmpNeed) {
        finish[iter.first] = false;
    }
    bool flagOuter;
    for (int i = 0; i < tmpNeed.size(); ++i) {
        flagOuter = false;
        for (auto &iter : tmpNeed) {
            if (!finish[iter.first]) {
                bool flag = true;
                for (int j = 0; j < r; ++j) {
                    if (iter.second[j] > tmpAvailable[j]) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    flagOuter = true;
                    for (int j = 0; j < r; ++j) {
                        tmpAvailable[j] += tmpAllocation[iter.first][j];
                    }
                    finish[iter.first] = true;
                }
            }
        }
        if (!flagOuter) {
            break;
        }
    }
    flagOuter = true;
    for (auto &iter : finish) {
        if(!iter.second){
            return false;
        }
    }
    for (int i = 0; i < r; ++i) {
        available[i] = available[i] - request[i];
    }
    need = tmpNeed;
    allocation = tmpAllocation;
    return true;
}

bool request_handle(int pid) {
    for (int i = 0; i < r; ++i) {
        if (maxNeed[pid][i] < request[i] + allocation[pid][i] ||
            request[i] > available[i]) {
            return false;
        }
    }
    map<int, vector<int> > tmpNeed;
    map<int, vector<int> > tmpAllocation;
    vector<int> tmpAvailable(r);
    tmpNeed = need;
    tmpAllocation = allocation;
    for (int i = 0; i < r; ++i) {
        tmpNeed[pid][i] = need[pid][i] - request[i];
        tmpAllocation[pid][i] = allocation[pid][i] + request[i];
        tmpAvailable[i] = available[i] - request[i];
    }
    return isSafe(tmpNeed, tmpAllocation, tmpAvailable);
}

bool terminate_handle(int pid) {
    if (maxNeed.find(pid) == maxNeed.end()) {
        return false;
    }
    for (int i = 0; i < r; ++i) {
        available[i] += allocation[pid][i];
    }
    need.erase(pid);
    allocation.erase(pid);
    maxNeed.erase(pid);
    return true;
}

int main() {
    cin >> r;
    int tmp;
    for (int i = 0; i < r; ++i) {
        cin >> tmp;
        resource.push_back(tmp);
        request.push_back(0);
    }
    available = resource;
    while (cin >> pid >> func) {
        if (func != "terminate") {
            for (int i = 0; i < r; ++i) {
                cin >> request[i];
            }
            if (func == "new") {
                if (new_handle(pid)) {
                    cout << "OK" << endl;
                } else {
                    cout << "NOT OK" << endl;
                }
            } else if (func == "request") {
                if (request_handle(pid)) {
                    cout << "OK" << endl;
                } else {
                    cout << "NOT OK" << endl;
                }
            }
        } else {
            if (terminate_handle(pid)) {
                cout << "OK" << endl;
            } else {
                cout << "NOT OK" << endl;
            }
        }
    }
    return 0;
}