# iframe
* 一个基于Mybtais的通用增删改查功能的工具包，mapper接口只要继承相应的接口，实体类添加几个注解即可面向对象操作数据
* iframe 基于`Spring boot, Gradle, mybatis3`实现，代码已通过测试


代码： `https://github.com/songshuiyang/iframe`

### 为什么有这个开发需求：
* 1、在实际整合了`Mybatis`的项目开发过程中经常会遇到变更数据库字段的情况，如果表结构发生了变化就需要重新修改mapper对应的xml文件，每次修改都要同步更新xml文件。
* 2、在普通的mapper接口中发现普通的增删改查这些方法每一个mapper接口都有，通过对比可以发现方法除了实体类属性不一样之外，其他的都一样(如下所示)，而且mapper文件也有大量增删改查的sql
```java
    int deleteByPrimaryKey(E id);

    int insert(T record);

    int insertSelective(T record);

    T selectByPrimaryKey(E id);

    int updateByPrimaryKeySelective(T record);

    int updateByPrimaryKey(T id);
```

### Mybatis 和 Hibernate 优缺点对比
> 现在开源项目中持久层框架用到最多的基本就是 MyBatis 和 Hibernate

#### Mybatis

##### 优点
* Mybatis入门简单，即学即用，提供了数据库查询的自动对象绑定功能，而且延续了很好的SQL使用经验
* 可以进行更为细致的SQL优化，可以减少查询字段
##### 缺点
* 虽然简化了数据绑定代码，但是整个底层数据库查询实际还是要自己写的，工作量也比较大，而且不太容易适应快速数据库修改。

#### Hibernate
##### 优点
* 不需要编写的SQL语句(不需要编辑JDBC)，只需要操作相应的对象就可以了，就可以能够存储、更新、删除、加载对象，可以提高生产效率
* 使用Hibernate，移植性好
##### 缺点
* 由于对持久层封装过于完整，导致开发人员无法对SQL进行优化，无法灵活使用JDBC的原生SQL，Hibernate封装了JDBC，所以没有JDBC直接访问数据库效率高。要使用数据库的特定优化机制的时候，不适合用Hibernate

### 开发目的
对比Mybatis 和 Hibernate 优缺点，可以发现他们之间的优缺点可以互补，为何不`取其精华, 去其糟粕, 双剑合并呢`, 所以初步想法是在Mybatis的基础框架上, 扩展一下其面向对象操作的功能。

### 使用方法

#### 准备
* 在自己的项目中导入 `com.songsy.iframe.core.persistence.provider` 包下的所有文件。
* 默认数据库各张表都有如下字段, 如果不符合项目需要即可修改对应的源码
```sql
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `last_modified_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `last_modified_by` varchar(32) DEFAULT NULL COMMENT '最后修改人',
  `version` bigint(20) DEFAULT NULL COMMENT '版本',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注',
  `enable` bit(1) DEFAULT b'1' COMMENT '是否启用',
```
* mybatis版本在3.0以上，需要使用其新特性
#### 使用
* 实体类继承`BaseEntity.class`类获得公共属性
```java
/**
 * 实体类基类
 * @author songshuiyang
 * @date 2018/10/28 10:13
 */
@Getter
@Setter
public class BaseEntity<ID> implements Serializable {

    private static final long serialVersionUID = -3873745966284869947L;

    /**
     * 主键
     */
    @Id(type = Integer.class)
    @GeneratedValue(strategy = GenerationType.CUSTOM)
    private ID id;
    /**
     * 创建人
     */
    private String createdBy;
    /**
     * 创建时间
     */
    private Date createdDate;
    /**
     * 最后修改人
     */
    private String lastModifiedBy;
    /**
     * 最后修改时间
     */
    private Date lastModifiedDate;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 乐观锁字段
     */
    @Version
    private Long version;
    /**
     * 逻辑删除标识
     */
    @Deleted
    private boolean enable = true;
    @Override
    public int hashCode() {
        return this.id != null ? this.id.hashCode() : null;
    }
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BaseEntity)) {
            return false;
        }
        BaseEntity i = (BaseEntity) obj;

        if (i.getId() == null || this.getId() == null) {
            return false;
        }
        if (this.getId().equals(i.getId())) {
            return true;
        }
        return false;
    }
}
```
* 实体类加上对应的注解
```java
/**
 * 用户
 * @author songshuiyang
 * @date 2017/11/28 21:36
 */
