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

package cn.unikue.springstarter.smssender.composer.impl;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.QuerySendDetailsRequest;
import com.aliyun.dysmsapi20170525.models.QuerySendDetailsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import cn.unikue.commonplexus.javaseutil.constant.CharVariantConst;
import cn.unikue.commonplexus.javaseutil.constant.StringVariantConst;
import cn.unikue.commonplexus.javaseutil.constant.TemporalFormatConst;
import cn.unikue.commonplexus.javaseutil.util.CollectionPlainWraps;
import cn.unikue.commonplexus.javaseutil.util.LocalDateWraps;
import cn.unikue.commonplexus.javaseutil.util.ObjectUtilsWraps;
import cn.unikue.commonplexus.javaseutil.util.RegexUtilsWraps;
import cn.unikue.commonplexus.javaseutil.util.StringUtilsWraps;
import cn.unikue.commonplexus.springutil.util.JsonParserWraps;
import cn.unikue.springstarter.smssender.composer.SmsSenderComposer;
import cn.unikue.springstarter.smssender.enumeration.SmsSendStatus;
import cn.unikue.springstarter.smssender.enumeration.SmsSenderType;
import cn.unikue.springstarter.smssender.event.SmsSentEvent;
import cn.unikue.springstarter.smssender.exception.SmsQueryException;
import cn.unikue.springstarter.smssender.exception.SmsSendException;
import cn.unikue.springstarter.smssender.property.AliyunSmsSenderProperties;
import cn.unikue.springstarter.smssender.structure.AliyunQuerySmsStatusParam;
import cn.unikue.springstarter.smssender.structure.AliyunQuerySmsStatusResult;
import cn.unikue.springstarter.smssender.structure.AliyunSendPlainSmsParam;
import cn.unikue.springstarter.smssender.structure.QuerySmsStatusParam;
import cn.unikue.springstarter.smssender.structure.QuerySmsStatusResult;
import cn.unikue.springstarter.smssender.structure.SendPlainSmsParam;
import cn.unikue.springstarter.smssender.util.AliyunSmsConfigUtils;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


