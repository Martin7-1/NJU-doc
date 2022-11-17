; 定义常用的系统调用, 用于 eax
%define SYS_EXIT 1
%define SYS_READ 3
%define SYS_WRITE 4

; 定义常用的文件描述符, 用于 ebx
%define STDIN 0
%define STDOUT 1

%define INF 42       ; 长度
%define MAX_LEN 21
%define MAX_MUL_LEN 41 ; 相乘的最长长度
%define MAX_ADD_LEN 21 ; 相加的最长长度

; 定义常用 ASCII 码
%define ZERO 48
%define ONE 49
%define NINE 57

section .data

prompt db 'please enter calculation(q to quit)', 0xA, 0xD  ; 0xA, 0xD \n\r
prompt_len equ $-prompt
invalid_prompt db 'Invalid', 0xA, 0xD
invalid_prompt_len equ $-invalid_prompt
line_seperator db 0xA, 0xD
line_seperator_len equ $-line_seperator

section .bss

; 输入的字符串
input resb INF
; 两个操作数的长度
number_one resb MAX_LEN
number_two resb MAX_LEN
res resb INF

section .text

global _start   ; nasm 编译，主程序入口

_start:
    ; 重置所有 bss 中的变量
    call reset_var
    ; 输出提示, 使用 eax 存放 msg，使用 ebx 存放 msg len
    mov eax, prompt
    mov ebx, prompt_len
    call print_str
    ; 读取输入
    xor eax, eax
    mov [input], eax      ; 读取输入前先清空之前的内容
    call read_input

    ; 如果等于 q 则推出循环循环
    ; 需要注意的是 input 会读取一个换行符
    mov eax, [input]
    cmp eax, 0xa71      ; 比较 0xa71，是因为换行 + q
    ; 退出程序
    je exit

    ; 获取运算符，此时运算符在 eax 中
    mov eax, input
    call get_operator
    mov ebx, input
    ; 获取两个操作数
    call get_operand
    mov ebx, eax    ; 暂存操作符
    call set_ptr        ; 设置要用到的指针
    mov eax, ebx

    ; 比较操作符是否为 +，如果是则进行加法，否则进行乘法。结果存储在 res 中
    cmp eax, '+'
    je .add_operand

    cmp eax, '*'
    je .mul_operand

; 将 number_one 和 number_two 相加，结果放在 res 中
.add_operand:
    call .do_add

    ; 此时 edx 指向了 res
    mov eax, edx
    call strlen
    mov ebx, eax
    mov eax, edx
    call print_str
    call println
    jmp _start      ; 回到主程序开始

.do_add:
    ; 循环加法
    ; 约定： cl 为 carry 位， al 为计算位
    ; 比较指针是否已经超过了某个操作数的最高位
    cmp edi, number_one
    jl .do_number_two_rest_add
    cmp esi, number_two
    jl .do_number_one_rest_add
    ; 上述是在为不同位数的加法的剩余处理
    xor eax, eax
    add al, BYTE[edi]   ; 第一个操作数
    sub al, ZERO        ; 减去字符串 0
    add al, BYTE[esi]   ; 加上第二个操作数
    add al, cl          ; 加上进位
    xor ecx, ecx        ; 重置 carry
    dec edi
    dec esi
    dec edx             ; 移动指针，向高位移动一位
    mov BYTE[edx], al
    cmp BYTE[edx], NINE        ; 比较结果和9，如果大于说明有进位
    jle .do_add          ; 如果 [al] < 9 则说明没有进位，继续循环
    sub BYTE[edx], 10   ; 如果 [al] > 9 则有进位，减去 10，设置进位即可
    mov ecx, 1          ; 设置进位，cl 就是 ecx 的低 8 位，所以直接将 ecx 设置为 1 即可
    jmp .do_add
.do_number_two_rest_add:
    cmp esi, number_two
    jl .do_after_add     ; 如果此时操作数一也已经执行完，那么就到加法后的步骤
    xor eax, eax
    add al, BYTE[esi]
    add al, cl
    xor ecx, ecx
    dec esi
    dec edx
    mov BYTE[edx], al
    cmp al, NINE
    jle .do_number_two_rest_add
    mov cl, 1
    sub BYTE[edx], 10
    jmp .do_number_two_rest_add