@Data
@Entity
@Table(name = "sys_user")
@EqualsAndHashCode(callSuper = false)
public class User extends BaseEntity<Integer> {

    private String username;

    private String password;

    private String nickname;

    private Integer sex;

    private Integer age;

    private String phone;

    private String email;

    private String address;

    private String salt;

    @Column(name = "head_portrait")
    private String headPortrait;
}
```
> 注解是参照Jpa的注解来定制的，详情可见`com.songsy.iframe.core.persistence.provider.annotation`

| 注解 | 作用 |
| ------ | ------ | 
| @Entity | 修饰实体类，指明该类将映射到指定的数据表| 
| @Table | 当实体类与映射的数据库表名不同名时需要使用 @Table 注解，该注解与 @Entity 注解并列使用，使用其 name 属性指明数据库的表名, 不填写`name`属性则默认是类名的转化成`_`格式的表名 |
| @Column | 当实体类属性名与数据库字段名不一致时, 可用该注解标识实体类对应在数据库的字段名| 
| @Id | 标识该属性为主键| 
| @GeneratedValue | 标注主键的生成策略，通过其 strategy 属性标识生成策略| 
| @Transient | 标注此注解后在操作数据表的时候将会忽略该属性 | 
| @Version | 标识乐观锁字段| 
| @Deleted | 逻辑删除标识| 

* mapper接口继承`BaseCurdMapper.java` ，Mapper层增加其通用增删改查方法, `<User,Integer>`：第一个是实体类类型，第二个标识主键类型
```java
/**
 * 用户
 * @author songshuiyang
 * @date 2017/11/28 20:12
 */
public interface UserMapper extends BaseCurdMapper<User,Integer> {

}
```
增加的方法：
```java
    /**
     * 查询所有数据
     * @return
     */
    List<T> findAll();

    /**
     * 根据id查询记录
     * @return
     */
    T findById(Object id);

    /**
     * 插入记录
     * @param entity
     * @return
     */
    int insert(T entity);

    /**
     * 更新记录
     * @param entity
     * @return
     */
    int update(T entity);

    /**
     * 更新记录(null值记录也更新)
     * @param entity
     * @return
     */
    int updateNull(T entity);

    /**
     * 根据id物理删除记录
     * @param id
     * @return
     */
    int deleteOne (Object id);

    /**
     * 根据id逻辑删除记录
     * @param id
     * @return
     */
    int logicDeleteOne (Object id);
```

* service接口继承`BaseService.java` ，Service层增加其通用增删改查方法
```java
/**
 * @author songshuiyang
 * @date 2018/10/28 10:13
 */
public interface UserService extends BaseService<User, Integer> {

}
```
增加的方法：
```java
/**
 * @author songsy
 * @Date 2018/10/31 18:06
 */
public interface BaseService <T extends BaseEntity, ID extends Serializable>{

    List<T> findAll();

    T findById(ID id);

    T saveSelective(T entity);

    T saveSelective(T entity, Boolean hasId);

    int updateNull(T entity);

    int deleteOne (ID id);

    int logicDeleteOne (ID id);
}
```

* service实现类继承`AbstractBaseService.java` ，重写`getRepository()`方法
```java
/**
 * @author songshuiyang
 * @date 2018/10/28 10:13
 */
@Service
public class UserServiceImpl extends AbstractBaseService<User, Integer> implements UserService  {

