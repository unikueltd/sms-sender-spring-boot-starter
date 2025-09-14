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

package cn.unikue.springstarter.smssender.composer;


import java.util.List;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import cn.unikue.commonplexus.javaseutil.util.ObjectUtilsWraps;
import cn.unikue.springstarter.smssender.enumeration.SmsSenderType;
import cn.unikue.springstarter.smssender.exception.SmsQueryException;
import cn.unikue.springstarter.smssender.exception.SmsSendException;
import cn.unikue.springstarter.smssender.structure.QuerySmsStatusParam;
import cn.unikue.springstarter.smssender.structure.QuerySmsStatusResult;
import cn.unikue.springstarter.smssender.structure.SendPlainSmsParam;


/**
 * General sms composer interface
 *
 * @author David Hsing
 */
@SuppressWarnings("unused")
public interface SmsSenderComposer {
    /**
     * Returns a receipt id for querying the final sending status
     *
     * @param param The plain sms parameter
     *
     * @return a receipt id for querying the final sending status
     */
    <T extends SendPlainSmsParam> String sendPlainSms(@Nonnull T param) throws SmsSendException;

    /**
     * Returns the sms final sending status
     *
     * @param param The query sms parameter
     *
     * @return the sms final sending status
     */
    <T extends QuerySmsStatusParam> List<QuerySmsStatusResult> querySmsStatus(@Nonnull T param) throws SmsQueryException;

    /**
     * Returns the raw sms client
     *
     * @return the raw sms client
     */
    Object getRawClient();

    /**
     * Returns the raw sms client as an expected type
     *
     * @param expectType The expected type of the raw sms client
     *
     * @return the raw sms client as an expected type
     */
    default <T> T getRawClientAs(@Nullable Class<T> expectType) {
        return ObjectUtilsWraps.castAs(getRawClient(), expectType);
    }

    /**
     * Returns the current sms type
     *
     * @return the current sms type
     */
    @Nonnull
    SmsSenderType getSenderType();
}
