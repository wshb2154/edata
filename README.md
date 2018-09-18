# 对接电力数据（深瑞）与对接视频监控（海康）#

## 对接电力数据
### 导入电力数据文件
![导入edata](https://github.com/wshb2154/edata/blob/master/imgs/E数据-文件解析和入库.png?raw=true)
### 计算发电量
![计算发电量电量](https://github.com/wshb2154/edata/blob/master/imgs/E数据-数据计算和处理.png?raw=true)
### 代码
![计算发电量电量](https://github.com/wshb2154/edata/blob/master/imgs/pic1.jpg?raw=true)
#### code1 控制
- CacheHelper 常用到的类和数据的缓冲
- EdataImportTask 导入数据任务
- EdataUpdateRealtimeTask 计算电量任务
- TaskManager（门面模式）
#### code2 实体
- 导入和计算用到的对象
#### code3 导入数据接口
- IFilesFilter 文件过滤器
- IEdataFileParser 文件解析
- IDataConverter 文件解析后导入数据库
- IImportedEdataFilesProcessor 导入数据库后文件如何处理
#### code4 计算电量接口
- Electricalculator 电量计算类
- DianhCalculator 电号计算类
#### code5 计算电量实现
- ElectricCalculatorImpl 电量计算实现类
- DayuDianhCalculator 点号计算实现类1，计算日发电量
- CountDianhCalculator 点号计算实现类1，计算累积总发电量

### 数据库
![计算发电量电量](https://github.com/wshb2154/edata/blob/master/imgs/pic2.jpg?raw=true)

## 对接视频监控

### code6 对接视频监控
- haikang.js 重构海康API,一个类一个方法解决免登录、重复登入、码流切换问题。
- detail.html 应用实例
