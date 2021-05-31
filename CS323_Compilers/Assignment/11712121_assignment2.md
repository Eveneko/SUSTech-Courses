*CS323 Compilation Principle*

<center><font size=72><b>Assignment 2</b></font></center>

<center>Name｜Yubin Hu</center>

<center>ID｜11712121</center>

<center>Date｜2020.10.06</center>

# Required Exercises

## Exercise 1

> Design NFAs and DFAs to recognize each of the following regular languages:
>
> 1. $L((a|b)^*b)$ [10 points]
> 2. $L(((\epsilon|a)^*b)^*)$ [10 points]
> 3. $L((a|b)^*a(a|b)(a|b))$ [10 points]
> 4. $L(a^*ba^*ba^*ba^*)$ [10 points]​

1. NFA:  <img src="11712121_assignment2.assets/image-20201009200535000.png" alt="image-20201009200535000" style="zoom:50%;" />

   DFA: <img src="11712121_assignment2.assets/image-20201009200845253.png" alt="image-20201009200845253" style="zoom:50%;" />
   
2. NFA:  <img src="11712121_assignment2.assets/image-20201009204218287.png" alt="image-20201009204218287" style="zoom:50%;" />

   DFA: <img src="11712121_assignment2.assets/image-20201010110848833.png" alt="image-20201010110848833" style="zoom:50%;" />
   
3. NFA:  ![image-20201010104547372](11712121_assignment2.assets/image-20201010104547372.png)

   DFA: <img src="11712121_assignment2.assets/image-20201010112917438.png" alt="image-20201010112917438" style="zoom:50%;" />
   
4. NFA:  <img src="11712121_assignment2.assets/image-20201010105559082.png" alt="image-20201010105559082" style="zoom:50%;" />

   DFA: <img src="11712121_assignment2.assets/image-20201010113710354.png" alt="image-20201010113710354" style="zoom:50%;" />





## Exercise 2

> Convert the following regular expressions to NFAs using the Thompson’s Construction Algorithm (Algorithm 3.23 in the dragon book). Please put down the detailed steps.
>
> 1. $((\epsilon|a)^*b)^*$ [10 points]
> 2. $(a|b)^*a(a|b)(a|b)$[10 points]
> 3. $a^*ba^*ba^*ba^*$ [10 points]

1. $((\epsilon|a)^*b)^*$

   1. NFA for the first $\epsilon$:

   <img src="11712121_assignment2.assets/image-20201009202829788.png" alt="image-20201009202829788" style="zoom:50%;" />

   2. NFA for the first a:

   <img src="11712121_assignment2.assets/image-20201009202856665.png" alt="image-20201009202856665" style="zoom:50%;" />

   3. NFA for $(\epsilon|a)$:

   <img src="11712121_assignment2.assets/image-20201009203119311.png" alt="image-20201009203119311" style="zoom:50%;" />

   4. NFA for $(\epsilon|a)^*$:

   <img src="11712121_assignment2.assets/image-20201009203347346.png" alt="image-20201009203347346" style="zoom:50%;" />NFA for $b$:

   <img src="11712121_assignment2.assets/image-20201009203613215.png" alt="image-20201009203613215" style="zoom:50%;" />

   5. NFA for $(\epsilon|a)^*b$:

   <img src="11712121_assignment2.assets/image-20201009203740521.png" alt="image-20201009203740521" style="zoom:50%;" />

   6. NFA for $((\epsilon|a)^*b)^*$:

   <img src="11712121_assignment2.assets/image-20201009204218287.png" alt="image-20201009204218287" style="zoom:50%;" />

   

2. $(a|b)^*a(a|b)(a|b)$

   1. NFA for $a$

      <img src="11712121_assignment2.assets/image-20201010103237542.png" alt="image-20201010103237542" style="zoom:50%;" />

   2. NFA for $b$

      <img src="11712121_assignment2.assets/image-20201010103316686.png" alt="image-20201010103316686" style="zoom:50%;" />

   3. NFA for $(a|b)$

      <img src="11712121_assignment2.assets/image-20201010103515698.png" alt="image-20201010103515698" style="zoom:50%;" />

   4. NFA for $(a|b)^*$

      <img src="11712121_assignment2.assets/image-20201010103654907.png" alt="image-20201010103654907" style="zoom:50%;" />

   5. NFA for $(a|b)^*a$

      <img src="11712121_assignment2.assets/image-20201010103914288.png" alt="image-20201010103914288" style="zoom:50%;" />

   6. NFA for $(a|b)^*a(a|b)$

      ![image-20201010104111874](11712121_assignment2.assets/image-20201010104111874.png)

   7. NFA for $(a|b)^*a(a|b)(a|b)$

      ![image-20201010104547372](11712121_assignment2.assets/image-20201010104547372.png)

