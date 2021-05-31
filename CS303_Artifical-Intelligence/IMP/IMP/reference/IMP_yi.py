# -*- coding: utf-8 -*-
"""
@project: ai_proj3
@author: yi
@e-mail: 11612917@mail.sustc.edu.cn
@file: IMP.py
@time: 18-12-11 上午3:57
"""
import multiprocessing as mp
import random
import time
import sys
import os
import math


class Worker(mp.Process):
    def __init__(self, outQ, theta):
        super(Worker, self).__init__(target=self.start)
        self.outQ = outQ
        self.R = []
        self.theta = theta

    def run(self):
        count = 0
        # ic_start_time = time.time()
        while count < self.theta:
            v = random.randint(1, V)
            if diffusion_model == 'IC':
                rr = get_rr([v])
            else:
                rr = get_rr_lt([v])
            self.R.append(rr)
            count += 1
        # print('iC_rr time:', time.time() - ic_start_time)
        self.outQ.put(self.R)


def create_worker(num, task_num):
    global worker
    for i in range(num):
        worker.append(Worker(mp.Queue(), task_num))
        worker[i].start()


def finish_worker():
    global worker
    for w in worker:
        w.terminate()


def log_binomial(n, k):
    fraction = 0
    for i in range(k + 1, n + 1):
        fraction += math.log(i)
    for i in range(1, n - k + 1):
        fraction -= math.log(i)
    # print(fraction)
    return fraction


def imm(k, eps, l):
    l = l * (1 + math.log(2) / math.log(V))
    R = sampling(k, eps, l)
    print('Time of sampling:', time.time() - start_time)
    S_k_star, frac = node_selection(R, k)
    ns_start_time = time.time()
    print('Time of NodeSelection:', time.time() - ns_start_time)
    return S_k_star


def sample(p):
    return p > random.uniform(0.0, 1.0)


def get_rr(v_set):
    # sub_time = time.time()
    rr_set = set()
    activity_set = set()        
    for i in v_set:
        activity_set.add(i)
        rr_set.add(i)
    while activity_set:
        new_activity_set = set()
        for seed in activity_set:
            for neighbor in rev_adj_graph[seed].keys():
                if neighbor not in rr_set and neighbor not in new_activity_set and sample(
                        rev_adj_graph[seed][neighbor]):
                    new_activity_set.add(neighbor)
        activity_set = new_activity_set
        rr_set = rr_set.union(new_activity_set)
    # print('sub_time:', time.time() - sub_time)
    return rr_set


def get_rr_lt(v_set):
    rr_set = set()
    activity_set = set()
    for i in v_set:
        activity_set.add(i)
        rr_set.add(i)
    while activity_set:
        new_activity_set = set()
        for seed in activity_set:
            if rev_adj_graph[seed]:
                rand_neighbor = random.sample(rev_adj_graph[seed].keys(), 1)[0]
                if rand_neighbor not in rr_set:
                    rr_set.add(rand_neighbor)
                    new_activity_set.add(rand_neighbor)
            else:
                continue
        activity_set = new_activity_set
        rr_set = rr_set.union(new_activity_set)
    return rr_set


def sampling(k, eps, l):
    global adj_graph, worker
    R = []
    LB = 1
    next_eps = eps * math.sqrt(2)
    alpha = math.sqrt(l * math.log(V) + math.log(2))
    log_bi = log_binomial(V, k)
    beta = math.sqrt((1 - 1 / math.e) * (log_bi + l * math.log(V) + math.log(2)))
    new_lambda = ((2 + 2 * next_eps / 3) * (
            log_bi + l * math.log(V) + math.log(math.log2(V))) * V) / pow(next_eps, 2)
    lambda_star = 2 * V * math.pow(((1 - 1 / math.e) * alpha + beta), 2) * pow(eps, -2)
    r = int(math.log2(V - 1))
    for i in range(1, r + 1):
        x = V / math.pow(2, i)
        theta_i = new_lambda / x
        print('theta:', theta_i)
        start = time.time()
        worker_num = 8
        create_worker(worker_num, (theta_i - len(R)) / 8)
        for w in worker:
            rrs = w.outQ.get()
            R += rrs
        worker = []
        end = time.time()
        # print(end - start)
        S_i, frac = node_selection(R, k)
        if V * frac >= (1 + next_eps) * x:
            LB = V * frac / (1 + next_eps)
            break
    theta = lambda_star / LB
    print('frac:', frac)
    print('alpha:', alpha)
    print('beta:', beta)
    print('lambda_star:', lambda_star)
    print('LB:', LB)
    print('theta:', theta)
    print('len(R):',len(R))
    while len(R) <= theta:
        v = random.randint(1, V)
        if diffusion_model == 'IC':
            R.append(get_rr([v]))
        else:
            R.append(get_rr_lt([v]))
    return R


