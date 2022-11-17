#include "Operation.h"

#include <utility>

#ifndef FAT12_LSOPERATION_H
#define FAT12_LSOPERATION_H


class LSOperation : public Operation {
public:
    explicit LSOperation(FAT12& fat12, std::string& pathName) : fat12(fat12), pathName(pathName) {}
    void doOperation() override;
    ~LSOperation() override = default;
private:
    void doLSOperation(Node* node);
private:
    FAT12& fat12;
    std::string pathName;
};


#endif //FAT12_LSOPERATION_H