.do_number_one_rest_add:
    cmp edi, number_one
    jl .do_after_add
    xor eax, eax
    add al, BYTE[edi]
    add al, cl
    xor ecx, ecx
    dec edi
    dec edx
    mov BYTE[edx], al
    cmp al, NINE
    jle .do_number_one_rest_add
    mov cl, 1
    sub BYTE[edx], 10
    jmp .do_number_one_rest_add
.do_after_add:
    cmp ecx, 1      ; 比较是否有进位
    je .add_carry
    ret
.add_carry:
    dec edx
    mov BYTE[edx], ONE ; 有进位就加一
    ret

; 将 number_one 和 number_two 相乘，结果放在 res 中
.mul_operand:
    call .do_mul
    ; 变成字符串
    mov eax, edx
    call to_string

    ; 此时 edx 指向了 res
    mov eax, edx
    call strlen
    mov ebx, eax
    mov eax, edx
    call print_str
    call println

    jmp _start
.do_mul:
    mov bh, 0   ; 重置计数器
    ; 模拟
    ; operand two的最低位和 operand_one 相乘
    ; 双重循环: 外部：循环 operand two，内部：循环 operand one
    ; 用一个寄存器保存 edx 当前位置，每次 - 1
    cmp esi, number_two
    jl .do_after_mul
    ; ------ 将 edi 指向 number_one 的最低位 ------
    mov eax, number_one
    call strlen
    mov edi, eax
    sub edi, 1
    add edi, number_one
    ; ------------------ end --------------------
    .inner_loop:
        ; ah 记录 edx 移动的位数
        cmp edi, number_one
        jl .finish_inner_loop  ; 说明已经循环完了 number_two 的一位，可以跳到下一次的循环
        xor al, al
        add al, BYTE[esi]
        sub al, ZERO    ; 获得数字
        mov bl, BYTE[edi]
        sub bl, ZERO    ; 获得数字
        mul bl          ; mul by digit, ax = al * bl。因为这边顶多为 81，所以可以用一个 byte 来表示
        add al, cl      ; add carry
        xor ecx, ecx
        dec edi
        dec edx
        inc bh
        add BYTE[edx], al   ; 和之前的结果进行相加
        cmp BYTE[edx], 9
        jle .inner_loop
        call .carry_loop
        jmp .inner_loop
; ------------ 处理进位问题 ---------------
.carry_loop:
    cmp BYTE[edx], 9
    jle .inner_loop
    sub BYTE[edx], 10
    add cl, 1
    jmp .carry_loop
.finish_inner_loop:
    ; 检查是不是有进位，如果有进位则需要处理，此时是最高位的进位
    cmp ecx, 1
    jge .highest_carry
    dec esi
    cmp esi, number_two
    jge .move_res_ptr
    jmp .do_mul
.highest_carry:
    dec edx
    inc bh  ; 移动一位
    mov BYTE[edx], 0
    .highest_loop_carry:
        add BYTE[edx], 1
        dec ecx
        cmp ecx, 0
        jg .highest_loop_carry
    jmp .finish_inner_loop  ; 此时 ecx 已经为 0，所以可以直接跳回 finish_inner_loop
.move_res_ptr:
    inc edx
    dec bh
    cmp bh, 0
    jne .move_res_ptr
    dec edx
    jmp .do_mul

; 结束后处理进位
.do_after_mul:
    cmp ecx, 1
    jge .add_mul_carry
    ret
; 进位
.add_mul_carry:
    dec edx
    mov BYTE[edx], 0
    .loop_carry:
        add BYTE[edx], 1
        dec ecx
        cmp ecx, 0
        jg .loop_carry
    ret

;------------------------ 处理函数 -------------------------
; 输出一个字符串，使用 eax 存放要输出的 str，使用 ebx 存放要输出的 str 长度
print_str:
    push edx
    push ecx
    push ebx
    push eax
    mov ecx, eax
    mov edx, ebx
    mov eax, SYS_WRITE      ; call system number(sys_write)
    mov ebx, STDOUT         ; file descriptor(stdout)
    int 0x80                ; call kernal
    pop eax
    pop ebx
    pop ecx
    pop edx
    ret

println:
    push edx
    push ecx
    push ebx
    push eax
    mov ecx, line_seperator
    mov edx, line_seperator_len
    mov eax, SYS_WRITE      ; call system number(sys_write)
    mov ebx, STDOUT         ; file descriptor(stdout)
    int 0x80                ; call kernal
    pop eax
    pop ebx
    pop ecx
    pop edx
    ret

