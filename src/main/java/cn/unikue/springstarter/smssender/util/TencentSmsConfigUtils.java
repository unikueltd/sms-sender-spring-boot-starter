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

package cn.unikue.springstarter.smssender.util;


import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import cn.unikue.commonplexus.javaseutil.enumeration.InetProtocolType;
import cn.unikue.commonplexus.javaseutil.util.ObjectUtilsWraps;
import cn.unikue.commonplexus.javaseutil.util.StringUtilsWraps;
import cn.unikue.springstarter.smssender.property.TencentSmsSenderProperties;


/**
 * Utilities for configuring tencent-sms client
 *
 * @author David Hsing
 */
@SuppressWarnings("unused")
public abstract class TencentSmsConfigUtils {
    /**
     * Returns a configured SMS client instance from the given properties
     *
     * @param properties the Tencent SMS storage properties
     *
     * @return a configured SMS client instance from the given properties
     *
     * @throws IllegalArgumentException if required properties are missing
     */
    @Nonnull
    @SuppressWarnings("RedundantThrows")
    public static SmsClient smsClient(@Nonnull TencentSmsSenderProperties properties) throws Exception {
        if (StringUtils.isBlank(properties.getSecretId())) {
            throw new IllegalArgumentException("SecretId cannot be blank for SMS client");
        }
        if (StringUtils.isBlank(properties.getSecretKey())) {
            throw new IllegalArgumentException("SecretKey cannot be blank for SMS client");
        }
        Credential credential = new Credential(properties.getSecretId(), properties.getSecretKey());
        HttpProfile httpProfile = new HttpProfile();
        ObjectUtilsWraps.ifNotNull(properties.getConnectionTimeout(), item -> httpProfile.setConnTimeout((int) item.toMillis()));
        ObjectUtilsWraps.ifNotNull(properties.getReadTimeout(), item -> httpProfile.setReadTimeout((int) item.toMillis()));
        ObjectUtilsWraps.ifNotNull(properties.getWriteTimeout(), item -> httpProfile.setWriteTimeout((int) item.toMillis()));
        String endpoint = properties.getEndpoint();
        boolean secureHttp = BooleanUtils.isTrue(properties.getSslEnabled()) || StringUtils.startsWithIgnoreCase(endpoint, InetProtocolType.HTTPS.getValueWithDelimiter());
        if (StringUtils.startsWithIgnoreCase(endpoint, InetProtocolType.HTTP.getValueWithDelimiter())) {
            endpoint = StringUtils.substring(endpoint, 7);
        } else if (StringUtils.startsWithIgnoreCase(endpoint, InetProtocolType.HTTPS.getValueWithDelimiter())) {
            endpoint = StringUtils.substring(endpoint, 8);
        }
        httpProfile.setEndpoint(endpoint);
        httpProfile.setProtocol(secureHttp ? HttpProfile.REQ_HTTPS : HttpProfile.REQ_HTTP);
        if (StringUtils.isNotBlank(properties.getProxyHost())) {
            httpProfile.setProxyHost(properties.getProxyHost());
            httpProfile.setProxyPort(properties.getProxyPort());
            if (StringUtils.isNotBlank(properties.getProxyUsername())) {
                httpProfile.setProxyUsername(properties.getProxyUsername());
                httpProfile.setProxyPassword(properties.getProxyPassword());
            }
        }
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        StringUtilsWraps.ifNotBlank(properties.getSignMethod(), clientProfile::setSignMethod);
        return new SmsClient(credential, properties.getRegion(), clientProfile);
    }
}
