package com.nekolr.fish.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity
@Table(name = "role")
public class Role implements Serializable {

    /**
     * 角色 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 角色名称
     */
    @NotBlank
    private String name;


    /**
     * 角色描述
     */
    private String description;


    /**
     * 创建时间
     */
    @CreationTimestamp
    @Column(name = "create_time")
    private Timestamp createTime;

    /**
     * 排序字段，数字越大越靠后
     */
    @NotNull
    private Long sort;

    /**
     * 资源集合
     */
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<User> users;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "role_resource",
            joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "resource_id", referencedColumnName = "id")})
    private Set<Resource> resources;
}