package com.fairy.utils.commons;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author yhh
 * 
 * @desc mng用户上下文环境
 *
 */
@Setter
@Getter
public class MngContext implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public String userName;

    public String role;

    private Map<String, Serializable> attributes = new HashMap<>();

    public MngContext(String userName, String role) {
        this.userName = userName;
        this.role = role;
    }

    public Object getAttributes(String key) {
        return attributes.get(key);
    }

    public void setAttribute(String key, Serializable value) {
        attributes.put(key, value);
    }

}
