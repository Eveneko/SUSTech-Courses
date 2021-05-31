from socket import *
import time

# Local
serverName = '127.0.0.1'
serverPort = 12000

# Public DNS server
pubName = '114.114.114.114'
pubPort = 53

# Cache
caches = []
cacheTTL = 3600  # one hour

"""
Header
ID 2bytes
Flags 2bytes
Questions, Answer RRs, Authority RRs, Addition RRs 8bytes

Body
QName length is not fixed, but must end with 0
QType 2bytes
QClass 2bytes
"""

# Query without EDNS


class DNSQuery(object):
    def __init__(self, data):
        self.rawData = list(data)
        self.id = data[0:2]
        self.parseQuestion()

    def parseQuestion(self):
        # Body data, after Header
        self.bodyData = self.rawData[12:]
        # QName must end with 0
        endIndexQName = self.bodyData.index(0x0)
        # QType and QClass
        self.question = self.bodyData[:endIndexQName+5]
        # Query without EDNS
        self.questionData = self.rawData[:12+endIndexQName+5]
        # Set ARcount zero
        self.questionData[11] = 0x0
        # to bytes
        self.questionData = bytes(self.questionData)


# Local DNS cache
class DNSRecord(DNSQuery):
    def __init__(self, query, response):
        # inherit DNSQuery
        super().__init__(response)
        self.query = query
        self.time = time.time()
        self.ttl = []
        # Record Answer RRs
        self.count = int.from_bytes(bytes(self.rawData[6:8]), byteorder='big')
        # Add Authority RRs
        self.count += int.from_bytes(bytes(self.rawData[8:10]), byteorder='big')
        self.parseRecord()

    def parseRecord(self):
        # After Header + Question
        pointer = len(self.questionData)
        rrCount = 0
        while rrCount < self.count:
            # After QName
            if self.rawData[pointer] & 0xc0 == 0xc0:
                # 2bytes
                pointer += 2
            else:
                # QName end with 0
                while self.rawData[pointer] != 0x0:
                    pointer += 1
                pointer += 1
            # After QType
            pointer += 2
            # After QClass
            pointer += 2
            # Record TTL
            self.ttl.append([pointer, int.from_bytes(bytes(self.rawData[pointer:(pointer+4)]), byteorder='big')])
            pointer += 4
            # After RLength and RData
            rlength = int.from_bytes(bytes(self.rawData[pointer:(pointer+2)]), byteorder='big')
            # After RData
            pointer += rlength
            # After Rlength
            pointer += 2
            rrCount += 1

    def getResponse(self, query):
        rr = self.rawData
        # replace ID
        rr[0:2] = query.id
        # Check TTL
        t = int(time.time() - self.time)
        for ttlRecord in self.ttl:
            pointer = ttlRecord[0]
            newTTL = ttlRecord[1] - t
            if newTTL <= 0:
                # Update the record from public DNS server
                self.__init__(self.questionData, queryPublicDNSServer(self.query))
                return self.getResponse(query)
            rr[pointer:(pointer+4)] = int.to_bytes(newTTL, length=4, byteorder='big')
        return bytes(rr)

    # Update TTL
    def revoke(self):
        t = int(time.time() - self.time)
        for ttlRecord in self.ttl:
            newTTL = ttlRecord[1] - t
            if newTTL <= 0:
                return True
        return False


# Query from the public DNS server
def queryPublicDNSServer(query):
    clientSocket.sendto(query.questionData, (pubName, pubPort))
    response, serverAddress = clientSocket.recvfrom(2048)
    return response


if __name__ == "__main__":
    # Record the server starttime
    startTime = time.time()
    serverSocket = socket(AF_INET, SOCK_DGRAM)
    serverSocket.bind((serverName, serverPort))
    clientSocket = socket(AF_INET, SOCK_DGRAM)
    print("The server is ready to receive at%s:%s" % (serverName, serverPort))
    while True:
        # Clean the record
        if time.time() - startTime >= cacheTTL:
            for ca in caches:
                if ca.revoke():
                    caches.remove(ca)
            startTime = time.time()
        message, clientAddress = serverSocket.recvfrom(2048)
        query = DNSQuery(message)

        # Judge whether the query is in the cache
        flag = False
        for cache in caches:
            if(set(query.bodyData) < set(cache.bodyData)):
                # The query is in the cache
                flag = True
                tmp = cache
                break
        if flag:
            response = tmp.getResponse(query)
            print('hit')
        else:
            # The query is not in the cache
            response = queryPublicDNSServer(query)
            # Add the response to cache
            caches.append(DNSRecord(query, response))
            print('miss')
        serverSocket.sendto(response, clientAddress)
