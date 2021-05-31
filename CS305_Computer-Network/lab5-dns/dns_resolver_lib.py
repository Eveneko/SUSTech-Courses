from socket import *
from dnslib import DNSRecord
import time

caches = {}
expire_time = {}

serverPort = 12000
# ip = '127.0.0.1'

def queryPublicDnsServer(query):
    clientSocket = socket(AF_INET, SOCK_DGRAM)
    clientSocket.sendto(query, ('114.114.114.114', 53))
    response, server_address = clientSocket.recvfrom(2048)
    return response

serverSocket = socket(AF_INET, SOCK_DGRAM)
serverSocket.bind(('', serverPort))
print("server start")

while True:
    data, clientAddress = serverSocket.recvfrom(2048)
    request = DNSRecord.parse(data)
    tid = request.header.id
    record_question = repr(request.questions)

    from_cache = caches.get(record_question)
    if from_cache is not None:
        print("hit cache")
        if time.time() > expire_time[record_question]:
            print("time expire")
            r_data = queryPublicDnsServer(data)
            response = DNSRecord.parse(r_data)
            min_ttl = 1800

            for i in response.rr:
                if i.ttl < min_ttl:
                    min_ttl = i.ttl

            for i in response.rr:
                i.ttl = min_ttl

            caches[record_question] = r_data
            expire_time[record_question] = time.time() + min_ttl
        else:
            print("Not expire")
            response = DNSRecord.parse(from_cache)
            for i in response.rr:
                i.ttl = int(expire_time[record_question] - time.time())

    else:
        print("Query the public server")
        r_data = queryPublicDnsServer(data)
        response = DNSRecord.parse(r_data)
        min_ttl = 1800

        for i in response.rr:
            if i.ttl < min_ttl:
                min_ttl = i.ttl

        for i in response.rr:
            i.ttl = min_ttl

        caches[record_question] = r_data
        expire_time[record_question] = time.time() + min_ttl
    response.header.id = tid
    print("send message")
    send_response = DNSRecord.pack(response)
    serverSocket.sendto(send_response, clientAddress)
