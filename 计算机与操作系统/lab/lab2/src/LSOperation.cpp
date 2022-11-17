#include "../lib/LSOperation.h"

void LSOperation::doOperation() {
    if (pathName == "root") {
        doLSOperation(fat12.getRoot());
    } else {
        std::string preHandlePathName = *pathName.begin() == '/' ? pathName : '/' + pathName;
        preHandlePathName = *(preHandlePathName.end() - 1) == '/' ? preHandlePathName.substr(0, preHandlePathName.size() - 1) : preHandlePathName;
        std::string truePathName = handlePath(preHandlePathName);
        Node* node = fat12.findNodeByPath(truePathName);
        if (node == nullptr) {
            std::string output = "没有该路径: " + pathName + "\n";
            print(output.c_str());
            return;
        }
        doLSOperation(node);
    }
}

void LSOperation::doLSOperation(Node* node) {
    std::string output = node->path + ":\n";
    print(output.c_str());
    for (Node* child : node->next) {
        // 区分文件夹和文件
        if (child->type == FileTypeEnum::DIR) {
            output = "\033[31m" + child->name + "\033[0m" + "  ";
        } else if (child->type == FileTypeEnum::PURE_FILE) {
            output = child->name + "  ";
        }
        print(output.c_str());
    }
    print("\n");
    // 递归处理子目录
    for (Node* child : node->next) {
        if (child->type == FileTypeEnum::DIR && !child->isDot) {
            doLSOperation(child);
        }
    }
}
