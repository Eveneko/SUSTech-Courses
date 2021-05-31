from udp import UDPsocket as socket
from rdt import rdt_payload
from rdt import rdt_server
from threading import Timer
import time
import sys
import logging

logging.basicConfig(level=logging.DEBUG,
                format='%(asctime)s %(filename)s[line:%(lineno)d] %(levelname)s %(message)s',
                datefmt='%a, %d %b %Y %H:%M:%S')

SERVER_ADDR = '127.0.0.1'
SERVER_PORT = 12000
ACK_NUM = 0

if __name__ == "__main__":
    server = socket()
    server.bind((SERVER_ADDR, SERVER_PORT))
    receiver = rdt_server(server, SERVER_ADDR, SERVER_PORT, ACK_NUM)
    logging.info("Wait for receiving from the client ...")
    start_time = time.time()
    end_time = time.time()
    while True:
        end_time = time.time()
        if abs(end_time - start_time) >60:
            logging.info("Server close")
            sys.exit()
        try:
            recv_packet, receiver.client_addr_port = server.recvfrom(4096)
            start_time = time.time()
            recv_payload = receiver.rdt_unpack(recv_packet)
            logging.info("Server receives packet {}".format(int.from_bytes(recv_payload.SEQ, 'big')))
            logging.info("cal_CHECKSUM: {}".format(recv_payload.cal_CHECKSUM()))
            logging.info("recv CHECKSUM: {}".format(int.from_bytes(recv_payload.CHECKSUM, 'big')))
            if recv_payload.cal_CHECKSUM() == int.from_bytes(recv_payload.CHECKSUM, 'big'):
                if receiver.ACK_num == int.from_bytes(recv_payload.SEQ, 'big'):
                    logging.info("Server send ACK {}".format(receiver.ACK_num))
                    send_packet = receiver.rdt_pack(recv_payload.sub_payload)
                    receiver.sended_packet.append(send_packet)
                    server.sendto(send_packet, receiver.client_addr_port)
                    receiver.ACK_num += 1
                    logging.info("ACK_num change:" + receiver.ACK_num)
                elif receiver.ACK_num < int.from_bytes(recv_payload.SEQ, 'big'):
                    logging.info("server expected ACK {} is small than client SEQ {}".format(receiver.ACK_num, int.from_bytes(recv_payload.SEQ, 'big')))
                elif receiver.ACK_num > int.from_bytes(recv_payload.SEQ, 'big'):
                    logging.info("server expected ACK {} is larger than client SEQ {}".format(receiver.ACK_num, int.from_bytes(recv_payload.SEQ, 'big')))
                    server.sendto(receiver.sended_packet[int.from_bytes(recv_payload.SEQ, 'big')], receiver.client_addr_port)
                    logging.info("Send packet {}".format(int.from_bytes(recv_payload.SEQ, 'big')))
        except TypeError:
            pass
