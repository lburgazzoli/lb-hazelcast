/*
 * Copyright 2014 lburgazzoli
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.lburgazzoli.hazelcast.spring.boot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;


@Configuration
@EnableAutoConfiguration
@Import(HzMulticastConfiguration.class)
//@EnableConfigurationProperties(MongoProperties.class)
//@ConditionalOnMissingBean(MongoDbFactory.class)
public class HzAutoConfiguration {

    @Autowired
    Environment env;

    @Autowired
    HzMulticastConfiguration multicastConfig;

    /*
    private HazelcastInstance hz;

    @PreDestroy
    public void close() {
        if (this.hz != null) {
            //this.hz.close();
        }
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_SINGLETON)
    public HazelcastInstance hazelcast()  {

        this.hz = null; //this.properties.createMongoClient(this.options);
        return this.hz;
    }
    */

    /*
    @Bean
    @Scope(BeanDefinition.SCOPE_SINGLETON)
    public HzMulticastConfiguration multicastConfig() {
        return new HzMulticastConfiguration();
    }
    */
}
