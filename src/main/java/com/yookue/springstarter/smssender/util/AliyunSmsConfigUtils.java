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

package com.yookue.springstarter.smssender.util;


import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.teaopenapi.models.Config;
import com.yookue.commonplexus.javaseutil.enumeration.InetProtocolType;
import com.yookue.commonplexus.javaseutil.util.ObjectUtilsWraps;
import com.yookue.commonplexus.javaseutil.util.StringUtilsWraps;
import com.yookue.springstarter.smssender.property.AliyunSmsSenderProperties;


/**
 * Utilities for configuring aliyun-sms client
 *
 * @author David Hsing
 */
public abstract class AliyunSmsConfigUtils {
    /**
     * Returns a configured SMS client instance from the given properties
     *
     * @param properties The aliyun SMS properties
     *
     * @return a configured SMS client instance from the given properties
     *
     * @throws IllegalArgumentException if required properties are missing
     */
    @Nonnull
    @SuppressWarnings("RedundantThrows")
    public static Client smsClient(@Nonnull AliyunSmsSenderProperties properties) throws Exception {
        if (StringUtils.isBlank(properties.getEndpoint())) {
            throw new IllegalArgumentException("Endpoint cannot be blank for SMS client");
        }
        if (StringUtils.isBlank(properties.getAccessKeyId())) {
            throw new IllegalArgumentException("AccessKeyId cannot be blank for SMS client");
        }
        if (StringUtils.isBlank(properties.getAccessKeySecret())) {
            throw new IllegalArgumentException("AccessKeySecret cannot be blank for SMS client");
        }
        Config config = new Config();
        String endpoint = properties.getEndpoint();
        boolean secureHttp = BooleanUtils.isTrue(properties.getSslEnabled()) || StringUtils.startsWithIgnoreCase(endpoint, InetProtocolType.HTTPS.getValueWithDelimiter());
        config.setProtocol(secureHttp ? InetProtocolType.HTTPS.getValue() : InetProtocolType.HTTP.getValue());
        if (StringUtils.startsWithIgnoreCase(endpoint, InetProtocolType.HTTP.getValueWithDelimiter())) {
            endpoint = StringUtils.substring(endpoint, 7);
        } else if (StringUtils.startsWithIgnoreCase(endpoint, InetProtocolType.HTTPS.getValueWithDelimiter())) {
            endpoint = StringUtils.substring(endpoint, 8);
        }
        config.setEndpoint(endpoint);
        config.setAccessKeyId(properties.getAccessKeyId());
        config.setAccessKeySecret(properties.getAccessKeySecret());
        StringUtilsWraps.ifNotBlank(properties.getRegion(), config::setRegionId);
        ObjectUtilsWraps.ifNotNull(properties.getConnectionTimeout(), item -> config.setConnectTimeout((int) item.toMillis()));
        ObjectUtilsWraps.ifNotNull(properties.getReadTimeout(), item -> config.setReadTimeout((int) item.toMillis()));
        ObjectUtilsWraps.ifNotNull(properties.getMaxIdleConnections(), config::setMaxIdleConns);
        StringUtilsWraps.ifNotBlank(properties.getUserAgent(), config::setUserAgent);
        StringUtilsWraps.ifNotBlank(properties.getHttpProxy(), config::setHttpProxy);
        StringUtilsWraps.ifNotBlank(properties.getHttpsProxy(), config::setHttpsProxy);
        ObjectUtilsWraps.ifNotNull(properties.getDisableHttp2(), config::setDisableHttp2);
        StringUtilsWraps.ifNotBlank(properties.getSecurityToken(), config::setSecurityToken);
        StringUtilsWraps.ifNotBlank(properties.getBearerToken(), config::setBearerToken);
        StringUtilsWraps.ifNotBlank(properties.getEndpointType(), config::setEndpointType);
        StringUtilsWraps.ifNotBlank(properties.getOpenPlatformEndpoint(), config::setOpenPlatformEndpoint);
        StringUtilsWraps.ifNotBlank(properties.getSignatureAlgorithm(), config::setSignatureAlgorithm);
        StringUtilsWraps.ifNotBlank(properties.getSignatureVersion(), config::setSignatureVersion);
        StringUtilsWraps.ifNotBlank(properties.getTlsMinVersion(), config::setTlsMinVersion);
        return new Client(config);
    }
}
