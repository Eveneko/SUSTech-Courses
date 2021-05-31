# -*- coding: utf-8 -*-

"""
python3 ISE.py -i network.txt -s seeds.txt -m IC -t 60
python3 ISE.py -i NetHEPT_fixed.txt -s seeds.txt -m IC -t 60
"""

import os
import sys
import copy
import time
import random
import argparse
import numpy as np
import multiprocessing as mp
from multiprocessing import Pool
from collections import defaultdict
from functools import partial



def read_network(network_file):
    vertex = 0
    edge = 0 
    network = defaultdict(dict)
    with open(network_file) as f:
        for i, line in enumerate(f):
            line = line.strip('\r\n').split(' ')
            if i == 0:
                vertex, edge = int(line[0]), int(line[1])
            else:
                u = int(line[0])
                v = int(line[1])
                w = float(line[2])
                network[u][v] = w
    return network, vertex, edge


def read_seeds(seeds_file):
    seeds = []
    with open(seeds_file) as f:
        for line in f:
            seeds.append(int(line))
    return seeds

def IC_model(network, seeds, vertex, edge, time_budget):
    model_start_time = time.time()
    N = 0
    res = 0
    while True:
        if time.time() - model_start_time > time_budget - 3:
            break
        isactive = [False for i in range(vertex+1)]
        active_list = []
        for u in seeds:
            active_list.append(u)
            isactive[u] = True
        cnt = len(seeds)
        while active_list:
            u = active_list.pop(0)
            for v in network[u]:
                if not isactive[v]:
                    if random.random() <= network[u][v]:
                        isactive[v] = True
                        active_list.append(v)
                        cnt += 1
        res += cnt
        N += 1
    return res, N


def LT_model(network, seeds, vertex, edge, time_budget):
    model_start_time = time.time()
    N = 0
    res = 0
    while True:
        if time.time() - model_start_time > time_budget - 3:
            break
        isactive = [False for i in range(vertex+1)]
        active_list = []
        for u in seeds:
            active_list.append(u)
            isactive[u] = True
        threshold = [random.random() for i in range(vertex+1)]
        prob_neb = defaultdict(float)
        cnt = len(seeds)
        while active_list:
            u = active_list.pop(0)
            for v in network[u]:
                if not isactive[v]:
                    prob_neb[v] += network[u][v]
                    if prob_neb[v] >= threshold[v]:
                        isactive[v] = True
                        active_list.append(v)
                        cnt += 1
        res += cnt
        N += 1
    return res, N


if __name__ == '__main__':
    start_time = time.time()
    # get parameters
    parser = argparse.ArgumentParser()
    parser.add_argument('-i', dest='social_network', type=str, default='network.txt', help='the path')
    parser.add_argument('-s', dest='seed_set', type=str, default='seeds.txt', help='the path')
    parser.add_argument('-m', dest='diffusion_model', type=str, default='IC', help='IC/LT')
    parser.add_argument('-t', dest='time_budget', type=int, default=60, help='60~120')
    args = parser.parse_args()
    network_file_path = args.social_network
    seeds_file_path = args.seed_set
    model = args.diffusion_model
    time_budget = args.time_budget

    # read data
    seeds = read_seeds(seeds_file_path)
    network, vertex, edge = read_network(network_file_path)

    # multiprocessing
    pool_size = 8
    p = Pool(pool_size)
    if model == 'IC':
        func = partial(IC_model, network, seeds, vertex, edge)
        ans = p.map(func, [(int(time_budget) - (time.time() - start_time)) for i in range(pool_size)])
        sum_ans = 0
        sum_n = 0
        for sub_ans in ans:
            sum_ans += sub_ans[0]
            sum_n += sub_ans[1]
        res = sum_ans / sum_n
        print(res)
    elif model == 'LT':
        func = partial(LT_model, network, seeds, vertex, edge)
        ans = p.map(func, [(int(time_budget) - (time.time() - start_time)) for i in range(pool_size)])
        sum_ans = 0
        sum_n = 0
        for sub_ans in ans:
            sum_ans += sub_ans[0]
            sum_n += sub_ans[1]
        res = sum_ans / sum_n
        print(res)
    p.close()
    p.join()

    # force exit, empty the cache
    sys.stdout.flush()
    os._exit(0)
