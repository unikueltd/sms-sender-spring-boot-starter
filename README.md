# Sms Sender Spring Boot Starter

Spring Boot application integrates sms-sender, support various sms engines.

## Quickstart

- Import dependencies

```xml
    <dependency>
        <groupId>com.yookue.springstarter</groupId>
        <artifactId>sms-sender-spring-boot-starter</artifactId>
        <version>LATEST</version>
    </dependency>
```

> By default, this starter will auto take effect, you can turn it off by `spring.sms-sender.enabled = false`

- Configure Spring Boot `application.yml` with prefix `spring.sms-sender`

```yml
spring:
    sms-sender:
        sender-type: 'tencent'
        aliyun:
            access-key-id: 'your-access-key'
            access-key-secret: 'your-secret-key'
            region: 'cn-beijing'
        tencent:
            secret-id: 'your-secret-id'
            secret-key: 'your-secret-key'
            region: 'ap-beijing'
            default-app-id: 'your-default-app-id'
            default-sign-name: 'your-default-sign-name'
```

- Then, in your code, you can use `SmsSenderComposer` to store and retrieve files

```java
@Service
public class DemoService {
    @Autowired
    private SmsSenderComposer senderComposer;

    public void demoMethod() {
        // Sends a plain sms
        SendPlainSmsParam sendParam = new SendPlainSmsParam();
        sendParam.setMobilePhones(Collections.singleton("+86-13800138000"));
        sendParam.setTemplateCode("1234567890");
        sendParam.setTemplateParams(Maps.of("verifyCode", "123456"));
        String receiptId = senderComposer.sendPlainSms(sendParam);

        // Queries the status of a sms
        QuerySmsStatusParam queryParam = new QuerySmsStatusParam();
        sendParam.setMobilePhone("+86-13800138000");
        sendParam.setReceiptId(receiptId);
        sendParam.setSentDate(LocalDate.now());
        List<QuerySmsStatusResult> queryResults = senderComposer.querySmsStatus(queryParam);

        // Gets the raw client for advanced usage
        senderComposer.getRawClient();
        senderComposer.getRawClientAs(expectType);
    }
}
```

## Document

- Github: https://github.com/yookue/sms-sender-spring-boot-starter

## Requirement

- jdk 17+

## License

This project is under the [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0)

See the `NOTICE.txt` file for required notices and attributions.

## Donation

You like this package? Then [donate to us](https://yookue.com/donation) to support the development.

## Copyright

Beijing Yookue Network Technology Ltd.

## Website

- Yookue: https://yookue.com
