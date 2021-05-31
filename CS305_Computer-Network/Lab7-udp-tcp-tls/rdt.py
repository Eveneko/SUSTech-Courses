from udp import UDPsocket  # import provided class
import sys
import time
from threading import Timer
import logging

logging.basicConfig(level=logging.DEBUG,
                    format='%(asctime)s %(filename)s[line:%(lineno)d] %(levelname)s %(message)s',
                    datefmt='%a, %d %b %Y %H:%M:%S')

### FLAG
SYN = 0b00000100
FIN = 0b00000010
ACK = 0b00000001


class WindowFullError(Exception):
    def __str__(self):
        return "The window is full, you can not send data right now."


class rdt_payload(object):
    def __init__(self):
        self.FLAG = bytes(1)
        self.SEQ = bytes(4)
        self.SEQ_ACK = bytes(4)
        self.LEN = bytes(4)
        self.CHECKSUM = bytes(2)
        self.sub_payload = b''

    def set_FLAG(self, FLAG):
        if isinstance(FLAG, bytes):
            self.FLAG = FLAG
        else:
            self.FLAG = FLAG.to_bytes(1, 'big')

    def set_SEQ(self, SEQ):
        if isinstance(SEQ, bytes):
            self.SEQ = SEQ
        else:
            self.SEQ = SEQ.to_bytes(4, 'big')

    def set_SEQ_ACK(self, SEQ_ACK):
        if isinstance(SEQ_ACK, bytes):
            self.SEQ_ACK = SEQ_ACK
        else:
            self.SEQ_ACK = SEQ_ACK.to_bytes(4, 'big')

    def set_LEN(self, LEN):
        if isinstance(LEN, bytes):
            self.LEN = LEN
        else:
            self.LEN = LEN.to_bytes(4, 'big')

    def set_CHECKSUM(self, CHECKSUM):
        if isinstance(CHECKSUM, bytes):
            self.CHECKSUM = CHECKSUM
        else:
            self.CHECKSUM = CHECKSUM.to_bytes(2, 'big')

    def cal_CHECKSUM(self):
        data = self.FLAG + self.SEQ + self.SEQ_ACK + self.LEN + b'\x00\x00' + self.sub_payload
        sum = 0
        for byte in data:
            sum += byte
            sum = -(sum % 256)
        sum = (sum & 0xFF)
        return sum

    def set_sub_payload(self, data):
        self.sub_payload = data

    def packet_to_bytes(self):
        return self.FLAG + self.SEQ + self.SEQ_ACK + self.LEN + self.CHECKSUM + self.sub_payload


class rdt_client(object):
    def __init__(self, client, server_addr, server_port, window_base, window_size, next_seq, time_out, payload_size):
        self.client = client
        self.server_addr = server_addr
        self.server_port = server_port
        self.window_base = window_base
        self.window_size = window_size
        self.next_seq = next_seq
        self.time_out = time_out
        self.payload_size = payload_size
        self.timer = None
        self.sended_packets = []
        self.end = -1

    def rtd_send(self, data):
        if self.window_base == self.next_seq:
            if self.timer is not None:
                self.timer.cancel()
            self.timer = Timer(self.time_out, self.rdt_resend)
            self.timer.start()
        self.sended_packets.append(self.rdt_pack(data))
        logging.info("Send packet {}".format(self.next_seq))
        self.client.sendto(self.sended_packets[self.next_seq], (self.server_addr, self.server_port))
        self.next_seq += 1

    def rdt_pack(self, data):
        payload = rdt_payload()
        payload.set_sub_payload(data)
        payload.set_LEN(len(payload.sub_payload) + 15)
        payload.set_FLAG(ACK)
        payload.set_SEQ(self.next_seq)
        payload.set_SEQ_ACK(self.window_base)
        payload.set_CHECKSUM(payload.cal_CHECKSUM())
        return payload.packet_to_bytes()

    def rdt_resend(self):
        logging.info("Time out! Resend the packets from {} to {}".format(self.window_base, self.next_seq - 1))
        if self.timer is not None:
            self.timer.cancel()
        self.timer = Timer(self.time_out, self.rdt_resend)
        self.timer.start()
        for i in range(self.window_base, self.next_seq):
            logging.info("Resend packet {}".format(i))
            self.client.sendto(self.sended_packets[i], (self.server_addr, self.server_port))

    def rdt_unpack(self, recv_packet):
        payload = rdt_payload()
        payload.set_sub_payload(recv_packet[15:])
        payload.set_LEN(recv_packet[9:13])
        payload.set_FLAG(recv_packet[0:1])
        payload.set_SEQ(recv_packet[1:5])
        payload.set_SEQ_ACK(recv_packet[5:9])
        payload.set_CHECKSUM(recv_packet[13:15])
        return payload


class rdt_server():
    def __init__(self, server, server_addr, server_port, ACK_num):
        self.server = server
        self.server_addr = server_addr
        self.server_port = server_port
        self.ACK_num = ACK_num
        self.ACK = 1
        self.client_addr_port = ()
        self.sended_packet = []

    def rdt_pack(self, data):
        payload = rdt_payload()
        payload.set_sub_payload(data)
        payload.set_LEN(len(payload.sub_payload) + 15)
        payload.set_FLAG(ACK)
        payload.set_SEQ(self.ACK_num)
        payload.set_SEQ_ACK(self.ACK_num + 1)
        payload.set_CHECKSUM(payload.cal_CHECKSUM())
        return payload.packet_to_bytes()

    def rdt_unpack(self, recv_packet):
        payload = rdt_payload()
        payload.set_sub_payload(recv_packet[15:])
        payload.set_LEN(recv_packet[9:13])
        payload.set_FLAG(recv_packet[0:1])
        payload.set_SEQ(recv_packet[1:5])
        payload.set_SEQ_ACK(recv_packet[5:9])
        payload.set_CHECKSUM(recv_packet[13:15])
        return payload
