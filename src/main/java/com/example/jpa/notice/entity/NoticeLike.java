package com.example.jpa.notice.entity;
import com.example.jpa.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
@Entity @Builder @Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeLike { @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @JoinColumn @ManyToOne
    private Notice notice;
    @JoinColumn @ManyToOne
    private User user;
}
