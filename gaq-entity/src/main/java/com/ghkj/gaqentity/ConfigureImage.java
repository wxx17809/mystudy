package com.ghkj.gaqentity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "configure_image")
@Data
public class ConfigureImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer imageId;

    private String title;

    private String imageType;

    private String imageSrc;

    private Integer type;

    private Integer imageCategory;

    private String imageUrl;

    private String appUrl;

    private Long createtime;

    private Byte isdelete;

}