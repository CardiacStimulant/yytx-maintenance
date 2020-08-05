package com.yytx.maintenance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class YytxMaintenanceApplication {

    public static void main(String[] args) {
        SpringApplication.run(YytxMaintenanceApplication.class, args);
    }

}
