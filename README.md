# PyGetterAndSetter
## 简介
这是一款自动生成Python对象属性的getter和setter方法的PyCharm插件。  

在Python中，并没有类似Java的访问控制符，所以对象属性可以直接访问到，
但是如果我们采用面向对象设计，那么Getter，Setter就是必要的，可以
收敛变量的修改。

## 安装方法
1. Windows系统点击`File-Settings-Plugins`, MacOS系统点击`Pycharm-Preferences-Plugins`
2. 点击顶部栏的三个竖点，点击`Install Plugin from Disk`
3. 在打开的文件选择器中，选择PyGetterAndSetter-xxx.zip文件
4. 重启PyCharm

## 使用方法
选中需要生成的所有对象属性的行，一定要把属性行全部选中，右键，选择`PyGetterAndSetter`，
即可在选中行下方生成对应的Getter和Setter。
![使用范例](../src/resources/img/use_plugin.png)

## 适用环境
PyCharm 2020.1及以上版本

## 原理
1. 获取选中行的文本内容
2. 正则匹配`self.(.*?)=`，可以获取变量名
3. 设定Getter，Setter模板，将变量名填充到模板
4. 模板内容回写到编辑器
