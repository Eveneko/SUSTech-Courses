# -*- coding: utf-8 -*-

import os
import sys
import copy
import time
import random
import argparse
import numpy as np
import multiprocessing as mp
from multiprocessing import Pool


class Vertex:
    def __init__(self, key):
        self.id = key
        self.connectedTo = {}

    def addNeighbor(self, id, weight=0):
        self.connectedTo[id] = weight

    def getConnections(self):
        return self.connectedTo.keys()

    def getId(self):
        return self.id

    def getWeight(self, id):
        return self.connectedTo[id]


class Graph:
    def __init__(self, num_vertices, num_edge):
        self.vert_List = {}
        self.num_vertices = num_vertices
        self.num_edge = num_edge

    def addVertex(self, key):
        newVertex = Vertex(key)
        self.vert_List[key] = newVertex
        return newVertex

    def getVertex(self, n):
        return self.vert_List[n]

    def __contains__(self, n):
        return n in self.vert_List

    def addEdge(self, u, v, cost=0):
        self.vert_List[u].addNeighbor(self.vert_List[v], cost)

    def getVertices(self):
        return self.vert_List.keys()

    def __iter__(self):
        return iter(self.vert_List.values())


def read_network(network_file):
    global network
    with open(network_file) as f:
        for i, line in enumerate(f):
            line = line.strip('\r\n').split(' ')
            line[0] = int(line[0])
            line[1] = int(line[1])
            if i == 0:
                network = Graph(line[0], line[1])
                for j in range(line[0] + 1):
                    network.addVertex(j)
            else:
                line[2] = float(line[2])
                network.addEdge(line[0], line[1], line[2])


def read_seeds(seeds_file):
    global seeds
    with open(seeds_file) as f:
        for seed in f:
            seeds.append(int(seed))


def IC_model(time_budget):
    time2 = time.time()
    N = 10000
    res = 0
    while True:
        if time.time() - time2 > time_budget - 3 or N < 0:
            break
        global seeds
        global network
        activity_set = copy.deepcopy(seeds)
        isActive = [v for v in seeds]
        cnt = len(activity_set)
        while len(activity_set) != 0:
            new_activity_set = []
            for u in activity_set:
                u_ver = network.getVertex(u)
                u_neb = u_ver.getConnections()
                for v in u_neb:
                    u_w = u_ver.connectedTo[v]
                    if v not in isActive and random.uniform(0,1) <= u_w:
                        isActive.append(v)
                        new_activity_set.append(v)
            cnt += len(new_activity_set)
            activity_set = copy.deepcopy(new_activity_set)
        N -= 1
        res += cnt
    return res/N


def LT_model(time_budget):
    time2 = time.time()
    N = 10000
    res = 0
    while True:
        if time.time() - time2 > time_budget - 3 or N < 0:
            break
        global seeds
        global network
        activity_set = copy.deepcopy(seeds)
        seeds_threshold = [random.uniform(0,1) for i in range(network.num_vertices)]
        for i in len(seeds_threshold):
            if seeds_threshold[i] == 0:
                activity_set.append(i)
        isActive = [v for v in activity_set]
        cnt = len(activity_set)
        while len(activity_set) != 0:
            new_activity_set = []
            for u in activity_set:
                u_ver = network.getVertex(u)
                u_neb = u_ver.getConnections()
                for v in u_neb:
                    v_ver = network.getVertex(v)
                    v_neb = v_ver.getConnections()
                    if v not in isActive:
                        cnt_w = 0
                        for seed in v_neb:
                            if seed in isActive:
                                neb_ver = network.getVertex(seed)
                                neb_w = neb_ver.connectedTo[v]
                                cnt_w += neb_w
                        if cnt_w >= seeds_threshold[v]:
                            isActive.append(v)
                            new_activity_set.append(v)
            cnt += len(new_activity_set)
            activity_set = copy.deepcopy(new_activity_set)
        N -= 1
        res += cnt
    return res / N


if __name__ == '__main__':
    time1 = time.time()
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

    # data
    global seeds
    seeds = []
    read_seeds(seeds_file_path)
    global network
    read_network(network_file_path)

    # multiprocessing
    global pool_size
    pool_size = 8
    p = Pool(pool_size)
    if model == 'IC':
        ans = p.map(IC_model, [int(time_budget) - (time.time() - time1) for i in range(pool_size)])
        print(ans)
    elif model == 'LT':
        ans = p.map(LT_model, [time_budget - (time.time() - time1) for i in range(pool_size)])
        print(ans)
    p.close()
    p.join()

    # force exit, empty the cache
    sys.stdout.flush()
    os._exit(0)
