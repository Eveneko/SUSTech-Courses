#include <bits/stdc++.h>
#include <unistd.h>

using namespace std;

#define PROCESS_NAME_LEN 32    //进程名最大长度
#define MIN_SLICE 10           //内碎片最大大小
#define DEFAULT_MEM_SIZE 1024  //总内存大小
#define DEFAULT_MEM_START 0    //内存开始分配时的起始地址

typedef pair<int, string> My_algo;

int mem_size = DEFAULT_MEM_SIZE;
bool flag = 0;  //当内存以及被分配了之后，不允许更改总内存大小的flag
static int pid = 0;
My_algo algo[123];

struct free_block {  //空闲数据块
    int size;
    int start_addr;
    struct free_block *next;
};

struct allocated_block {  //已分配的数据块
    int pid;
    int size;
    int start_addr;
    int *data;
    struct allocated_block *next;
};

free_block *free_block_head;                   // 空闲数据块首指针
allocated_block *allocated_block_head = NULL;  // 分配块首指针

allocated_block *find_process(int id);      // 寻找pid为id的分配块
free_block *init_free_block(int mem_size);  // 空闲块初始化
void display_menu();                        // 显示选项菜单
void set_mem_size();                        // 设置内存大小
int allocate_mem(allocated_block *ab);      // 为制定块分配内存
void rearrange();                           // 对块进行重新分配
int create_new_process();                   // 创建新的进程
int free_mem(allocated_block *ab);          // 释放分配块
void swap(int *p, int *q);                  // 交换地址
int dispose(allocated_block *ab);           // 释放分配块结构体
void display_mem_usage();                   // 显示内存情况
void kill_process();      // 杀死对应进程并释放其空间与结构体
void Usemy_algo(int id);  // 使用对应的分配算法
bool first_fit(allocated_block *ab, free_block *&pre, free_block *&cur);    // first fit algorithm
bool best_fit(allocated_block *ab, free_block *&pre, free_block *&cur);     // best fit algorithm
bool worst_fit(allocated_block *ab, free_block *&pre, free_block *&cur);    // worst fit algorithm
bool (*fit_func)(allocated_block *ab, free_block *&pre, free_block *&cur) = first_fit;  // function ptr
void set_fit_func(int alg); // set fit algorithm
bool check_merge(int i, int j, int start, int end); // check whether two block can be merged

int alg;  // algorithm number

//主函数
int main() {
    int op;
    pid = 0;
    free_block_head = init_free_block(
            mem_size);  //初始化一个可以使用的内存块，类似与操作系统可用的总存储空间
    for (;;) {
        sleep(1);
        display_menu();
        fflush(stdin);
        scanf("%d", &op);
        switch (op) {
            case 1: {
                set_mem_size();
                break;
            }
            case 2: {
                printf("Choose an algorithm\n");
                printf(
                        " 1: Best Fit\n 2: Worst Fit\n 3: First Fit\n 4: Buddy System\n");
                scanf("%d", &alg);
                set_fit_func(alg);
                break;
            }
            case 3: {
                create_new_process();
                break;
            }
            case 4: {
                kill_process();
                break;
            }
            case 5: {
                display_mem_usage();
                break;
            }
            case 233: {
                puts("bye....");
                sleep(1);
                return 0;
            }
            defaut:
                break;
        }
    }
}

// Todo: if (allocated_block_head == NULL)
allocated_block *find_process(int id) {  //循环遍历分配块链表，寻找pid=id的进程所对应的块
    if (allocated_block_head == NULL) {
        puts("No allocated process");
        return NULL;
    }
    allocated_block *tmp = allocated_block_head;
    while (tmp != NULL) {
        if (tmp->pid == id)
            return tmp;
        tmp = tmp->next;
    }
    puts("The pid of killed process does not exist");
}

free_block *init_free_block(int mem_size) {  //初始化空闲块，这里的mem_size表示允许的最大虚拟内存大小
    free_block *p;
    p = (free_block *) malloc(sizeof(free_block));
    if (p == NULL) {
        puts("No memory left");
        return NULL;
    }
    p->size = mem_size;
    p->start_addr = DEFAULT_MEM_START;
    p->next = NULL;
    return p;
}

void display_menu() {
    puts("\n\n******************menu*******************");
    printf("1) Set memory size (default = %d)\n", DEFAULT_MEM_SIZE);
    printf("2) Set memory allocation algorithm\n");
    printf("3) Create a new process\n");
    printf("4) Kill a process\n");
    printf("5) Display memory usage\n");
    printf("233) Exit\n");
}

// Todo: if(!flag)
// 不会中途切换algorithm
void set_mem_size() {  //更改最大内存大小
    if (!flag) {
        puts("Please input the size of the memory");
        int tmp_mem_size = 0;
        scanf("%d", &tmp_mem_size);
        if (tmp_mem_size <= 0) {
            puts("Memory size should be a positive number");
            return;
        }
        mem_size = tmp_mem_size;
        free_block_head = init_free_block(mem_size);
        printf("The memory size is %d now\n", mem_size);
    } else {
        puts("The memory has been allocated");
    }
}

