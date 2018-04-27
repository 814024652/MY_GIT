package com.zhong.pms.config.datasource;

import com.gome.datasource.shard.ShardDataSource;
import com.gome.datasource.shard.ShardDataSourceAspect;
import com.gome.datasource.shard.SlaveConfig;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author zhangliewei
 * @date 2018/3/21 10:25
 * @opyright(c) gome inc Gome Co.,LTD
 */
@Configuration
@EnableTransactionManagement
public class DataSourcesConfig {

    @Autowired
    private Environment env;

    @Autowired
    private DatasourcesClusterProperties clusterProperties;

    @Bean("dataSource")
    public ShardDataSource shardDataSource() {
        List<GomeDruidDataSource> dataSources = clusterProperties.getNodes();
        if (dataSources == null || dataSources.size() == 0) {
            throw new RuntimeException("at leaset one datasource,please check datasource");
        }
        GomeDruidDataSource master = null;
        int masterCounts = 0;
        Set<SlaveConfig> slaveConfigs = new HashSet<>();
        for (GomeDruidDataSource dataSource : dataSources) {
            if (dataSource.getMaster() != null && dataSource.getMaster()) {
                master = dataSource;
                masterCounts++;
            } else {
                SlaveConfig slaveConfig = new SlaveConfig();
                slaveConfig.setName(dataSource.getName());
                slaveConfig.setWeight(dataSource.getWeight());
                slaveConfig.setDataSources(dataSource);
                slaveConfig.setInterval(dataSource.getInterval());
                slaveConfigs.add(slaveConfig);
            }
        }
        if (master == null) {
            throw new RuntimeException("at leaset one master datasource,please check datasource");
        }
        if (masterCounts > 1) {
            throw new RuntimeException("more than one master datasource,please check datasource");
        }
        ShardDataSource shardDataSource = new ShardDataSource(master, master, slaveConfigs);
        return shardDataSource;
    }


//    /**
//     * 创建数据源
//     */
//    @Bean(name = "dataSource")
//    public DataSource getDataSource() {
//        try {
//            Properties props = new Properties();
//            props.put("driverClassName", env.getProperty("jdbc.driverClassName"));
//            props.put("url", env.getProperty("jdbc.url"));
//            props.put("username", env.getProperty("jdbc.username"));
//            props.put("password", env.getProperty("jdbc.password"));
//
//            props.put("maxActive", env.getProperty("jdbc.pool.maxActive"));
//            props.put("maxWait", env.getProperty("jdbc.pool.maxWait"));
//            props.put("initialSize", env.getProperty("jdbc.pool.initialSize"));
//            props.put("minIdle", env.getProperty("jdbc.pool.minIdle"));
//
//            props.put("timeBetweenEvictionRunsMillis", env.getProperty("jdbc.pool.timeBetweenEvictionRunsMillis"));
//            props.put("minEvictableIdleTimeMillis", env.getProperty("jdbc.pool.minEvictableIdleTimeMillis"));
//            props.put("validationQuery", env.getProperty("jdbc.pool.validationQuery"));
//            props.put("testWhileIdle", env.getProperty("jdbc.pool.testWhileIdle"));
//            props.put("testOnBorrow", env.getProperty("jdbc.pool.testOnBorrow"));
//            props.put("testOnReturn", env.getProperty("jdbc.pool.testOnReturn"));
//            props.put("poolPreparedStatements", env.getProperty("jdbc.pool.poolPreparedStatements"));
//            props.put("maxPoolPreparedStatementPerConnectionSize", env.getProperty("jdbc.pool.maxPoolPreparedStatementPerConnectionSize"));
//
//            return DruidDataSourceFactory.createDataSource(props);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    @Bean
    public ShardDataSourceAspect shardDataSourceAspect() {
        ShardDataSourceAspect shardDataSourceAspect = new ShardDataSourceAspect();
        return shardDataSourceAspect;
    }

    @Bean
    public DataSourceTransactionManager transactionManager(ShardDataSource dataSource) throws Exception {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(ShardDataSource ds) throws Exception {
        SqlSessionFactoryBean fb = new SqlSessionFactoryBean();

        // 指定数据源(这个必须有，否则报错)
        fb.setDataSource(ds);
        // 下边两句仅仅用于*.xml文件，如果整个持久层操作不需要使用到xml文件的话（只用注解就可以搞定），则不加
        // 指定基包
        fb.setTypeAliasesPackage(env.getProperty("mybatis.typeAliasesPackage"));
        fb.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(env.getProperty("mybatis.mapperLocations")));
        fb.setConfigLocation(new PathMatchingResourcePatternResolver().getResource(env.getProperty("mybatis.configLocation")));

        return fb.getObject();
    }
}
