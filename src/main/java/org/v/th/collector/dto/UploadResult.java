package org.v.th.collector.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UploadResult implements Serializable {

    private int code;

    private String message;

}
