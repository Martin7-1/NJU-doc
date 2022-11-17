#include "../lib/util.h"

int getTrimSize(const std::string& str) {
    if (str.empty()) {
        return 0;
    }

    int startIndex = str.find_first_not_of(' ');
    int endIndex = str.find_last_not_of(' ');
    return endIndex - startIndex + 1;
}

void split(const std::string& str, char splitLetter, std::vector<std::string>& paramList) {
    std::stringstream ss(str);
    std::string item;

    while (std::getline(ss, item, splitLetter)) {
        paramList.push_back(item);
    }
}

std::string handlePath(const std::string& rawPath) {
    // 思路：
    // 遍历路径，非 . 的都加入到 truePath 中
    // 1. 如果是 . 则 index += 2（即比如 /./NJU 则 index 指向 . 的时候下一个循环就指向 N）
    // 2. 如果是 .. 则 index += 3，且 truePath 删除最后一个 / 和之后的内容
    std::string truePath;
    int index = 0;
    while (index < rawPath.size()) {
        if (rawPath[index] != '.') {
            if (*(truePath.end() - 1) == '/' && rawPath[index] == '/') {
                index++;
                continue;
            }
            truePath += rawPath[index];
        } else if (rawPath[index - 1] != '/') {
            // 判断是不是文件的 .
            // . 前面如果不是 / 说明是文件的点
            truePath += rawPath[index];
        } else {
            // 判断是 . 还是 ..
            bool isDoubleDot = false;
            if (index + 1 < rawPath.size() && rawPath[index + 1] == '.') {
                isDoubleDot = true;
            }
            if (isDoubleDot) {
                index += 1;
                // 根目录的多少个 .. 都是 /
                if (truePath != "/") {
                    // 找到倒数第二个 /
                    std::string tempPath = truePath.substr(0, truePath.size() - 1);
                    int length = tempPath.find_last_of('/');
                    truePath = truePath.substr(0, length);
                }
            } else {
                truePath = truePath.substr(0, truePath.size() - 1);
            }
            if (truePath.empty()) {
                truePath += '/';
            }
        }
        index++;
    }

    return truePath;
}

void print(std::string str) {
    std::cout << str;
}