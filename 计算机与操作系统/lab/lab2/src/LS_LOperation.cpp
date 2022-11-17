#include "../lib/LS_LOperation.h"

void LS_LOperation::doOperation() {
    // 首先判断参数是否合法
    std::string pathName;
    bool isParamValid = true;
    bool isFindFile = false;
    for (int i = 1; i < paramList.size(); i++) {
        std::string param = paramList[i];
        if (param.find('-') != std::string::npos) {
            // 判断是不是 -l 或者 -ll 之类的
            if (param[0] != '-') {
                isParamValid = false;
                break;
            } else {
                for (int j = 1; j < param.size(); j++) {
                    if (param[j] != 'l') {
                        isParamValid = false;
                        break;
                    }
                }
            }
        } else if (!isFindFile) {
            pathName = param;
            isFindFile = true;
        } else {
            print("ls 参数错误：不能有多个文件名\n");
            return;
        }
    }

    if (!isParamValid) {
        print("ls 参数错误！\n");
        return;
    }

    Node* node;
    if (isFindFile) {
        // 如果有路径名
        // 处理路径，比如 /NJU/./.. == /
        std::string preHandlePathName = *pathName.begin() == '/' ? pathName : '/' + pathName;
        preHandlePathName = *(preHandlePathName.end() - 1) == '/' ? preHandlePathName.substr(0, preHandlePathName.size() - 1) : preHandlePathName;
        pathName = handlePath(preHandlePathName);
        node = fat12.findNodeByPath(pathName);
        if (node == nullptr) {
            std::string output = "没有该路径: " + pathName + "\n";
            print(output.c_str());
            return;
        }
    } else {
        // 如果没有路径名
        node = fat12.getRoot();
    }

    // 判断是文件还是目录
    if (node->type == FileTypeEnum::PURE_FILE) {
        // 输出文件大小
        std::string output = node->path + " " + std::to_string(node->content.size()) + "\n";
        print(output.c_str());
    } else if (node->type == FileTypeEnum::DIR) {
        // 目录
        doLS_LOperation(node);
    }
}

void LS_LOperation::doLS_LOperation(Node* node) {
    std::string curPath = node->path;
    std::string output = curPath + " " + std::to_string(node->dirCount) + " " + std::to_string(node->fileCount) + ":\n";
    print(output.c_str());
    for (Node* child : node->next) {
        // 区分文件夹和文件
        if (child->type == FileTypeEnum::DIR) {
            output = "\033[31m" + child->name + "\033[0m" + " ";
            if (!child->isDot) {
                output += std::to_string(child->dirCount) + " " + std::to_string(child->fileCount);
            }
        } else if (child->type == FileTypeEnum::PURE_FILE) {
            output = child->name + "  " + std::to_string(child->fileSize);
        }
        print(output.c_str());
        print("\n");
    }
    print("\n");
    // 递归处理子目录
    for (Node* child : node->next) {
        if (child->type == FileTypeEnum::DIR && !child->isDot) {
            doLS_LOperation(child);
        }
    }
}
