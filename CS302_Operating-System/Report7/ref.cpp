#include <algorithm>
#include <cmath>
#include <cstdio>
#include <cstring>
#include <ctime>
#include <iostream>
#include <list>
#include <map>
#include <queue>
#include <set>
#include <unordered_map>
#include <unordered_set>
#include <vector>

using namespace std;
typedef pair<int, int> pii;
int K;
int A;
int N;

int fifo() {
    int hit = 0;
    int order[N];
    queue<int> que;
    unordered_set<int> ele_set;
    for (int i = 0; i < N; i++) {
        scanf("%d", &order[i]);
    }
    for (int i = 0; i < N; i++) {
        // miss
        if (ele_set.find(order[i]) == ele_set.end()) {
            if (que.size() == K) {
                int f = que.front();
                que.pop();
                auto iter = ele_set.find(f);
                ele_set.erase(iter);
            }
            que.push(order[i]);
            ele_set.insert(order[i]);
        }
        // hit
        else {
            hit++;
        }
    }
    return hit;
}

int lru() {
    int hit = 0;
    int order[N];
    list<int> ls;
    unordered_map<int, list<int>::iterator> pos_map;
    for (int i = 0; i < N; i++) {
        scanf("%d", &order[i]);
    }
    for (int i = 0; i < N; i++) {
        // miss
        if (pos_map.find(order[i]) == pos_map.end()) {
            if (pos_map.size() == K) {
                int ind = ls.front();
                ls.pop_front();
                pos_map.erase(pos_map.find(ind));
            }
            ls.push_back(order[i]);
            pos_map[order[i]] = --ls.end();
        }
        // hit
        // adjust the used list
        else {
            hit++;
            auto pos = pos_map[order[i]];
            ls.erase(pos);
            ls.push_back(order[i]);
            pos_map[order[i]] = --ls.end();
        }
    }
    return hit;
}

int min() {
    int hit = 0;
    int order[N];
    list<int> ls;
    unordered_map<int, list<int> > mls;
    unordered_map<int, list<int>::iterator> pos_map;
    for (int i = 0; i < N; i++) {
        scanf("%d", &order[i]);
        mls[order[i]].push_back(i);
    }
    for (int i = 0; i < N; i++) {
        mls[order[i]].push_back(N + 1);
    }
    for (int i = 0; i < N; i++) {
        // miss
        if (pos_map.find(order[i]) == pos_map.end()) {
            if (pos_map.size() == K) {
                int max_pos = -1;
                int max_num = -1;
                for (auto &iter : pos_map) {
                    auto it = mls[iter.first].begin();
                    while (it != mls[iter.first].end()) {
                        if (*it < i) {
                            mls[iter.first].pop_front();
                        } else {
                            if (*it > max_num) {
                                max_pos = iter.first;
                                max_num = *it;
                            }
                            break;
                        }
                        it = mls[iter.first].begin();
                    }
                }
                ls.erase(pos_map[max_pos]);
                pos_map.erase(pos_map.find(max_pos));
            }
            ls.push_back(order[i]);
            pos_map[order[i]] = --ls.end();
        } else {
            hit++;
        }
    }
    return hit;
}

int get_ind(int ind) {
    while (ind > K) {
        ind -= K;
    }
    return ind;
}

vector<pii>::iterator _find(vector<pii> &vec, int find_ele) {
    for (auto it = vec.begin(); it != vec.end(); it++) {
        if (it->first == find_ele) {
            return it;
        }
    }
    return vec.end();
}

