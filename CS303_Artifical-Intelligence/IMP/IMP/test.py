import time

a = dict()
b = list()
for i in range(10000000):
    a[i] = i
    b.append(i)


sum = 0
time1 = time.time()

for i in a.keys():
    sum += i

print('dict time:', time.time() - time1)


sum = 0
time2 = time.time()

for i in b:
    sum += i

print('list time:', time.time() - time2)
