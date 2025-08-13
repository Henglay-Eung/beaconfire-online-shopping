package org.example.onlineshopping.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "permission")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id")
    private Long permissionId;

    private String value;

}
