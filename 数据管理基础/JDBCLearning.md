# JDBCLearning

## 1 What is JDBC

> The Java Database Connectivity (JDBC) API provides universal data access from the Java programming language. Using the JDBC API, you can access virtually any data source, from relational databases to spreadsheets and flat files. JDBC technology also provides a common base on which tools and alternate interfaces can be built.

JDBC 指 Java 数据库连接，是一种标准Java应用编程接口（ JAVA API），用来连接 Java 编程语言和广泛的数据库。

JDBC API 库包含下面提到的每个任务，都是与数据库相关的常用用法。

- 制作到数据库的连接。
- 创建 SQL 或 MySQL 语句。
- 执行 SQL 或 MySQL 查询数据库。
- 查看和修改所产生的记录。

从根本上来说，JDBC 是一种规范，它提供了一套完整的接口，允许便携式访问到底层数据库，因此可以用 Java 编写不同类型的可执行文件，例如：

- Java 应用程序
- Java Applets
- Java Servlets
- Java ServerPages (JSPs)
- Enterprise JavaBeans (EJBs)

所有这些不同的可执行文件就可以使用 JDBC 驱动程序来访问数据库，这样可以方便的访问数据。

JDBC 具有 ODBC 一样的性能，允许 Java 程序包含与数据库无关的代码。

## 2 JDBC架构

DBC 的 API 支持两层和三层处理模式进行数据库访问，但一般的 JDBC 架构由两层处理模式组成：

- **JDBC API**: 提供了应用程序对 JDBC 管理器的连接。
- **JDBC Driver API**: 提供了 JDBC 管理器对驱动程序连接。

JDBC API 使用驱动程序管理器和数据库特定的驱动程序来提供异构（heterogeneous）数据库的透明连接。

JDBC 驱动程序管理器可确保正确的驱动程序来访问每个数据源。该驱动程序管理器能够支持连接到多个异构数据库的多个并发的驱动程序。

以下是结构图，其中显示了驱动程序管理器相对于在 JDBC 驱动程序和 Java 应用程序所处的位置。

