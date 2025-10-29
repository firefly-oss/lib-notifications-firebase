# lib-notifications-firebase

Firebase Cloud Messaging (FCM) push adapter for Firefly Notifications.

## Install
```xml path=null start=null
<dependency>
  <groupId>com.firefly</groupId>
  <artifactId>lib-notifications-firebase</artifactId>
  <version>1.0.0-SNAPSHOT</version>
</dependency>
```

## Configuration (application.yml)
```yaml path=null start=null
firebase:
  credentialsPath: /path/to/service-account.json  # optional; uses ADC if omitted
  projectId: your-project-id                      # optional
```

Inject `PushService` from the core library and call `sendPush`.