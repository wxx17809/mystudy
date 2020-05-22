package com.ghkj.gaqentity;

import lombok.Data;

import java.io.Serializable;

@Data
public class TokenUser implements Serializable {
    private Integer id;
    private String username;
    private String lastIp;
    private String token;
}