    @Autowired
    private UserMapper userMapper;

    @Override
    public BaseCurdMapper<User, Integer> getRepository() {
        return userMapper;
    }

}
```
AbstractBaseService.java
```java
/**
 * 抽象service基类
 *
 * @author songsy
 * @Date 2018/131 17:17
 */
@Slf4j
public abstract class AbstractBaseService<T extends BaseEntity, ID extends Serializable> {
    
    public abstract BaseCurdMapper<T, ID> getRepository();

    public List<T> findAll() {
        return getRepository().findAll();
    }

    public T findById(ID id) {
        return getRepository().findById(id);
    }

    public int updateNull(T entity) {
        return getRepository().updateNull(entity);
    }

    public int deleteOne(ID id) {
        return getRepository().deleteOne(id);
    }

    public int logicDeleteOne(ID id) {
        return getRepository().logicDeleteOne(id);
    }

    /**
     * 通用插入更新方法
     *
     * @param entity
     * @return
     */
    @Transactional
    public T saveSelective(T entity) {
        return saveSelective(entity, false);
    }

    @Transactional
    public T saveSelective(T entity, Boolean hasId) {
        if (hasId) {
            // 之前已经生成了id
            insertSelective(entity);
        } else if (!StringUtils.isEmpty(entity.getId())) {
            updateSelective(entity);
            // 插入数据库之后 实体类乐观锁字段自增
            entity.setVersion(entity.getVersion() + 1);
        } else {
            Class idClass = ReflectionUtils.getPrimarykeyClassType(entity.getClass());
            // 如果主键是字符类型，则采用32位随机字符作为主键
            if (idClass.equals(String.class)) {
                entity.setId(IDGeneratorUtils.generateID());
            } else {
                // 默认主键由数据库自动生成（主要是自动增长型）
            }
            insertSelective(entity);
        }
        return entity;
    }

    private void insertSelective(T entity) {
        entity.setCreatedDate(new Date());
        entity.setLastModifiedDate(new Date());
        entity.setVersion(new Long(1));
        // 设置当前登录人
//        if (null == entity.getCreatedBy()) {
//            entity.setCreatedBy("");
//        }
//        if (null == entity.getLastModifiedBy()) {
//            entity.setLastModifiedBy("");
//        }
        getRepository().insert(entity);
    }

    private void updateSelective(T entity) {
        if (entity.getVersion() == null) {
            throw new VersionException();
        }
        entity.setLastModifiedDate(new Date());
        // 设置当前登录人
//        if (null == entity.getLastModifiedBy()) {
//            entity.setLastModifiedBy("");
//        }
        Integer flag = getRepository().update(entity);
        if (flag == 0) {
            throw new UpdateException();
        }
    }
}
```
#### 测试
```java
/**
 * @author songsy
 * @Date 2018/10/31 18:00
 */
public class UserServiceTest extends BaseTest {

    @Autowired
    UserService userService;
    
    @Test
    public void findAll () {
        userService.findAll();
    }

    @Test
    public void insertUser () {
        User user = new User();
        user.setUsername("songsy");
        user.setAddress("广东深圳");
        user.setAge(88);
        user.setEmail("1459074711@qq.com");
        user.setHeadPortrait("头像");
        user.setNickname("宋某");
        user.setPassword("root");
        user.setSex(1);
        userService.saveSelective(user);
    }

    @Test
    public void updateUser1 () {
        User user = new User();
        user.setId(48);
        user.setUsername("songsy");
        user.setAddress("广东深圳");
        user.setAge(88);
        user.setEmail("1459074711@qq.com");
        user.setHeadPortrait("头像");
        user.setNickname("宋某某");
        user.setPassword("root");
        user.setSex(1);
        user.setVersion(1l);
        userService.saveSelective(user);
    }

