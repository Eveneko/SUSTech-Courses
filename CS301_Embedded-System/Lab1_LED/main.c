#include "stm32f10x.h"
#include "sys.h"
#include "delay.h"

// 对应关系 ： PA8 —— led0      PD2 —— led1        IO状态为低时灯亮，为高时灭
int main()
{		 

	GPIO_InitTypeDef  GPIO_InitStructure;
	RCC_APB2PeriphClockCmd(RCC_APB2Periph_GPIOA|RCC_APB2Periph_GPIOD, ENABLE);	 //使能PA,PD端口时钟

	delay_init();	
	GPIO_InitStructure.GPIO_Pin = GPIO_Pin_8;				 //LED0-->PA.8 端口配置
	GPIO_InitStructure.GPIO_Mode = GPIO_Mode_Out_PP; 		 //推挽输出
	GPIO_InitStructure.GPIO_Speed = GPIO_Speed_50MHz;		 //IO口速度为50MHz
	GPIO_Init(GPIOA, &GPIO_InitStructure);					 //根据设定参数初始化GPIOA.8
	 
	GPIO_InitStructure.GPIO_Pin = GPIO_Pin_2;	
	GPIO_InitStructure.GPIO_Mode = GPIO_Mode_Out_PP; 		 //推挽输出
	GPIO_InitStructure.GPIO_Speed = GPIO_Speed_50MHz;		 //IO口速度为50MHz
  GPIO_Init(GPIOD, &GPIO_InitStructure);
	
	GPIO_SetBits(GPIOA,GPIO_Pin_8);	// 关闭Pin8
	GPIO_SetBits(GPIOD,GPIO_Pin_2); // 关闭pin2
	
  while(1)
	{  		 
		 int i;		 
		 // 正常速度交替闪烁
		 // 系统正常运行
		 for(i = 0;i < 4; i++){
			 GPIO_ResetBits(GPIOA,GPIO_Pin_8);	// 设置为低电位，LED灯亮
			 GPIO_SetBits(GPIOD,GPIO_Pin_2);		// 设置为高电位，LED灯不亮
			 delay_ms(1000);	
			 GPIO_SetBits(GPIOA,GPIO_Pin_8);		// 设置为高电位，LED灯不亮
			 GPIO_ResetBits(GPIOD,GPIO_Pin_2);	// 设置为低电位，LED灯亮
			 delay_ms(1000);	
		 }
	   
		 // 快速交替闪烁
		 // 一般异常(waring)
		 for(i = 0;i < 4; i++){
			 GPIO_ResetBits(GPIOA,GPIO_Pin_8);	// 设置为低电位，LED灯亮
			 GPIO_SetBits(GPIOD,GPIO_Pin_2);		// 设置为高电位，LED灯不亮
			 delay_ms(250);		
			 GPIO_SetBits(GPIOA,GPIO_Pin_8);		// 设置为高电位，LED灯不亮
			 GPIO_ResetBits(GPIOD,GPIO_Pin_2);	// 设置为低电位，LED灯亮
			 delay_ms(250);	
		 }
		 
		 // 快速，同时闪烁，同时熄灭
		 // 严重警告(error)
		 for(i = 0;i < 4; i++){
			 GPIO_ResetBits(GPIOA,GPIO_Pin_8);	// 设置为低电位，LED灯亮
			 GPIO_ResetBits(GPIOD,GPIO_Pin_2);	// 设置为低电位，LED灯亮
			 delay_ms(250);	
			 GPIO_SetBits(GPIOA,GPIO_Pin_8);		// 设置为高电位，LED灯不亮
			 GPIO_SetBits(GPIOD,GPIO_Pin_2);		// 设置为高电位，LED灯不亮
			 delay_ms(250);	
		 }
	}
 }
 