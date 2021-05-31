#include "stm32f10x.h"
#include "sys.h"
#include "delay.h"

// ��Ӧ��ϵ �� PA8 ���� led0      PD2 ���� led1        IO״̬Ϊ��ʱ������Ϊ��ʱ��
int main()
{		 

	GPIO_InitTypeDef  GPIO_InitStructure;
	RCC_APB2PeriphClockCmd(RCC_APB2Periph_GPIOA|RCC_APB2Periph_GPIOD, ENABLE);	 //ʹ��PA,PD�˿�ʱ��

	delay_init();	
	GPIO_InitStructure.GPIO_Pin = GPIO_Pin_8;				 //LED0-->PA.8 �˿�����
	GPIO_InitStructure.GPIO_Mode = GPIO_Mode_Out_PP; 		 //�������
	GPIO_InitStructure.GPIO_Speed = GPIO_Speed_50MHz;		 //IO���ٶ�Ϊ50MHz
	GPIO_Init(GPIOA, &GPIO_InitStructure);					 //�����趨������ʼ��GPIOA.8
	 
	GPIO_InitStructure.GPIO_Pin = GPIO_Pin_2;	
	GPIO_InitStructure.GPIO_Mode = GPIO_Mode_Out_PP; 		 //�������
	GPIO_InitStructure.GPIO_Speed = GPIO_Speed_50MHz;		 //IO���ٶ�Ϊ50MHz
  GPIO_Init(GPIOD, &GPIO_InitStructure);
	
	GPIO_SetBits(GPIOA,GPIO_Pin_8);	// �ر�Pin8
	GPIO_SetBits(GPIOD,GPIO_Pin_2); // �ر�pin2
	
  while(1)
	{  		 
		 int i;		 
		 // �����ٶȽ�����˸
		 // ϵͳ��������
		 for(i = 0;i < 4; i++){
			 GPIO_ResetBits(GPIOA,GPIO_Pin_8);	// ����Ϊ�͵�λ��LED����
			 GPIO_SetBits(GPIOD,GPIO_Pin_2);		// ����Ϊ�ߵ�λ��LED�Ʋ���
			 delay_ms(1000);	
			 GPIO_SetBits(GPIOA,GPIO_Pin_8);		// ����Ϊ�ߵ�λ��LED�Ʋ���
			 GPIO_ResetBits(GPIOD,GPIO_Pin_2);	// ����Ϊ�͵�λ��LED����
			 delay_ms(1000);	
		 }
	   
		 // ���ٽ�����˸
		 // һ���쳣(waring)
		 for(i = 0;i < 4; i++){
			 GPIO_ResetBits(GPIOA,GPIO_Pin_8);	// ����Ϊ�͵�λ��LED����
			 GPIO_SetBits(GPIOD,GPIO_Pin_2);		// ����Ϊ�ߵ�λ��LED�Ʋ���
			 delay_ms(250);		
			 GPIO_SetBits(GPIOA,GPIO_Pin_8);		// ����Ϊ�ߵ�λ��LED�Ʋ���
			 GPIO_ResetBits(GPIOD,GPIO_Pin_2);	// ����Ϊ�͵�λ��LED����
			 delay_ms(250);	
		 }
		 
		 // ���٣�ͬʱ��˸��ͬʱϨ��
		 // ���ؾ���(error)
		 for(i = 0;i < 4; i++){
			 GPIO_ResetBits(GPIOA,GPIO_Pin_8);	// ����Ϊ�͵�λ��LED����
			 GPIO_ResetBits(GPIOD,GPIO_Pin_2);	// ����Ϊ�͵�λ��LED����
			 delay_ms(250);	
			 GPIO_SetBits(GPIOA,GPIO_Pin_8);		// ����Ϊ�ߵ�λ��LED�Ʋ���
			 GPIO_SetBits(GPIOD,GPIO_Pin_2);		// ����Ϊ�ߵ�λ��LED�Ʋ���
			 delay_ms(250);	
		 }
	}
 }
 