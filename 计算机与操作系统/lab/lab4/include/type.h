
/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
							type.h
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
													Forrest Yu, 2005
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

#ifndef _ORANGES_TYPE_H_
#define _ORANGES_TYPE_H_

typedef unsigned int u32;
typedef unsigned short u16;
typedef unsigned char u8;

typedef char* va_list;

typedef void (*int_handler)();
typedef void (*task_f)();
typedef void (*irq_handler)(int irq);

typedef void (*read_f)(int, int);
typedef void (*write_f)(int, int);

typedef void* system_call;

#endif /* _ORANGES_TYPE_H_ */
