package com.zencart.notification_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@Table(name = "notifications")
@Data
@Builder
public class Notification extends BaseEntity implements Serializable{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "recipient_id")
    private String recipientId;
    private String content;

    @Column(name = "is_read")
    private boolean read;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;
    @Column(name = "notification_type")
    private String notificationType;
    private String link;


    public Notification() {
        this.timestamp = LocalDateTime.now();
    }

}
