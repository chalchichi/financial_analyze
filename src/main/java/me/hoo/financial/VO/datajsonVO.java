package me.hoo.financial.VO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class datajsonVO
{
    public datajsonVO(String key, String value)
    {
        Key = key;
        Value = value;
    }
    String Key;
    String Value;
}