    @Test
    public void updateUser2 () {
        User user = userService.findAll().get(0);
        User userDb = new User();
        userDb.setId(user.getId());
        userDb.setVersion(user.getVersion());
        userDb.setUsername("测试乐观锁111");
        userService.saveSelective(userDb);
    }
    
    @Test
    public void updateNull () {
        User user = userService.findById(50);
        User userDb = new User();
        userDb.setId(user.getId());
        userDb.setVersion(user.getVersion());
        userDb.setUsername("测试updateNull");
        userService.updateNull(userDb);
    }

    @Test
    public void deleteOne () {
        userService.deleteOne(48);
    }

    @Test
    public void logicDeleteOne () {
        userService.logicDeleteOne(49);
    }
}
```

#### 使用总结
* 如果增加或者修改了数据库字段，只要修改对应的实体类文件即可，配合注解的使用可以十分方便完成修改，对于增删改查的操作代码再也不用一个个去修改xml文件了
* 不用在每一个`mapper`接口, Mybatis xml文件添加一些重复的代码
* 在service层即可完成通用增删改查方法，使用Mybatis也可以像`Hibernate` 那样用对象来更新数据库了

### 实现解析
> 详细实现可见`com.songsy.iframe.core.persistence.provider`

按步骤解析：

* 使用`Spring Aop`收集实体类信息及缓存起来，每次调用继承了`BaseCurdMapper.java`的Mapper接口就会触发
```java
package com.songsy.iframe.core.persistence.provider.aspect;

import com.google.common.collect.Maps;
import com.songsy.iframe.core.persistence.provider.exception.ParameterizedTypeException;
import com.songsy.iframe.core.persistence.provider.mapper.BaseCurdMapper;
import com.songsy.iframe.core.persistence.provider.threadlocal.EntityProperty;
import com.songsy.iframe.core.persistence.provider.threadlocal.EntityThreadLocal;
import com.songsy.iframe.core.persistence.provider.utils.ReflectionUtils;
import org.apache.ibatis.binding.MapperProxy;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * BaseCurdMapper接口AOP，用于获取实体类属性
 *
 * @author songshuiyang
 * @date 2018/10/30 21:44
 */
@Aspect
@Component
public class BaseCurdMapperAspect {

    private final static Logger logger = LoggerFactory.getLogger(BaseCurdMapperAspect.class);

    /**
     * 缓存实体类属性
     * key: 实体类类型
     * value: 实体类属性对象
     */
    private static Map<String, EntityProperty> entityPropertyMap = Maps.newHashMap();

    /**
     * 定义切点
     * Spring Aop是基于代理的，生成的bean也是一个代理对象，this就是这个代理对象，
     * 当这个对象可以转换为指定的类型时，对应的切入点就是它了，Spring Aop将生效。
     */
    @Pointcut("this(com.songsy.iframe.core.persistence.provider.mapper.BaseCurdMapper)")
    public void pointcut() {
    }

    /**
     * 前置增强：获取BaseCurdMapper接口 泛型属性，并设置到ThreadLocal中
     * @param point
     */
    @Before("pointcut()")
    public void before(JoinPoint point) {
        Class entityClass = null;
        Class entityIdClass = null;
        Object target= point.getTarget();
        // 是否继承 BaseCurdMapper 接口
        if (BaseCurdMapper.class.isAssignableFrom(target.getClass())) {
            // 获取Mybatis代理类对象
            MapperProxy mapperProxy = (MapperProxy) Proxy.getInvocationHandler(target);
            Class mapperInterface = (Class) ReflectionUtils.getFieldValue(mapperProxy, "mapperInterface");
            // 获取接口泛型对象
            ParameterizedType parameterizedType = (ParameterizedType) mapperInterface.getGenericInterfaces()[0];
            Type[] types = parameterizedType.getActualTypeArguments();
            if (types.length != 2) {
                logger.error("parameterizedType type length error");
                throw new ParameterizedTypeException(parameterizedType.getTypeName());
            }
            try {
                entityClass = Class.forName(types[0].getTypeName());
                entityIdClass = Class.forName(types[1].getTypeName());
                // 如果不存在则加入到entityPropertyMap缓存中
                if (!entityPropertyMap.containsKey(entityClass.getName())) {
                    EntityProperty entityProperty = new EntityProperty(entityClass, entityIdClass);
                    entityPropertyMap.put(entityClass.getTypeName(),entityProperty);
                }
            } catch (ClassNotFoundException e) {
                logger.error(e.getMessage());
            }
        }
        // 设置ThreadLocal
        if (null != entityClass) {
            EntityThreadLocal.set(entityPropertyMap.get(entityClass.getName()));
        }
    }

