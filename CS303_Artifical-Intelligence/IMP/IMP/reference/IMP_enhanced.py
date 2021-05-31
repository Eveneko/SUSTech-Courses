import os
import sys
import math
import time
import heapq
import random
import argparse
import numpy as np
from collections import defaultdict
import multiprocessing as mp

'''
1. Node index starts from 1
2. The first line contains the number of nodes n and number of edges m
3. Each of the next m lines describes an edge following the format: <src> <dest> <weight>
'''


class Graph:
    def __init__(self, vertices, edges, adj_list, adj_list_rev, edge_weight):
        self.vertices = vertices
        self.edges = edges
        self.adj_list = adj_list
        self.adj_list_rev = adj_list_rev
        self.edge_weight = edge_weight


def read_social_network_graph(file_name):
    edge_weight, adj_list, adj_list_rev = [[], [], []]
    vertices, edges = [0, 0]

    with open(file_name, 'r') as fp:
        for i, line in enumerate(fp):

            line = line.split(' ')
            for e in range(len(line)):
                if e == 2:
                    line[e] = float(line[e])
                else:
                    line[e] = int(line[e])

            if i == 0:
                vertices, edges = line
                adj_list = [list() for i in range(vertices + 1)]
                adj_list_rev = [list() for i in range(vertices + 1)]
                edge_weight = [defaultdict() for i in range(vertices + 1)]
            elif 0 < i <= edges:
                u, v, w = line
                adj_list[u].append(v)
                adj_list_rev[v].append(u)
                edge_weight[u][v] = w

    return Graph(vertices, edges, adj_list, adj_list_rev, edge_weight)


def node_selection(node_edges: dict, R_len: int, k: int):
    S = set()
    max_heap = list()
    for key, value in node_edges.items():
        max_heap.append([-len(value), key, 0])

    heapq.heapify(max_heap)
    covered_set = set()
    i = 0
    while i < k:
        val = heapq.heappop(max_heap)
        if val[2] != i:
            node_edges[val[1]] -= covered_set
            val[0] = - len(node_edges[val[1]])
            val[2] = i
            heapq.heappush(max_heap, val)

        else:
            S.add(val[1])
            i += 1
            covered_set |= node_edges[val[1]]
    # print(R_len)
    return [len(covered_set) / R_len, S]


def get_rr_IC(cnt, graph, old_theta, no):
    R = list()
    n = graph.vertices
    for _ in range(cnt):
        rand_v = random.randint(1, n)
        isActived = {rand_v}
        activity_set = {rand_v}
        while len(activity_set) > 0:
            new_activity_set = set()
            for seed in activity_set:
                for u in graph.adj_list_rev[seed]:
                    if u not in isActived and random.uniform(0, 1) <= graph.edge_weight[u][seed]:
                        isActived.add(u)
                        new_activity_set.add(u)
                activity_set = new_activity_set
        R.append(isActived)
    node_edges = dict()
    for i, RR in enumerate(R):
        for v in RR:
            if v in node_edges:
                node_edges[v].add(old_theta + cnt * no + i)
            else:
                node_edges[v] = {old_theta + cnt * no + i}
    return node_edges


def get_rr_LT(cnt, graph, old_theta, no):
    R = list()
    n = graph.vertices
    for _ in range(cnt):
        rand_v = random.randint(1, n)
        curnode = rand_v
        isActived = {rand_v}
        while True:
            size = len(graph.adj_list_rev[curnode])
            if size == 0:
                break
            op = random.randrange(0, size)
            if graph.adj_list_rev[curnode][op] in isActived:
                break
            isActived.add(graph.adj_list_rev[curnode][op])
            curnode = graph.adj_list_rev[curnode][op]
        R.append(isActived)
    node_edges = dict()
    for i, RR in enumerate(R):
        for v in RR:
            if v in node_edges:
                node_edges[v].add(old_theta + cnt * no + i)
            else:
                node_edges[v] = {old_theta + cnt * no + i}
    return node_edges


