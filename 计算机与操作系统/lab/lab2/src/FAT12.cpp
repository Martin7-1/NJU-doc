#include "../lib/FAT12.h"

FAT12::FAT12(FILE* fatImg) {
    // 设置 BPB
    constructBPB(fatImg);
    root->name = "";
    // 根路径
    root->path = "/";
    // 构造文件链表，通过对 RootEntry 的解析来获得
    constructFileNode(fatImg);
}

void FAT12::constructBPB(FILE* fatImg) {
    BPB *bpbPtr = &FATHeader;
    // 将文件定位到第 11 个字节，也是 BPB 开始的字节
    int isValid = std::fseek(fatImg, 11, SEEK_SET);
    if (isValid != 0) {
        print("将文件指针定位到第三个 Byte 错误!\n");
    }

    // 读取 25 个 item，每个 item 的大小为 1 Byte 到 FatHeder 中
    // fread 返回值为成功读取的元素个数，这里如果正常会返回 25
    int readByte = std::fread(bpbPtr, 1, 25, fatImg);
    if (readByte != 25) {
        print("从 FAT12 软盘读取 BPB 异常\n");
    }
}

void FAT12::constructFileNode(FILE* fatImg) {
    BPB fatHeader = getFatHeader();
    // 获得目录文件项（FAT表）初始的偏移量
    int bootSecNum = fatHeader.rsvdSecCnt;
    int FATSecNum = fatHeader.FATSz16 * fatHeader.numFats;
    int offset = (bootSecNum + FATSecNum) * fatHeader.bytesPerSec;
    std::string tempName;
    Node* fatherNode = root;

    // 依次处理 rootEntry
    for (int i = 0; i < fatHeader.rootEntCnt; i++) {
        RootEntry curEntry{};
        RootEntry* entryPtr = &curEntry;

        // 文件定位到 offset 字节，是 RootEntry 开始的字节
        int isValid = std::fseek(fatImg, offset, SEEK_SET);
        if (isValid != 0) {
            print("将文件指针定位到 RootEntry 开始位置错误\n");
        }

        // 读取内容到 RootEntry 中
        int readByte = std::fread(entryPtr, 1, 32, fatImg);
        if (readByte != 32) {
            std::string output = std::to_string(readByte) + ", 从 FAT12 软盘读取 RootEntry 异常，索引: " + std::to_string(i) + "\n";
            print(output.c_str());
            break;
        }
        // 指针偏移 32 Byte，因为一个 RootEntry 的大小是 32 Byte
        offset += 32;

        // 判断名字有效性
        if (entryPtr->DIR_Name[0] == '\0' || !isValidName(entryPtr->DIR_Name)) {
            continue;
        }

        // 根据 DIR_Attr 来判断文件类型，这里不考虑隐藏文件
        // 0x10 是目录，0x20 是普通文件
        tempName = getFileName(entryPtr->DIR_Name);
        Node* sonNode = new Node();
        fatherNode->next.push_back(sonNode);
        sonNode->name = tempName;
        sonNode->path = fatherNode->path + tempName + "/";
        if (entryPtr->DIR_Attr == 0x20) {
            // 是普通文件
            sonNode->fileSize = entryPtr->DIR_FileSize;
            sonNode->type = FileTypeEnum::PURE_FILE;
            // 增加文件数目
            fatherNode->fileCount++;
            getContent(fatImg, entryPtr->DIR_FstClus, sonNode);
        } else if (entryPtr->DIR_Attr == 0x10) {
            // 是目录
            sonNode->type = FileTypeEnum::DIR;
            // 增加父节点的目录数量
            fatherNode->dirCount++;
            // 添加 . 和 ..
            constructCurAndParentNode(sonNode);
            // 递归处理该目录的子文件/子目录
            getChildrenContent(fatImg, entryPtr->DIR_FstClus, sonNode);  //读取目录的内容, 递归处理
        }
    }
}

bool FAT12::isValidName(const char* dirName) {
    for (int i = 0; i < 11; i++) {
        char letter = dirName[i];
        // 非数字、字母、空格，为非法的文件名
        if (!((letter >= '0' && letter <= '9') || (letter >= 'a' && letter <= 'z') || (letter >= 'A' && letter <= 'Z') || letter == ' ')) {
            return false;
        }
    }

    return true;
}

std::string FAT12::getFileName(const char* dirName) {
    // 8 Byte 文件名，3 Byte 拓展名
    std::string name;
    // 用来标记是不是文件，如果是目录的话不需要 .
    bool isFile;

    for (int i = 0; i < 11; i++) {
        char letter = dirName[i];
        // 未满 8 Byte 的名字会在后面加上空格
        if (letter != ' ') {
            name += letter;
            if (i > 7) {
                isFile = true;
            }
        }

        // 第 8 个字节结束加上 . 以分割文件名和扩展名
        if (i == 7) {
            name += '.';
        }
    }

    if (!isFile) {
        name = name.substr(0, name.size() - 1);
    }
    return name;
}