; 读取输入并存放在 ecx 中
read_input:
    mov ebx, STDIN          ; fild desciptor(stdin)
    mov eax, SYS_READ       ; call system number(sys_read)
    mov edx, INF            ; 读取的长度
    mov ecx, input
    int 0x80
    ret

exit:
    xor ebx, ebx
    mov eax, SYS_EXIT       ; call system number(sys_exit)
    int 0x80                ; call kernel
    ret

; 获取字符串长度，字符串存储在 eax 中
strlen:
    push ebx
    mov ebx, eax
.next:
    cmp BYTE[ebx], 0        ; 字符串结尾为 ASCII 0，如果为 0 则说明到达字符串结尾
    je .finish
    inc ebx                 ; 字符串长度++
    jmp .next               ; 循环
.finish:
    sub ebx, eax
    mov eax, ebx
    pop ebx
    ret

; 获取操作符，字符串存储在 eax 中，字符串长度存放在 ebx 中，运算符结果存放在 eax 中
get_operator:
    ; 遍历字符串，如果遇到 + 就为 +，如果遇到 * 就为 *
    push ecx
    push edx
    mov ecx, ebx    ; 长度放在 ecx 中
    mov edx, eax    ; 字符串放在 edx 中
str_loop:
    dec ecx
    cmp ecx, 0
    je .finish
    cmp BYTE[edx], '+'
    je .get_add
    cmp BYTE[edx], '*'
    je .get_mul
    ; 判断是否不在数字范围内
    ; 判断无效的操作符
    cmp BYTE[edx], ZERO
    jl .invalid_output
    cmp BYTE[edx], NINE
    jg .invalid_output
    inc edx
    jmp str_loop
.get_add:
    xor eax, eax
    mov eax, '+'
    jmp .finish
.get_mul:
    xor eax, eax
    mov eax, '*'
    jmp .finish
.invalid_output:
    mov eax, invalid_prompt
    mov ebx, invalid_prompt_len
    call print_str
    jmp _start
.finish:
    pop edx
    pop ecx
    ret

; 获取操作数，字符串存储在 ebx 中，结果存放在 number_one 和 number_two 中
get_operand:
    push ecx
    mov ecx, number_one   ; 用 ecx 存储 number_one 地址
.get_operand_one:
    ; 如果为+号或者*号，说明第一个操作数获取完毕
    cmp BYTE[ebx], '+'
    je .start_get_operand_two
    cmp BYTE[ebx], '*'
    je .start_get_operand_two
    ; 将当前地址的字符转移到 ecx 中
    mov dl, BYTE[ebx]
    mov BYTE[ecx], dl
    ; 地址增加
    inc ebx
    inc ecx
    jmp .get_operand_one
.start_get_operand_two:
    inc ebx     ; 此时 ebx 地址还在操作符，因此需要地址 + 1
    mov ecx, number_two
.get_operand_two:
    cmp BYTE[ebx], 10   ; 比较是否为换行符
    je .finish
    mov dl, BYTE[ebx]
    mov BYTE[ecx], dl
    inc ebx
    inc ecx
    jmp .get_operand_two
.finish:
    pop ecx
    ret

; 重置所有变量为 0
reset_var:
    push eax
    push ecx
    mov eax, number_one
    mov ecx, MAX_LEN
    call .do_reset
    mov eax, number_two
    mov ecx, MAX_LEN
    call .do_reset
    mov eax, res
    mov ecx, 255
    call .do_reset
    pop ecx
    pop eax
    ret
.do_reset:
    dec ecx
    cmp ecx, 0
    je .finish
    mov BYTE[eax], 0
    inc eax
    jmp .do_reset
.finish:
    ret

set_ptr:
    push eax
    ; 用 edi 和 esi 分别指向两个数的最低位
    mov eax, number_one
    call strlen
    mov edi, eax
    sub edi, 1
    add edi, number_one

    mov eax, number_two
    call strlen
    mov esi, eax
    sub esi, 1
    add esi, number_two
    
    ; edx 存放 res 的最后一个位置的指针
    mov edx, res
    add edx, INF
    xor ecx, ecx    ; 重置 carry 位
    pop eax
    ret

to_string:
    push edx
    mov edx, res
    add edx, INF
    to_string_loop:
        cmp eax, edx
        je to_string_finish
        add BYTE[eax], ZERO
        inc eax
        jmp to_string_loop
    to_string_finish:
        pop edx
        ret