    /**
     * 后置增强：清除 threadLocal 防止内存泄漏
     * @param point
     */
    @After("pointcut()")
    public void after(JoinPoint point) {
        EntityThreadLocal.remove();
    }

}
```
* 使用`ThreadLocal` 获取当前访问线程实体类信息
```java
/**
 * ThreadLocal为变量在每个线程中都创建了一个副本，那么每个线程可以访问自己内部的副本变量。
 * 每次调用mapper接口方法的时候，先把实体类的信息存放在ThreadLocal中
 * @author songshuiyang
 * @date 2018/10/30 21:27
 */
public class EntityThreadLocal {

    private static ThreadLocal<EntityProperty> threadLocal = new ThreadLocal<>();

    /**
     * 获取当前线程的实体类属性
     * @return
     */
    public static EntityProperty get () {
        if (null == threadLocal) {
            initialValue();
        }
        return threadLocal.get();
    }

    /**
     * 设置当前线程的实体类属性
     * @param entityProperty
     */
    public static void set(EntityProperty entityProperty) {
        if (entityProperty != null) {
            threadLocal.set(entityProperty);
        }
    }

    /**
     * 清除 threadLocal
     */
    public static void remove() {
        threadLocal.remove();
    }

    /**
     * 默认初始化Object.class
     */
    private static void initialValue() {
        EntityProperty entityProperty = new EntityProperty();
        entityProperty.setEntityClass(Object.class);
        entityProperty.setIdClass(null);
        threadLocal.set(entityProperty);
    }
}
```

* 使用Mybatis3的`@SelectProvider`、` @InsertProvider`, `@UpdateProvider`,`@DeleteProvider`，使用注解来配置Mapper
```java
/**
 * 通用增删改查Mapper
 * @author songshuiyang
 * @date 2018/10/28 11:22
 */
public interface CurdMapper<T extends BaseEntity, ID extends Serializable> {
    /**
     * 查询所有数据
     * @return
     */
    @SelectProvider(type=MybatisProvider.class,method = MybatisProvider.FIND_ALL)
    List<T> findAll();

    /**
     * 根据id查询记录
     * @return
     */
    @SelectProvider(type=MybatisProvider.class, method = MybatisProvider.FIND_BY_ID)
    T findById(Object id);

    /**
     * 插入记录
     * @param entity
     * @return
     */
    @InsertProvider(type=MybatisProvider.class, method = MybatisProvider.INSERT)
    int insert(T entity);

    /**
     * 更新记录
     * @param entity
     * @return
     */
    @UpdateProvider(type=MybatisProvider.class, method = MybatisProvider.UPDATE)
    int update(T entity);

    /**
     * 更新记录(null值记录也更新)
     * @param entity
     * @return
     */
    @UpdateProvider(type=MybatisProvider.class, method = MybatisProvider.UPDATE_NULL)
    int updateNull(T entity);

    /**
     * 根据id物理删除记录
     * @param id
     * @return
     */
    @DeleteProvider(type=MybatisProvider.class, method = MybatisProvider.DELETE_ONE)
    int deleteOne (Object id);