void FAT12::constructCurAndParentNode(Node* node) {
    Node* curNode = new Node();
    curNode->name = ".";
    curNode->isDot = true;
    node->next.push_back(curNode);
    Node* parentNode = new Node();
    parentNode->name = "..";
    parentNode->isDot = true;
    node->next.push_back(parentNode);
}

void FAT12::getContent(FILE* fatImg, int startClus, Node* node) {
    BPB fatHeader = getFatHeader();
    // 获取簇大小
    int clusSize = fatHeader.bytesPerSec * fatHeader.secPerClus;
    // 获取偏移量，数据区前有：引导区 + FAT1 + FAT2 + 目录区
    int bootSecNum = fatHeader.rsvdSecCnt;
    int FATSecNum = fatHeader.FATSz16 * fatHeader.numFats;
    int rootEntrySecNum = (fatHeader.rootEntCnt * 32) / fatHeader.bytesPerSec;
    // 开始的字节
    int offset = fatHeader.bytesPerSec * (bootSecNum + FATSecNum + rootEntrySecNum);
    int curClus = startClus;

    // 逻辑：不断读取下一个簇的内容，下一个簇的 FAT 表项指向的就是下下一个簇，所有簇的数据加起来就是内容
    // FAT2 是 FAT1 的备份，需要注意的是 FAT 表的一个表项大小为 12bit = 1.5 Byte，也就是说对于奇偶号的表项处理方式有所不一样
    // FAT 表的第 0 项为 0xFF0，第 1 项为 0xFFF，是链表的结束指针
    while (curClus < 0xFF8) {
        if (curClus == 0xFF7) {
            // 0xFF7 代表是坏簇
            print("读取到坏簇，请检查 FAT12 软盘\n");
            return;
        }

        // 找到下一簇在数据区的地址
        // 需要注意的是簇的前两表项不能用，所以簇号需要 -2。
        int addr = offset + (curClus - 2) * clusSize;
        u8* content = new u8[clusSize];

        // 定位指针
        int isValid = std::fseek(fatImg, addr, SEEK_SET);
        if (isValid != 0) {
            print("定位到当前簇的位置异常\n");
            return;
        }

        // 读取数据
        int readBytes = std::fread(content, 1, clusSize, fatImg);
        if (readBytes != clusSize) {
            print("读取簇数据异常\n");
            return;
        }

        for (int i = 0; i < clusSize; i++) {
            if (content[i] == '\0') {
                break;
            }
            node->content += content[i];
        }

        curClus = getFATValue(fatImg, curClus);
        delete[] content;
    }
}

void FAT12::getChildrenContent(FILE* fatImg, int startClus, Node* fatherNode) {
    // 递归读取
    BPB fatHeader = getFatHeader();
    // 获取扇区大小
    int clusSize = fatHeader.bytesPerSec * fatHeader.secPerClus;
    // 获取偏移量，数据区前有：引导区 + FAT1 + FAT2 + 目录区
    int bootSecNum = fatHeader.rsvdSecCnt;
    int FATSecNum = fatHeader.FATSz16 * fatHeader.numFats;
    int rootEntrySecNum = (fatHeader.rootEntCnt * 32) / fatHeader.bytesPerSec;
    // 开始的字节
    int offset = fatHeader.bytesPerSec * (bootSecNum + FATSecNum + rootEntrySecNum);
    int curClus = startClus;

    while (curClus < 0xFF8) {
        if (curClus == 0xFF7) {
            // 0xFF7 代表是坏簇
            print("读取到坏簇，请检查 FAT12 软盘\n");
            return;
        }

        int addr = offset + (curClus - 2) * clusSize;
        int loopCount = 0;
        std::string tempName;
        while (loopCount < clusSize) {
            RootEntry curEntry{};
            RootEntry* entryPtr = &curEntry;

            // 文件定位到 offset 字节，是 RootEntry 开始的字节
            int isValid = std::fseek(fatImg, addr + loopCount, SEEK_SET);
            if (isValid != 0) {
                print("将文件指针定位到 RootEntry 开始位置错误\n");
            }

            // 读取内容到 RootEntry 中
            int readByte = std::fread(entryPtr, 1, 32, fatImg);
            if (readByte != 32) {
                std::string output = std::to_string(readByte) + ", 从 FAT12 软盘读取 RootEntry 异常\n";
                print(output.c_str());
                break;
            }
            // 指针偏移 32 Byte，因为一个 RootEntry 的大小是 32 Byte
            loopCount += 32;

            // 判断名字有效性
            if (!isValidName(entryPtr->DIR_Name)) {
                continue;
            }

            // 根据 DIR_Attr 来判断文件类型，这里不考虑隐藏文件
            // 0x10 是目录，0x20 是普通文件
            tempName = getFileName(entryPtr->DIR_Name);
            Node* sonNode = new Node();
            fatherNode->next.push_back(sonNode);
            sonNode->name = tempName;
            sonNode->path = fatherNode->path + tempName + "/";
            if (entryPtr->DIR_Attr == 0x20) {
                // 是普通文件
                sonNode->fileSize = entryPtr->DIR_FileSize;
                sonNode->type = FileTypeEnum::PURE_FILE;
                // 增加文件数目
                fatherNode->fileCount++;
                getContent(fatImg, entryPtr->DIR_FstClus, sonNode);
            } else if (entryPtr->DIR_Attr == 0x10) {
                // 是目录
                sonNode->type = FileTypeEnum::DIR;
                // 增加父节点的目录数量
                fatherNode->dirCount++;
                // 添加 . 和 ..
                constructCurAndParentNode(sonNode);
                // 递归处理该目录的子文件/子目录
                getChildrenContent(fatImg, entryPtr->DIR_FstClus, sonNode);  //读取目录的内容, 递归处理
            }
        }

        curClus = getFATValue(fatImg, curClus);
    }
}

