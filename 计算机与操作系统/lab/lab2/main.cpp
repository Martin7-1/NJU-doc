#include "lib/FAT12.h"
#include "lib/util.h"
#include "lib/Operation.h"
#include "lib/LSOperation.h"
#include "lib/LS_LOperation.h"
#include "lib/CatOperation.h"

const std::string FILE_NAME = "/Users/zhengyi/Downloads/a2.img";
// const std::string FILE_NAME = "/root/a.img";

int main() {
    // 读取 FAT12 img 文件
    FILE* fatImg = std::fopen(FILE_NAME.c_str(), "rb");

    if (fatImg == nullptr) {
        print("找不到 a.img\n");
        return 0;
    }

    // 构造 FAT12 对象
    FAT12 fat12(fatImg);
    std::string input;

    // 读取输入
    print("> ");
    std::getline(std::cin, input);

    while (input != "exit") {
        Operation* operation = Operation::constructOperation(input, fat12);
        if (operation == nullptr) {
            print("不支持的指令，请在 ls/ls -l/cat 中选择输入\n");
            print("> ");
            std::getline(std::cin, input);
            continue;
        }
        operation->doOperation();
        delete operation;
        print("> ");
        std::getline(std::cin, input);
    }

    std::fclose(fatImg);
    return 0;
}