#include "util.h"
#include "FAT12.h"
#include "Operation.h"

#ifndef FAT12_CATOPERATION_H
#define FAT12_CATOPERATION_H


class CatOperation : public Operation {
public:
    CatOperation(FAT12& fat12, std::string& pathName) : fat12(fat12), pathName(pathName) {}
    void doOperation() override;
    ~CatOperation() override = default;
private:
    FAT12& fat12;
    // 存储要查找的路径名
    std::string pathName;
};


#endif //FAT12_CATOPERATION_H