int _clock() {
    int hit = 0;
    int order[N];
    int hand = 1;
    vector<pii> vec(static_cast<unsigned long>(N + 1), {-1, 0});
    unordered_set<int> ele_set;
    for (int i = 0; i < N; i++) {
        scanf("%d", &order[i]);
    }
    for (int i = 0; i < N; i++) {
        //        printf("%d\n", i);
        // miss
        if (ele_set.find(order[i]) == ele_set.end()) {
            while (true) {
                if (vec[hand].second == 0) {
                    if (ele_set.find(vec[hand].first) != ele_set.end()) {
                        ele_set.erase(ele_set.find(vec[hand].first));
                    }
                    vec[hand].first = order[i];
                    ele_set.insert(vec[hand].first);
                    vec[hand].second = 1;
                    break;
                }
                vec[hand].second = 0;
                hand = get_ind(++hand);
            }
            hand = get_ind(++hand);
        }
        // hit
        else {
            hit++;
            auto iter = _find(vec, order[i]);
            iter->second = 1;
        }
    }
    return hit;
}

int second_chance() {
    int hit = 0;
    int order[N];
    int fifo_size = K / 2;
    int lru_size = K - K / 2;
    queue<int> que;
    unordered_set<int> ele_set;
    list<int> ls;
    unordered_map<int, list<int>::iterator> pos_map;
    for (int i = 0; i < N; i++) {
        scanf("%d", &order[i]);
    }
    for (int i = 0; i < N; i++) {
        // hit
        auto set_find = ele_set.find(order[i]);
        auto map_find = pos_map.find(order[i]);
        if (set_find != ele_set.end() || map_find != pos_map.end()) {
            hit++;
            // e
            if (map_find != pos_map.end()) {
                // lru erase
                int item = map_find->first;
                ls.erase(map_find->second);
                pos_map.erase(map_find);
                // fifo pop
                int f = que.front();
                que.pop();
                auto iter = ele_set.find(f);
                ele_set.erase(iter);
                // lru push
                ls.push_back(f);
                pos_map[f] = --ls.end();
                // fifo push
                que.push(item);
                ele_set.insert(item);
            }
        } else {
            // b
            if (que.size() < fifo_size) {
                que.push(order[i]);
                ele_set.insert(order[i]);
            }
            // c
            else if (que.size() == fifo_size && ls.size() < lru_size) {
                // fifo pop
                int f = que.front();
                que.pop();
                auto iter = ele_set.find(f);
                ele_set.erase(iter);
                // fifo push
                que.push(order[i]);
                ele_set.insert(order[i]);
                // lru push
                ls.push_back(f);
                pos_map[f] = --ls.end();
            }
            // d
            else if (que.size() == fifo_size && ls.size() == lru_size) {
                // lru pop
                int ind = ls.front();
                ls.pop_front();
                auto item = pos_map.find(ind);
                pos_map.erase(item);
                // fifo pop
                int f = que.front();
                que.pop();
                auto iter = ele_set.find(f);
                ele_set.erase(iter);
                // fifo push
                que.push(order[i]);
                ele_set.insert(order[i]);
                // lru push
                ls.push_back(f);
                pos_map[f] = --ls.end();
            }
        }
    }
    return hit;
}

int main() {
    //    freopen("1.in", "r", stdin);
    // cache size
    clock_t start_t, end_t;
    start_t = clock();
    scanf("%d", &K);
    scanf("%d", &A);
    scanf("%d", &N);
    int hit;
    switch (A) {
        // fifo
        case 0:
            hit = fifo();
            break;
            // min
        case 1:
            hit = lru();
            break;
            // lru
        case 2:
            hit = min();
            break;
            // clock
        case 3:
            hit = _clock();
            break;
            // second_chance
        case 4:
            hit = second_chance();
            break;
        default:
            printf("wrong algorithm number input, check again");
            exit(-1);
    }
    double res;
    res = hit * 1.0 * 100 / N * 1.0;
    string zero = "";
    for (int i = 0; i < 1 - (int)floor(res) % 10; i++) {
        zero.append("0");
    }
    printf("Hit ratio = %s%.2f%%\n", &zero, res);
    end_t = clock();
    printf("Running time is %.6f s",
           (double)(end_t - start_t) / CLOCKS_PER_SEC);
    return 0;
}