3. $a^*ba^*ba^*ba^*$

   1. NFA for $a$

      <img src="11712121_assignment2.assets/image-20201010103237542.png" alt="image-20201010103237542" style="zoom:50%;" />

   2. NFA for $a^*$

      <img src="11712121_assignment2.assets/image-20201010104956937.png" alt="image-20201010104956937" style="zoom:50%;" />

   3. NFA for $b$

      <img src="11712121_assignment2.assets/image-20201010103316686.png" alt="image-20201010103316686" style="zoom:50%;" />

   4. NFA for $a^*b$

      <img src="11712121_assignment2.assets/image-20201010105031651.png" alt="image-20201010105031651" style="zoom:50%;" />

   5. NFA for $a^*ba^*ba^*ba^*$

      <img src="11712121_assignment2.assets/image-20201010105559082.png" alt="image-20201010105559082" style="zoom:50%;" />

## Exercise 3

> Convert the NFAs in Exercise 2 to DFAs using the Subset Construction Algorithm (Algorithm 3.20 in the dragon book). Please put down the detailed steps. [30 points in total; 10 points for each correct conversion]

1. NFA:  z<img src="11712121_assignment2.assets/image-20201009204218287.png" alt="image-20201009204218287" style="zoom:50%;" />

   | state |         $I$          | $I_a$ | $I_b$ |
   | :---: | :------------------: | :---: | :---: |
   |   A   | {0,1,2,3,4,5,7,8,10} |   B   |   C   |
   |   B   |   {2,3,4,5,6,7,8}    |   B   |   C   |
   |   C   | {1,2,3,4,5,6,8,9,10} |   B   |   C   |

   DFA: <img src="11712121_assignment2.assets/image-20201010110757008.png" alt="image-20201010110757008" style="zoom:50%;" />

2. NFA:  ![image-20201010104547372](11712121_assignment2.assets/image-20201010104547372.png)

   | state |                          $I$                           | $I_a$ | $I_b$ |
   | :---: | :----------------------------------------------------: | :---: | :---: |
   |   A   |                      {0,1,2,4,7}                       |   B   |   C   |
   |   B   |              {1,2,==3==,4,6,7,==8==,9,11}              |   D   |   E   |
   |   C   |                   {1,2,4,==5==,6,7}                    |   B   |   C   |
   |   D   |      {1,2,==3==,4,6,7,==8==,9,==10==,11,13,14,16}      |   F   |   G   |
   |   E   |           {1,2,4,==5==,6,7,==12==,13,14,16}            |   H   |   I   |
   |   F   | {1,2,==3==,4,6,7,==8==,9,==10,==11,13,14,==15==,16,18} |   F   |   G   |
   |   G   |      {1,2,4,==5==,6,7,==12==,13,14,16,==17==,18}       |   H   |   I   |
   |   H   |         {1,2,==3==,4,6,7,==8==,9,11,==15==,18}         |   D   |   E   |
   |   I   |              {1,2,4,==5==,6,7,==17==,18}               |   B   |   C   |

   DFA: <img src="11712121_assignment2.assets/image-20201010112906380.png" alt="image-20201010112906380" style="zoom:50%;" />

3. NFA:  <img src="11712121_assignment2.assets/image-20201010105559082.png" alt="image-20201010105559082" style="zoom:50%;" />

   | state |    $I$     | $I_a$ | $I_b$ |
   | :---: | :--------: | :---: | :---: |
   |   A   |  {0,1,3}   |   B   |   C   |
   |   B   |  {1,2,3}   |   B   |   C   |
   |   C   |  {4,5,7}   |   D   |   E   |
|   D   |  {5,6,7}   |   D   |   E   |
   |   E   |  {8,9,11}  |   F   |   G   |
   |   F   | {9,10,11}  |   F   |   G   |
   |   G   | {12,13,15} |   H   |       |
   |   H   | {13,14,15} |   H   |       |
   
   DFA: <img src="11712121_assignment2.assets/image-20201010113653210.png" alt="image-20201010113653210" style="zoom:50%;" />



# Optional Exercises

## Exercise 1

> Minimize the number of states of the DFAs you have built for regular expressions 2 and 3 in Exercise 2 using the State-Minimization Algorithm (Algorithm 3.39 in the dragon book). Please put down the detailed steps. [10 points for each correct minimization process]

