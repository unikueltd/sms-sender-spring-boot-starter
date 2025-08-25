/*
 * Copyright (c) 2025 Yookue Ltd. All rights reserved.
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

package com.yookue.springstarter.smssender.property;


import java.io.Serializable;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import com.yookue.springstarter.smssender.config.SmsSenderAutoConfiguration;
import com.yookue.springstarter.smssender.enumeration.SmsSenderType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * Properties for file storage
 *
 * @author David Hsing
 */
@ConfigurationProperties(prefix = SmsSenderAutoConfiguration.PROPERTIES_PREFIX)
@Getter
@Setter
@ToString
public class SmsSenderProperties implements Serializable {
    /**
     * Indicates whether to enable this starter or not
     * <p>
     * Default is {@code true}
     */
    private Boolean enabled = true;

    /**
     * The sender type pointer to the corresponding properties
     */
    private SmsSenderType senderType;

    /**
     * Indicates whether to publish event when a sms is sent
     * <p>
     * Default is {@code true}
     */
    private Boolean publishEvent = true;

    /**
     * The properties of aliyun-sms
     */
    @NestedConfigurationProperty
    private final AliyunSmsSenderProperties aliyun = new AliyunSmsSenderProperties();

    /**
     * The properties of tencent-sms
     */
    @NestedConfigurationProperty
    private final TencentSmsSenderProperties tencent = new TencentSmsSenderProperties();
}
