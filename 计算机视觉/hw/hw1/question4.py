import cv2


def replace_char(old_string, char, index):
    old_string = str(old_string)
    # 新的字符串 = 老字符串[:要替换的索引位置] + 替换成的目标字符 + 老字符串[要替换的索引位置+1:]
    new_string = old_string[:index] + char + old_string[index + 1:]
    return new_string


if __name__ == '__main__':
    img_from = cv2.imread('./hw01-I2.jpg', -1)

    size = img_from.shape
    # 8 bit，从低到高替换
    for bit in range(0, 8):
        # 读取的是 BGR 格式的
        # 这里我们将 R 通道的做变换
        img_to = cv2.imread('./hw01-I0.jpeg')
        print(img_to)
        for i in range(0, size[1]):
            for j in range(0, size[0]):
                to = img_to[j][i][2]
                to_binary = "{0:b}".format(to).rjust(8, '0')
                base = img_from[j][i]
                base_binary = "{0:b}".format(base).rjust(8, '0')
                res_binary = replace_char(to_binary, base_binary[7 - bit], 7 - bit)
                img_to[j][i][2] = int(res_binary, 2)
                print('to: ' + str(to) + ", to_binary: " + to_binary + ", base: " + str(base) + ", base_binary: " +
                      base_binary + ", res: " + str(img_to[j][i][2]) + ", res_binary: " + res_binary)

        file_name = './hw01-I' + str(bit + 11) + ".jpeg"
        cv2.imwrite(file_name, img_to)