    /**
     * 根据id逻辑删除记录
     * @param id
     * @return
     */
    @DeleteProvider(type=MybatisProvider.class, method = MybatisProvider.LOGIC_DELETE_ONE)
    int logicDeleteOne (Object id);


    /**
     * 分页查询
     * @param page
     * @return
     */
    @SelectProvider(type=MybatisProvider.class,method = MybatisProvider.FIND_AUTO_BY_PAGE)
    List<T> findAutoByPage(Page<T> page);
}
```
* 通用增删改查实现类，在这里实现sql的拼接

```java
package com.songsy.iframe.core.persistence.provider;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.songsy.iframe.core.persistence.provider.annotation.Version;
import com.songsy.iframe.core.persistence.provider.entity.ColumnEntity;
import com.songsy.iframe.core.persistence.provider.entity.TableEntity;
import com.songsy.iframe.core.persistence.provider.utils.MybatisTableUtils;
import com.songsy.iframe.core.persistence.provider.utils.PageUtils;
import com.songsy.iframe.core.persistence.provider.utils.ReflectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 通用增删改查实现方法
 * @author songshuiyang
 * @date 2018/10/28 11:34
 */
public class CrudProvider {

    private static Logger logger = LoggerFactory.getLogger(CrudProvider.class);

    public static final String FIND_ALL = "findAll";
    public static final String FIND_BY_ID = "findById";
    public static final String INSERT = "insert";
    public static final String UPDATE = "update";
    public static final String UPDATE_NULL = "updateNull";
    public static final String DELETE_ONE = "deleteOne";
    public static final String LOGIC_DELETE_ONE ="logicDeleteOne";
    public static final String FIND_AUTO_BY_PAGE = "findAutoByPage";

    /**
     * 查询所有数据
     * @return
     */
    public String findAll() {
        TableEntity tableEntity = MybatisTableUtils.getCurrentTableEntity();
        String sql = "SELECT * FROM " +  tableEntity.getTableName();
        return sql;
    }

    /**
     * 根据id查询记录
     * @param id
     * @return
     */
    public String findById (Object id) {
        TableEntity tableEntity = MybatisTableUtils.getCurrentTableEntity();
        StringBuilder sb = new StringBuilder("SELECT ");
        sb.append(" * ");
        sb.append("FROM");
        sb.append(" ").append(tableEntity.getTableName()).append(" ");
        sb.append(" WHERE ").append(tableEntity.getIdColumnEntity().getColumnName()).append("=").append(id);
        return sb.toString();
    }

    /**
     * 插入记录
     * @param entity
     */
    public String insert (Object entity) {
        TableEntity tableEntity = MybatisTableUtils.getCurrentTableEntity();
        List<ColumnEntity> columnEntities = tableEntity.getColumnEntities();
        List<String> fieldNames = Lists.newArrayList();
        List<String> columnNames = Lists.newArrayList();
        for (ColumnEntity columnEntity : columnEntities) {
            Object value = ReflectionUtils.getFieldValue(entity, columnEntity.getFieldName());
            // 字段为null不插入
            if (value != null) {
                columnNames.add(columnEntity.getColumnName());
                fieldNames.add("#{" + columnEntity.getFieldName() + "}");
            }
        }
        StringBuilder sb = new StringBuilder("INSERT INTO ");
        sb.append(tableEntity.getTableName());
        sb.append(" (");
        sb.append(StringUtils.join(columnNames, ","));
        sb.append(") ");
        sb.append(" VALUES(");
        sb.append(StringUtils.join(fieldNames, ","));
        sb.append(")");
        String sql = sb.toString();
        return sql;
    }

