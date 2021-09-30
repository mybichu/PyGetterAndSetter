# PyGetterAndSetter
## 简介
这是一款自动生成Python对象属性的getter和setter方法的PyCharm插件。  

在Python中，并没有类似Java的访问控制符，所以对象属性可以直接访问到，
但是如果我们采用面向对象设计，那么Getter，Setter就是必要的，可以
收敛变量的修改。


## 使用方法
选中需要生成的所有对象属性的行，右键，选择`PyGetterAndSetter`，
即可在选中行下方生成对应的Getter和Setter。

## 适用环境
PyCharm 2020.1及以上版本

## 原理
1. 获取选中行的文本内容
2. 正则匹配`self.(.*?)=`，可以获取变量名
3. 设定Getter，Setter模板，将变量名填充到模板
4. 模板内容回写到编辑器