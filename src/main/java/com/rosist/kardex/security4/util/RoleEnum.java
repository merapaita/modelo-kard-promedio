package com.rosist.kardex.security4.util;

import java.util.Arrays;
import java.util.List;

public enum RoleEnum {
    ADMINISTRATOR(Arrays.asList(
            RolePermisionEnum.READ_ALL_GROUPS,
            RolePermisionEnum.READ_ALL_GROUPS_PAGE,
            RolePermisionEnum.READ_ALL_GROUPS_FOR_TYPE,
            RolePermisionEnum.READ_ONE_GROUP,
            RolePermisionEnum.CREATE_ONE_GROUP,
            RolePermisionEnum.UPDATE_ONE_GROUP,
            RolePermisionEnum.DELETE_ONE_GROUP,

            RolePermisionEnum.READ_ALL_CLASS,
            RolePermisionEnum.READ_ALL_CLASS_PAGE,
            RolePermisionEnum.READ_ALL_CLASS_FOR_GROUP,
            RolePermisionEnum.READ_ONE_CLASS,
            RolePermisionEnum.CREATE_ONE_CLASS,
            RolePermisionEnum.UPDATE_ONE_CLASS,
            RolePermisionEnum.DELETE_ONE_CLASS,
            RolePermisionEnum.READ_MY_PROFILE
    )),
    ASSISTANT_ADMINISTRATOR(Arrays.asList(
            RolePermisionEnum.READ_ALL_GROUPS,
            RolePermisionEnum.READ_ALL_GROUPS_PAGE,
            RolePermisionEnum.READ_ALL_GROUPS_FOR_TYPE,
            RolePermisionEnum.READ_ONE_GROUP,
            RolePermisionEnum.UPDATE_ONE_GROUP,

            RolePermisionEnum.READ_ALL_CLASS,
            RolePermisionEnum.READ_ALL_CLASS_PAGE,
            RolePermisionEnum.READ_ALL_CLASS_FOR_GROUP,
            RolePermisionEnum.READ_ONE_CLASS,
            RolePermisionEnum.UPDATE_ONE_CLASS,
            RolePermisionEnum.READ_MY_PROFILE
    )),
    CUSTOMER(Arrays.asList(
            RolePermisionEnum.READ_MY_PROFILE
    ));

    private List<RolePermisionEnum> permisions;

    RoleEnum(List<RolePermisionEnum> permisions) {
        this.permisions = permisions;
    }

    public List<RolePermisionEnum> getPermisions() {
        return permisions;
    }

    public void setPermisions(List<RolePermisionEnum> permisions) {
        this.permisions = permisions;
    }

}
