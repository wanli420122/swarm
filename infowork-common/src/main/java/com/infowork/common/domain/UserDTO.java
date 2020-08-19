package com.infowork.common.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by macro on 2020/6/19.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String password;
    private String clientId;
    private Integer status;
    private List<String> roles;

}
