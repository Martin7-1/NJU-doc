## 特征选择

特征选择是特征工程里的一个重要问题，其目标是寻找最优特征子集。特征选择能剔除不相关(irrelevant)或冗余(redundant)的特征，从而达到减少特征个数，提高模型精确度，减少运行时间的目的。另一方面，选取出真正相关的特征简化模型，协助理解数据产生的过程。并且常能听到“数据和特征决定了机器学习的上限，而模型和算法只是逼近这个上限而已”，由此可见其重要性。但是它几乎很少出现于机器学习书本里面的某一章。然而在机器学习方面的成功很大程度上在于如果使用特征工程。

在作业三中，主要选择了以下的特征选择方法：

1. 常量剔除
2. 相关性计算
3. 共线性计算
4. 互信息(mutual info)计算

### 1 常量

如果一个特征为常量，说明这个特征对于 y 值的预测基本没有影响，因此不应该作为最后被选择的特征，可以被剔除。通过如下的代码可以计算一个特征是否为常量：

```python
data_copy = data.copy(deep=True)
quasi_constant_feature = []
for feature in data_copy.columns:
    predominant = (data_copy[feature].value_counts() / np.float(
        len(data_copy))).sort_values(ascending=False).values[0]
    if predominant >= threshold:
        quasi_constant_feature.append(feature)
print(len(quasi_constant_feature), ' variables are found to be almost constant')
return quasi_constant_feature
```

实际使用过程中，我们认为有 90% 值为重复的即为常量，认为其对于星级和信用的预测没有很大影响，因此将其剔除。

在星级预测当中，我们剔除了 ['avg_qur_x', 'fin_bal', 'td_crd_bal', 'sa_td_bal', 'ntc_bal', 'td_3m_bal', 'td_6m_bal', 'td_2y_bal', 'out_td_bal', 'cd_bal', 'frz_sts', 'stp_sts', 'avg_qur_y'] 这些列。

### 相关性

通过相关性计算可以找到类似的特征，然后将类似特征进行剔除，保证特征的独立性。通过以下代码来实现：

```python
def corr_feature_detect(data, threshold=0.8):
    corrmat = data.corr()
    corrmat = corrmat.abs().unstack()  # absolute value of corr coef
    corrmat = corrmat.sort_values(ascending=False)
    corrmat = corrmat[corrmat >= threshold]
    corrmat = corrmat[corrmat < 1]  # remove the digonal
    corrmat = pd.DataFrame(corrmat).reset_index()
    corrmat.columns = ['feature1', 'feature2', 'corr']

    grouped_feature_ls = []
    correlated_groups = []

    for feature in corrmat.feature1.unique():
        if feature not in grouped_feature_ls:
            # find all features correlated to a single feature
            correlated_block = corrmat[corrmat.feature1 == feature]
            grouped_feature_ls = grouped_feature_ls + list(
                correlated_block.feature2.unique()) + [feature]

            # append the block of features to the list
            correlated_groups.append(correlated_block)
    return correlated_groups
```

### 共线性

多重共线性是指自变量之间存在一定程度的线性相关，会给变量对模型的贡献性带来影响。即若有两个变量存在共线性，在相互作用计算后，其一的变量的影响会相对减弱，而另一个变量的作用却会相对增强。对于共线性较强的变量（我们这里以 20 作为共线性的判断标准）

```python
def calc_VIF(df: pd.DataFrame) -> pd.DataFrame:
    """

    :param df: 数据
    :return:
    """
    vif = pd.DataFrame()
    vif['index'] = df.columns
    vif['VIF'] = [variance_inflation_factor(df.values, i) for i in range(df.shape[1])]
    return vif
```

### 互信息

互信息是信息论中用以评价两个随机变量之间的依赖程度的一个度量。可以用来更好的选择特征。

```python
def mutual_info(X, y, select_k=10):
    #    mi = mutual_info_classif(X,y)
    #    mi = pd.Series(mi)
    #    mi.index = X.columns
    #    mi.sort_values(ascending=False)

    if select_k >= 1:
        sel_ = SelectKBest(mutual_info_classif, k=select_k).fit(X, y)
        col = X.columns[sel_.get_support()]

    elif 0 < select_k < 1:
        sel_ = SelectPercentile(mutual_info_classif, percentile=select_k * 100).fit(X, y)
        col = X.columns[sel_.get_support()]

    else:
        raise ValueError("select_k must be a positive number")

    return col
```