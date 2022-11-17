#include "../lib/Operation.h"
#include "../lib/LSOperation.h"
#include "../lib/LS_LOperation.h"
#include "../lib/CatOperation.h"

Operation* Operation::constructOperation(std::string& input, FAT12& fat12) {
    std::vector<std::string> paramList;
    split(input, ' ', paramList);
    if (input.substr(0, 2) == "ls") {
        if (paramList.size() == 1) {
            // 如果只有一个 ls，那么肯定是 ls
            std::string defaultParam = "root";
            return new LSOperation(fat12, defaultParam);
        } else if (paramList.size() == 2 && *paramList[1].begin() != '-') {
            // 说明是 ls PATH
            return new LSOperation(fat12, paramList[1]);
        }
        // ls - l
        return new LS_LOperation(fat12, paramList);
    } else if (input.substr(0, 3) == "cat") {
        // 判断是否有参数
        if (getTrimSize(input) == 3) {
            print("cat 指令需要携带非空参数\n");
            return nullptr;
        }
        if (paramList.size() != 2) {
            print("cat 指令只能够携带一个参数\n");
        }
        // 获得文件名
        std::string fileName = paramList[1];
        return new CatOperation(fat12, fileName);
    }

    return nullptr;
}