// Todo: func = {first_fit, best_fit, worst_fit}
// Todo: buddy单独写
int allocate_mem(allocated_block *ab) {  //为块分配内存，真正的操作系统会在这里进行置换等操作
    if (free_block_head == NULL) {
        puts("Memory is unallocated or memory limit exceed");
        return -1;
    }
    free_block *cur = free_block_head;
    free_block *pre = NULL;
    bool has_mem = fit_func(ab, pre, cur);
    if (has_mem) {
        ab->start_addr = cur->start_addr;
        if (alg == 4) {  // buddy system
            ab->size = cur->size;
            if (cur == free_block_head) {
                free_block_head = cur->next;
            } else {
                pre->next = cur->next;
            }
        } else {
            if (cur == free_block_head) {
                if (ab->size == cur->size) {
                    free_block_head = cur->next;
                } else {
                    free_block *new_block =
                            (free_block *) malloc(sizeof(free_block));
                    new_block->start_addr = cur->start_addr + ab->size;
                    new_block->size = cur->size - ab->size;
                    new_block->next = cur->next;
                    free_block_head = new_block;
                }
            } else {
                if (ab->size == cur->size) {
                    pre->next = cur->next;
                } else {
                    free_block *new_block =
                            (free_block *) malloc(sizeof(free_block));
                    new_block->start_addr = cur->start_addr + ab->size;
                    new_block->size = cur->size - ab->size;
                    new_block->next = cur->next;
                    pre->next = new_block;
                }
            }
        }
        ab->pid = ++pid;
        // Todo: allocated
        allocated_block *tmp = allocated_block_head;
        if (tmp == NULL) {
            allocated_block_head = ab;
            return 0;
        }
        while (tmp->next) tmp = tmp->next;
        tmp->next = ab;
        return 0;
    } else {
        puts("Memory limit exceed");
        return -2;
    }
}

// Todo:
bool first_fit(allocated_block *ab, free_block *&pre, free_block *&cur) {
    while (cur != NULL) {
        if (cur->size >= ab->size) {
            return true;
        }
        pre = cur;
        cur = cur->next;
    }
    return false;
}

// Todo:
bool best_fit(allocated_block *ab, free_block *&pre, free_block *&cur) {
    int frag_size = mem_size;
    free_block *best_pre = pre;
    free_block *best_cur = cur;
    bool has_mem = false;
    while (cur != NULL) {
        if (cur->size >= ab->size && cur->size - ab->size < frag_size) {
            best_cur = cur;
            best_pre = pre;
            frag_size = cur->size - ab->size;
            has_mem = true;
        }
        pre = cur;
        cur = cur->next;
    }
    pre = best_pre;
    cur = best_cur;
    return has_mem;
}

// Todo:
bool worst_fit(allocated_block *ab, free_block *&pre, free_block *&cur) {
    int frag_size = -1;
    free_block *worst_pre = pre;
    free_block *worst_cur = cur;
    bool has_mem = false;
    while (cur != NULL) {
        if (cur->size >= ab->size && cur->size - ab->size > frag_size) {
            worst_cur = cur;
            worst_pre = pre;
            frag_size = cur->size - ab->size;
            has_mem = true;
        }
        pre = cur;
        cur = cur->next;
    }
    pre = worst_pre;
    cur = worst_cur;
    return has_mem;
}

// Todo 保证了内存是2的幂次
bool buddy(allocated_block *ab, free_block *&pre, free_block *&cur) {
    int frag_size = mem_size;
    free_block *best_pre = pre;
    free_block *best_cur = cur;
    bool has_mem = false;
    while (cur != NULL) {
        if (cur->size >= ab->size && cur->size - ab->size < frag_size) {
            best_cur = cur;
            best_pre = pre;
            frag_size = cur->size - ab->size;
            has_mem = true;
        }
        pre = cur;
        cur = cur->next;
    }
    pre = best_pre;
    cur = best_cur;
    if (!has_mem) return has_mem;
    while (cur->size >= 2 * ab->size) {
        free_block *new_free_block = (free_block *) malloc(sizeof(free_block));
        new_free_block->size = cur->size / 2;
        cur->size = cur->size / 2;
        new_free_block->start_addr = cur->start_addr + cur->size;
        new_free_block->next = cur->next;
        cur->next = new_free_block;
    }
    return has_mem;
}

// Todo:
void set_fit_func(int alg) {
    switch (alg) {
        case 1:
            fit_func = best_fit;
            puts("The algorithm is best fit now");
            break;
        case 2:
            fit_func = worst_fit;
            puts("The algorithm is worst fit now");
            break;
        case 3:
            fit_func = first_fit;
            puts("The algorithm is first fit now");
            break;
        case 4:
            fit_func = buddy;
            puts("The algorithm is buddy now");
            break;
        default:
            puts("Invalid algorithm number");
            break;
    }
}