    /**
     * 更新记录
     * 字段属性为null不更新
     * @param entity
     */
    public String  update (Object entity) {
        TableEntity tableEntity = MybatisTableUtils.getCurrentTableEntity();
        List<ColumnEntity> columnEntities = tableEntity.getColumnEntities();
        ColumnEntity versionColumnEntity = null;
        List<String> updateColumns = Lists.newArrayList();
        for (ColumnEntity columnEntity : columnEntities) {
            // 乐观锁处理 更新后version字段加一
            Field field = columnEntity.getField();
            Version version = field.getAnnotation(Version.class); {
                if (version != null) {
                    versionColumnEntity = columnEntity;
                    updateColumns.add(columnEntity.getColumnName() + " = " + columnEntity.getFieldName() + " + 1");
                    continue;
                }
            }
            Object value = ReflectionUtils.getFieldValue(entity, columnEntity.getFieldName());
            if (value != null) {
                updateColumns.add(columnEntity.getColumnName() + " = " + "#{" + columnEntity.getFieldName() + "}");
            }
        }
        StringBuilder sb = new StringBuilder("UPDATE ");
        sb.append(tableEntity.getTableName());
        sb.append(" SET ");
        sb.append(StringUtils.join(updateColumns, ","));
        sb.append(" WHERE ");
        sb.append(tableEntity.getIdColumnEntity().getColumnName());
        sb.append(" = ");
        sb.append("#{" + tableEntity.getIdColumnEntity().getFieldName() + "}");
        sb.append(" and ");
        sb.append(versionColumnEntity.getColumnName());
        sb.append(" = ");
        sb.append("#{" + versionColumnEntity.getFieldName() + "}");
        String sql = sb.toString();
        return sql;
    }

    /**
     * 更新记录
     * 字段属性为null 也会更新为null
     * @param entity
     */
    public String  updateNull (Object entity) {
        TableEntity tableEntity = MybatisTableUtils.getCurrentTableEntity();
        List<ColumnEntity> columnEntities = tableEntity.getColumnEntities();
        ColumnEntity versionColumnEntity = null;
        List<String> updateColumns = Lists.newArrayList();
        for (ColumnEntity columnEntity : columnEntities) {
            // 乐观锁处理 更新后version字段加一
            Field field = columnEntity.getField();
            Version version = field.getAnnotation(Version.class); {
                if (version != null) {
                    versionColumnEntity = columnEntity;
                    updateColumns.add(columnEntity.getColumnName() + " = " + columnEntity.getFieldName() + " + 1");
                    continue;
                }
            }
            updateColumns.add(columnEntity.getColumnName() + " = " + "#{" + columnEntity.getFieldName() + "}");
        }
        StringBuilder sb = new StringBuilder("UPDATE ");
        sb.append(tableEntity.getTableName());
        sb.append(" SET ");
        sb.append(StringUtils.join(updateColumns, ","));
        sb.append(" WHERE ");
        sb.append(tableEntity.getIdColumnEntity().getColumnName());
        sb.append(" = ");
        sb.append("#{" + tableEntity.getIdColumnEntity().getFieldName() + "}");
        sb.append(" and ");
        sb.append(versionColumnEntity.getColumnName());
        sb.append(" = ");
        sb.append("#{" + versionColumnEntity.getFieldName() + "}");
        String sql = sb.toString();
        return sql;
    }

    /**
     * 根据id物理删除记录
     * @param id
     * @return
     */
    public String deleteOne(Object id) {
        TableEntity tableEntity = MybatisTableUtils.getCurrentTableEntity();
        String sql = "DELETE FROM " + tableEntity.getTableName() + " WHERE " + tableEntity.getIdColumnEntity().getColumnName()
                + " = #{id}";
        return sql;
    }

    /**
     * 根据id逻辑删除记录
     * @param id
     * @return
     */
    public String logicDeleteOne(Object id) {
        TableEntity tableEntity = MybatisTableUtils.getCurrentTableEntity();
        String sql = "UPDATE " +
                tableEntity.getTableName() +
                " SET " + tableEntity.getDeleteColunmEntity().getColumnName() + " = 0 " +
                "WHERE " + tableEntity.getIdColumnEntity().getColumnName() + " = #{id}";
        return sql;
    }
}
```



