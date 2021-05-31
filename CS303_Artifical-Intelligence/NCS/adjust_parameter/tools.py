import os
import json
import random
import math
import time
import datetime
from algorithm_ncs import ncs_c as ncs

parameter = {
    "r": 0.802,  # [float]
    "lambda": 1.022,  # [float] range in (1, 10)
    "epoch": 20,  # [int] should small than 300000
    "n": 2  # [int] range in [1, 100]
}


def store(data, address):
    with open(address, 'w') as fw:
        json.dump(data, fw)


def load(address):
    with open(address, 'r') as f:
        data = json.load(f)
        return data


def rand(para) -> dict:
    para['r'] = random.uniform(0, 1)
    para['lambda'] = random.uniform(0, 10)
    para['epoch'] = random.randint(1, 100)
    para['n'] = random.randint(1, 10)
    return para


def rand6(para) -> dict:
    para['r'] = random.uniform(0.096, 0.116)
    para['lambda'] = random.uniform(1.538, 1.558)
    para['epoch'] = random.randint(5, 5)
    para['n'] = random.randint(1, 1)
    return para


def rand12(para) -> dict:
    para['r'] = random.uniform(0.780, 0.822)
    para['lambda'] = random.uniform(0.822, 1.222)
    para['epoch'] = random.randint(20, 20)
    para['n'] = random.randint(2, 2)
    return para


def get_result(ncs_para, data):
    # print(ncs_para)
    r = ncs_para["r"]
    _lambda = ncs_para["lambda"]
    epoch = ncs_para["epoch"]
    n = ncs_para["n"]
    p = data

    try:
        ncs_para = ncs.NCS_CParameter(tmax=300000, lambda_exp=_lambda, r=r, epoch=epoch, N=n)
        ncs_c = ncs.NCS_C(ncs_para, p)
        ncs_res = ncs_c.loop(quiet=False, seeds=0)
    except:
        ncs_res = 1000000000

    return ncs_res


def hill_climb(p):
    start_time = time.time()
    # 随机登山点
    # para = rand(parameter)
    para = parameter.copy()
    std = 390 if p == 6 else -460
    ans = 1000000000
    eps = 1e-4
    step = {
        "r": 0.001,  # [float]
        "lambda": 0.001,  # [float] range in (1, 10)
        "epoch": 1,  # [int] should small than 300000
        "n": 1  # [int] range in [1, 100]
    }
    test_para = para.copy()

    def argmin(old_para, new_para, ans, p):
        # cur = get_result(old_para, p)
        next = get_result(new_para, p)
        if ans > next:
            ans = next
            print("[", ans, new_para, "]")
            return ans, new_para
        else:
            # ans = cur
            return ans, old_para

    def judge(a, b):
        return a & (1 << (b - 1)) > 0

    cnt = 0
    while (ans - std > eps) & (time.time() - start_time < 3600 * 4):
        max_para = test_para.copy()
        for r in [0, 1, -1]:
            for _lambda in [0, 1, -1]:
                for epoch in [0, 1, -1]:
                    for n in [0, 1, -1]:
                        tmp_para = test_para.copy()
                        if 0 < r * step["r"] + tmp_para["r"] < 1:
                            tmp_para["r"] = r * step["r"] + tmp_para["r"]
                        if 1 < _lambda * step["lambda"] + tmp_para["lambda"] < 10:
                            tmp_para["lambda"] = _lambda * step["lambda"] + tmp_para["lambda"]
                        if 1 <= epoch * step["epoch"] + tmp_para["epoch"] <= 300000:
                            tmp_para["epoch"] = epoch * step["epoch"] + tmp_para["epoch"]
                        if 1 <= n * step["n"] + tmp_para["n"] <= 100:
                            tmp_para["n"] = n * step["n"] + tmp_para["n"]
                        print(tmp_para)
                        ans, max_para = argmin(max_para, tmp_para, ans, p)
        # for i in range(16):
        #     print(".", end="" if i!= 31 else "\n")
        #     tmp_para = test_para.copy()

        if test_para == max_para:
            break
        else:
            test_para = max_para.copy()
            cnt += 1
            print('th iteration is', ans, test_para)
    return ans, test_para


if __name__ == '__main__':
    print(get_result(parameter, 12))
    for i in range(1):
        print("The th", i + 1, "hill climbing!")
        hill_climb(12)
