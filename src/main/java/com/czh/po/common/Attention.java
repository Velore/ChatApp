package com.czh.po.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author chenzhuohong
 */
@Getter
@Setter
@NoArgsConstructor
public class Attention {

    private String followerId;

    private String acceptId;

    private LocalDateTime followTime;
}
