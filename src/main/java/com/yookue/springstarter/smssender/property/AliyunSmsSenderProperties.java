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
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * Properties for aliyun-sms sender
 *
 * @author David Hsing
 */
@Getter
@Setter
@ToString
public class AliyunSmsSenderProperties implements Serializable {
    /**
     * The endpoint of SMS service
     * <p>
     * If not specified, the client default values is {@code "dysmsapi.aliyuncs.com"}
     */
    private String endpoint;

    /**
     * The access key ID for authentication
     */
    private String accessKeyId;

    /**
     * The access key secret for authentication
     */
    private String accessKeySecret;

    /**
     * The region of the SMS service
     * <p>
     * For example: "cn-beijing"
     */
    private String region;

    /**
     * The default sign name if not specified in the send request
     */
    private String defaultSignName;

    /**
     * The connection timeout
     * <p>
     * Default is 5 seconds
     */
    private Duration connectionTimeout = Duration.ofSeconds(5);

    /**
     * The read timeout
     * <p>
     * Default is 10 seconds
     */
    private Duration readTimeout = Duration.ofSeconds(10);

    /**
     * The maximum idle connections allowed
     * <p>
     * Default is 10
     */
    private Integer maxIdleConnections = 10;

    /**
     * Indicates whether to use https protocol
     * <p>
     * Default is {@code true}
     */
    private Boolean sslEnabled = true;

    /**
     * Name for user agent
     */
    private String userAgent;

    /**
     * The proxy for http
     * <p>
     * Format: {@code "http://[username:password@]host:port"}
     */
    private String httpProxy;

    /**
     * The proxy for https
     * <p>
     * Format: {@code "https://[username:password@]host:port"}
     */
    private String httpsProxy;

    /**
     * Indicates whether to disable http 2.0
     */
    public Boolean disableHttp2;

    /**
     * security token for endpoint
     */
    public String securityToken;

    /**
     * bearer token for endpoint
     */
    public String bearerToken;

    /**
     * endpoint type
     */
    private String endpointType;

    /**
     * open platform endpoint
     */
    private String openPlatformEndpoint;

    /**
     * signature algorithm
     */
    private String signatureAlgorithm;

    /**
     * signature version
     */
    private String signatureVersion;

    /**
     * signature version
     */
    public String tlsMinVersion;
}
