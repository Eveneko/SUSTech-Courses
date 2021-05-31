#pragma GCC optimize(2)
#include <bits/stdc++.h>

#include <unordered_map>
#include <unordered_set>
using namespace std;

#define maxN 1000010

int K;        // cache size
int A;        // algorithm index
int N;        // the number of page
int hit = 0;  // hit time

void FIFO() {
    int num;
    queue<int> qu;
    unordered_set<int> check;
    for (int i = 0; i < N; ++i) {
        scanf("%d", &num);
        if (check.find(num) == check.end()) {
            if (qu.size() == K) {
                check.erase(check.find(qu.front()));
                qu.pop();
            }
            qu.push(num);
            check.insert(num);
        } else {
            hit++;
        }
    }
}

// void FIFO() {
//     int num;
//     queue<int> qu;
//     bool check[N + 1];
//     memset(check, false, sizeof(check));
//     for (int i = 0; i < N; ++i) {
//         scanf("%d", &num);
//         if (!check[num]) {
//             if (qu.size() == K) {
//                 check[qu.front()] = false;
//                 qu.pop();
//             }
//             qu.push(num);
//             check[num] = true;
//         } else {
//             hit++;
//         }
//     }
// }

void Min() {
    int num;
    vector<int> vec;
    unordered_set<int> check;
    unordered_map<int, queue<int> > mp;
    for (int i = 0; i < N; ++i) {
        scanf("%d", &num);
        vec.push_back(num);
        mp[num].push(i);
    }
    for (int i = 0; i < N; ++i) {
        mp[vec[i]].push(N + 1);
    }
    for (int i = 0; i < N; ++i) {
        mp[vec[i]].pop();
        if (check.find(vec[i]) == check.end()) {
            if (check.size() == K) {
                int max_page = -1;
                int max_time = -1;
                for (auto iter : check) {
                    // while (mp[iter].front() < i) {
                    //     mp[iter].pop();
                    // }
                    if (mp[iter].front() > max_time) {
                        max_time = mp[iter].front();
                        max_page = iter;
                    }
                }
                check.erase(max_page);
            }
            check.insert(vec[i]);
        } else {
            hit++;
        }
    }
}

void LRU() {
    int num;
    list<int> li;
    unordered_map<int, list<int>::iterator> check;
    for (int i = 0; i < N; ++i) {
        scanf("%d", &num);
        if (check.find(num) == check.end()) {
            if (li.size() == K) {
                check.erase(check.find(li.front()));
                li.pop_front();
            }
            li.push_back(num);
            check[num] = --li.end();
        } else {
            hit++;
            li.erase(check[num]);
            li.push_back(num);
            check[num] = --li.end();
        }
    }
}

// void LRU() {
//     int num;
//     list<int> li;
//     list<int>::iterator check[N + 1];
//     for(int i = 0; i < N; ++i){
//         check[i] = li.end();
//     }
//     for (int i = 0; i < N; ++i) {
//         scanf("%d", &num);
//         if (check[num] == li.end()) {
//             if (li.size() == K) {
//                 check[li.front()] = li.end();
//                 li.pop_front();
//             }
//             li.push_back(num);
//             check[num] = --li.end();
//         } else {
//             hit++;
//             li.erase(check[num]);
//             li.push_back(num);
//             check[num] = --li.end();
//         }
//     }
// }

struct node {
    int id;
    int valid;
    node *next;
    node() {
        id = -1;
        valid = 0;
        next = nullptr;
    }
};

void Clock() {
    int num;
    node li[K];
    for (int i = 0; i < K - 1; ++i) {
        li[i].next = &li[i + 1];
    }
    li[K - 1].next = &li[0];
    node *hand = &li[0];
    unordered_map<int, node *> check;
    for (int i = 0; i < N; ++i) {
        scanf("%d", &num);
        if (check.find(num) == check.end()) {
            while (hand->valid != 0) {
                hand->valid = 0;
                hand = hand->next;
            }
            if (hand->id != -1) {
                check.erase(check.find(hand->id));
            }
            hand->id = num;
            hand->valid = 1;
            check[num] = hand;
            hand = hand->next;
        } else {
            hit++;
            check[num]->valid = 1;
        }
    }
}

void SecondChance() {
    int num;
    int FIFO_size = K / 2;
    int LRU_size = K - K / 2;
    queue<int> qu;
    unordered_set<int> FIFO_check;
    list<int> li;
    unordered_map<int, list<int>::iterator> LRU_check;
    for (int i = 0; i < N; ++i) {
        scanf("%d", &num);
        auto FIFO_find = FIFO_check.find(num);
        auto LRU_find = LRU_check.find(num);
        if (FIFO_find != FIFO_check.end()) {
            hit++;
        } else if (LRU_find != LRU_check.end()) {
            hit++;
            // LRU-
            int li_first = LRU_find->first;
            li.erase(LRU_find->second);
            LRU_check.erase(LRU_find);
            // FIFO-
            int qu_front = qu.front();
            qu.pop();
            FIFO_check.erase(FIFO_check.find(qu_front));
            // LRU+
            li.push_back(qu_front);
            LRU_check[qu_front] = --li.end();
            // FIFO+
            qu.push(li_first);
            FIFO_check.insert(li_first);
        } else {
            if (qu.size() < FIFO_size) {
                // FIFO+
                qu.push(num);
                FIFO_check.insert(num);
            } else if (qu.size() == FIFO_size && li.size() < LRU_size) {
                // FIFO-
                int qu_front = qu.front();
                qu.pop();
                FIFO_check.erase(FIFO_check.find(qu_front));
                // LRU+
                li.push_back(qu_front);
                LRU_check[qu_front] = --li.end();
                // FIFO+
                qu.push(num);
                FIFO_check.insert(num);
            } else if (qu.size() == FIFO_size && li.size() == LRU_size) {
                // LRU-
                int li_front = li.front();
                li.pop_front();
                LRU_check.erase(LRU_check.find(li_front));
                // FIFO-
                int qu_front = qu.front();
                qu.pop();
                FIFO_check.erase(FIFO_check.find(qu_front));
                // LRU+
                li.push_back(qu_front);
                LRU_check[qu_front] = --li.end();
                // FIFO+
                qu.push(num);
                FIFO_check.insert(num);
            }
        }
    }
}

int main() {
    clock_t time_start = clock();

    scanf("%d\n%d\n%d", &K, &A, &N);
    switch (A) {
        case 0:
            FIFO();
            break;
        case 1:
            LRU();
            break;
        case 2:
            Min();
            break;
        case 3:
            Clock();
            break;
        case 4:
            SecondChance();
            break;
        default:
            puts("The algorithm index is invalid");
            return -1;
    }
    double res = 1.0 * hit * 100 / (1.0 * N);
    string zero = "";
    for (int i = 0; i < 1 - (int)floor(res) % 10; i++) {
        zero.append("0");
    }
    printf("Hit ratio = %s%.2f%%\n", &zero, res);

    clock_t time_end = clock();
    printf("time use: %fms\n",
           1000 * (time_end - time_start) / (double)CLOCKS_PER_SEC);
    return 0;
}