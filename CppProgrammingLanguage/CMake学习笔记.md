# CMake学习笔记

> 参考[CMake教程（一） - 知乎 (zhihu.com)](https://zhuanlan.zhihu.com/p/119426899)

## 一 基础

> 源代码
>
> ```cpp
> #include <iostream>
> #include <cstdlib>
> #include <cmath>
> 
> int main(int argc, char* argv[]) {
>     if (argc < 2) {
>         fprintf(stdout, "Usage: %s number\n", argv[0]);
>         return 1;
>     }
>     
>     double inputValue = atof(argv[1]);
>     double outputValue = sqrt(inputValue);
>     
>     fprintf(stdout, "The square root of %g is %g\n", inputValue, outputValue);
>     
>     return 0;
> }
> ```
>
> 

创建`CMakeLists.txt`文件，并在文件中指定如下几项：

1. CMake版本、工程名、构建目标app的源文件

```cmake
cmake_minimum_required(VERSION 3.10)

# set the project name
project(CalculateSqrt)

# add the executable
add_executable(CalculateSqrt hello.cxx)
```



## 二 为项目添加版本号和可配置的头文件

​	虽然可以在源码中指定具体的版本，但是通过`CmakeLists.txt`来指定项目的版本号则更加的灵活。

​	在`CMakeLists.txt`中首先指定project的版本号，然后添加可配置的同文件，指定`c++`版本。

```cmake
cmake_minimum_required(VERSION 3.10) 

# 设定工程名和版本号 
project(CalculateSqrt VERSION 1.0) 

# configure_file的作用将一份文件拷贝到另一个位置并修改它的内容，使得在代码中使用CMake中定义的变量 
# configure_file官方文档：https://cmake.org/cmake/help/latest/command/configure_file.html 
configure_file(CalculateSqrtConfig.h.in CalculateSqrtConfig.h) 

# specify the C++ standard 
set(CMAKE_CXX_STANDARD 11) 
set(CMAKE_CXX_STANDARD_REQUIRED True) 

# add the executable 
add_executable(CalculateSqrt calculate.cpp) 

# 指定项目编译的时候需要include的文件路径，PROJECT_BINARY_DIR变量为编译发生的目录，也就是make执行的目录，PROJECT_SOURCE_DIR为工程所在的目录 
# target_include_directories官方文档：https://cmake.org/cmake/help/v3.3/command/target_include_directories.html 
target_include_directories(CalculateSqrt PUBLIC 
                           "${PROJECT_BINARY_DIR}" 
                           ) 
```

