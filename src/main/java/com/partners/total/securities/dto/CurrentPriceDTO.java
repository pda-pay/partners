package com.partners.total.securities.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrentPriceDTO {

    private String rt_cd;
    private String msg_cd;
    private String msg1;
    private CurrentPriceDTO.Output output;

    public CurrentPriceDTO() {}

    public CurrentPriceDTO(String rt_cd, String msg_cd, String msg1, CurrentPriceDTO.Output output) {
        this.rt_cd = rt_cd;
        this.msg_cd = msg_cd;
        this.msg1 = msg1;
        this.output = output;
    }

    @Getter
    @Setter
    public static class Output {
        private String stck_prpr;

        public Output() {}

        public Output(String stck_prpr) {
            this.stck_prpr = stck_prpr;
        }
    }
}