def sampling(graph: Graph, k: int, epsilon, l, mode):
    LB = 1
    n = graph.vertices
    epsilon_prime = math.sqrt(2) * epsilon
    f = math.factorial
    lambda_prime = (2 + (2 / 3) * epsilon_prime) * (
                math.log(f(n) // f(k) // f(n - k)) + l * math.log(n) + math.log(math.log(n, 2))) * n / math.pow(
        epsilon_prime, 2)
    alpha = math.sqrt(l * math.log(n) + math.log(2))
    beta = math.sqrt((1 - 1 / math.e) * (math.log(f(n) // f(k) // f(n - k)) + l * math.log(n) + math.log(2)))
    lambda_star = 2 * n * math.pow(((1 - 1 / math.e) * alpha + beta), 2) * math.pow(epsilon, -2)
    node_edges = dict()
    old_theta = 0
    for i in range(1, int(math.log(n, 2))):
        x = n / math.pow(2, i)
        theta = lambda_prime / x
        print("theta", theta)
        curtime = time.time()
        for i in range(worker_cnt):
            worker[i].inQ.put((old_theta, math.ceil((theta - old_theta) / worker_cnt)))
        for i in range(worker_cnt):
            new_dict = worker[i].outQ.get()
            for key, value in new_dict.items():
                if key in node_edges:
                    node_edges[key] |= value
                else:
                    node_edges[key] = value
        # print("sampling", time.time() - curtime)

        old_theta = theta
        curtime = time.time()
        FR = node_selection(node_edges, theta, k)[0]
        # print("node selection", time.time() - curtime)
        # print("FR", FR)
        if n * FR >= (1 + epsilon_prime) * x:
            LB = n * FR / (1 + epsilon_prime)
            break

    theta = lambda_star / LB

    for i in range(worker_cnt):
        worker[i].inQ.put((old_theta, math.ceil((theta - old_theta) / worker_cnt)))
    for i in range(worker_cnt):
        new_dict = worker[i].outQ.get()
        for key, value in new_dict.items():
            if key in node_edges:
                node_edges[key] |= value
            else:
                node_edges[key] = value
    S = node_selection(node_edges, theta, k)[1]
    return S


def IMM(graph: Graph, k: int, epsilon, l, mode):
    n = graph.vertices
    l = l * (1 + math.log(2) / math.log(n))
    S = sampling(graph, k, epsilon, l, mode)

    return S


'''=============================================='''


class Worker(mp.Process):
    def __init__(self, inQ, outQ, mode, graph, no):
        super(Worker, self).__init__(target=self.start)
        self.inQ = inQ
        self.outQ = outQ
        self.mode = mode
        self.graph = graph
        self.no = no
        np.random.seed(random.randrange(1, 100))

    def run(self):
        while True:
            task = self.inQ.get()
            old_theta, cnt = task  # 解析任务
            if self.mode == 'IC':
                res = get_rr_IC(cnt, self.graph, old_theta, self.no)  # 执行任务
            else:
                res = get_rr_LT(cnt, self.graph, old_theta, self.no)
            self.outQ.put(res)  # 返回结果


def create_worker(num, mode):
    for i in range(num):
        worker.append(Worker(mp.Queue(), mp.Queue(), mode, graph, i))
        worker[i].start()


def finish_worker():
    for w in worker:
        w.terminate()


def ISE(S, model, fn):
    # print('''==================ISE TEST=================''')
    fw = './seeds_out.txt'
    with open(fw, 'w') as fp:
        for s in S:
            fp.write('{s}\n'.format(s=s))
    if model == 'IC':
        os.system('python ~/PycharmProjects/IMP/ISE/ISE_final.py -i {} -s {} -m IC -t 60'.format(fn, fw))
    elif model == 'LT':
        os.system('python ~/PycharmProjects/IMP/ISE/ISE_final.py -i {} -s {} -m LT -t 60'.format(fn, fw))


'''=============================================='''

if __name__ == "__main__":
    curtime = time.time()
    parser = argparse.ArgumentParser()
    parser.add_argument('-i', dest='social_network', help='the absolute path of the social network file')
    parser.add_argument('-k', dest='seed_size', help='predefined size of the seed set')
    parser.add_argument('-m', dest='diffusion_model', help='only be IC or LT')
    parser.add_argument('-t', dest='time_budget', help='the seconds that my algorithm can spend on this instance')
    parse_res = parser.parse_args()

    graph = read_social_network_graph(parse_res.social_network)

    global worker_cnt
    worker_cnt = 8

    global worker
    worker = []
    create_worker(worker_cnt, parse_res.diffusion_model)
    k = int(parse_res.seed_size)
    epsilon = 0.1

    l = 1
    model = parse_res.diffusion_model
    S = IMM(graph, k, epsilon, l, model)
    # for s in S:
    # print(s)
    # 
    finish_worker()
    print("elapsed time", time.time() - curtime)
    ISE(S, model, parse_res.social_network)
    sys.stdout.flush()
    os._exit(0)
