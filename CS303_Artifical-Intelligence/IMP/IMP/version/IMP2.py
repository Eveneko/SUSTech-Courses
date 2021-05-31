# -*- coding: utf-8 -*-

import os
import sys
import copy
import math
import time
import heapq
import random
import argparse
import numpy as np
import multiprocessing as mp
from collections import defaultdict


Comb = lambda x, y: math.factorial(x) / (math.factorial(y) * math.factorial(x - y))
P = lambda x: x > random.random()

# worker = []
# worker_num = 8
# model = 'IC'


class Network:
    def __init__(self, vertex_cnt, edge_cnt, weight_dict, adj_list, adj_list_rev):
        self.vertex_cnt = vertex_cnt
        self.edge_cnt = edge_cnt
        self.weight_dict = weight_dict
        self.adj_list = adj_list
        self.adj_list_rev = adj_list_rev


def read_network(network_file):
    vertex_cnt = 0
    edge_cnt = 0 
    weight_dict = defaultdict(dict)
    adj_list = []
    adj_list_rev = []
    with open(network_file) as f:
        for i, line in enumerate(f):
            line = line.strip('\r\n').split(' ')
            if i == 0:
                vertex_cnt, edge_cnt = int(line[0]), int(line[1])
                adj_list = [list() for i in range(vertex_cnt + 1)]
                adj_list_rev = [list() for i in range(vertex_cnt + 1)]
            else:
                u = int(line[0])
                v = int(line[1])
                w = float(line[2])
                weight_dict[u][v] = w
                adj_list[u].append(v)
                adj_list_rev[v].append(u)
    return Network(vertex_cnt, edge_cnt, weight_dict, adj_list, adj_list_rev)


class Worker(mp.Process):
    def __init__ (self, inQ, outQ, model):
        super(Worker, self).__init__(target=self.start)
        self.inQ = inQ
        self.outQ = outQ 
        self.model = model
        np.random.seed(int(time.time()))
    
    def run (self):
        while True:
            task = self.inQ.get()
            theta = task
            if self.model == 'IC':
                R = IC_rr(theta)
            elif self.model == 'LT':
                R = LT_rr(theta)
            self.outQ.put(R)        


def create_worker(num, model):
    global worker
    for i in range(num):
        worker.append(Worker(mp.Queue(), mp.Queue(), model))
        worker[i].start()


def finish_worker():
    global worker
    for w in worker:
        w.terminate()


def IMM(k, e, l):
    n = network.vertex_cnt
    l = l * (1 + math.log(2) / math.log(n))
    R = Sampling(k, e, l)
    print('Time of sampling:', time.time() - start_time)
    ns_start_time = time.time()
    S_k_star = NodeSelection(R, k)[0]
    print('Time of NodeSelection:', time.time() - ns_start_time)
    return S_k_star


def Sampling(k, e, l):
    global worker, worker_num, model
    R = list()
    LB = 1
    _e = math.sqrt(2) * e
    n = network.vertex_cnt
    _lambda = (2 + 2 * _e / 3) * (math.log(Comb(n, k)) + l * math.log(n) + math.log(math.log2(n))) * n / (pow(_e, 2))
    for i in range(1, int(math.log2(n))):
        x = n / pow(2, i)
        theta = _lambda / x
        print('theta:', theta)
        for j in range(worker_num):
            worker[j].inQ.put(math.ceil((theta - len(R)) / worker_num))
        for sub_worker in worker:
            sub_R = sub_worker.outQ.get()
            R += sub_R
        S, F_R = NodeSelection(R, k)
        if n * F_R >= (1 + _e) * x:
            LB = n * F_R / (1 + _e)
            break
    alpha = math.sqrt(l * math.log(n) + math.log(2))
    beta = math.sqrt((1 - 1 / math.e) * (math.log(Comb(n, k)) + l * math.log(n) + math.log(2)))
    lambda_star = 2 * n * pow((1 - 1 / math.e) * alpha + beta, 2) * pow(e, -2)
    theta = lambda_star / LB
    print('F_R:', F_R)
    print('alpha:', alpha)
    print('beta:', beta)
    print('lambda_star:', lambda_star)
    print('LB:', LB)
    print('theta:', theta)
    print('len(R):',len(R))
    while len(R) <= theta:
        if model == 'IC':
            R.append(IC_rr(1)[0])
        elif model == 'LT':
            R.append(LT_rr(1)[0])
    return R


