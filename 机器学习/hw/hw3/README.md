# 决策树实验报告

201250182 郑义

## 数据处理和分析

对于决策树的训练数据集，主要使用了 `usefulCount` 和 `sideEffects` 来作为特征数据构建决策树，其中 `usefulCount` 是连续特征，`sideEffects` 转化为整数值作为离散特征（`sideEffects` 只有五种取值，所以可以将字符串转化为整数值代表不同的副作用等级）。其他的特征都是无用特征，分析如下：

1. `recordId` 只是记录 id，和药品等级没有任何关系
2. `drugName` 是药名，无法从 `drugName` 知道该药品和其等级的关联，也没办法直接将其作为连续型 / 离散型特征来构建决策树
3. `condition` 是该药使用的条件，和等级有一定关系，但没有办法直接的作为连续型 / 离散型来构建（并没有确定的几种离散值选择）
4. `reviewComment` 太多种多样了，没办法直接使用，可能需要 nlp 之类的技术来做情感分析之后才能来作为评价药品等级的特征
5. `date` 和药品等级无关系

## 决策树设计原理和核心代码

主要采用了 C4.5 的决策树设计思路，通过 `usefuleCount` 和 `sideEffects` 作为特征进行决策树构建，核心代码如下：

```python
# 获取最佳划分特征
def get_best_feature(data):
    features = data.columns[:-1]
    res = {}
    for a in features:
        if a in continuous_features:
            temp_val, temp = cal_information_gain_continuous(data, a)
            res[a] = [temp_val, temp]
        else:
            temp = cal_information_gain(data, a)
            res[a] = [-1, temp]  #离散值没有划分点，用-1代替

    res = sorted(res.items(), key=lambda x: x[1][1], reverse=True)
    return res[0][0], res[0][1][0]

# 创建决策树
def create_tree(data):
    if len(data) == 0:
        # 没有数据，设置为 examples 中最普遍的数据值
        return most_label
    data_label = data.iloc[:, -1]
    # 只有一类
    if len(data_label.value_counts()) == 1:
        return data_label.values[0]
    # 所有数据的特征值一样，选样本最多的类作为分类结果
    if all(len(data[i].value_counts()) == 1 for i in data.iloc[:, :-1].columns):
        return get_most_label(data)
    # 根据信息增益得到的最优划分特征
    best_feature, best_feature_val = get_best_feature(data)
    # 连续值
    if best_feature in continuous_features:
        node_name = best_feature + '<' + str(best_feature_val)
        # 用字典形式存储决策树
        tree = {node_name: {}}
        tree[node_name]['是'] = create_tree(data.loc[data[best_feature] < best_feature_val])
        tree[node_name]['否'] = create_tree(data.loc[data[best_feature] > best_feature_val])
    else:
        tree = {best_feature: {}}
        # 当前数据下最佳特征的取值
        exist_vals = pd.unique(data[best_feature])
        # 如果特征的取值相比于原来的少了
        if len(exist_vals) != len(column_count[best_feature]):
            # 少的那些特征
            no_exist_attr = set(column_count[best_feature]) - set(exist_vals)
            for no_feat in no_exist_attr:
                # 缺失的特征分类为当前类别最多的
                tree[best_feature][no_feat] = get_most_label(data)
        # 根据特征值的不同递归创建决策树
        for item in drop_exist_feature(data, best_feature):
            tree[best_feature][item[0]] = create_tree(item[1])
    return tree
```

## 验证集评估结果

![评估结果](https://img-bed-1309306776.cos.ap-shanghai.myqcloud.com/img/20230331194939.png)
