# Report 7

1. Briefly describe the FIFO page-replacement algorithm and analyze its algorithm complexity

   - Description
     - A FIFO replacement algorithm associates with each page the time when that page was brought into memory. When a page must be replaced, the oldest page is chosen.
     - Notice that it is not strictly necessary to record the time when a page is brought in. We can create a FIFO queue to hold all pages in memory. We replace the page at the head of the queue. When a page is brought into memory, we insert it at the tail of the queue.
     - This most unexpected result is known as Belady’s anomaly: for some page-replacement algorithms, the page-fault rate may increase as the number of allocated frames increases.
   - Algorithm complexity
     - The time complexity of replacement is O(1). Because just pop the head of the queue and push the page into the tail of the queue.
     - FIFO查找page是否存在，可以用hashmap维护，时间复杂度近似O(1)，替换的时间复杂度近似O(1)。

2. Briefly describe the MIN page-replacement algorithm and analyze its algorithm complexity

   - Description
     - Replace the page that will not be used for the longest period of time.
     - Use of this page-replacement algorithm guarantees the lowest possible page- fault rate for a fixed number of frames.
   - Algorithm complexity
     - The MIN page-replacement algorithm is difficult to implement, because it requires future knowledge of the reference.
     - It can be implemented in with O(N) time complexity.
     - MIN查找page是否存在，可以用hashmap维护，时间复杂度近似O(1)。替换时需要寻找cache里面的page最晚出现的那个，这里为每个page维护了一个queue记录出现位置。需要遍历寻找最大值，时间复杂度O(N)，N是cache大小。
     - 如果用set去维护所有cache内page的queue，每次将排序后最大的替换，这样时间复杂度是O(logN)，N是cache大小。

3. Briefly describe the LRU page-replacement algorithm and analyze its algorithm complexity

   - Description
     - If we use the recent past as an approximation of the near future, then we can replace the page that has not been used for the longest period of time.
     - LRU replacement associates with each page the time of that page’s last use. duixWhen a page must be replaced, LRU chooses the page that has not been used for the longest period of time.
   - Algorithm complexity
     - Two implementations are feasible：
       - Counters. We associate with each page-table entry a time-of-use field and add to the CPU a logical clock or counter. The clock is incremented for every memory reference. In this way, we always have the “time” of the last reference to each page. We replace the page with the smallest time value.
       - Stack. Another approach to implementing LRU replacement is to keep a stack of page numbers. Whenever a page is referenced, it is removed from the stack and put on the top. In this way, the most recently used page is always at the top of the stack and the least recently used page is always at the bottom.
     - It can be implemented in with O(n) time complexity for both implementations.
     - 如果按照龙书上，使用Counters或者stack，查找时间复杂度为O(N)，N是cache大小。
     - LRU查找page是否存在，可以用hashmap维护，时间复杂度近似O(1)。用双向链表记录，替换的时间复杂度是O(1)。

4. Briefly describe the clock algorithm and analyze its algorithm complexity

   - Description
     - The basic algorithm of clock replacement is a FIFO replacement algorithm. When a page has been selected, however, we inspect its reference bit. If the value is 0, we proceed to replace this page; but if the reference bit is set to 1, we give the page a second chance and move on to select the next FIFO page. When a page gets a second chance, its reference bit is cleared, and its arrival time is reset to the current time. Thus, a page that is given a second chance will not be replaced until all other pages have been replaced (or given second chances). In addition, if a page is used often enough to keep its reference bit set, it will never be replaced.
   - Algorithm complexity
     - One way to implement the clock algorithm is as a circular queue. A pointer (that is, a hand on the clock) indicates which page is to be replaced next. When a frame is needed, the pointer advances until it finds a page with a 0 reference bit. As it advances, it clears the reference bits. Once a victim page is found, the page is replaced, and the new page is inserted in the circular queue in that position. Notice that, in the worst case, when all bits are set, the pointer cycles through the whole queue, giving each page a second chance. It clears all the reference bits before selecting the next page for replacement. Second-chance replacement degenerates to FIFO replacement if all bits are set.
     - It can be implemented in with O(n) time complexity.
     - 查询可替换的page的时间复杂度是O(N)，N是cache大小，因为需要在环上遍历查询，替换的复杂度是O(1)。

5. Briefly describe the second-chance algorithm and analyze its algorithm complexity

   - Description
     - Second-chance list algorithm splits memory into two part: Active list(FIFO) and SC list(LRU).
   - Algorithm complexity
     - Access pages in Active list at full speed.
     - Otherwise, Page Fault
       - Always move overflow page from end of Active list to front of Second-chance list (SC) and mark invalid
       - Desired Page On SC List: move to front of Active list, mark RW
       - Not on SC list: page in to front of Active list, mark RW; page out LRU victim at end of SC list
     - It can be implemented in with O(n) time complexity.
