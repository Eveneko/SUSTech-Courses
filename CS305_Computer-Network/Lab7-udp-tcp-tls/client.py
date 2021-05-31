from udp import UDPsocket as socket
from rdt import rdt_payload
from rdt import rdt_client
from rdt import WindowFullError
from threading import Timer
import sys
import logging

logging.basicConfig(level=logging.DEBUG,
                format='%(asctime)s %(filename)s[line:%(lineno)d] %(levelname)s %(message)s',
                datefmt='%a, %d %b %Y %H:%M:%S')

SERVER_ADDR = '127.0.0.1'
SERVER_PORT = 12000
WINDOW_BASE = 0
WINDOW_SIZE = 5
TIME_OUT = 1
PAYLOAD_SIZE = 1024

with open('./alice.txt', 'rb') as f:
    MESSAGE = f.read()

if __name__ == "__main__":
    client = socket()
    sender = rdt_client(client, SERVER_ADDR, SERVER_PORT, WINDOW_BASE, WINDOW_SIZE, WINDOW_BASE, TIME_OUT, PAYLOAD_SIZE)
    file = open('./alice.txt', 'rb')
    recv_file = open('./recv_file.txt', 'wb')

    while True:
        while True:
            try:
                if sender.next_seq < sender.window_base + sender.window_size:
                    message = file.read(sender.payload_size)
                    logging.info("Read file into message")
                    if not message:
                        logging.info("File read completed")
                        sender.end = sender.next_seq - 1
                        break
                    else:
                        sender.rtd_send(message)
                else:
                    raise WindowFullError
            except WindowFullError:
                logging.warn("Window is Full")
                break
        try:
            recv_packet, addr_port = client.recvfrom(4096)
            recv_payload = sender.rdt_unpack(recv_packet)
            logging.info("Client receives packet {}".format(int.from_bytes(recv_payload.SEQ, 'big')))
            logging.info("calsum: {}".format(recv_payload.cal_CHECKSUM()))
            logging.info("recvsum: {}".format(int.from_bytes(recv_payload.CHECKSUM, 'big')))
            if recv_payload.cal_CHECKSUM() == int.from_bytes(recv_payload.CHECKSUM, 'big'):
                logging.info("Receive ACK {}".format(int.from_bytes(recv_payload.SEQ, 'big')))
                if int.from_bytes(recv_payload.SEQ, 'big') == sender.window_base:
                    if int.from_bytes(recv_payload.SEQ, 'big') == sender.end:
                        logging.info("Transmite the whole file completed")
                        sender.timer.cancel()
                        sys.exit()
                    recv_file.write(recv_payload.sub_payload)
                    sender.window_base += 1
                    if sender.window_base == sender.next_seq:
                        if sender.timer is not None:
                            sender.timer.cancel()
                        sender.timer = Timer(sender.time_out, sender.rdt_resend)
                        sender.timer.start()
                    logging.info("Window move to {}".format(sender.window_base))
                else:
                    pass
            else:
                logging.info("Packet corrupt")
        except TypeError:
            pass
