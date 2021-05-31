import math

def find_prime(start, end) -> list:
    prime = []
    for i in range(start, end + 1):
    	if i < 2:
    		continue
        flag = True
        j = 2
        while j * j <= i:
            if i % j == 0:
                flag = False
                break
            j += 1
        if flag:
            prime.append(i)
    return prime


if __name__ == "__main__":
    start = 2
    end = 100000
    prime = find_prime(start, end)
    print (prime)
    print ("There are " + str(len(prime)) + " primes between", start, 'and', end)
