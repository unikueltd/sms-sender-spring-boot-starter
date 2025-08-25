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
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import org.springframework.boot.convert.DurationUnit;
import com.tencentcloudapi.common.profile.ClientProfile;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * Properties for tencent-sms storage
 *
 * @author David Hsing
 *
 * @reference "https://cloud.tencent.com/document/product/382/52077"
 */
@Getter
@Setter
@ToString
@SuppressWarnings({"JavadocDeclaration", "JavadocLinkAsPlainText"})
public class TencentSmsSenderProperties implements Serializable {
    /**
     * The endpoint of SMS service
     * <p>
     * If not specified, the client default values is {@code "sms.tencentcloudapi.com"}
     */
    private String endpoint;

    /**
     * The secret ID for authentication
     */
    private String secretId;

    /**
     * The secret key for authentication
     */
    private String secretKey;

    /**
     * The region of the SMS service
     * <p>
     * For example: "ap-beijing"
     */
    private String region;

    /**
     * The default app id if not specified in the send request
     */
    private String defaultAppId;

    /**
     * The default sign name if not specified in the send request
     */
    private String defaultSignName;

    /**
     * The sign method for authentication
     */
    private String signMethod = ClientProfile.SIGN_TC3_256;

    /**
     * Connection timeout
     */
    @DurationUnit(ChronoUnit.SECONDS)
    private Duration connectionTimeout = Duration.ofSeconds(60);

    /**
     * Read timeout
     */
    @DurationUnit(ChronoUnit.SECONDS)
    private Duration readTimeout = Duration.ofSeconds(60);

    /**
     * Write timeout
     */
    @DurationUnit(ChronoUnit.SECONDS)
    private Duration writeTimeout = Duration.ofSeconds(60);

    /**
     * HTTP proxy host
     */
    private String proxyHost;

    /**
     * HTTP proxy port
     */
    private Integer proxyPort;

    /**
     * HTTP proxy username
     */
    private String proxyUsername;

    /**
     * HTTP proxy password
     */
    private String proxyPassword;

    /**
     * Indicates the protocol to access endpoint, using HTTPS or HTTP
     * <p>
     * Default is {@code true}
     */
    private Boolean sslEnabled = true;

    /**
     * Maximum connections
     */
    private Integer maxConnections = 64;

    /**
     * Connection pool keep alive time
     */
    @DurationUnit(ChronoUnit.MINUTES)
    private Duration connectionPoolKeepAlive = Duration.ofMinutes(5);
}
