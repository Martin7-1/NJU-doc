
/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
							global.c
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
													Forrest Yu, 2005
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

#define GLOBAL_VARIABLES_HERE

#include "type.h"
#include "const.h"
#include "protect.h"
#include "tty.h"
#include "console.h"
#include "proc.h"
#include "global.h"
#include "proto.h"

PUBLIC PROCESS proc_table[NR_TASKS + NR_PROCS];

PUBLIC TASK task_table[NR_TASKS] = {
	{task_tty, STACK_SIZE_TTY, "tty"}};

PUBLIC TASK user_proc_table[NR_PROCS] = {
	{ReaderB, STACK_SIZE_READERB, "ReaderB"},
	{ReaderC, STACK_SIZE_READERC, "ReaderC"},
	{ReaderD, STACK_SIZE_READERD, "ReaderD"},
	{WriterE, STACK_SIZE_WRITERE, "WriterE"},
	{WriterF, STACK_SIZE_WRITERF, "WriterF"},
	{NormalA, STACK_SIZE_NORMALA, "NormalA"}};

PUBLIC char task_stack[STACK_SIZE_TOTAL];

PUBLIC TTY tty_table[NR_CONSOLES];
PUBLIC CONSOLE console_table[NR_CONSOLES];

PUBLIC irq_handler irq_table[NR_IRQ];

PUBLIC system_call sys_call_table[NR_SYS_CALL] = {
	sys_get_ticks,
	sys_write_str,
	sys_sleep,
	p_process,
	v_process
};

PUBLIC SEMAPHORE rw_mutex = {1, 0, 0};		// 用来保证读写互斥和写写互斥
PUBLIC SEMAPHORE w_mutex = {1, 0, 0};		// 用来控制 writers_amount 这个临界变量，在读写公平中用来保证有写进程的时候阻塞读进程
PUBLIC SEMAPHORE r_mutex = {1, 0, 0};		// 用来控制 readers_amount 这个临界变量
PUBLIC SEMAPHORE queue = {1, 0, 0};			// 在r上不允许建造长队列，否则写进程将不能跳过这个队列，因此，只允许一个读进程在 n_r_mutex 上排队，而所有其他读进程在等待 n_r_mutex 之前，在信号量 queue 上排队。
PUBLIC SEMAPHORE n_r_mutex = {MAX_READERS, 0, 0};