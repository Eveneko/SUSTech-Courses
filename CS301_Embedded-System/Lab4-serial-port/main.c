#include "led.h" 
#include "delay.h" 
#include "sys.h" 
#include "usart.h" 
#include "key.h"
#include <string.h>

 char str[1024];
 void function()
 {
	 u8 t;
	 u8 len;
	 u8 key;
	 while(1){
     if(USART_RX_STA&0x8000)
		 {
			 len = USART_RX_STA & 0x3fff;		// ��ȡ���ݳ���
			 for (t = 0; t < len; t++) {
				str[t] = USART_RX_BUF[t];
			}
			 if(strcmp(str, "led0 on") == 0)
			 {
				 GPIO_ResetBits(GPIOA,GPIO_Pin_8);	// led0��
				 delay_ms(100);
			 }
			 else if(strcmp(str, "led0 off") == 0)
			 {
				 GPIO_SetBits(GPIOA,GPIO_Pin_8);	// led0��
				 delay_ms(100);
			 }
			 else if(strcmp(str, "led1 on") == 0)
			 {
				 GPIO_ResetBits(GPIOD,GPIO_Pin_2);	// led1��
				 delay_ms(100);
			 }
			 else if(strcmp(str, "led1 off") == 0)
			 {
				 GPIO_SetBits(GPIOD,GPIO_Pin_2);	// led1��
				 delay_ms(100);
			 }
			 else if(len != 0)	// �������led��������򷵻ء�Hello��xxx��
			 {
				printf("Hello, ");
				
				for(t=0;t<len;t++)
				{
					USART1->DR=USART_RX_BUF[t];
					while((USART1->SR&0X40)==0); 
				}
				printf("\r\n");
			 }
			 memset(USART_RX_BUF,0, sizeof(USART_RX_BUF));
			 memset(str,0, sizeof(str));
			USART_RX_STA = 0;
		 }
		 else
		 {
			 key = KEY_Scan(0);	// ��ȡ����״̬
			 if(key == KEY0_PRES)
				 printf("key0 pressed\r\n");
			 else if (key == KEY1_PRES)
				 printf("key1 pressed\r\n");
				
		 }
	 }
 }
 
 int main(void)
 {
	delay_init();	// ��ʱ��ʼ��
	NVIC_PriorityGroupConfig(NVIC_PriorityGroup_2);
	uart_init(9600);	// ���ڲ�����Ϊ9600
	LED_Init();	// LED�Ƴ�ʼ��
	KEY_Init();	// ������ʼ��
	 	
	function();
}
