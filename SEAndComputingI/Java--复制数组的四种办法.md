# Java--复制数组的四种办法

## Arrays.copyOf()

​	**Arrays.copyOf(dataType[] array, int length)**

​	即将数组复制给一个指定长度的新数组，如果length > array.length，则余下的位置会以该类型的默认值来填充，如果length < array.length，则会对array进行截断。

​	**Remark: **该方法会重构目标数组



## Arrays.copyOfRange()

​	**Arrays.copyOfRange(dataType[] array, int startLength, int endLength)**

​	注意这个索引为左开右闭，length > array.length的处理办法同第一种。

​	**Remark: **该方法同样会重构目标数组



## System.arraycopy()

​	**System.arraycopy(srcArray, srcArrayStartLength, targetArray, targetLength)**

​	将从原数组索引为**scrArrayStartLength**开始，长度为**targetLength**的所有元素复制到**targetArray**中。



## Object.clone()

​	省略，不是很经常用到