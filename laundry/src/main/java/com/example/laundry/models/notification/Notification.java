package com.example.laundry.models.notification;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String recipient;

    @Column(nullable = false, length = 1000)
    private String message;

    @Column
    @CreationTimestamp
    private Date sendDate;

    @Column
    private String notificationType;

    public Notification(String recipient, String message, Date sendDate, String notificationType) {
        this.recipient = recipient;
        this.message = message;
        this.sendDate = sendDate;
        this.notificationType = notificationType;
    }
}