/**
 * Sms sender composer for aliyun-sms
 *
 * @author David Hsing
 */
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class AliyunSmsSenderComposer implements SmsSenderComposer, ApplicationEventPublisherAware, InitializingBean {
    private final AliyunSmsSenderProperties properties;
    private final boolean publishEvent;
    private Client smsClient;

    @Setter
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void afterPropertiesSet() throws Exception {
        smsClient = AliyunSmsConfigUtils.smsClient(properties);
    }

    @Override
    public <T extends SendPlainSmsParam> String sendPlainSms(@Nonnull T param) throws SmsSendException {
        if (CollectionPlainWraps.isEmpty(param.getMobilePhones())) {
            throw new SmsSendException("Mobile phones can not be empty");
        }
        Set<String> mobilePhones = param.getMobilePhones().stream().map(item -> RegexUtilsWraps.removeAll(item, CharVariantConst.CROSS, CharVariantConst.HYPHEN)).collect(Collectors.toSet());
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(StringUtils.join(mobilePhones, CharVariantConst.COMMA));
        request.setSignName(StringUtils.defaultIfBlank(param.getSignName(), properties.getDefaultSignName()));
        request.setTemplateCode(param.getTemplateCode());
        request.setTemplateParam(JsonParserWraps.toJsonString(param.getTemplateParams()));
        if (param instanceof AliyunSendPlainSmsParam alias) {
            request.setOutId(alias.getOutId());
            request.setOwnerId(alias.getOwnerId());
            request.setResourceOwnerAccount(alias.getResourceOwnerAccount());
            request.setResourceOwnerId(alias.getResourceOwnerId());
            request.setSmsUpExtendCode(alias.getSmsUpExtendCode());
        }
        try {
            SendSmsResponse response = smsClient.sendSms(request);
            if (response == null || response.getBody() == null || response.getBody().getCode() == null) {
                throw new SmsSendException("Sms send request done, but none body code respond");    // $NON-NLS-1$
            }
            if (StringUtils.equalsIgnoreCase(response.getBody().getCode(), StringVariantConst.OK)) {
                if (publishEvent) {
                    applicationEventPublisher.publishEvent(new SmsSentEvent(param, response.getBody().getBizId(), SmsSenderType.ALIYUN));
                }
                return response.getBody().getBizId();
            }
            throw new SmsSendException(response.getBody().getMessage());
        } catch (Exception ex) {
            throw (ex instanceof SmsSendException alias) ? alias : new SmsSendException(ex);
        }
    }

    @Override
    public <T extends QuerySmsStatusParam> List<QuerySmsStatusResult> querySmsStatus(@Nonnull T param) throws SmsQueryException {
        QuerySendDetailsRequest request = new QuerySendDetailsRequest();
        StringUtilsWraps.ifNotBlank(param.getReceiptId(), request::setBizId);
        request.setPhoneNumber(RegexUtilsWraps.removeAll(param.getMobilePhone(), CharVariantConst.CROSS, CharVariantConst.HYPHEN));
        request.setSendDate(LocalDateWraps.formatDate(param.getSentDate(), TemporalFormatConst.RAW_YYYYMMDD));
        request.setPageSize(param.getPageLimit());
        if (param instanceof AliyunQuerySmsStatusParam alias) {
            request.setCurrentPage(alias.getCurrentPage());
            request.setOwnerId(alias.getOwnerId());
            request.setResourceOwnerId(alias.getResourceOwnerId());
            request.setResourceOwnerAccount(alias.getResourceOwnerAccount());
        }
        ObjectUtilsWraps.ifNull(request.getCurrentPage(), () -> request.setCurrentPage(1L));
        try {
            QuerySendDetailsResponse response = smsClient.querySendDetails(request);
            if (response == null || response.getBody() == null || response.getBody().getCode() == null) {
                throw new SmsQueryException("Sms query request done, but none body code respond");    // $NON-NLS-1$
            }
            if (StringUtils.equalsIgnoreCase(response.getBody().getCode(), StringVariantConst.OK)) {
                return response.getBody().getSmsSendDetailDTOs().getSmsSendDetailDTO().stream().map(item -> {
                    AliyunQuerySmsStatusResult result = new AliyunQuerySmsStatusResult();
                    result.setMobilePhone(item.getPhoneNum());
                    result.setTemplateCode(item.getTemplateCode());
                    result.setErrorCode(item.getErrCode());
                    result.setSentDate(LocalDateWraps.parseDate(item.getSendDate(), TemporalFormatConst.RAW_YYYYMMDD));
                    ObjectUtilsWraps.ifNotNull(LocalDateWraps.parseDate(item.getReceiveDate(), TemporalFormatConst.RAW_YYYYMMDD), alias -> result.setReceiveDate(LocalDateWraps.toLocalDateTime(alias)));
                    if (item.getSendStatus() == 1) {
                        result.setSendStatus(SmsSendStatus.WAITING);
                    } else if (item.getSendStatus() == 2) {
                        result.setSendStatus(SmsSendStatus.SENDING);
                    } else if (item.getSendStatus() == 3) {
                        result.setSendStatus(SmsSendStatus.SEND_SUCCESS);
                    } else if (item.getSendStatus() == 4) {
                        result.setSendStatus(SmsSendStatus.SEND_FAILURE);
                    } else if (item.getSendStatus() == 5) {
                        result.setSendStatus(SmsSendStatus.OVER_LIMITATION);
                    } else if (item.getSendStatus() == 6) {
                        result.setSendStatus(SmsSendStatus.OVER_FREQUENCY);
                    } else if (item.getSendStatus() == 7) {
                        result.setSendStatus(SmsSendStatus.SEND_EXCEPTION);
                    } else if (item.getSendStatus() == 8) {
                        result.setSendStatus(SmsSendStatus.BLACKLIST);
                    } else {
                        result.setSendStatus(SmsSendStatus.UNKNOWN);
                    }
                    return result;
                }).collect(Collectors.toList());
            }
            throw new SmsQueryException(response.getBody().getMessage());
        } catch (Exception ex) {
            throw (ex instanceof SmsQueryException alias) ? alias : new SmsQueryException(ex);
        }
    }

    @Override
    public Object getRawClient() {
        return smsClient;
    }

    @Nonnull
    @Override
    public SmsSenderType getSenderType() {
        return SmsSenderType.ALIYUN;
    }
}
