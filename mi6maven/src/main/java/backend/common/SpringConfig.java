/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.DERBY;

import backend.managers.*;

/**
 * Spring configuration class.
 * 
 * @author Matej Majdis
 */
@Configuration 
@EnableTransactionManagement
public class SpringConfig {
    
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(DERBY)
                .addScript("classpath:createTables.sql")
                //.addScript("")
                .build();
    }
    
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
    
    @Bean
    public AgentManager agentManager() {
        AgentManagerImpl am = new AgentManagerImpl();
        am.setDataSource(dataSource());
        return am;
    }
    
    @Bean
    public MissionManager missionManager() {
        MissionManagerImpl mm = new MissionManagerImpl();
        mm.setDataSource(dataSource());
        return mm;
    }
    
    @Bean
    public AssignmentManager assignmentManager() {
        AssignmentManagerImpl am = new AssignmentManagerImpl();
        am.setDataSource(dataSource());       
        return am;
    }
}
