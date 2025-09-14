/*
 * Copyright (c) 2025 Unikue Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.unikue.springstarter.smssender.config;


import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.Order;
import cn.unikue.commonplexus.springcondition.annotation.ConditionalOnAllProperties;
import cn.unikue.springstarter.smssender.composer.SmsSenderComposer;
import cn.unikue.springstarter.smssender.composer.impl.AliyunSmsSenderComposer;
import cn.unikue.springstarter.smssender.composer.impl.TencentSmsSenderComposer;
import cn.unikue.springstarter.smssender.property.SmsSenderProperties;


/**
 * Configuration for file storage
 *
 * @author David Hsing
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnBooleanProperty(prefix = SmsSenderAutoConfiguration.PROPERTIES_PREFIX, name = "enabled", matchIfMissing = true)
@Import(value = {SmsSenderAutoConfiguration.Entry.class, SmsSenderAutoConfiguration.Stage.class})
public class SmsSenderAutoConfiguration {
    public static final String PROPERTIES_PREFIX = "spring.sms-sender";    // $NON-NLS-1$


    @Order(value = 0)
    @Role(value = BeanDefinition.ROLE_INFRASTRUCTURE)
    static class Entry {
        @Bean
        @ConditionalOnMissingBean
        public SmsSenderProperties fileStorageProperties() {
            return new SmsSenderProperties();
        }
    }


    @Order(value = 1)
    @Role(value = BeanDefinition.ROLE_INFRASTRUCTURE)
    static class Stage {
        @Bean
        @ConditionalOnAllProperties(value = {
            @ConditionalOnProperty(prefix = SmsSenderAutoConfiguration.PROPERTIES_PREFIX, name = "sender-type", havingValue = "aliyun"),
            @ConditionalOnProperty(prefix = SmsSenderAutoConfiguration.PROPERTIES_PREFIX + ".aliyun", name = "access-key-id")
        })
        @ConditionalOnClass(value = com.aliyun.dysmsapi20170525.Client.class)
        @ConditionalOnMissingBean
        public SmsSenderComposer aliyunSmsSenderComposer(@Nonnull SmsSenderProperties properties) {
            return new AliyunSmsSenderComposer(properties.getAliyun(), BooleanUtils.isTrue(properties.getPublishEvent()));
        }

        @Bean
        @ConditionalOnAllProperties(value = {
            @ConditionalOnProperty(prefix = SmsSenderAutoConfiguration.PROPERTIES_PREFIX, name = "sender-type", havingValue = "tencent"),
            @ConditionalOnProperty(prefix = SmsSenderAutoConfiguration.PROPERTIES_PREFIX + ".tencent", name = "secret-id")
        })
        @ConditionalOnClass(value = com.tencentcloudapi.sms.v20210111.SmsClient.class)
        @ConditionalOnMissingBean
        public SmsSenderComposer tencentSmsSenderComposer(@Nonnull SmsSenderProperties properties) {
            return new TencentSmsSenderComposer(properties.getTencent(), BooleanUtils.isTrue(properties.getPublishEvent()));
        }
    }
}
