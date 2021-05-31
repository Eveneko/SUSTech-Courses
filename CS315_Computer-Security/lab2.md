*CS315 Computer Security*

<center><font size=72><b>Lab Assignment2</b></font></center>

<center>Name｜Yubin Hu</center>

<center>ID｜11712121</center>

<center>Date｜2020.09.18</center>

a. What happens when you compile without "-z execstack" ?

> `-z execstack`: Turn off the NX protection to make the stack executable
>
> If without "-z execstack", we can not execute the instructions in stack.



b. What happens if you enable ASLR? Does the return address change?

> Address Space Layout Randomization (ASLR) is a security features used in most Operating system today. 
>
> ASLR randomly arranges the address spaces of processes, including stack, heap, and libraries.
>
> The return address will be changed.



c. Does the address of the buffer[] in memory change when you run BOF using 

> When we run GDB, it will change the stack(GDB push something into stack), so the return address is different.
>
> - GDB
>
>   ![image-20200919130409873](lab2.assets/image-20200919130409873.png)
>
> - /home/root/Desktop/Lab2-BufferOverflows/BOF
>
>   ![image-20200919130239470](lab2.assets/image-20200919130239470.png)
>
> - ./BOF
>
>   ![image-20200919130333435](lab2.assets/image-20200919130333435.png)

