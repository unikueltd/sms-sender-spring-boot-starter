/*
 * Copyright (c) 2016 Yookue Ltd. All rights reserved.
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

package com.yookue.springstarter.smssender.event;


import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.context.ApplicationEvent;
import com.yookue.commonplexus.javaseutil.util.ObjectUtilsWraps;
import com.yookue.springstarter.smssender.enumeration.SmsSenderType;
import com.yookue.springstarter.smssender.structure.SendSmsParam;
import lombok.Getter;


/**
 * Event when request rate is limited
 *
 * @author David Hsing
 */
@Getter
@SuppressWarnings("unused")
public class SmsSentEvent extends ApplicationEvent {
    private final String receiptId;
    private final SmsSenderType senderType;

    public SmsSentEvent(@Nonnull SendSmsParam param, @Nullable String receiptId, @Nonnull SmsSenderType senderType) {
        super(param);
        this.receiptId = receiptId;
        this.senderType = senderType;
    }

    @Nonnull
    public SendSmsParam getSendParam() {
        return ObjectUtilsWraps.castAs(super.getSource(), SendSmsParam.class);
    }
}
