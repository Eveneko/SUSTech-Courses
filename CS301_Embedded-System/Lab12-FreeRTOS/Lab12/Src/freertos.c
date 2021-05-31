/* USER CODE BEGIN Header */
/**
 ******************************************************************************
 * File Name          : freertos.c
 * Description        : Code for freertos applications
 ******************************************************************************
 * @attention
 *
 * <h2><center>&copy; Copyright (c) 2019 STMicroelectronics.
 * All rights reserved.</center></h2>
 *
 * This software component is licensed by ST under Ultimate Liberty license
 * SLA0044, the "License"; You may not use this file except in compliance with
 * the License. You may obtain a copy of the License at:
 *                             www.st.com/SLA0044
 *
 ******************************************************************************
 */
/* USER CODE END Header */

/* Includes ------------------------------------------------------------------*/
#include "FreeRTOS.h"
#include "task.h"
#include "main.h"
#include "cmsis_os.h"
#include "usart.h"

/* Private includes ----------------------------------------------------------*/
/* USER CODE BEGIN Includes */

/* USER CODE END Includes */

/* Private typedef -----------------------------------------------------------*/
/* USER CODE BEGIN PTD */

/* USER CODE END PTD */

/* Private define ------------------------------------------------------------*/
/* USER CODE BEGIN PD */

/* USER CODE END PD */

/* Private macro -------------------------------------------------------------*/
/* USER CODE BEGIN PM */

/* USER CODE END PM */

/* Private variables ---------------------------------------------------------*/
/* USER CODE BEGIN Variables */

/* USER CODE END Variables */
osThreadId MailProducerHandle;
osThreadId MailConsimerHandle;

/* Private function prototypes -----------------------------------------------*/
/* USER CODE BEGIN FunctionPrototypes */

/* USER CODE END FunctionPrototypes */

void MailProducerTask(void const * argument);
void MailConsumerTask(void const * argument);

void MX_FREERTOS_Init(void); /* (MISRA C 2004 rule 8.1) */

/**
 * @brief  FreeRTOS initialization
 * @param  None
 * @retval None
 */
typedef struct {
	uint16_t var;
} mailStruct;
osMailQId mail01Handle;

void MX_FREERTOS_Init(void) {
	/* USER CODE BEGIN Init */

	/* USER CODE END Init */

	/* USER CODE BEGIN RTOS_MUTEX */
	/* add mutexes, ... */
	/* USER CODE END RTOS_MUTEX */

	/* USER CODE BEGIN RTOS_SEMAPHORES */
	/* add semaphores, ... */
	/* USER CODE END RTOS_SEMAPHORES */

	/* USER CODE BEGIN RTOS_TIMERS */
	/* start timers, add new ones, ... */
	/* USER CODE END RTOS_TIMERS */

	/* USER CODE BEGIN RTOS_QUEUES */
	/* add queues, ... */
	osMailQDef(mail01, 4, mailStruct);
	mail01Handle = osMailCreate(osMailQ(mail01), NULL);
	/* USER CODE END RTOS_QUEUES */

	/* Create the thread(s) */
	/* definition and creation of MailProducer */
	osThreadDef(MailProducer, MailProducerTask, osPriorityNormal, 0, 128);
	MailProducerHandle = osThreadCreate(osThread(MailProducer), NULL);

	/* definition and creation of MailConsimer */
	osThreadDef(MailConsimer, MailConsumerTask, osPriorityNormal, 0, 128);
	MailConsimerHandle = osThreadCreate(osThread(MailConsimer), NULL);

	/* USER CODE BEGIN RTOS_THREADS */
	/* add threads, ... */
	/* USER CODE END RTOS_THREADS */

}

/* USER CODE BEGIN Header_MailProducerTask */
/**
 * @brief  Function implementing the MailProducer thread.
 * @param  argument: Not used
 * @retval None
 */
/* USER CODE END Header_MailProducerTask */
void MailProducerTask(void const * argument) {

	/* USER CODE BEGIN MailProducerTask */
	mailStruct * mail;
	/* Infinite loop */
	int i=1;
	for (;;) {
		mail = (mailStruct *)osMailAlloc(mail01Handle, osWaitForever);
				mail->var = i;
				if(osMailPut(mail01Handle, mail) == osErrorOS) {    //放入一个mail
					char msg[25];
			  		sprintf(msg, "Producer produce fail  %d\r\n",i );
			  		HAL_UART_Transmit(&huart1, (uint8_t*)msg, strlen(msg), HAL_MAX_DELAY);
			  	 }else {
			  		char msg[25];
			  		sprintf(msg, "Producer produce success  %d\r\n",i );
			  		HAL_UART_Transmit(&huart1, (uint8_t*)msg, strlen(msg), HAL_MAX_DELAY);
			  		i++;
			  	 }
				osDelay(1000);
	}
	/* USER CODE END MailProducerTask */
}

void MailConsumerTask(void const * argument) {
	/* USER CODE BEGIN MailConsumerTask */

	 osEvent event;
	 mailStruct * pMail;
	 char msg[25];
	for(;;) {osDelay(3000);
		  	  event = osMailGet(mail01Handle, osWaitForever);  //get 一个 event
		  	  if (event.status == osEventMail)
		  	  {
		  		  pMail = event.value.p;  //获取value 并输出
		  		  sprintf(msg, "Consumer consume: %d\r\n", pMail->var);
		  		  HAL_UART_Transmit(&huart1, (uint8_t*)msg, strlen(msg), HAL_MAX_DELAY);
		  		  osMailFree(mail01Handle, pMail);  //释放该Mail
		  	  }
	}

	/* USER CODE END MailConsumerTask */
}

/* Private application code --------------------------------------------------*/
/* USER CODE BEGIN Application */

/* USER CODE END Application */

/************************ (C) COPYRIGHT STMicroelectronics *****END OF FILE****/