![img](https://atts.w3cschool.cn/attachments/image/wk/jdbc/r8GqQJ3.jpg)

## 3 常见的JDBC组件

JDBC 的 API 提供了以下接口和类：

**DriverManager ：**这个类管理一系列数据库驱动程序。匹配连接使用通信子协议从 JAVA 应用程序中请求合适的数据库驱动程序。识别 JDBC 下某个子协议的第一驱动程序将被用于建立数据库连接。

**Driver :** 这个接口处理与数据库服务器的通信。你将很少直接与驱动程序互动。相反，你使用 DriverManager 中的对象，它管理此类型的对象。它也抽象与驱动程序对象工作相关的详细信息。

**Connection :** 此接口具有接触数据库的所有方法。该连接对象表示通信上下文，即，所有与数据库的通信仅通过这个连接对象进行。

**Statement :** 使用创建于这个接口的对象将 SQL 语句提交到数据库。除了执行存储过程以外，一些派生的接口也接受参数。

**ResultSet :** 在你使用语句对象执行 SQL 查询后，这些对象保存从数据获得的数据。它作为一个**迭代器**，让您可以通过它的数据来移动。

**SQLException :** 这个类处理发生在数据库应用程序的任何错误。



## 4 JDBC示例代码

首先我们需要在maven中引入mysql-connector

```xml
<!-- https://mvnrepository.com/artifact/mysql/mysql-connector-java -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.49</version>
        </dependency>
```

示例代码，使用JDBC来进行数据库的增删改查。

```java
import java.sql.*;

/**
 * @author Zyi
 */
public class FirstExample {

    /**
     * MySQL驱动
     */
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    /**
     * 数据库URL
     */
    private static final String DB_URL = "jdbc:mysql://localhost:3306/JDBCLearning?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT&useSSL=true";
    /**
     * 数据库用户名
     */
    private static final String USER_NAME = "root";
    /**
     * 数据库密码
     */
    private static final String USER_PWD = "Zzzyi123456";

    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;

        try {
            // Register JDBC Driver
            Class.forName(JDBC_DRIVER);

            // Open Connection
            System.out.println("Connecting to Database...");
            connection = DriverManager.getConnection(DB_URL, USER_NAME, USER_PWD);

            // Execute a query
            System.out.println("Creating statement...");
            statement = connection.createStatement();
            String sql = "SELECT * FROM `user`";
            String insert = "INSERT INTO `user`(`user_name`, `user_pwd`) VALUES ('Martin', '123456')";
            String remove = "DELETE FROM `user` WHERE `user_id` in (7, 8)";
            String update = "UPDATE `user` SET `user_name` = 'newUser' WHERE `user_id` = 9";
            int removeRow = statement.executeUpdate(remove);
            int insertRow = statement.executeUpdate(insert);
            int updateRow = statement.executeUpdate(update);
            ResultSet resultSet = statement.executeQuery(sql);

            System.out.println("Remove row number: " + removeRow);
            System.out.println("Insert row number: " + insertRow);
            System.out.println("Update row number: " + updateRow);

            while (resultSet.next()) {
                int id = resultSet.getInt("user_id");
                String userName = resultSet.getString("user_name");
                String password = resultSet.getString("user_pwd");

                System.out.println(id);
                System.out.println(userName);
                System.out.println(password);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }

                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        System.out.println("GoodBye");
    }
}

```

数据库建表如下所示：

```mysql
CREATE DATABASE IF NOT EXISTS `JDBCLearning`;
USE DATABASE `JDBCLearning`;

CREATE TABLE IF NOT EXISTS `user` (
	`user_id` INT NOT NULL AUTO INCREASEMENT,
    `user_name` VARCHAR(40) NOT NULL,
    `user_pwd` VARCHAR(40) NOT NULL,
    CONSTRAINT PRIMARY KEY (`user_id`)
)ENGINE = InnoDB CHARSET = utf8;
```

步骤总结：

1. 加载数据库对应的驱动，MYSQL对应的为`com.mysql.jdbc.Driver`。通过`Class.forName(driverNmae)`来加载驱动（或者可以加一个`newInstance()`，其实就是加载对应的驱动类）
2. 连接数据库 `DriverManager` 获得一个`Connection`。
3. 获得执行SQL的对象，`Statement`或者`PreparedStatement`
4. 通过`ResultSet`来获得返回的结果集，`executeQuery()`执行查询，`executeUpdate()`执行UPDATE/DELETE/INSERT
5. 关闭连接，释放资源

> 加载驱动和URL可以写在配置文件中，解耦合，通过加载配置文件来获取数据库连接



## 5 JDBC中的类的解析

### 5.1 Connection

Connection是与数据库进行连接的一个类，可以通过该类获得与数据库连接的对象，通过对象我们可以执行一些数据库的操作：

```java
connection.rollback(); // 事务回滚
connection.commit(); // 事务提交
connection.setAutoCommit(); // 设置是否开启自动提交事务
```

### 5.2 Statement and PreparedStatement

这两个类都是用于执行SQL语句的类，对于`Statement`来说:

```java
String sql = "SELECT * FROM `user`"

statement.executeQuery(sql); // 查询操作
statement.execute(sql); // 执行任何SQL语句
statement.executeUpdate(null); // 更新 INSERT/DELTE/UPDATE
```

> 两者的差别会在后面进行比较

### 5.3 ResultSet

获取SQL查询的结果集，可以通过数据表的列名来获取对应查询到的数据。

```java
resultSet.getString("列名");
resultSet.getInt("列名");

// 可以通过resultSet.next() 来获得迭代器的下一个数据
```



### 5.4 Difference between Statement and PreparedStatement

PreparedStatement的出现是为了防止SQL注入，且效率较高。

```java
Connection connection = null;
PreparedStatement preparedStatement;

try {
    // 问号占位符 + 预编译的方式
    String sql = "INSERT INTO `user`(`user_name`, `user_pwd`) VALUES (?, ?)"
    
    connecntion = DriverManger.getConnection(url, name, password);
    preparedStatement = connection.preparedStatement(sql);
    
    // 设置值
    preparedStatement.setString(1, "test");
    preparedStatement.setString(2, "test");
} catch (SQLException e) {
    // todo
} finally {
    // close the connection and statement and resultset
}
```

