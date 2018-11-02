# iframe
### 
开发一个通用增删改查功能的工具包，只要继承相应的接口，添加几个注解即可拥有

持久基于Grandle搭建Springboot

在Java 应用的数据库开发中，不可避免地会使用到持久层框架，而现在开源项目中持久层框架用到最多的基本就是 MyBatis 和 Hibernate 了，


### 为什么有这个开发需求：
* 1、在实际项目开发过程中经常会遇到变更数据库字段的情况，如果表结构发生了变化就需要重新修改mapper对应的xml文件，每次修改都要同步更新xml文件。
* 2、在普通的mapper接口中发现普通的增删改查这些方法每一个mapper接口都有，通过对比可以发现方法除了实体类属性不一样之外，其他的都一样，而且mapper文件也有大量增删改查的sql
```java
    int deleteByPrimaryKey(E id);

    int insert(T record);

    int insertSelective(T record);

    T selectByPrimaryKey(E id);

    int updateByPrimaryKeySelective(T record);

    int updateByPrimaryKey(T id);
```
* 3、

1、如果数据库字段变化很频繁，就需要反复重新生成代码，并且由于 MBG 覆盖生成代码和追加方式生成 XML，导致每次重新生成都需要大量的比对修改
