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
osThreadId HandleHandle;
osThreadId PeriodicHandle;
osThreadId ProcuderHandle;
osThreadId ConsumerHandle;
osSemaphoreId bSem01Handle;
osSemaphoreId bSemEmptyHandle;
osSemaphoreId bSemFilledHandle;

/* Private function prototypes -----------------------------------------------*/
/* USER CODE BEGIN FunctionPrototypes */
   
/* USER CODE END FunctionPrototypes */

void HandleTask(void const * argument);
void PeriodicTask(void const * argument);
void FuncProcuder(void const * argument);
void FuncConsumer(void const * argument);
UART_HandleTypeDef huart1;
void MX_FREERTOS_Init(void); /* (MISRA C 2004 rule 8.1) */
char msg[35];
/**
  * @brief  FreeRTOS initialization
  * @param  None
  * @retval None
  */
void MX_FREERTOS_Init(void) {
  /* USER CODE BEGIN Init */
       
  /* USER CODE END Init */

  /* USER CODE BEGIN RTOS_MUTEX */
  /* add mutexes, ... */
  /* USER CODE END RTOS_MUTEX */

  /* Create the semaphores(s) */
  /* definition and creation of bSem01 */
  osSemaphoreDef(bSem01);
  bSem01Handle = osSemaphoreCreate(osSemaphore(bSem01), 1);

  /* definition and creation of bSemEmpty */
  osSemaphoreDef(bSemEmpty);
  bSemEmptyHandle = osSemaphoreCreate(osSemaphore(bSemEmpty), 4);

  /* definition and creation of bSemFilled */
  osSemaphoreDef(bSemFilled);
  bSemFilledHandle = osSemaphoreCreate(osSemaphore(bSemFilled), 4);

  /* USER CODE BEGIN RTOS_SEMAPHORES */
  /* add semaphores, ... */
  /* USER CODE END RTOS_SEMAPHORES */

  /* USER CODE BEGIN RTOS_TIMERS */
  /* start timers, add new ones, ... */
  /* USER CODE END RTOS_TIMERS */

  /* USER CODE BEGIN RTOS_QUEUES */
  /* add queues, ... */
  /* USER CODE END RTOS_QUEUES */

  /* Create the thread(s) */
  /* definition and creation of Handle */
  osThreadDef(Handle, HandleTask, osPriorityNormal, 0, 128);
  HandleHandle = osThreadCreate(osThread(Handle), NULL);

  /* definition and creation of Periodic */
  osThreadDef(Periodic, PeriodicTask, osPriorityNormal, 0, 128);
  PeriodicHandle = osThreadCreate(osThread(Periodic), NULL);

  /* definition and creation of Procuder */
  osThreadDef(Procuder, FuncProcuder, osPriorityNormal, 0, 128);
  ProcuderHandle = osThreadCreate(osThread(Procuder), NULL);

  /* definition and creation of Consumer */
  osThreadDef(Consumer, FuncConsumer, osPriorityBelowNormal, 0, 128);
  ConsumerHandle = osThreadCreate(osThread(Consumer), NULL);

  /* USER CODE BEGIN RTOS_THREADS */
  /* add threads, ... */
  /* USER CODE END RTOS_THREADS */

}

/* USER CODE BEGIN Header_HandleTask */
/**
  * @brief  Function implementing the Handle thread.
  * @param  argument: Not used 
  * @retval None
  */
/* USER CODE END Header_HandleTask */
void HandleTask(void const * argument)
{
    
    
    
    
    

  /* USER CODE BEGIN HandleTask */
  /* Infinite loop */
  for(;;)
  {
	  osSemaphoreWait(bSem01Handle, osWaitForever);
	  HAL_GPIO_TogglePin(LED0_GPIO_Port, LED0_Pin);
	  HAL_GPIO_TogglePin(LED1_GPIO_Port, LED1_Pin);
  }
  /* USER CODE END HandleTask */
}

/* USER CODE BEGIN Header_PeriodicTask */
/**
* @brief Function implementing the Periodic thread.
* @param argument: Not used
* @retval None
*/
/* USER CODE END Header_PeriodicTask */
void PeriodicTask(void const * argument)
{
  /* USER CODE BEGIN PeriodicTask */
  /* Infinite loop */
  for(;;)
  {
	  osDelay(1000);
	  osSemaphoreRelease(bSem01Handle);
  }
  /* USER CODE END PeriodicTask */
}

/* USER CODE BEGIN Header_FuncProcuder */
/**
* @brief Function implementing the Procuder thread.
* @param argument: Not used
* @retval None
*/
/* USER CODE END Header_FuncProcuder */
void FuncProcuder(void const * argument)
{
  /* USER CODE BEGIN FuncProcuder */
  /* Infinite loop */
  for(;;)
  {
	  osSemaphoreWait(bSemEmptyHandle, osWaitForever);   //等待bSemEmptyHandle 信息
	  sprintf(msg, "Producer produce data\r\n");
	  HAL_UART_Transmit(&huart1, (uint8_t*)msg, strlen(msg), HAL_MAX_DELAY);
	  HAL_Delay(500);
	  osSemaphoreRelease(bSemFilledHandle);  // 释放bSemFilledHandle信息
	  osDelay(100);
  }
  /* USER CODE END FuncProcuder */
}

/* USER CODE BEGIN Header_FuncConsumer */
/**
* @brief Function implementing the Consumer thread.
* @param argument: Not used
* @retval None
*/
/* USER CODE END Header_FuncConsumer */
void FuncConsumer(void const * argument)
{
  /* USER CODE BEGIN FuncConsumer */
  /* Infinite loop */
  for(;;)
  {
	  osSemaphoreWait(bSemFilledHandle, osWaitForever);//等待bSemFilledHandle 信息
	  sprintf(msg, "Consumer consume data\r\n");
	  HAL_UART_Transmit(&huart1, (uint8_t*)msg, strlen(msg), HAL_MAX_DELAY);
	  HAL_Delay(500);
	  osSemaphoreRelease(bSemEmptyHandle);// 释放bSemEmptyHandle 信息
	  osDelay(200);
  }
  /* USER CODE END FuncConsumer */
}

/* Private application code --------------------------------------------------*/
/* USER CODE BEGIN Application */
     
/* USER CODE END Application */

/************************ (C) COPYRIGHT STMicroelectronics *****END OF FILE****/