def node_selection(R, k):
    theta = len(R)
    s_star = set()
    rr_deg = [0 for i in range(V + 1)]
    rr_ape = {}
    for j in range(0, len(R)):
        rr = R[j]
        for _ in rr:
            rr_deg[_] += 1
            if _ not in rr_ape:
                rr_ape[_] = []
            rr_ape[_].append(j)
    for i in range(k):
        _max = rr_deg.index(max(rr_deg))
        if _max == 0:
            z = i
            pos = 1
            for i in range(z, k):
                for x in range(pos, V + 1):
                    if x not in s_star:
                        s_star.add(x)
                        break
            break
        s_star.add(_max)
        index = []
        for _ in rr_ape[_max]:
            index.append(_)
        for _ in index:
            x = R[_]
            for __ in x:
                rr_deg[__] -= 1
                rr_ape[__].remove(_)

    count = 0
    for rr in R:
        for s in s_star:
            if s in rr:
                count += 1
    # print('S:', list(s_star))
    # print('F_R:', count / theta)
    return list(s_star), count / theta


def read_graph():
    global adj_graph, rev_adj_graph, V, E
    V = E = 0
    adj_graph = None
    rev_adj_graph = None
    with open(social_network) as file:
        for line in file:
            line = line.rstrip()
            if len(line.split(' ')) == 2:
                V = int(line.split(' ')[0])
                E = int(line.split(' ')[1])
                adj_graph = [{} for _ in range(V + 1)]
                rev_adj_graph = [{} for _ in range(V + 1)]
            else:
                start_v = int(line.split(' ')[0])
                end_v = int(line.split(' ')[1])
                prob = float(line.split(' ')[2])
                adj_graph[start_v][end_v] = prob
                rev_adj_graph[end_v][start_v] = prob


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

if __name__ == '__main__':
    start_time = time.time()
    social_network = str(sys.argv[2])
    predefined_size = int(sys.argv[4])
    diffusion_model = str(sys.argv[6])
    time_budget = int(sys.argv[8])
    read_graph()
    worker = []
    # print(rev_adj_graph)
    par = 0.1
    if V >= 20000 and predefined_size >= 100 and time_budget <= 60:
        par = 0.2
    if V >= 30000 and time_budget <= 60:
        par = 0.2
    if time_budget >= 120:
        par = 0.1
    if time_budget <= 30 and V >= 50000:
        par = 0.3
    result = imm(predefined_size, par, 1)
    for i in result:
        print(i)
    print('Total time of IMP:', time.time() - start_time)


    # ISE_test(social_network, result, diffusion_model, 10)


# single process
# 40000 nodes 23s k = 50 0.1
# 40000 nodes 29s k = 500 0.2
# 40000 nodes 53s k = 500 0.15
# 40000 nodes 66s k = 500 0.1
# 40000 nodes 29s k = 500 0.2
# 80000 nodes 51s k = 50 0.2
# 80000 nodes 109s k = 500 0.2
# 80000 nodes 74s k = 500 0.25
# 80000 nodes 63s k = 500 0.275
# 80000 nodes 61s k = 500 0.28
# 80000 nodes 55s k = 500 0.285
# 80000 nodes 51s k = 500 0.3