int FAT12::getFATValue(FILE* fatImg, int clusNum) {
    // 首先获取 FAT1 的偏移
    BPB fatHeader = getFatHeader();
    int offset = fatHeader.rsvdSecCnt * fatHeader.bytesPerSec;
    // 一个 FAT 表项大小是 12 bit，1.5 Byte，对于奇偶处理不同
    // 对于奇数的 FAT 表，需要从偶数的最后一个表项开始读（即比如 3 号表项的开头 4 bit 和 2 号表项的结尾 4 bit 是属于一个 byte 的）
    int pos = offset + clusNum * 3 / 2;

    int isValid = std::fseek(fatImg, pos, SEEK_SET);
    if (isValid != 0) {
        print("移动指针到 FAT 表位置异常");
        return -1;
    }

    // 定义指向 FAT 表项的指针
    u16 fatValue = 0;
    u16* valuePtr = &fatValue;
    // 读取大小为 1 byte，总共 2 byte 的数据
    int readBytes = std::fread(valuePtr, 1, 2, fatImg);
    if (readBytes != 2) {
        print("读取 FAT 表项的 2 bytes 异常");
        return -1;
    }

    // 根据奇偶来进行划分
    // 注意 8086 为小端存储
    // 获得的 2 Bytes 的第一个 byte 是 高地址，第二个 byte 是低地址
    u16 ans;
    if (clusNum % 2 == 0) {
        // 偶数表项，FAT 表项的值为两个 byte 左移 4 bit 再 右移 4 bit
        u16 tempAns = fatValue << 4;
        ans = tempAns >> 4;
    } else {
        // 奇数表项，FAT 表项的值为两个 byte 右移 4 bit
        ans = fatValue >> 4;
    }

    return ans;
}

Node* FAT12::findNodeByName(const std::string& fileName) {
    // 递归方式查找
    return findNodeByName(root, fileName);
}

Node* FAT12::findNodeByPath(const std::string& pathName) {
    if (pathName == "/") {
        return root;
    }
    return findNodeByPath(root, pathName);
}

Node* FAT12::findNodeByName(Node* node, const std::string& fileName) {
    // 递归方式
    if (node->name == fileName) {
        return node;
    } else {
        for (Node* child : node->next) {
            if (!child->isDot) {
                Node* ans = findNodeByName(child, fileName);
                if (ans != nullptr) {
                    return ans;
                }
            }
        }
    }

    return nullptr;
}

Node *FAT12::findNodeByPath(Node* node, const std::string& pathName) {
    // 递归方式
    // 注意的是 path 存的是 /NJU/ 这种
    // 而 pathName 应该是 /NJU
    if (node->path.substr(0, node->path.size() - 1) == pathName) {
        return node;
    } else {
        for (Node* child : node->next) {
            if (!child->isDot) {
                Node* ans = findNodeByPath(child, pathName);
                if (ans != nullptr) {
                    return ans;
                }
            }
        }
    }

    return nullptr;
}

FAT12::~FAT12() {
    delete root;
}

Node::~Node() {
    for (Node* child : next) {
        delete child;
    }
}
