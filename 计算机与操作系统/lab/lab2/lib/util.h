#include <iostream>
#include <string>
#include <vector>
#include <sstream>

#ifndef FAT12_UTIL_H
#define FAT12_UTIL_H

//extern "C" {
//    /**
//     * 输出，使用 nasm 汇编进行处理
//     *
//     * @param str: 要输出的字符串
//     * @param length: 输出的字符串长度
//     */
//    void print(const char* str);
//}
void print(std::string str);

int getTrimSize(const std::string& str);
void split(const std::string& str, char splitLetter, std::vector<std::string>& paramList);
std::string handlePath(const std::string& rawPath);

#endif //FAT12_UTIL_H
