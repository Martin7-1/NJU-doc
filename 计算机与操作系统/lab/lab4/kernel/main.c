
/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
							main.c
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
													Forrest Yu, 2005
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

#include "type.h"
#include "const.h"
#include "protect.h"
#include "string.h"
#include "proc.h"
#include "tty.h"
#include "console.h"
#include "global.h"
#include "proto.h"

int strategy;

int sleep_time[5] = {
	1 * TIME_SLICE,
	1 * TIME_SLICE,
	1 * TIME_SLICE,
	1 * TIME_SLICE,
	1 * TIME_SLICE
};	/* 记录进程的休息时间 */

int work_time[5] = {
	2 * TIME_SLICE, 
	3 * TIME_SLICE,
	3 * TIME_SLICE,
	3 * TIME_SLICE,
	4 * TIME_SLICE
};	/* 记录进程工作的时间 */

PRIVATE void init_tasks()
{
	init_screen(tty_table);
	clean(console_table);

	// 表驱动，对应进程0, 1, 2, 3, 4, 5, 6
	int prior[7] = {1, 1, 1, 1, 1, 1, 1};
	for (int i = 0; i < 7; ++i)
	{
		proc_table[i].ticks = prior[i];
		proc_table[i].priority = prior[i];
	}

	// 初始化任务状态为休息
	for (int i = 0; i < 5; i++) {
		task_state[i] = 0;
	}

	// initialization
	k_reenter = 0;
	ticks = 0;
	readers_amount = 0;
	writers_amount = 0;

	strategy = 0; // 切换策略，0 是读写公平，1 是优先读，2 是优先写

	p_proc_ready = proc_table;
}
/*======================================================================*
							kernel_main
 *======================================================================*/
PUBLIC int kernel_main()
{
	disp_str("-----\"kernel_main\" begins-----\n");

	TASK* p_task = task_table;
	PROCESS* p_proc = proc_table;
	char* p_task_stack = task_stack + STACK_SIZE_TOTAL;
	u16 selector_ldt = SELECTOR_LDT_FIRST;
	int i;
	u8 privilege;
	u8 rpl;
	int eflags;
	for (i = 0; i < NR_TASKS + NR_PROCS; i++)
	{
		if (i < NR_TASKS)
		{ /* 任务 */
			p_task = task_table + i;
			privilege = PRIVILEGE_TASK;
			rpl = RPL_TASK;
			eflags = 0x1202; /* IF=1, IOPL=1, bit 2 is always 1 */
		}
		else
		{ /* 用户进程 */
			p_task = user_proc_table + (i - NR_TASKS);
			privilege = PRIVILEGE_USER;
			rpl = RPL_USER;
			eflags = 0x202; /* IF=1, bit 2 is always 1 */
			// 第一个是 ReaderB，index 为 0，task_state 和 user_proc_table 的 index 对应
			p_proc->index = i - NR_TASKS;
		}

		strcpy(p_proc->p_name, p_task->name); // name of the process
		p_proc->pid = i;					  // pid
		p_proc->sleep_time = 0;				  // 初始化结构体新增成员
		p_proc->isBlock = 0;

		p_proc->ldt_sel = selector_ldt;

		memcpy(&p_proc->ldts[0], &gdt[SELECTOR_KERNEL_CS >> 3],
			   sizeof(DESCRIPTOR));
		p_proc->ldts[0].attr1 = DA_C | privilege << 5;
		memcpy(&p_proc->ldts[1], &gdt[SELECTOR_KERNEL_DS >> 3],
			   sizeof(DESCRIPTOR));
		p_proc->ldts[1].attr1 = DA_DRW | privilege << 5;
		p_proc->regs.cs = (0 & SA_RPL_MASK & SA_TI_MASK) | SA_TIL | rpl;
		p_proc->regs.ds = (8 & SA_RPL_MASK & SA_TI_MASK) | SA_TIL | rpl;
		p_proc->regs.es = (8 & SA_RPL_MASK & SA_TI_MASK) | SA_TIL | rpl;
		p_proc->regs.fs = (8 & SA_RPL_MASK & SA_TI_MASK) | SA_TIL | rpl;
		p_proc->regs.ss = (8 & SA_RPL_MASK & SA_TI_MASK) | SA_TIL | rpl;
		p_proc->regs.gs = (SELECTOR_KERNEL_GS & SA_RPL_MASK) | rpl;

		p_proc->regs.eip = (u32)p_task->initial_eip;
		p_proc->regs.esp = (u32)p_task_stack;
		p_proc->regs.eflags = eflags;

		p_proc->nr_tty = 0;

		p_task_stack -= p_task->stacksize;
		p_proc++;
		p_task++;
		selector_ldt += 1 << 3;
	}

	init_tasks();
	init_clock();
	init_keyboard();
	restart();

	while (1)
	{
	}
}

PRIVATE read_proc(int index, int work_time)
{
	task_state[index] = 1;
	sleep_ms(work_time); // 读耗时
}

PRIVATE write_proc(int index, int work_time)
{
	task_state[index] = 1;
	sleep_ms(work_time); // 写耗时
}