def NodeSelection(R, k):
    S = set()
    rr_deg = [0 for i in range(network.vertex_cnt + 1)]
    rr_dict = {}
    for i in range (0, len(R)):
        rr = R[i]
        for u in rr:
            rr_deg[u] += 1
            if u not in rr_dict:
                rr_dict[u] = set()
            rr_dict[u].add(i)
    max_heap = list()
    for key, value in rr_dict.items():
        max_heap.append([-rr_deg[key], key])
    heapq.heapify(max_heap)
    covered_seeds = set()
    cnt = 0
    while cnt < k:
        val = heapq.heappop(max_heap)
        rr_dict[val[1]] -= covered_seeds
        if val[0] == -len(rr_dict[val[1]]):
            S.add(val[1])
            cnt += 1
            covered_seeds |= rr_dict[val[1]]
        else:
            val[0] = -len(rr_dict[val[1]])
            heapq.heappush(max_heap ,val)
    # print('S:', S)
    # print('covered_seeds:', len(covered_seeds))
    # print('len(R):', len(R))
    # print('F_R:', len(covered_seeds) / len(R))
    return S, len(covered_seeds) / len(R)


def IC_rr(theta):
    # ic_start_time = time.time()
    R = list()
    n = network.vertex_cnt
    # print('theta:', theta)
    while theta > 0:
        # sub_time = time.time()
        rr_set = set()
        activity_set = set()
        v = random.randint(1, n)
        rr_set.add(v)
        activity_set.add(v)
        while activity_set:
            new_activity_set = set()
            for u in activity_set:
                for v in network.adj_list_rev[u]:
                    if v not in rr_set and v not in new_activity_set and P(network.weight_dict[v][u]):
                        new_activity_set.add(v)
            activity_set = new_activity_set
            rr_set = rr_set.union(new_activity_set)
        R.append(rr_set)
        theta -= 1
    #     print('sub_time:', time.time() - sub_time)
    # print('iC_rr time:', time.time() - ic_start_time)
    return R


def LT_rr(theta):
    R = list()
    n = network.vertex_cnt
    while theta:
        rr_set = set()
        activity_set = set()
        v = random.randint(1, n)
        rr_set.add(v)
        activity_set.add(v)
        while activity_set:
            new_activity_set = set()
            for u in activity_set:
                if network.adj_list_rev[u]:
                    v = random.sample(network.adj_list_rev[u], 1)[0]
                    if v not in rr_set and v not in new_activity_set:
                        new_activity_set.add(v)
                else:
                    continue
            activity_set = new_activity_set
            rr_set = rr_set.union(new_activity_set)
        R.append(rr_set)
        theta -= 1
    return R


def ISE_test(network_file_path, S_k_star, model, time_budget):
    seeds_file = time.strftime("%Y-%m-%d-%H:%M:%S", time.localtime()) + '.txt'
    with open(seeds_file, 'w') as f:
        for seed in S_k_star:
            f.write('{}\n'.format(seed))
    print('ISE test start')
    start_time = time.time()
    print('ISE:', end=' ')
    os.system('python3 {} -i {} -s {} -m {} -t {}'.format('~/PycharmProjects/IMP/ISE/ISE_final.py', network_file_path, seeds_file, model, time_budget))
    end_time = time.time()
    print('Total time of ISE:', end_time - start_time)


"""
python3 IMP.py -i ./data/network.txt -k 5 -m IC -t 60
"""


if __name__ == '__main__':
    start_time = time.time()
    parser = argparse.ArgumentParser()
    parser.add_argument('-i', dest='social_network', type=str, default='./data/network.txt', help='the path')
    parser.add_argument('-k', dest='seed_cnt', type=int, default=5, help='the path')
    parser.add_argument('-m', dest='diffusion_model', type=str, default='IC', help='IC/LT')
    parser.add_argument('-t', dest='time_budget', type=int, default=60, help='60~120')
    args = parser.parse_args()
    network_file_path = args.social_network
    k = args.seed_cnt
    model = args.diffusion_model
    time_budget = args.time_budget

    network = read_network(network_file_path)

    worker = []
    worker_num = 8
    create_worker(worker_num, model)

    l = 1
    e = 0.1

    S_k_star = IMM(k, e, l)
    # print('IMM:', end=' ')
    for seed in S_k_star:
        print(seed)
    end_time = time.time()
    print('Total time of IMP:', end_time - start_time)

    # ISE_test(network_file_path, S_k_star, model, 10)

    sys.stdout.flush()
    os._exit(0)