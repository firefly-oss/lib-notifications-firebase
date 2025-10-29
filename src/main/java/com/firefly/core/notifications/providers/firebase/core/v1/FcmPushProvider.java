/*
 * Copyright 2025 Firefly Software Solutions Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.firefly.core.notifications.providers.firebase.core.v1;

import com.firefly.core.notifications.interfaces.dtos.push.v1.PushNotificationRequest;
import com.firefly.core.notifications.interfaces.dtos.push.v1.PushNotificationResponse;
import com.firefly.core.notifications.interfaces.interfaces.providers.push.v1.PushProvider;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Map;

@Component
public class FcmPushProvider implements PushProvider {

    @Autowired
    private FirebaseMessaging messaging;

    @Override
    public Mono<PushNotificationResponse> sendPush(PushNotificationRequest request) {
        return Mono.fromCallable(() -> {
            Message.Builder builder = Message.builder();
            if (request.getToken() != null && !request.getToken().isBlank()) {
                builder.setToken(request.getToken());
            }
            Notification notification = Notification.builder()
                    .setTitle(request.getTitle())
                    .setBody(request.getBody())
                    .build();
            builder.setNotification(notification);
            Map<String, String> data = request.getData();
            if (data != null && !data.isEmpty()) {
                builder.putAllData(data);
            }
            String messageId = messaging.send(builder.build());
            return PushNotificationResponse.builder()
                    .messageId(messageId)
                    .success(true)
                    .build();
        }).onErrorReturn(PushNotificationResponse.builder()
                .success(false)
                .errorMessage("Failed to send push notification")
                .build()).subscribeOn(Schedulers.boundedElastic());
    }
}
