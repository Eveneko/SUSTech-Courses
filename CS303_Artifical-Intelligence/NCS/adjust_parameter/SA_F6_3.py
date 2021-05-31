import tools
import os
import time
import random
from multiprocessing import Process, Pool

parameter = {
    "r": 0.524762603092272,  # [float]
    "lambda": 1.18940275844635,  # [float] range in (1, 10)
    "epoch": 8,  # [int] should small than 300000
    "n": 1  # [int] range in [1, 100]
}

min_val = 1000000000


def func(name):
    eps = 0.001
    para = parameter.copy()
    para["r"] += random.uniform(-eps, eps)
    para["lambda"] += random.uniform(-eps, eps)
    while para["r"] > 2 or para["r"] < 0:
        para["r"] += random.uniform(-eps, eps)
    while para["lambda"] > 2 or para["lambda"] < 0:
        para["lambda"] += random.uniform(-eps, eps)
    ans = tools.get_result(para, 6)
    print('process', os.getpid(), ans, para.__str__())
    global min_val
    filename = os.path.split(__file__)[-1].split(".")[0] + '.txt'
    with open(filename, "a") as f:
        f.write(str(os.getpid()) + ',' + name + ',' + str(ans) + ',' + str(para["r"]) + ',' + str(para["lambda"]) + ',' + str(
            para["epoch"]) + ',' + str(para["n"]) + ',' + '\n')


if __name__ == '__main__':
    print('Parent process %s.' % os.getpid())
    p = Pool(5)
    for i in range(10000):
        p.apply_async(func, args=(i,))
    print('Waiting for all subprocesses done...')
    p.close()
    p.join()
    print('All subprocesses done.')
