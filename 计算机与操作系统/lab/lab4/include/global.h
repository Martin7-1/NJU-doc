
/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
							global.h
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
													Forrest Yu, 2005
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

/* EXTERN is defined as extern except in global.c */
#ifdef GLOBAL_VARIABLES_HERE
#undef EXTERN
#define EXTERN
#endif

EXTERN int ticks;
EXTERN int task_state[5]; /* 记录读写进程的状态，0 代表休息, 1 代表正在读写，2 代表等待读写 */
EXTERN int readers_amount;
EXTERN int writers_amount;

EXTERN int disp_pos;
EXTERN u8 gdt_ptr[6]; // 0~15:Limit  16~47:Base
EXTERN DESCRIPTOR gdt[GDT_SIZE];
EXTERN u8 idt_ptr[6]; // 0~15:Limit  16~47:Base
EXTERN GATE idt[IDT_SIZE];

EXTERN u32 k_reenter;

EXTERN TSS tss;
EXTERN PROCESS *p_proc_ready;

EXTERN int nr_current_console;

extern PROCESS proc_table[];
extern char task_stack[];
extern TASK task_table[];
extern TASK user_proc_table[];
extern irq_handler irq_table[];
extern TTY tty_table[];
extern CONSOLE console_table[];

extern SEMAPHORE rw_mutex;	// 互斥访问共享文件，即有读者的时候写者不可入，有写者的时候读者不可入
extern SEMAPHORE w_mutex;	// 写进程的信号量
extern SEMAPHORE r_mutex;	// 互斥访问 readers_amount 变量的信号量
extern SEMAPHORE queue;		// 控制读写公平
extern SEMAPHORE n_r_mutex;	// 控制同时只能有几个读者
