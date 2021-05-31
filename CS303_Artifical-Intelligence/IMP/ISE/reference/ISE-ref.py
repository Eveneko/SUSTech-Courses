import os
import sys
import time
import math
import random
import argparse
from collections import defaultdict
from multiprocessing import Pool

seed_set = []

class Graph:
    def __init__(self, vertices, edges, edge_weight, adj_list, neighbor):
        self.vertices = vertices
        self.edges = edges
        self.edge_weight = edge_weight
        self.adj_list = adj_list
        self.neighbor = neighbor


def read_seed_set(file_name):
    global seed_set
    with open(file_name) as fp:
        for line in fp:
            seed_set.append(int(line))


def read_social_network_graph(file_name):
    edge_weight, adj_list, neighbor = [[], [], []]
    vertices, edges = [0, 0]

    with open(file_name) as fp:
        for i, line in enumerate(fp):

            line = line.split(" ")
            for e in range(len(line)):
                if e == 2:
                    line[e] = float(line[e])
                else:
                    line[e] = int(line[e])
            
            if i == 0:
                vertices, edges = line
                adj_list = [list() for i in range(vertices+1)]
                neighbor = [list() for i in range(vertices+1)]
                edge_weight = [defaultdict() for i in range(vertices+1)]
            else:
                u,v,w = line
                adj_list[u].append(v)
                neighbor[v].append(u)
                edge_weight[u][v] = w

    return Graph(vertices, edges, edge_weight, adj_list, neighbor)
    

def LT_model(time_budget):
    curtime = time.time()
    sum = 0
    N = 0
    last_time = 0
    while True:
        startime = time.time()
        if time.time() - curtime + last_time > time_budget - 1 or N >= 10000:
            break

        activity_set = seed_set
        thresholds = [0 for i in range(graph.vertices+1)]
        isActive = set()
        for i in range(len(thresholds)):          
            if i == 0:
                thresholds[i] = math.inf
            else:
                rand_num = random.random()
                thresholds[i] = rand_num
                if rand_num == 0:
                    activity_set.append(i)
        count = len(activity_set)         
        for e in activity_set:
            isActive.add(e)             
        while activity_set:
            new_activity_set = []
            for seed in activity_set:
                for u in graph.adj_list[seed]:
                    if u not in isActive:
                        w_total = 0
                        for neighbor in graph.neighbor[u]:
                            if neighbor in isActive:
                                w_total += graph.edge_weight[neighbor][u]
                        if w_total >= thresholds[u]:
                            isActive.add(u)
                            new_activity_set.append(u)
            count += len(new_activity_set)
            activity_set = new_activity_set

        N += 1
        sum += count 
        last_time = time.time() - startime
    return sum / N
    

def IC_model(time_budget):
    curtime = time.time()
    sum = 0
    N = 0
    last_time = 0
    while True:
        startime = time.time()
        if time.time() - curtime + last_time > time_budget - 1 or N >= 10000:
            break

        activity_set = seed_set
        isActive = set()
        # initialize isActive      
        for e in activity_set:
            isActive.add(e)
    
        count = len(activity_set)

        while len(activity_set) > 0:
            new_activity_set = []
            for seed in activity_set:
                for u in graph.adj_list[seed]:
                    if u not in isActive and random.uniform(0, 1) <= graph.edge_weight[seed][u]:
                            isActive.add(u)
                            new_activity_set.append(u)
            count += len(new_activity_set)
            activity_set = new_activity_set
        
        N += 1
        sum += count 
        last_time = time.time() - startime

    return sum / N
        


if __name__ == "__main__":
    curtime = time.time()
    parser = argparse.ArgumentParser()
    parser.add_argument('-i', dest='social_network' ,help='the absolute path of the social network file')
    parser.add_argument('-s', dest='seed_set', help='the absolute path of the seed set file')
    parser.add_argument('-m', dest='diffusion_model', help='only be IC or LT')
    parser.add_argument('-t', dest='time_budget', type=int, help='the seconds that my algorithm can spend on this instance')
    parse_res = parser.parse_args()
    
    read_seed_set(parse_res.seed_set)
    
    global workers
    workers = 8
    global graph
    graph = read_social_network_graph(parse_res.social_network)

    if parse_res.diffusion_model == 'IC':
        with Pool(workers) as p:
            res = p.map(IC_model, [int(parse_res.time_budget) - (time.time() - curtime) for i in range(workers)]) 
            print(sum(res) / workers)
    elif parse_res.diffusion_model == 'LT':
        with Pool(workers) as p:
            res = p.map(LT_model, [int(parse_res.time_budget) - (time.time() - curtime) for i in range(workers)]) 
            print(sum(res) / workers)
    # print(time.time() - curtime)
    sys.stdout.flush()
    os._exit(0)
    