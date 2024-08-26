package com.partners.total.securities.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClosePriceDTO {
    private String rt_cd;
    private String msg_cd;
    private String msg1;
    private ClosePriceDTO.Output1 output1;

    public ClosePriceDTO() {}

    public ClosePriceDTO(String rt_cd, String msg_cd, String msg1, ClosePriceDTO.Output1 output1) {
        this.rt_cd = rt_cd;
        this.msg_cd = msg_cd;
        this.msg1 = msg1;
        this.output1 = output1;
    }

    @Getter
    @Setter
    public static class Output1 {
        private String stck_prdy_clpr;
        private String hts_kor_isnm;

        public Output1() {}

        public Output1(String stck_prdy_clpr, String hts_kor_isnm) {
            this.stck_prdy_clpr = stck_prdy_clpr;
            this.hts_kor_isnm = hts_kor_isnm;
        }
    }
}
