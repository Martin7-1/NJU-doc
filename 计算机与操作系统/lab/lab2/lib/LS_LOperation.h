#include "util.h"
#include "FAT12.h"
#include "Operation.h"

#ifndef FAT12_LS_LOPERATION_H
#define FAT12_LS_LOPERATION_H


class LS_LOperation : public Operation {
public:
    LS_LOperation(FAT12& fat12, std::vector<std::string>& paramList) : fat12(fat12), paramList(paramList) {}
    void doOperation() override;
    void doLS_LOperation(Node* node);
    ~LS_LOperation() override = default;
private:
    FAT12& fat12;
    // 存储参数列表
    std::vector<std::string> paramList;
};


#endif //FAT12_LS_LOPERATION_H
