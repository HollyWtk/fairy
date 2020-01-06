package com.fairy.configure;



import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
public class FairyProperties {

    private String rsaPrivateKey = "MIICWwIBAAKBgQCFfTfCHHbZ2dYcFZd2tYE0PKB90agfJJlbAK/+n4L5CX9MMH7v" + 
            "0ZSbz+pZmM15EOkAvLrqCHx3Y0W8teB0+6qKCFqGxDHGy+TZp5doUsEgjvvX0ugc" + 
            "LbofAIdgyBCwUQM3W5g4eXZ/CLNvHWlLLNJgasLfcZlQKjWdga815hI/hQIDAQAB" + 
            "AoGAH0sM/LTWidh+0IOhR8NiACBsUUKKgo5+Yu6E05CKUB5RWMePomHD0EkL+6gO" + 
            "FyEHNlSIDuAhaY18pjIO5e2veycZLTlu0dSX/+px0Qlix2P8GwV/coO6Y1PCI25u" + 
            "8R5NUDHIdQxatyON4UzlnkIV0PohDkF6qBUM0cFQzpJwORECQQCYp6D895HYBVAV" + 
            "HUF2xXUaOH/o0mHipHQ7vPOhvTB5HPZH6r782sNzlWFDwHOGFIiG3Q5XAm54IP86" + 
            "tIqUDXKdAkEA39wEItsS+n2+K2FOydthflpEasf0HEwDGJ0mcq1z+a895qbrp9Gv" + 
            "7j8sieMtL9FDSqpZVKCrh478PKfOf/yYCQJAIpxnENPk2lCiDpd6fz5XxaEnxxbk" + 
            "IKnevKijqHkno14grL+Zm+TRycQFsIHS74R6S/GUzb98YnpAxAOpI9ZEtQJAG+un" + 
            "IGnOjiuMwoBagVa5bDb5/Y9+5OCeKj2/g+IaavPudowvLDk9Biwe8/u/sQ0apX9l" + 
            "sOZxPnEZCL+tQ81PcQJAV+mqZYcZTj9i38rs5afGEnhKsjf9Qu8vwbu+xPprMXKh" + 
            "cIvWV0aK7Na8a5bbPDowoaaH4g0ebL/tf1rf6pddTQ==";
    
    private String rsaPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCFfTfCHHbZ2dYcFZd2tYE0PKB9" + 
            "0agfJJlbAK/+n4L5CX9MMH7v0ZSbz+pZmM15EOkAvLrqCHx3Y0W8teB0+6qKCFqG" + 
            "xDHGy+TZp5doUsEgjvvX0ugcLbofAIdgyBCwUQM3W5g4eXZ/CLNvHWlLLNJgasLf" + 
            "cZlQKjWdga815hI/hQIDAQAB";
    
    private boolean enableValidate = true;

}
