#include "../lib/CatOperation.h"

void CatOperation::doOperation() {
    // 这里需要处理路径
    // 如果 pathName 不是 / 开头，就加一个 /
    std::string preHandlePathName = *pathName.begin() == '/' ? pathName : '/' + pathName;
    preHandlePathName = *(preHandlePathName.end() - 1) == '/' ? preHandlePathName.substr(0, preHandlePathName.size() - 1) : preHandlePathName;
    std::string truePathName = handlePath(preHandlePathName);
    Node* node = fat12.findNodeByPath(truePathName);
    if (node == nullptr) {
        print("找不到该名字对应的文件\n");
        return;
    }
    if (node->type != FileTypeEnum::PURE_FILE) {
        print("该名字对应的并不是文件！\n");
        return;
    }

    std::string output = node->content + "\n";
    print(output.c_str());
}
