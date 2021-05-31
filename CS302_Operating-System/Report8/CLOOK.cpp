#include <bits/stdc++.h>
using namespace std;

int S;  // the position of beginning track
int M;  // the total number of tracks in disk
int N;  // the number of scheduling requests
vector<int> tracks;

int main() {
    scanf("%d %d %d", &S, &M, &N);
    int num;
    tracks.push_back(S);
    for (int i = 0; i < N; ++i) {
        scanf("%d", &num);
        tracks.push_back(num);
    }
    sort(tracks.begin(), tracks.end());
    // tracks.erase(unique(tracks.begin(), tracks.end()), tracks.end());
    int ptr = -1;
    for (int i = N; i >= 0; --i) {
        if (tracks[i] == S) {
            ptr = i;
            break;
        }
    }
    int l = ptr - 1;
    int r = N;
    ptr++;
    int dis = 0;
    int pre = S;
    printf("%d", S);
    while (l >= 0) {
        dis += abs(tracks[l] - pre);
        pre = tracks[l];
        printf(" %d", tracks[l--]);
    }

    while (r >= ptr) {
        dis += abs(tracks[r] - pre);
        pre = tracks[r];
        printf(" %d", tracks[r--]);
    }

    printf("\n%d\n", dis);
    return 0;
}