// Todo: ++pid
int create_new_process() {  //创建新进程
    int mem_sz = 0;
    printf("Please input memory size\n");
    scanf("%d", &mem_sz);
    // Write your code here
    if (mem_sz <= 0) {
        puts("Process memory size should be a positive number");
        return -1;
    }
    allocated_block *ab = (allocated_block *) malloc(sizeof(allocated_block));
    if (ab == NULL) {
        puts("No memory left");
        return -2;
    }
    ab->next = NULL;
    // ab->pid = ++pid; 避免内存分配失败仍分配pid
    ab->size = mem_sz;
    allocate_mem(ab);
    flag = true;
    return pid;
}

void swap(int *p, int *q) {
    int tmp = *p;
    *p = *q;
    *q = tmp;
    return;
}

// Todo: check 能否合并
bool check_merge(int i, int j, int start, int end) {
    if (end - start < j - i)
        return false;
    if (i == start && j == end)
        return true;
    int mid = (start + end) >> 1;
    if (i >= mid) 
        return check_merge(i, j, mid, end);
    else if (j <= mid)
        return check_merge(i, j, start, mid);
    return false;
}

// Todo: BUG? 缺少连续空间合并
// Todo: buddy system 合并
void rearrange() {  //将块按照地址大小进行排序
    free_block *tmp, *tmpx;
    puts("Rearrange begins...");
    puts("Rearrange by address...");
    tmp = free_block_head;
    while (tmp != NULL) {
        tmpx = tmp->next;
        while (tmpx != NULL) {
            if (tmpx->start_addr < tmp->start_addr) {
                swap(&tmp->start_addr, &tmpx->start_addr);
                swap(&tmp->size, &tmpx->size);
            }
            tmpx = tmpx->next;
        }
        tmp = tmp->next;
    }
    // 合并
    if (alg == 4) {  // buddy system
        tmp = free_block_head;
        bool flag = true;
        while (tmp != NULL && tmp->next != NULL) {
            if (tmp->size == tmp->next->size &&
                tmp->start_addr + tmp->size == tmp->next->start_addr &&
                check_merge(
                        tmp->start_addr - DEFAULT_MEM_START,
                        tmp->next->start_addr + tmp->size - DEFAULT_MEM_START,
                        DEFAULT_MEM_START, DEFAULT_MEM_START + mem_size)) {
                tmp->size *= 2;
                tmp->next = tmp->next->next;
                flag = false;
                tmp = free_block_head;  // 向前合并
            } else
                tmp = tmp->next;
        }
    } else {
        tmp = free_block_head;
        while (tmp != NULL && tmp->next != NULL) {
            if (tmp->start_addr + tmp->size == tmp->next->start_addr) {
                tmp->size += tmp->next->size;
                tmp->next = tmp->next->next;
                continue;
            }
            tmp = tmp->next;
        }
    }
    // flag
    if (free_block_head->next == NULL && free_block_head->size == mem_size) {
        flag = false;
    }
    usleep(500);
    puts("Rearrange Done.");
}

// Todo: free_block
int free_mem(allocated_block *ab) {  //释放某一块的内存
    free_block *new_free_block = (free_block *) malloc(sizeof(free_block));
    new_free_block->next = NULL;
    new_free_block->start_addr = ab->start_addr;
    new_free_block->size = ab->size;
    if (free_block_head) {
        new_free_block->next = free_block_head->next;
        free_block_head->next = new_free_block;
    } else
        free_block_head = new_free_block;
    rearrange();
}

int dispose(allocated_block *fab) {  //释放结构体所占的内存
    allocated_block *pre, *ab;
    if (fab == allocated_block_head) {
        allocated_block_head = allocated_block_head->next;
        free(fab);
        return 1;
    }
    pre = allocated_block_head;
    ab = allocated_block_head->next;
    while (ab != fab) {
        pre = ab;
        ab = ab->next;
    }
    pre->next = ab->next;
    free(ab);
    return 2;
}

void display_mem_usage() {
    free_block *fb = free_block_head;
    allocated_block *ab = allocated_block_head;
    puts("*********************Free Memory*********************");
    printf("%20s %20s\n", "start_addr", "size");
    int cnt = 0;
    while (fb != NULL) {
        cnt++;
        printf("%20d %20d\n", fb->start_addr, fb->size);
        fb = fb->next;
    }
    if (!cnt)
        puts("No Free Memory");
    else
        printf("Totaly %d free blocks\n", cnt);
    puts("");
    puts("*******************Used Memory*********************");
    printf("%10s %10s %20s\n", "PID", "start_addr", "size");
    cnt = 0;
    while (ab != NULL) {
        cnt++;
        printf("%10d %10d %20d\n", ab->pid, ab->start_addr, ab->size);
        ab = ab->next;
    }
    if (!cnt)
        puts("No allocated block");
    else
        printf("Totaly %d allocated blocks\n", cnt);
    return;
}

void kill_process() {  //杀死某个进程
    allocated_block *ab;
    int pid;
    puts("Please input the pid of Killed process");
    scanf("%d", &pid);
    ab = find_process(pid);
    if (ab != NULL) {
        free_mem(ab);
        dispose(ab);
    }
}
