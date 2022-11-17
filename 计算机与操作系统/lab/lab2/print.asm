section .text

global print
print:
    push ebp        ; 保存栈帧
    mov ebp, esp    ; 栈顶元素
    mov ecx, [ebp+8]
    mov ebx, ecx
    call strlen
    mov edx, ebx
    mov ebx, 1      ; file descriptor(stdout)
    mov eax, 4      ; call system number(SYS_WRITE)
    int 80h         ; call kernel
    pop ebp
    ret

; 获取字符串长度，字符串存储在 ebx 中，返回值存放在 ebx 中
strlen:
    push ecx
    mov ecx, ebx
.next:
    cmp BYTE[ecx], 0        ; 字符串结尾为 ASCII 0，如果为 0 则说明到达字符串结尾
    je .finish
    inc ecx                 ; 字符串长度++
    jmp .next               ; 循环
.finish:
    sub ecx, ebx
    mov ebx, ecx
    pop ecx
    ret