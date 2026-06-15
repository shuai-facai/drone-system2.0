/*项目启动自动初始化数据库，就是中间代码：
读取 resources 里schema.sql脚本，自动执行建表、初始化测试数据*/
package com.example.drone.config;//声明当前这个 Java 类所在的包路径
//Spring 框架相关导入
import org.springframework.beans.factory.annotation.Autowired;//Spring 自动依赖注入注解，用来把JdbcTemplate自动注入到类中，不用手动 new 对象
import org.springframework.boot.CommandLineRunner;//SpringBoot 接口，自动执行里面的方法
import org.springframework.context.annotation.Bean;//把方法返回对象交给 Spring 容器管理
import org.springframework.context.annotation.Configuration;//标记当前类是Spring 配置类，项目启动时会加载这个类的逻辑
import org.springframework.jdbc.core.JdbcTemplate;//Spring 封装的数据库操作工具
import org.springframework.core.io.ClassPathResource;//读取项目resources资源文件夹下的文件（这里用来加载schema.sql）
import org.springframework.util.StreamUtils;//把文件输入流转换成字符串
//Java 原生工具导入
import java.nio.charset.StandardCharsets;//避免中文乱码
import java.sql.Connection;//获取和数据库的会话
import java.sql.Statement;//用来逐条运行建表语句

/**
 * 数据库初始化配置类
 * 在应用启动时初始化数据库表结构
 */
@Configuration
public class DatabaseInitConfig {

    /** JDBC模板，用于数据库操作 */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 创建数据库初始化的CommandLineRunner
     * 应用启动时执行，检查表是否存在，不存在则创建
     * @return CommandLineRunner实例
     */
    @Bean
    public CommandLineRunner initDatabase() {
        return args -> {
            try {
                // 检查表是否存在
                Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM t_uav", Integer.class);
                if (count == null || count == 0) {
                    createTableAndInsertData();
                }
            } catch (Exception e) {
                // 表不存在时创建表
                createTableAndInsertData();
            }
        };
    }

    /**
     * 执行schema.sql创建表结构
     * 自动读取项目资源目录下的 schema.sql 文件，拆分 SQL 语句并逐条执行
     * @throws Exception 执行异常
     */
    private void createTableAndInsertData() throws Exception {
        //加载资源文件
        ClassPathResource resource = new ClassPathResource("schema.sql");
        String sql = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        //获取数据库连接
        try (Connection conn = jdbcTemplate.getDataSource().getConnection();
             Statement stmt = conn.createStatement()) {
                //拆分并循环执行 SQL
            String[] sqlStatements = sql.split(";");
            for (String sqlStatement : sqlStatements) {
                String trimmed = sqlStatement.trim();
                if (!trimmed.isEmpty()) {
                    stmt.execute(trimmed);
                }
            }
        }
    }
}
