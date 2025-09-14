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

package cn.unikue.springstarter.smssender.structure;


import java.io.Serializable;
import java.time.LocalDateTime;
import cn.unikue.springstarter.smssender.enumeration.SmsSendStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * Query sms send status result
 *
 * @author David Hsing
 */
@Getter
@Setter
@ToString
public class QuerySmsStatusResult implements Serializable {
    public String mobilePhone;
    public SmsSendStatus sendStatus;
    public LocalDateTime receiveDate;
}
