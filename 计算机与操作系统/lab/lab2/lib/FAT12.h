#include "util.h"

#ifndef FAT12_FAT12_H
#define FAT12_FAT12_H

typedef unsigned char u8;    // 1字节
typedef unsigned short u16;    // 2字节
typedef unsigned int u32;    // 4字节

#pragma pack(push)
// 设置当前内存对齐方式为1个字节
#pragma pack(1)

// FAT12 MBR 引导区 BPB 结构
// 需要注意的是从第 11 个字节开始才是 BPB，0 - 10 是跳转指令和厂商名字
// 总共 25 Bytes
struct BPB {
    u16 bytesPerSec; // 每扇区字节数
    u8 secPerClus;   // 每簇扇区数
    u16 rsvdSecCnt;  // Boot 占用的扇区数
    u8 numFats;      // FAT 表的记录数
    u16 rootEntCnt;  // 最大根目录文件数
    u16 totSec16;    // 逻辑扇区总数
    u8 media;        // 媒体描述符
    u16 FATSz16;     // 每个 FAT 占用的扇区数
    u16 secPerTrk;   // 每个磁道扇区数
    u16 numHeads;    // 磁头数
    u32 hiddSecs;    // 隐藏扇区数
    u32 totSec32;    // 如果 BPB_TolSec16 是0，则在这里记录扇区总数
};

// 根目录区由目录项构成，每一个目录项代表根目录中的一个文件索引
// 总共大小为 32 BYTE
struct RootEntry {
    char DIR_Name[11];  // 文件名，11个 Byte
    u8 DIR_Attr;        // 文件属性，1个 Byte
    char Reserve[10];   // 保留位，10个 Byte
    u16 DIR_WrtTime;    // 最后一次写入时间
    u16 DIR_WrtDate;    // 最后一次写入日期
    u16 DIR_FstClus;    // 文件开始的簇号
    u32 DIR_FileSize;   // 文件大小
};

// 恢复内存对齐方式为8个字节
#pragma pack(pop)

enum FileTypeEnum {
    PURE_FILE,
    DIR,
    HIDDEN_FILE
};

// 文件数据，链表存储
class Node {
public:
    std::string name;           // 文件名字
    std::vector<Node*> next;    // 链表指向的下一簇
    std::string path;           // 当前路径，便于输出
    u32 fileSize {};             // 文件大小
    FileTypeEnum type = FileTypeEnum::DIR;          // 文件类型
    bool isDot = false;       // 是否是.或者..
    int dirCount = 0;          // 记录当前目录下有多少目录，只有当 type == DIR 时才有效，否则为 0
    int fileCount = 0;          // 记录当前目录下有多少文件，只有当 type == DIR 时才有效，否则为 0
    std::string content;        // 内容
    ~Node();
};

// FAT12 类
class FAT12 {
public:
    explicit FAT12(FILE* fatImg);
    BPB getFatHeader() { return this->FATHeader; }
    Node* getRoot() { return this->root; }
    Node* findNodeByName(const std::string& fileName);
    Node* findNodeByPath(const std::string& pathName);
    ~FAT12();
private:
    void constructBPB(FILE* fatImg);
    void constructFileNode(FILE* fatImg);
    bool isValidName(const char* dirName);
    std::string getFileName(const char* dirName);
    void constructCurAndParentNode(Node* node);
    void getContent(FILE* fatImg, int startClus, Node* node);
    void getChildrenContent(FILE* fatImg, int startClus, Node* fatherNode);
    int getFATValue(FILE* fatImg, int clusNum);
    Node* findNodeByName(Node* node, const std::string& fileName);
    Node* findNodeByPath(Node* node, const std::string& pathName);
private:
    BPB FATHeader{};
    Node* root = new Node();
};


#endif //FAT12_FAT12_H