// 读写公平方案
void read_eq(int index, int work_time)
{
	P(&w_mutex);
	P(&r_mutex);
	if (readers_amount == 0) {
		P(&rw_mutex); // 有读者正在使用，写者不可抢占
	}
	readers_amount++;
	V(&r_mutex);
	V(&w_mutex);

	read_proc(index, work_time);

	P(&r_mutex);
	readers_amount--;
	if (readers_amount == 0) {
		V(&rw_mutex); // 没有读者时，可以开始写了
	}
	V(&r_mutex);
}

void write_eq(int index, int work_time)
{
	P(&w_mutex);
	P(&rw_mutex);

	write_proc(index, work_time);

	V(&w_mutex);
	V(&rw_mutex);
}

// 读者优先
void reader_first_read(int index, int work_time)
{
	P(&r_mutex);		// 互斥访问 readers_amount 变量
	if (readers_amount == 0) {
		P(&rw_mutex);	// 有读者，此时写者不能够执行写操作
	}
	readers_amount++;			// 增加 readers_amount 数量
	V(&r_mutex);		// 释放互斥访问 readers_amount 变量

	read_proc(index, work_time);

	P(&r_mutex);		// 互斥访问 readers_amount 变量
	readers_amount--;			// 减少 readers_amount 数量
	if (readers_amount == 0) {
		V(&rw_mutex); 	// 没有读者，可以开始写了
	}
	V(&r_mutex);		// 释放互斥访问 readers_amount 变量
}

void reader_first_write(int index, int work_time)
{
	P(&rw_mutex);		// 互斥访问文件，即有写者的时候读者不能读，有写者的时候另一个写者也不能进入
	write_proc(index, work_time);
	V(&rw_mutex);		// 释放互斥访问文件
}

// 写者优先
void writer_first_read(int index, int work_time)
{
	P(&n_r_mutex);
	P(&queue);		// 保证队列中只有一个读进程，这样写进程在进入的时候才不需要跨过多个读进程
	P(&r_mutex);
	if (readers_amount == 0) {
		P(&rw_mutex);
	}
	readers_amount++;
	V(&r_mutex);
	V(&queue);
	V(&n_r_mutex);
	
	read_proc(index, work_time);

	P(&r_mutex);
	readers_amount--;
	if (readers_amount == 0) {
		V(&rw_mutex); // 没有读者，可以开始写了
	}
	V(&r_mutex);
}

void writer_first_write(int index, int work_time)
{
	P(&w_mutex);		// writers_amount 临界变量的访问控制
	if (writers_amount == 0) {
		P(&queue);		// 有写进程在队列中，此时不能够有读进程
	}
	writers_amount++;
	V(&w_mutex);

	P(&rw_mutex);
	write_proc(index, work_time);
	V(&rw_mutex);

	P(&w_mutex);
	writers_amount--;
	if (writers_amount == 0) {
		V(&queue);
	}
	V(&w_mutex);
}

read_f read_funcs[3] = {read_eq, reader_first_read, writer_first_read};
write_f write_funcs[3] = {write_eq, reader_first_write, writer_first_write};

void ReaderB()
{
	sleep_ms(TIME_SLICE);
	while (1)
	{
		read_funcs[strategy](0, work_time[0]);
		task_state[0] = 0;	// 设置为休息中
		sleep_ms(sleep_time[0]);
	}
}

void ReaderC()
{
	sleep_ms(2 * TIME_SLICE);
	while (1)
	{
		read_funcs[strategy](1, work_time[1]);
		task_state[1] = 0;
		sleep_ms(sleep_time[1]);
	}
}

void ReaderD()
{
	sleep_ms(3 * TIME_SLICE);
	while (1)
	{
		read_funcs[strategy](2, work_time[2]);
		task_state[2] = 0;
		sleep_ms(sleep_time[2]);
	}
}

void WriterE()
{
	sleep_ms(4 * TIME_SLICE);
	while (1)
	{
		write_funcs[strategy](3, work_time[3]);
		task_state[3] = 0;
		sleep_ms(sleep_time[3]);
	}
}

void WriterF()
{
	sleep_ms(5 * TIME_SLICE);
	while (1)
	{
		write_funcs[strategy](4, work_time[4]);
		task_state[4] = 0;
		sleep_ms(sleep_time[4]);
	}
}

void NormalA()
{
	sleep_ms(TIME_SLICE);
	int time = 1;
	while (1)
	{
		if (time <= 20) {
			if (time < 10) {
				printf("%c%c ", '\06', '0' + time);
			} else {
				printf("%c%c%c ", '\06', '0' + (time / 10), '0' + (time - (time / 10) * 10));
			}
			
			for (int i = 0; i < 5; i++) {
				int state = task_state[i];
				switch (state)
				{
				case 0:
					printf("%c%c ", '\03', 'Z');
					break;
				case 1:
					printf("%c%c ", '\02', 'O');
					break;
				case 2:
					printf("%c%c ", '\01', 'X');
					break;
				default:
					break;
				}
			}
			printf("\n");
			sleep_ms(TIME_SLICE);
			time++;
		}
	}
}