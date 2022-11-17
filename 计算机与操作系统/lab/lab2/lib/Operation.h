#include "util.h"
#include "FAT12.h"

#ifndef FAT12_OPERATION_H
#define FAT12_OPERATION_H


class Operation {
public:
    static Operation* constructOperation(std::string& input, FAT12& fat12);
    virtual void doOperation() = 0;
    virtual ~Operation() = default;
};


#endif //FAT12_OPERATION_H
